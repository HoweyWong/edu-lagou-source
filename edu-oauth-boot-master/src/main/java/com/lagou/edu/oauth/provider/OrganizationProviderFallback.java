package com.lagou.edu.oauth.provider;

import com.lagou.edu.common.entity.vo.Result;
import com.lagou.edu.oauth.entity.Role;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class OrganizationProviderFallback implements OrganizationProvider {

    @Override
    public Result<Set<Role>> queryRolesByUserId(String userId) {
        return Result.success(new HashSet<Role>());
    }
}
