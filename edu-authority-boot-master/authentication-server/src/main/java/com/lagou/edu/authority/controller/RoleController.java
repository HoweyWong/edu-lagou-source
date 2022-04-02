package com.lagou.edu.authority.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.lagou.edu.auth.client.dto.AllocateUserRoleDTO;
import com.lagou.edu.auth.client.dto.RoleDTO;
import com.lagou.edu.auth.client.param.RoleQueryParam;
import com.lagou.edu.authority.entity.po.Role;
import com.lagou.edu.authority.service.IRoleService;
import com.lagou.edu.common.entity.vo.Result;
import com.lagou.edu.common.util.ConvertUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 角色接口
 *
 * @author : chenrg
 * @create 2020/7/9 11:16
 **/
@Api("角色管理")
@Slf4j
@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private IRoleService roleService;

    @ApiOperation(value = "获取用户角色", notes = "根据用户userId查询用户角色")
    @GetMapping(value = "/getUserRoles")
    public Result<Set<RoleDTO>> getUserRoles(@RequestParam("userId") Integer userId) {
        List<Role> roles = roleService.queryByUserId(userId);
        Set<RoleDTO> roleSet = Sets.newHashSet();
        if (CollectionUtils.isNotEmpty(roles)) {
            roles.stream().forEach(role -> {
                roleSet.add(ConvertUtils.convert(role, RoleDTO.class));
            });
        }
        return Result.success(roleSet);
    }

    @ApiOperation(value = "创建或更新角色", notes = "创建或更新角色")
    @PostMapping("/saveOrUpdate")
    public Result saveOrUpdateRole(@RequestBody RoleDTO roleDTO) {
        Role role = ConvertUtils.convert(roleDTO, Role.class);
        role.setUpdatedTime(new Date());
        roleService.saveOrUpdate(role);
        return Result.success();
    }

    @ApiOperation(value = "删除角色", notes = "根据ID删除角色")
    @DeleteMapping(value = "/{id}")
    public Result deleteById(@PathVariable Integer id) {
        return Result.success(roleService.deleteWithAssociation(id));
    }

    @ApiOperation(value = "查询角色", notes = "根据ID查询角色")
    @GetMapping("/{id}")
    public Result<RoleDTO> getById(@PathVariable Integer id) {
        Role role = roleService.get(id);
        return Result.success(ConvertUtils.convert(role, RoleDTO.class));
    }

    @ApiOperation(value = "查询所有角色", notes = "查询所有角色")
    @GetMapping("/getAll")
    public Result<List<RoleDTO>> getAll() {
        List<Role> roles = roleService.getAll();
        List<RoleDTO> roleDTOList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(roles)) {
            roles.stream().forEach(role -> {
                roleDTOList.add(ConvertUtils.convert(role, RoleDTO.class));
            });
        }
        return Result.success(roleDTOList);
    }


    @ApiOperation(value = "给用户分配角色", notes = "给用户分配角色，可同时分配多个角色")
    @PostMapping("/allocateUserRoles")
    public Result<Boolean> allocateUserRoles(@RequestBody AllocateUserRoleDTO allocateUserRoleDTO) {
        log.info("Allocate user roles with params:{}", allocateUserRoleDTO);
        roleService.allocateUserRoles(allocateUserRoleDTO);
        return Result.success(Boolean.TRUE);
    }

    @ApiOperation(value = "分布查询角色列表")
    @PostMapping("/getRolePages")
    public Result<Page<RoleDTO>> getRolePages(@RequestBody RoleQueryParam roleQueryParam) {
        Page<Role> rolePages = roleService.getRolePages(roleQueryParam);
        Page<RoleDTO> roleDTOPages = new Page<>();
        BeanUtils.copyProperties(rolePages, roleDTOPages);
        if (CollectionUtils.isNotEmpty(rolePages.getRecords())) {
            List<RoleDTO> records = rolePages.getRecords().stream()
                    .map(role -> ConvertUtils.convert(role, RoleDTO.class)).collect(Collectors.toList());
            roleDTOPages.setRecords(records);
        }
        return Result.success(roleDTOPages);
    }
}
