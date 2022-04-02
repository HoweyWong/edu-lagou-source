package com.lagou.edu.oauth.service.impl;

import com.lagou.edu.oauth.service.IUserService;
import com.lagou.edu.user.api.UserRemoteService;
import com.lagou.edu.user.api.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserRemoteService userRemoteService;


    @Override
    public UserDTO getByPhone(String phone) {
        return this.userRemoteService.getUserByPhone(phone);
    }

    @Override
    public UserDTO getByUserId(Integer userId) {
        return this.userRemoteService.getUserById(userId);
    }

    @Override
    public UserDTO save(String name, String phone, String portrait, String password) {
        UserDTO dto = new UserDTO();
        dto.setAccountNonExpired(true);
        dto.setAccountNonLocked(true);
        dto.setCredentialsNonExpired(true);
        dto.setStatus("ENABLE");
        dto.setRegIp("127.0.0.1");
        dto.setName(name);
        dto.setPassword(password);
        dto.setCreateTime(new Date());
        dto.setIsDel(false);
        dto.setPhone(phone);
        dto.setUpdateTime(new Date());
        dto.setPortrait(portrait);
        return this.userRemoteService.saveUser(dto);
    }
}
