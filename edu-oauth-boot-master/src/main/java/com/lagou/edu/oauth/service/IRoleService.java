package com.lagou.edu.oauth.service;

import com.lagou.edu.oauth.entity.Role;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public interface IRoleService {

    Set<Role> queryUserRolesByUserId(String userId);

}
