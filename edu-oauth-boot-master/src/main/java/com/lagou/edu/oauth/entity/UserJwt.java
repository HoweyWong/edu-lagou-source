package com.lagou.edu.oauth.entity;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class UserJwt extends org.springframework.security.core.userdetails.User {
    // 用户id
    private String id;

    public UserJwt(String username, String password, Collection<? extends GrantedAuthority> authorities, String id) {
        super(username, password, authorities);
        this.id = id;
    }

    public UserJwt(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities, String id) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}