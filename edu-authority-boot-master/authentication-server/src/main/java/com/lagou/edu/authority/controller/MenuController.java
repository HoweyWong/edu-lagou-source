package com.lagou.edu.authority.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lagou.edu.auth.client.dto.AllocateRoleMenuDTO;
import com.lagou.edu.auth.client.dto.MenuDTO;
import com.lagou.edu.auth.client.dto.MenuNodeDTO;
import com.lagou.edu.auth.client.param.MenuQueryParam;
import com.lagou.edu.authority.entity.po.Menu;
import com.lagou.edu.authority.service.IMenuService;
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
import java.util.stream.Collectors;

/**
 * 菜单管理功能
 *
 * @author : chenrg
 * @create 2020/7/13 11:18
 **/
@Api("菜单管理")
@Slf4j
@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private IMenuService menuService;


    @ApiOperation(value = "保存或新增菜单", notes = "保存或新增菜单")
    @PostMapping(value = "/saveOrUpdate")
    public Result<Boolean> saveOrUpdate(@RequestBody MenuDTO menuDTO) {
        Menu menu = ConvertUtils.convert(menuDTO, Menu.class);
        menu.setUpdatedTime(new Date());
        if (menu.getParentId() == -1) {
            menu.setLevel(0);
        } else {
            Menu parentMenu = menuService.getById(menu.getParentId());
            menu.setLevel(parentMenu.getLevel() + 1);
        }
        return Result.success(menuService.saveOrUpdate(menu));
    }

    @ApiOperation(value = "根据ID查询菜单", notes = "根据ID查询菜单")
    @GetMapping("/{id}")
    public Result<MenuDTO> getById(@PathVariable Integer id) {
        MenuDTO menuDTO = ConvertUtils.convert(menuService.getById(id), MenuDTO.class);
        return Result.success(menuDTO);
    }

    @ApiOperation(value = "删除菜单", notes = "根据id删除菜单")
    @DeleteMapping(value = "/{id}")
    public Result<Boolean> delete(@PathVariable Integer id) {
        return Result.success(menuService.deleteWithAssociation(id));
    }


    @ApiOperation(value = "获取所有菜单", notes = "获取所有菜单")
    @GetMapping("/getAll")
    public Result<List<MenuDTO>> getAll() {
        return Result.success(ConvertUtils.convertList(menuService.list(), MenuDTO.class));
    }

    @ApiOperation(value = "根据查询条件分页查询菜单")
    @PostMapping("/getMenuPages")
    public Result<Page<MenuDTO>> getMenuPages(@RequestBody MenuQueryParam menuQueryParam) {
        log.info("Get menu list with paging. params:{}", menuQueryParam);
        Page<Menu> selectedPage = menuService.getMenuPages(menuQueryParam);
        Page<MenuDTO> menuDTOPage = new Page<>();
        BeanUtils.copyProperties(selectedPage, menuDTOPage);
        if (CollectionUtils.isNotEmpty(selectedPage.getRecords())) {
            List<MenuDTO> menuDTOList = selectedPage.getRecords().stream()
                    .map(menu -> ConvertUtils.convert(menu, MenuDTO.class)).collect(Collectors.toList());
            menuDTOPage.setRecords(menuDTOList);
        }
        return Result.success(menuDTOPage);
    }

    @ApiOperation("/是否显示开关")
    @PostMapping("/switchShown")
    public Result<Boolean> switchShown(@RequestBody MenuDTO menuDTO) {
        return Result.success(menuService.switchShown(ConvertUtils.convert(menuDTO, Menu.class)));
    }

    @ApiOperation("返回菜单树")
    @GetMapping("/getMenuNodeList")
    public Result<List<MenuNodeDTO>> getMenuNodeList() {
        return Result.success(menuService.getMenuNodeList());
    }

    @ApiOperation("获取角色分配的菜单列表")
    @GetMapping("/getByRoleId")
    Result<List<MenuDTO>> getByRoleId(@RequestParam("roleId") Integer roleId) {
        return Result.success(ConvertUtils.convertList(menuService.getByRoleIdIgnoreIsShown(roleId), MenuDTO.class));
    }

    @ApiOperation("给角色分配菜单")
    @PostMapping("/allocateRoleMenus")
    Result<Boolean> allocateRoleMenus(@RequestBody AllocateRoleMenuDTO allocateRoleMenuDTO) {
        log.info("Allocate role menus with params:{}", allocateRoleMenuDTO);
        menuService.allocateRoleMenus(allocateRoleMenuDTO);
        return Result.success(Boolean.TRUE);
    }
}
