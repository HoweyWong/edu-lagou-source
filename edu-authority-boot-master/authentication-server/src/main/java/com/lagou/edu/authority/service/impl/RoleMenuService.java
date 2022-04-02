package com.lagou.edu.authority.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lagou.edu.authority.entity.po.RoleMenu;
import com.lagou.edu.authority.mapper.RoleMenuMapper;
import com.lagou.edu.authority.service.IRoleMenuService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleMenuService extends ServiceImpl<RoleMenuMapper, RoleMenu> implements IRoleMenuService {

    @Override
    public Set<Integer> queryByRoleIds(Set<Integer> roleIds) {
        if (CollectionUtils.isEmpty(roleIds)) {
            return null;
        }
        QueryWrapper<RoleMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("role_id", roleIds);
        List<RoleMenu> roleMenus = this.list(queryWrapper);
        return roleMenus.stream().map(RoleMenu::getMenuId).collect(Collectors.toSet());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeByRoleId(Integer roleId) {
        QueryWrapper<RoleMenu> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(RoleMenu::getRoleId, roleId);
        return this.remove(queryWrapper);
    }

    @Override
    public boolean removeByMenuId(Integer menuId) {
        QueryWrapper<RoleMenu> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(RoleMenu::getMenuId, menuId);
        return this.remove(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeByRoleIdAndMenuIds(Integer roleId, Set<Integer> needToDelMenus) {
        QueryWrapper<RoleMenu> queryWrapper = new QueryWrapper();
        queryWrapper.eq("role_id", roleId).in("menu_id", needToDelMenus);
        return this.remove(queryWrapper);
    }
}
