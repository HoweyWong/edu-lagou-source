package com.lagou.edu.message.server;

import java.net.SocketAddress;
import java.util.*;

import org.apache.commons.lang.StringUtils;

import com.corundumstudio.socketio.*;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.corundumstudio.socketio.listener.ExceptionListener;
import com.corundumstudio.socketio.namespace.Namespace;
import com.corundumstudio.socketio.protocol.Packet;
import com.corundumstudio.socketio.protocol.PacketType;
import com.lagou.edu.common.jwt.JwtUtil;
import com.lagou.edu.common.string.GsonUtil;
import com.lagou.edu.message.api.dto.Message;
import com.lagou.edu.message.consts.Constants;
import com.lagou.edu.message.server.store.StoreFacotryProvider;
import com.lagou.edu.message.util.PushUtils;
import com.lagou.edu.message.util.ServerConfigUtils;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PushServer {

    public static final PushServer pushServer = new PushServer();
    private Namespace pushNamespace;
    private SocketIOServer server;

    private PushServer() {
        final Configuration configuration = new Configuration();
        configuration.setStoreFactory(StoreFacotryProvider.getRedissonStoreFactory());
        configuration.setAuthorizationListener(new UserAuthorizationListener());
        configuration.setPort(ServerConfigUtils.instance.getWebSocketPort());
        configuration.setContext(ServerConfigUtils.instance.getWebSocketContext());
        configuration.setOrigin(ServerConfigUtils.instance.getWebSocketOrigin());
        final SocketConfig socketConfig = new SocketConfig();
        socketConfig.setReuseAddress(true);
        configuration.setSocketConfig(socketConfig);
        server = new SocketIOServer(configuration);
        pushNamespace = (Namespace)server.addNamespace(ServerConfigUtils.instance.getWebSocketContext());
        configuration.setExceptionListener(new ExceptionListener() {
            @Override
            public void onEventException(Exception e, List<Object> list, SocketIOClient client) {
                UUID sessionId = client.getSessionId();
                log.error("onEventException,sessionId:{},roomList:{}", sessionId, client.get(Constants.ROOM), e);
            }

            @Override
            public void onDisconnectException(Exception e, SocketIOClient client) {
                UUID sessionId = client.getSessionId();
                log.error("onDisconnectException,sessionId:{},roomList:{}", sessionId, client.get(Constants.ROOM), e);
            }

            @Override
            public void onConnectException(Exception e, SocketIOClient client) {
                UUID sessionId = client.getSessionId();
                log.error("onConnectException,sessionId:{},roomList:{}", sessionId, client.get(Constants.ROOM), e);
            }

            @Override
            public void onPingException(Exception e, SocketIOClient client) {
                try {
                    boolean channelOpen = client.isChannelOpen();
                    SocketAddress remoteAddress = client.getRemoteAddress();
                    UUID sessionId = client.getSessionId();
                    log.error("onPingException,sessionId:{},roomList:{},channelOpen:{},remoteAddress:{}", sessionId,
                        client.get(Constants.ROOM), channelOpen, remoteAddress, e);
                } catch (Exception e1) {
                    log.error("", e1);
                }
            }

            @Override
            public boolean exceptionCaught(ChannelHandlerContext ctx, Throwable e) throws Exception {
                try {
                    Channel channel = ctx.channel();
                    if (null != channel) {
                        log.error("exceptionCaught,channel:{},isOpen:{},remoteAddress:{}", channel.id(),
                            channel.isOpen(), channel.remoteAddress(), e);
                    }
                } catch (Exception ex) {
                    log.error("exceptionCaught", ex);
                }
                return false;
            }
        });
        pushNamespace.addEventListener("send-message", Map.class, new DataListener<Map>() {
            @Override
            public void onData(SocketIOClient client, Map data, AckRequest ackRequest) throws Exception {
                JwtUtil.JwtResult user = UserAuthorizationListener.getUserInfo(client.getHandshakeData());
                Integer toUserId = user.getUserId();
                String content = (String)data.get("content");
                String type = (String)data.get("type");
                Message message = new Message(type, content, toUserId);
                log.info("from userId {} type {} content{}", toUserId, type, content);
                push(message);
            }
        });
        pushNamespace.addDisconnectListener(new DisconnectListener() {
            @Override
            public void onDisconnect(SocketIOClient client) {
                UUID sid = client.getSessionId();
                JwtUtil.JwtResult user = UserAuthorizationListener.getUserInfo(client.getHandshakeData());
                if (user != null) {
                    log.info("disconnect userId:{} userName:{} sid:{}", user.getUserId(), user.getUserId(), sid);
                } else {
                    client.disconnect();
                    log.info("client get handShakeData is null ,disconnect.sid:{}", sid);
                }
                String userId = Integer.toString(user.getUserId());
                List<String> roomList = PushUtils.getRoomList(userId, null, null);
                for (String str : roomList) {
                    client.leaveRoom(str);
                }
                try {
                    List<String> oldRoomList = client.get(Constants.ROOM);
                    if (null != oldRoomList && oldRoomList.size() > 0) {
                        for (String room : oldRoomList) {
                            if (StringUtils.isBlank(room)) {
                                continue;
                            }
                            client.leaveRoom(room);
                        }
                    }
                    client.disconnect();
                } catch (Exception e) {
                    log.error("leave old room exception,sid:{}", sid);
                } finally {
                    try {
                        client.del(Constants.ROOM);
                    } catch (Exception e) {
                        log.error("", e);
                    }
                }
            }
        });
        pushNamespace.addConnectListener(new ConnectListener() {
            @Override
            public void onConnect(SocketIOClient client) {
                UUID sid = client.getSessionId();
                JwtUtil.JwtResult user = UserAuthorizationListener.getUserInfo(client.getHandshakeData());
                try {
                    if (user != null) {
                        Set<String> rooms = client.getAllRooms();
                        for (String room : rooms) {
                            client.leaveRoom(room);
                        }
                        String userId = Integer.toString(user.getUserId());
                        List<String> roomList = PushUtils.getRoomList(userId, null, null);
                        for (String str : roomList) {
                            client.joinRoom(str);
                            log.info("userId:{} sid:{} join room {}", userId, sid, str);
                        }
                        client.set(Constants.ROOM, roomList);
                    } else {
                        client.disconnect();
                        log.info("sid:{} has no userId", sid);
                    }
                    client.disconnect();
                } catch (Exception e) {
                    log.error("addConnectListener-err", sid);
                }
            }
        });
    }

    /**
     * 推送消息
     *
     * @param message
     */
    public void push(Message message) {
        final String type;
        final Integer userId;
        final String json;
        long l11;
        try {
            long l0 = System.currentTimeMillis();
            type = message.getType();
            userId = message.getUserId();
            json = GsonUtil.toJson(message);
            l11 = System.currentTimeMillis();
            if (l11 - l0 > 501) {

            }
        } finally {
        }
        String room;
        long l12;
        try {
            if (userId == null) {
                throw new NullPointerException("userId 不能为空");
            }
            room = PushUtils.getRoom(null, userId, null);
        } finally {
        }
        Packet packet;
        try {
            packet = new Packet(PacketType.MESSAGE);
            packet.setSubType(PacketType.EVENT);
            packet.setName("message");
            ArrayList<Object> data = new ArrayList<>();
            data.add(json);
            packet.setData(data);
            packet.setNsp(pushNamespace.getName());
        } finally {
        }
        try {
            try {
                Iterable<SocketIOClient> clients = pushNamespace.getRoomClients(room);
                for (SocketIOClient socketIOClient : clients) {
                    socketIOClient.send(packet);
                }
            } catch (Exception e) {
                log.error("当前服务推送失败", e);
            }
        } finally {
        }
    }

    /**
     * 同步启动服务；
     */
    public void start() {
        try {
            server.start();
        } catch (Exception e) {
            log.error("Push server start failed!", e);
            System.exit(-1);
        }
    }

    /**
     * 停止服务
     */
    public void stop() {
        server.stop();
    }

    public Map<String, Object> getStatus() {
        HashMap<String, Object> status = new HashMap<>();
        status.put("namespace", pushNamespace.getName()); // namespace
        status.put("rooms", pushNamespace.getRooms());
        status.put("clients", pushNamespace.getAllClients().size());
        return status;
    }

}
