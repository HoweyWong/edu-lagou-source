package com.lagou.edu.oauth.provider;

import com.lagou.edu.common.entity.vo.Result;
import com.lagou.edu.oauth.entity.Role;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Set;

@FeignClient(name = "edu-boss-boot", fallback = OrganizationProviderFallback.class)
public interface OrganizationProvider {

    /**
     * 查询用户角色
     *
     * @param userId
     * @return
     */
    @GetMapping(value = "/role/user/{userId}")
    Result<Set<Role>> queryRolesByUserId(@PathVariable("userId") String userId);
}
