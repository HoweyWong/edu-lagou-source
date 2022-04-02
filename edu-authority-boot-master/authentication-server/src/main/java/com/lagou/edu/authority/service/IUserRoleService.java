package com.lagou.edu.authority.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lagou.edu.authority.entity.po.UserRole;

import java.util.Set;

public interface IUserRoleService extends IService<UserRole> {

    /**
     * 删除用户拥有的角色
     *
     * @param userId
     * @return
     */
    boolean removeByUserId(Integer userId);

    /**
     * 根据userId查询用户拥有角色id集合
     *
     * @param userId
     * @return
     */
    Set<Integer> queryByUserId(Integer userId);

    /**
     * 根据角色ID删除用户-角色关系
     *
     * @param roleId
     * @return
     */
    boolean removeByRoleId(Integer roleId);

    /**
     * 根据用户ID，角色id列表删除用户-角色关系
     *
     * @param userId
     * @param roleIds
     */
    boolean removeByRoleIds(Integer userId, Set<Integer> roleIds);
}
