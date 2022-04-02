package com.lagou.edu.authority.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lagou.edu.authority.entity.po.RoleResource;
import com.lagou.edu.authority.mapper.RoleResourceMapper;
import com.lagou.edu.authority.service.IRoleResourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RoleResourceService extends ServiceImpl<RoleResourceMapper, RoleResource> implements IRoleResourceService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeByRoleId(Integer roleId) {
        QueryWrapper<RoleResource> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(RoleResource::getRoleId, roleId);
        return remove(queryWrapper);
    }

    @Override
    public Set<Integer> queryByRoleId(Integer roleId) {
        QueryWrapper<RoleResource> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_id", roleId);
        List<RoleResource> userRoleList = list(queryWrapper);
        return userRoleList.stream().map(RoleResource::getResourceId).collect(Collectors.toSet());
    }

    @Override
    public List<RoleResource> queryByRoleIds(Set<Integer> roleIds) {
        QueryWrapper<RoleResource> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("role_id", roleIds);
        return this.list(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeByResourceId(Integer resourceId) {
        QueryWrapper<RoleResource> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(RoleResource::getResourceId, resourceId);
        return this.remove(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByRoleIdAndResourceIds(Integer roleId, Set<Integer> resourceIds) {
        QueryWrapper<RoleResource> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_id", roleId).in("resource_id", resourceIds);
        this.remove(queryWrapper);
    }

}
