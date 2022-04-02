package com.lagou.edu.auth.client.provider.fallback;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lagou.edu.auth.client.dto.AllocateUserRoleDTO;
import com.lagou.edu.auth.client.dto.RoleDTO;
import com.lagou.edu.auth.client.param.RoleQueryParam;
import com.lagou.edu.auth.client.provider.RoleProvider;
import com.lagou.edu.common.entity.vo.Result;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * @author : chenrg
 * @create 2020/7/17 10:41
 **/
@Slf4j
@Component
public class RoleProviderFallbackFactory implements FallbackFactory<RoleProvider> {

    @Override
    public RoleProvider create(Throwable throwable) {
        return new RoleProvider() {
            @Override
            public Result<Set<RoleDTO>> getUserRoles(Integer userId) {
                log.error("Get user roles failed. userId:{}", userId, throwable);
                return Result.fail();
            }

            @Override
            public Result<Boolean> saveOrUpdate(RoleDTO roleDTO) {
                log.error("Save or update role failed. params:{}", roleDTO, throwable);
                return Result.fail();
            }

            @Override
            public Result<Boolean> delete(Integer id) {
                log.error("Delete role failed. id:{}", id, throwable);
                return Result.fail();
            }

            @Override
            public Result<RoleDTO> getById(Integer id) {
                log.error("Get role by id failed. id:{}", id, throwable);
                return Result.fail();
            }

            @Override
            public Result<List<RoleDTO>> getAll() {
                log.error("Get all roles failed.", throwable);
                return Result.fail();
            }

            @Override
            public Result<Boolean> allocateUserRoles(AllocateUserRoleDTO allocateUserRoleDTO) {
                log.error("Allocate user roles failed. params:{}", allocateUserRoleDTO, throwable);
                return Result.fail();
            }

            @Override
            public Result<Page<RoleDTO>> getRolePages(RoleQueryParam roleQueryParam) {
                log.error("Get roles paging failed. params:{}", roleQueryParam, throwable);
                return Result.fail();
            }
        };
    }
}
