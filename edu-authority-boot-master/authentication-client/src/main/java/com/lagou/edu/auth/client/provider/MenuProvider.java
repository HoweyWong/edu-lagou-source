package com.lagou.edu.auth.client.provider;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lagou.edu.auth.client.dto.AllocateRoleMenuDTO;
import com.lagou.edu.auth.client.dto.MenuDTO;
import com.lagou.edu.auth.client.dto.MenuNodeDTO;
import com.lagou.edu.auth.client.param.MenuQueryParam;
import com.lagou.edu.auth.client.provider.fallback.MenuProviderFallbackFactory;
import com.lagou.edu.common.entity.vo.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜单接口
 *
 * @author : chenrg
 * @create 2020/7/13 12:22
 **/
@Component
@FeignClient(name = "${remote.feign.edu-authority-boot.name:edu-authority-boot}", path = "/menu",
        fallbackFactory = MenuProviderFallbackFactory.class)
public interface MenuProvider {


    /**
     * 保存或更新菜单
     *
     * @param menuDTO
     * @return
     */
    @PostMapping("/saveOrUpdate")
    Result<Boolean> saveOrUpdate(@RequestBody MenuDTO menuDTO);

    /**
     * 根据ID查询
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    Result<MenuDTO> getById(@PathVariable("id") Integer id);

    /**
     * 根据id删除
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    Result<Boolean> deleteById(@PathVariable("id") Integer id);

    /**
     * 获取所有菜单
     *
     * @return
     */
    @GetMapping("/getAll")
    Result<List<MenuDTO>> getAll();

    /**
     * 分页查询菜单
     *
     * @param menuQueryParam
     * @return
     */
    @PostMapping("/getMenuPages")
    Result<Page<MenuDTO>> getMenuPages(@RequestBody MenuQueryParam menuQueryParam);

    /**
     * 是否显示开关
     *
     * @param menuDTO
     * @return
     */
    @PostMapping("/switchShown")
    Result<Boolean> switchShown(@RequestBody MenuDTO menuDTO);

    /**
     * 获取所有菜单并按层级展示
     *
     * @return
     */
    @GetMapping("/getMenuNodeList")
    Result<List<MenuNodeDTO>> getMenuNodeList();

    /**
     * 根据角色ID查询该角色拥有的菜单
     *
     * @param roleId
     * @return
     */
    @GetMapping("/getByRoleId")
    Result<List<MenuDTO>> getByRoleId(@RequestParam("roleId") Integer roleId);

    /**
     * 给角色分配菜单
     *
     * @param allocateRoleMenuDTO
     * @return
     */
    @PostMapping("/allocateRoleMenus")
    Result<Boolean> allocateRoleMenus(@RequestBody AllocateRoleMenuDTO allocateRoleMenuDTO);
}
