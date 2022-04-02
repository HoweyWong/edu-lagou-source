package com.lagou.edu.message.server;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;

import com.corundumstudio.socketio.AuthorizationListener;
import com.corundumstudio.socketio.HandshakeData;
import com.lagou.edu.common.jwt.JwtUtil;
import com.lagou.edu.common.jwt.JwtUtil.JwtResult;
import com.lagou.edu.message.util.ServerConfigUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: ma wei long
 * @date:   2020年7月1日 下午8:29:14
 */
@Slf4j
public class UserAuthorizationListener implements AuthorizationListener {
	
    @Override
    public boolean isAuthorized(HandshakeData data) {
    	JwtResult userInfo = getUserInfo(data);
        if (userInfo == null || userInfo.getUserId() == null) {
            String uri = data.getSingleUrlParam("uri");
            log.error("auth failed. url: {}", uri);
            return false;
        } else {
        	log.info("auth success userId: {} userName: {}", userInfo.getUserId(), userInfo.getUserName());
            return true;
        }
    }
    
    /**
     * @author: ma wei long
     * @date:   2020年7月1日 下午8:29:23   
    */
    public static JwtResult getUserInfo(HandshakeData data) {
    	Map<String, List<String>> params = data.getUrlParams();
    	String authentication = params.get(HttpHeaders.AUTHORIZATION).get(0);
    	if(StringUtils.isBlank(authentication)) {
    		return null;
    	}
    	return JwtUtil.parse(ServerConfigUtils.instance.getSigningKey(), authentication);
    }
}
