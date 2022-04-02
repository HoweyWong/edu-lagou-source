package com.lagou.edu.authority.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lagou.edu.authority.entity.po.Menu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
@Mapper
public interface MenuMapper extends BaseMapper<Menu> {

    /**
     * 根据角色id列表关联menu,role_menu_relation表，查询角色下所拥有的菜单
     *
     * @param roleIds
     * @return
     */
    List<Menu> queryByRoleIds(@Param("roleIds") Set<Integer> roleIds);

    /**
     * 查询角色菜单，忽略shown字段值
     *
     * @param roleId
     * @return
     */
    List<Menu> queryByRoleIdIgnoreIsShown(@Param("roleId") Integer roleId);
}