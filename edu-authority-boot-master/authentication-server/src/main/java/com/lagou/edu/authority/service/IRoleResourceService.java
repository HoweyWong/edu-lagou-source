package com.lagou.edu.authority.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lagou.edu.authority.entity.po.RoleResource;

import java.util.List;
import java.util.Set;

public interface IRoleResourceService extends IService<RoleResource> {

    /**
     * 删除角色拥有的资源
     *
     * @param roleId 角色id
     * @return 是否操作成功
     */
    boolean removeByRoleId(Integer roleId);

    /**
     * 查询角色拥有资源id
     *
     * @param roleId 角色id
     * @return 角色拥有的资源id集合
     */
    Set<Integer> queryByRoleId(Integer roleId);

    /**
     * 根据角色id列表查询资源关系
     *
     * @param roleIds 角色id集合
     * @return 角色资源关系集合
     */
    List<RoleResource> queryByRoleIds(Set<Integer> roleIds);

    /**
     * 根据资源ID删除角色-资源关系
     *
     * @param resourceId
     * @return
     */
    boolean removeByResourceId(Integer resourceId);

    /**
     * 删除角色-资源关系
     *
     * @param roleId
     * @param resourceIds
     */
    void removeByRoleIdAndResourceIds(Integer roleId, Set<Integer> resourceIds);

}
