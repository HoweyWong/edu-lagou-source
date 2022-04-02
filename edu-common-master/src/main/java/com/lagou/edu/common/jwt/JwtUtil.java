package com.lagou.edu.common.jwt;

import io.jsonwebtoken.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 解析jwt
 */
@Slf4j
public final class JwtUtil {
    private static final String X_USER_ID = "user_id";
    private static final String X_USER_NAME = "user_name";

    public static JwtResult parse(String signKey, String jwtToken) {
        try {
            Jws<Claims> jwt = Jwts.parser()
                    .setSigningKey(signKey.getBytes())
                    .parseClaimsJws(jwtToken);
            if (null != jwt && null != jwt.getBody()) {
                Integer userId = Integer.parseInt(jwt.getBody().get(X_USER_ID, String.class));
                String userName = jwt.getBody().get(X_USER_NAME, String.class);
                return new JwtResult(userId, userName);
            }
        } catch (ExpiredJwtException | MalformedJwtException | SignatureException e) {
            log.error("user token error :{}", e.getMessage());
        }
        return new JwtResult();
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class JwtResult {
        private Integer userId;
        private String userName;
    }
}
