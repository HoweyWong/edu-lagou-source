package com.lagou.edu.auth.client.provider;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lagou.edu.auth.client.dto.AllocateRoleResourceDTO;
import com.lagou.edu.auth.client.dto.ResourceCategoryDTO;
import com.lagou.edu.auth.client.dto.ResourceCategoryNodeDTO;
import com.lagou.edu.auth.client.dto.ResourceDTO;
import com.lagou.edu.auth.client.param.ResourceQueryParam;
import com.lagou.edu.auth.client.provider.fallback.ResourceProviderFallbackFactory;
import com.lagou.edu.common.entity.vo.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 资源操作接口
 *
 * @author : chenrg
 * @create 2020/7/10 19:12
 **/
@Component
@FeignClient(name = "${remote.feign.edu-authority-boot.name:edu-authority-boot}", path = "/resource",
        fallbackFactory = ResourceProviderFallbackFactory.class)
public interface ResourceProvider {

    /**
     * 新增或更新资源
     *
     * @param resourceDTO
     * @return
     */
    @PostMapping("/saveOrUpdate")
    Result<Boolean> saveOrUpdate(@RequestBody ResourceDTO resourceDTO);

    /**
     * 根据ID获取资源信息
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    Result<ResourceDTO> getById(@PathVariable("id") Integer id);

    /**
     * 根据ID删除资源，同时需要删除角色-资源关联关系
     *
     * @param id
     * @return
     */
    @DeleteMapping(value = "/{id}")
    Result<Boolean> delete(@PathVariable("id") Integer id);

    /**
     * 获取所有资源
     *
     * @return
     */
    @GetMapping("/getAll")
    Result<List<ResourceDTO>> getAll();

    /**
     * 按条件查询资源列表
     *
     * @param resourceQueryParam
     * @return
     */
    @PostMapping("/getResourcePages")
    Result<Page<ResourceDTO>> getResourcePages(@RequestBody ResourceQueryParam resourceQueryParam);

    /**
     * 更新或保存资源分类
     *
     * @param resourceCategoryDTO
     * @return
     */
    @PostMapping("/category/saveOrderUpdate")
    Result<Boolean> saveOrUpdateCategory(@RequestBody ResourceCategoryDTO resourceCategoryDTO);

    /**
     * 删除资源分类
     *
     * @param id
     * @return
     */
    @DeleteMapping("/category/{id}")
    Result<Boolean> deleteCategoryById(@PathVariable("id") Integer id);

    /**
     * 查询所有资源分类
     *
     * @return
     */
    @GetMapping("/category/getAll")
    Result<List<ResourceCategoryDTO>> getAllCategories();

    /**
     * 给角色分配资源
     *
     * @param allocateRoleResourceDTO
     * @return
     */
    @PostMapping("/allocateRoleResources")
    public Result<Boolean> allocateRoleResources(@RequestBody AllocateRoleResourceDTO allocateRoleResourceDTO);

    /**
     * 获取角色分配的资源信息。
     * 会列出所有资源分类及分类下的资源列表，并标记当前角色是否已经拥有该资源
     *
     * @param roleId
     * @return
     */
    @GetMapping("/getRoleResources")
    Result<List<ResourceCategoryNodeDTO>> getRoleResources(@RequestParam("roleId") Integer roleId);
}
