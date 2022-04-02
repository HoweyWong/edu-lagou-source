package com.lagou.edu.authority.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lagou.edu.auth.client.dto.AllocateRoleMenuDTO;
import com.lagou.edu.auth.client.dto.MenuNodeDTO;
import com.lagou.edu.auth.client.param.MenuQueryParam;
import com.lagou.edu.authority.entity.po.Menu;

import java.util.List;
import java.util.Set;

public interface IMenuService extends IService<Menu> {

    /**
     * 根据父id查询菜单
     *
     * @return
     */
    List<Menu> queryByParentId(Integer id);

    /**
     * 根据id列表查询
     *
     * @param ids
     * @return
     */
    List<Menu> queryByIds(List<Integer> ids);

    /**
     * 根据角色关联查询该角色拥有的菜单权限
     *
     * @param roleIds
     * @return
     */
    List<Menu> queryByRoleIds(Set<Integer> roleIds);

    /**
     * 删除菜单，同时删除角色-菜单关联关系
     *
     * @param id
     * @return
     */
    boolean deleteWithAssociation(Integer id);

    /**
     * 分页查询菜单
     *
     * @param menuQueryParam
     * @return
     */
    Page<Menu> getMenuPages(MenuQueryParam menuQueryParam);

    /**
     * 是否显示开关
     *
     * @param menu
     * @return
     */
    boolean switchShown(Menu menu);

    /**
     * 获取树型结构的菜单列表，每个菜单如果有子菜单，则列出子菜单
     *
     * @return
     */
    List<MenuNodeDTO> getMenuNodeList();

    /**
     * 填充菜单级别关系，将当前菜单的子菜单挂到当前菜单的子菜单列表，使用递归的方式逐级填充，直到菜单没有下一级菜单
     *
     * @param menu
     * @return
     */
    MenuNodeDTO fillMenuNode(Menu menu);

    /**
     * 给角色分配菜单
     *
     * @param allocateRoleMenuDTO
     */
    void allocateRoleMenus(AllocateRoleMenuDTO allocateRoleMenuDTO);

    /**
     * 获取角色菜单列表，并忽略是否显示字段值
     *
     * @param roleId
     * @return
     */
    List<Menu> getByRoleIdIgnoreIsShown(Integer roleId);
}
