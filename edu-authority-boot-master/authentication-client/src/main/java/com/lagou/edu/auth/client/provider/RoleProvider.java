package com.lagou.edu.auth.client.provider;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lagou.edu.auth.client.dto.AllocateUserRoleDTO;
import com.lagou.edu.auth.client.dto.RoleDTO;
import com.lagou.edu.auth.client.param.RoleQueryParam;
import com.lagou.edu.auth.client.provider.fallback.RoleProviderFallbackFactory;
import com.lagou.edu.common.entity.vo.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;


/**
 * 角色接口
 *
 * @author : chenrg
 * @create 2020/7/9 11:06
 **/
@Component
@FeignClient(name = "${remote.feign.edu-authority-boot.name:edu-authority-boot}", path = "/role",
        fallbackFactory = RoleProviderFallbackFactory.class)
public interface RoleProvider {

    /**
     * 根据用户id查询用户角色列表
     *
     * @param userId
     * @return
     */
    @GetMapping(value = "/getUserRoles")
    Result<Set<RoleDTO>> getUserRoles(@RequestParam("userId") Integer userId);

    /**
     * 保存或更新角色
     *
     * @param roleDTO
     * @return
     */
    @PostMapping(value = "/saveOrUpdate")
    Result<Boolean> saveOrUpdate(@RequestBody RoleDTO roleDTO);

    /**
     * 删除角色，同时删除用户-角色关系，角色-菜单关系，角色-资源关系
     *
     * @param id
     * @return
     */
    @DeleteMapping(value = "/{id}")
    Result<Boolean> delete(@PathVariable("id") Integer id);

    /**
     * 根据id获取
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    Result<RoleDTO> getById(@PathVariable("id") Integer id);

    /**
     * 获取所有角色
     *
     * @return
     */
    @GetMapping("/getAll")
    Result<List<RoleDTO>> getAll();

    /**
     * 给用户分配角色，可同时分配多个角色
     *
     * @param allocateUserRoleDTO
     * @return
     */
    @PostMapping("/allocateUserRoles")
    Result<Boolean> allocateUserRoles(@RequestBody AllocateUserRoleDTO allocateUserRoleDTO);

    /**
     * 分页查询角色
     *
     * @param roleQueryParam
     * @return
     */
    @PostMapping("/getRolePages")
    Result<Page<RoleDTO>> getRolePages(@RequestBody RoleQueryParam roleQueryParam);
}
