package com.lagou.edu.authority.service.impl;

import com.google.common.collect.Lists;
import com.lagou.edu.auth.client.dto.MenuNodeDTO;
import com.lagou.edu.auth.client.dto.PermissionDTO;
import com.lagou.edu.auth.client.dto.ResourceDTO;
import com.lagou.edu.authority.common.contant.PubContants;
import com.lagou.edu.authority.entity.po.Menu;
import com.lagou.edu.authority.entity.po.Resource;
import com.lagou.edu.authority.service.IAuthenticationService;
import com.lagou.edu.authority.service.IResourceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AuthenticationService implements IAuthenticationService {

    /**
     * 未在资源库中的URL默认标识
     */
    public static final String NONEXISTENT_URL = "NONEXISTENT_URL";

    @Autowired
    private IResourceService resourceService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private MenuService menuService;

    @Override
    public boolean decide(String userId, HttpServletRequest request) {
        log.info("Access user:{}, url:{}, method:{}", userId, request.getServletPath(), request.getMethod());
        // 用户是否有某些角色
        Set<Integer> roleIds = userRoleService.queryByUserId(Integer.parseInt(userId));
        if (CollectionUtils.isEmpty(roleIds)) {
            return false;
        }
        // 用户是否有某些菜单权限
        List<Menu> menus = menuService.queryByRoleIds(roleIds);
        if (hasMenuPermission(menus, request.getServletPath())) {
            return true;
        }
        // 用户是否有某些资源的权限
        return resourceService.matchUserResources(roleIds, request);
    }

    /**
     * 转换资源数据
     *
     * @param roleIds
     * @return
     */
    private List<ResourceDTO> convertResources(Set<Integer> roleIds) {
        List<Resource> list = resourceService.queryByRoleIds(roleIds);
        if (CollectionUtils.isEmpty(list)) {
            return Lists.newArrayList();
        }
        List<ResourceDTO> resourceList = Lists.newArrayList();
        list.stream().forEach(r -> {
            ResourceDTO resourceDTO = new ResourceDTO();
            BeanUtils.copyProperties(r, resourceDTO);
            resourceList.add(resourceDTO);
        });
        return resourceList;
    }

    @Override
    public PermissionDTO listUserPermission(Integer userId) {
        Set<Integer> roleIds = userRoleService.queryByUserId(userId);
        if (CollectionUtils.isEmpty(roleIds)) {
            return new PermissionDTO(Lists.newArrayList(), Lists.newArrayList());
        }
        List<MenuNodeDTO> menuList = convertMenus(roleIds);
        Collections.sort(menuList);
        List<ResourceDTO> resourceList = convertResources(roleIds);
        return new PermissionDTO(menuList, resourceList);
    }

    /**
     * 转换菜单数据，找出每个菜单的子菜单列表
     *
     * @param roleIds
     * @return
     */
    private List<MenuNodeDTO> convertMenus(Set<Integer> roleIds) {
        List<Menu> menus = menuService.queryByRoleIds(roleIds);
        if (CollectionUtils.isEmpty(menus)) {
            return Lists.newArrayList();
        }
        List<Menu> topLevelMenus = menus.stream()
                .filter(menu -> Objects.equals(menu.getLevel(), PubContants.MENU_TOP_LEVEL))
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(topLevelMenus)) {
            return Lists.newArrayList();
        }
        return topLevelMenus.stream().map(menu -> menuService.fillMenuNode(menu)).collect(Collectors.toList());
    }

    /**
     * 判断用户是否有某菜单的权限，通过对比菜单href和当前url
     *
     * @param menus
     * @param url
     * @return
     */
    private boolean hasMenuPermission(List<Menu> menus, String url) {
        if (CollectionUtils.isEmpty(menus)) {
            return false;
        }
        for (Menu menu : menus) {
            if (StringUtils.isNotBlank(menu.getHref()) && StringUtils.startsWith(url, menu.getHref())) {
                return true;
            }
        }
        return false;
    }

}
