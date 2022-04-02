package com.lagou.edu.auth.client.provider.fallback;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lagou.edu.auth.client.dto.AllocateRoleResourceDTO;
import com.lagou.edu.auth.client.dto.ResourceCategoryDTO;
import com.lagou.edu.auth.client.dto.ResourceCategoryNodeDTO;
import com.lagou.edu.auth.client.dto.ResourceDTO;
import com.lagou.edu.auth.client.param.ResourceQueryParam;
import com.lagou.edu.auth.client.provider.ResourceProvider;
import com.lagou.edu.common.entity.vo.Result;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author : chenrg
 * @create 2020/7/17 10:46
 **/
@Slf4j
@Component
public class ResourceProviderFallbackFactory implements FallbackFactory<ResourceProvider> {

    @Override
    public ResourceProvider create(Throwable throwable) {
        return new ResourceProvider() {
            @Override
            public Result<Boolean> saveOrUpdate(ResourceDTO resourceDTO) {
                log.error("Save or update resource failed. params:{}", resourceDTO, throwable);
                return Result.fail();
            }

            @Override
            public Result<ResourceDTO> getById(Integer id) {
                log.error("Get resource by id failed. id:{}", id, throwable);
                return Result.fail();
            }

            @Override
            public Result<Boolean> delete(Integer id) {
                log.error("Delete resource by id failed. id:{}", id, throwable);
                return Result.fail();
            }

            @Override
            public Result<List<ResourceDTO>> getAll() {
                log.error("Get all resources failed.", throwable);
                return Result.fail();
            }

            @Override
            public Result<Page<ResourceDTO>> getResourcePages(ResourceQueryParam resourceQueryParam) {
                log.error("Get resources paging failed. params:{}", resourceQueryParam, throwable);
                return Result.fail();
            }

            @Override
            public Result<Boolean> saveOrUpdateCategory(ResourceCategoryDTO resourceCategoryDTO) {
                log.error("Save or update resource category failed. params:{}", resourceCategoryDTO, throwable);
                return Result.fail();
            }

            @Override
            public Result<Boolean> deleteCategoryById(Integer id) {
                log.error("Delete resource category failed. id:{}", id, throwable);
                return Result.fail();
            }

            @Override
            public Result<List<ResourceCategoryDTO>> getAllCategories() {
                log.error("Get all resource categories failed.", throwable);
                return Result.fail();
            }

            @Override
            public Result<Boolean> allocateRoleResources(AllocateRoleResourceDTO allocateRoleResourceDTO) {
                log.error("Allocate role resources failed. params:{}", allocateRoleResourceDTO, throwable);
                return Result.fail();
            }

            @Override
            public Result<List<ResourceCategoryNodeDTO>> getRoleResources(Integer roleId) {
                log.error("Get role resources failed. roleId:{}", roleId, throwable);
                return Result.fail();
            }
        };
    }
}
