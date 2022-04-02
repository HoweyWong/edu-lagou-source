package com.lagou.edu.message.server.dicpacth;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.corundumstudio.socketio.store.pubsub.DispatchMessage;
import com.corundumstudio.socketio.store.pubsub.PubSubStore;
import com.corundumstudio.socketio.store.pubsub.PubSubType;

/**
 * 消息分发给其他节点
 *
 * @author alanzhang
 * @since 2017/4/26
 */
@Component
public final class Dispatcher {

    private static final Logger logger = LoggerFactory.getLogger(Dispatcher.class);

    /**
     * 使用无界队列的线程池来分发消息。
     */
    private static ThreadPoolExecutor executor = null;
    static {
        executor = new ThreadPoolExecutor(5, 5, 5, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>(),
                new ThreadPoolExecutor.CallerRunsPolicy());
    }

    /**
     * 分发消息。
     *
     * @param dispatchMessage not null
     */
    public static void dispatch(final PubSubStore pubSubStore, final DispatchMessage dispatchMessage) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                final long l1 = System.currentTimeMillis();
                try {
                    pubSubStore.publish(PubSubType.DISPATCH, dispatchMessage);
                    //logger.info("分发消息 pubSubStore:{} msg:{}",pubSubStore,dispatchMessage);
                } catch (Exception e) {
                    logger.error("分发消息失败", e);
                }
                final long l2 = System.currentTimeMillis() - l1;
                if (l2 > 50l) {
                    logger.info("分发耗时,room={},time={}ms", dispatchMessage.getRoom(), l2);
                }
            }
        });

    }

}
