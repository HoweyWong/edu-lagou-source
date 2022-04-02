package com.lagou.edu.authority.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lagou.edu.authority.entity.po.RoleMenu;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * @author chenrg
 * @date 2020年7月7日19:45:33
 */
public interface IRoleMenuService extends IService<RoleMenu> {

    /**
     * 根据角色id列表查询菜单ID列表
     *
     * @param roleIds 角色id列表，如果为空，则返回空
     * @return
     */
    Set<Integer> queryByRoleIds(Set<Integer> roleIds);

    /**
     * 根据角色id删除角色-菜单关系
     *
     * @param roleId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    boolean removeByRoleId(Integer roleId);

    /**
     * 根据菜单id删除角色-菜单关系
     *
     * @param menuId
     * @return
     */
    boolean removeByMenuId(Integer menuId);


    boolean removeByRoleIdAndMenuIds(Integer roleId, Set<Integer> needToDelMenus);
}
