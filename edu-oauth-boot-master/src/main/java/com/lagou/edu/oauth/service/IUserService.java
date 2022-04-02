package com.lagou.edu.oauth.service;

import com.lagou.edu.user.api.dto.UserDTO;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public interface IUserService {

    /**
     * 根据用户手机号获取用户信息
     *
     * @return
     */
    @Cacheable(value = "#phone")
    UserDTO getByPhone(String phone);

    /**
     * 根据用户唯一标识获取用户信息
     *
     * @return
     */
    @Cacheable(value = "#userId")
    UserDTO getByUserId(Integer userId);

    UserDTO save(String name, String phone, String portrait, String password);
}
