package com.lagou.edu.boss.rest;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.lagou.edu.auth.client.dto.AllocateRoleMenuDTO;
import com.lagou.edu.auth.client.dto.MenuDTO;
import com.lagou.edu.auth.client.dto.MenuNodeDTO;
import com.lagou.edu.auth.client.param.MenuQueryParam;
import com.lagou.edu.auth.client.provider.MenuProvider;
import com.lagou.edu.boss.common.UserManager;
import com.lagou.edu.boss.entity.form.AllocateRoleMenuForm;
import com.lagou.edu.boss.entity.form.MenuForm;
import com.lagou.edu.common.entity.vo.Result;
import com.lagou.edu.common.util.ConvertUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 菜单管理
 *
 * @author : chenrg
 * @create 2020/7/10 17:10
 **/
@Slf4j
@Api(tags = "菜单管理", produces = "application/json")
@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private MenuProvider menuProvider;

    @ApiOperation(value = "保存或新增菜单", notes = "保存或新增菜单")
    @PostMapping(value = "/saveOrUpdate")
    public Result<Boolean> saveOrUpdate(@RequestBody MenuForm menuForm) {
        MenuDTO menuDTO = ConvertUtils.convert(menuForm, MenuDTO.class);
        if (Objects.isNull(menuDTO.getId())) {
            menuDTO.setCreatedBy(UserManager.getUserName());
            menuDTO.setCreatedTime(new Date());
        }
        menuDTO.setUpdatedBy(UserManager.getUserName());
        return menuProvider.saveOrUpdate(menuDTO);
    }

    @ApiOperation(value = "根据ID查询菜单", notes = "根据ID查询菜单")
    @GetMapping("/{id}")
    public Result<MenuDTO> getById(@PathVariable("id") Integer id) {
        return menuProvider.getById(id);
    }

    @ApiOperation(value = "删除菜单", notes = "根据id删除菜单")
    @DeleteMapping(value = "/{id}")
    public Result<Boolean> delete(@PathVariable("id") Integer id) {
        return menuProvider.deleteById(id);
    }


    @ApiOperation(value = "获取所有菜单", notes = "获取所有菜单")
    @GetMapping("/getAll")
    public Result<List<MenuDTO>> getAll() {
        return menuProvider.getAll();
    }

    @ApiOperation("按条件分页查询菜单")
    @PostMapping("/getMenuPages")
    public Result<Page<MenuDTO>> getMenuPages(@RequestBody MenuQueryParam menuQueryParam) {
        return menuProvider.getMenuPages(menuQueryParam);
    }

    @ApiOperation("是否显示开关")
    @GetMapping("/switchShown")
    public Result<Boolean> switchShown(@RequestParam("id") Integer id) {
        MenuDTO menuDTO = new MenuDTO();
        menuDTO.setId(id);
        menuDTO.setUpdatedBy(UserManager.getUserName());
        menuDTO.setUpdatedTime(new Date());
        return menuProvider.switchShown(menuDTO);
    }

    @ApiOperation("获取所有菜单并按层级展示")
    @GetMapping("/getMenuNodeList")
    public Result<List<MenuNodeDTO>> getMenuNodeList() {
        return menuProvider.getMenuNodeList();
    }

    @ApiOperation("获取编辑菜单页面信息")
    @ApiImplicitParams({
            @ApiImplicitParam(type = "form", name = "id", value = "菜单ID", paramType = "String", required = true)
    })
    @GetMapping("/getEditMenuInfo")
    public Result<Map> getEditMenuInfo(@RequestParam("id") Integer id) {
        Map<String, Object> resultMap = new HashMap<>();
        MenuDTO menuDTO = null;
        List<MenuNodeDTO> menuNodeDTOList = Lists.newArrayList();
        Result<List<MenuNodeDTO>> menuNodeListResult = menuProvider.getMenuNodeList();
        Result<MenuDTO> menuResult = menuProvider.getById(id);
        if (menuNodeListResult.isSuccess() && CollectionUtils.isNotEmpty(menuNodeListResult.getData())) {
            menuNodeDTOList = menuNodeListResult.getData();
        }

        if (menuResult.isSuccess() && Objects.nonNull(menuResult.getData())) {
            menuDTO = menuResult.getData();
        }

        if (Objects.nonNull(menuDTO)) {
            Integer parentId = menuDTO.getParentId();
            menuNodeDTOList.stream().forEach(menuNodeDTO -> setSelectedFlag(parentId, menuNodeDTO));
        }

        resultMap.put("menuInfo", menuDTO);
        resultMap.put("parentMenuList", menuNodeDTOList);
        return Result.success(resultMap);
    }

    @ApiOperation(value = "获取角色拥有的菜单列表", notes = "在给角色分配菜单时，跳转到角色-菜单列表页，并标记哪些菜单已分配给该角色")
    @GetMapping("/getRoleMenus")
    public Result<List<MenuNodeDTO>> getRoleMenus(@RequestParam Integer roleId) {
        Result<List<MenuDTO>> menusResult = menuProvider.getByRoleId(roleId);

        Result<List<MenuNodeDTO>> menuNodeListResult = menuProvider.getMenuNodeList();

        List<MenuDTO> roleMenus = Lists.newArrayList();
        if (menusResult.isSuccess() && CollectionUtils.isNotEmpty(menusResult.getData())) {
            roleMenus = menusResult.getData();
        }

        List<MenuNodeDTO> menuNodes = Lists.newArrayList();
        if (menuNodeListResult.isSuccess() && CollectionUtils.isNotEmpty(menuNodeListResult.getData())) {
            menuNodes = menuNodeListResult.getData();
        }

        if (CollectionUtils.isEmpty(menuNodes)) {
            return Result.fail("未查询到菜单列表");
        }

        final Set<Integer> roleMenuIds = roleMenus.stream().map(role -> role.getId()).collect(Collectors.toSet());
        menuNodes.stream().forEach(menuNodeDTO -> setSelectedFlag(menuNodeDTO, roleMenuIds));

        return Result.success(menuNodes);
    }

    @ApiOperation(value = "给角色分配菜单")
    @PostMapping("/allocateRoleMenus")
    public Result<Boolean> allocateRoleMenus(@RequestBody AllocateRoleMenuForm allocateRoleMenuForm) {
        log.info("Allocate role menus, params:{}", allocateRoleMenuForm);
        if (Objects.isNull(allocateRoleMenuForm.getRoleId())) {
            return Result.fail("角色ID不能为空");
        }
        AllocateRoleMenuDTO allocateRoleMenuDTO = ConvertUtils.convert(allocateRoleMenuForm, AllocateRoleMenuDTO.class);
        allocateRoleMenuDTO.setCreatedBy(UserManager.getUserName());
        allocateRoleMenuDTO.setUpdatedBy(UserManager.getUserName());
        return menuProvider.allocateRoleMenus(allocateRoleMenuDTO);
    }

    private void setSelectedFlag(MenuNodeDTO menuNodeDTO, Set<Integer> roleMenuIds) {
        if (roleMenuIds.contains(menuNodeDTO.getId())) {
            menuNodeDTO.setSelected(true);
        }
        if (CollectionUtils.isEmpty(menuNodeDTO.getSubMenuList())) {
            return;
        }
        menuNodeDTO.getSubMenuList().stream().forEach(subMenuNode -> setSelectedFlag(subMenuNode, roleMenuIds));
    }

    /**
     * 选中当前菜单的上级菜单，置为选中状态
     *
     * @param parentId    当前菜单的父级菜单ID
     * @param menuNodeDTO 所有菜单列表
     */
    private void setSelectedFlag(Integer parentId, MenuNodeDTO menuNodeDTO) {
        if (Objects.equals(menuNodeDTO.getId(), parentId)) {
            menuNodeDTO.setSelected(true);
            return;
        }
        if (CollectionUtils.isEmpty(menuNodeDTO.getSubMenuList())) {
            return;
        }
        menuNodeDTO.getSubMenuList().stream().forEach(subMenuNode -> setSelectedFlag(parentId, subMenuNode));
    }

}