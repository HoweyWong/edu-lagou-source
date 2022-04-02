package com.lagou.edu.auth.client.provider.fallback;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lagou.edu.auth.client.dto.AllocateRoleMenuDTO;
import com.lagou.edu.auth.client.dto.MenuDTO;
import com.lagou.edu.auth.client.dto.MenuNodeDTO;
import com.lagou.edu.auth.client.param.MenuQueryParam;
import com.lagou.edu.auth.client.provider.MenuProvider;
import com.lagou.edu.common.entity.vo.Result;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author : chenrg
 * @create 2020/7/17 10:33
 **/
@Slf4j
@Component
public class MenuProviderFallbackFactory implements FallbackFactory<MenuProvider> {

    @Override
    public MenuProvider create(Throwable throwable) {
        return new MenuProvider() {
            @Override
            public Result<Boolean> saveOrUpdate(MenuDTO menuDTO) {
                log.error("Save or update menu failed. params:{}", menuDTO, throwable);
                return Result.fail();
            }

            @Override
            public Result<MenuDTO> getById(Integer id) {
                log.error("Get menu by id failed. id:{}", id, throwable);
                return Result.fail();
            }

            @Override
            public Result<Boolean> deleteById(Integer id) {
                log.error("Delete menu failed. id:{}", id, throwable);
                return Result.fail();
            }

            @Override
            public Result<List<MenuDTO>> getAll() {
                log.error("Get all menus failed.", throwable);
                return Result.fail();
            }

            @Override
            public Result<Page<MenuDTO>> getMenuPages(MenuQueryParam menuQueryParam) {
                log.error("Get menus paging failed. params:{}", menuQueryParam, throwable);
                return Result.fail();
            }

            @Override
            public Result<Boolean> switchShown(MenuDTO menuDTO) {
                log.error("Switch menu is shown failed. params:{}", menuDTO, throwable);
                return Result.fail();
            }

            @Override
            public Result<List<MenuNodeDTO>> getMenuNodeList() {
                log.error("Get menu node list failed.", throwable);
                return Result.fail();
            }

            @Override
            public Result<List<MenuDTO>> getByRoleId(Integer roleId) {
                log.error("Get menu by roleId failed. roleId:{}", roleId, throwable);
                return Result.fail();
            }

            @Override
            public Result<Boolean> allocateRoleMenus(AllocateRoleMenuDTO allocateRoleMenuDTO) {
                log.error("Allocate role menus failed. params:{}", allocateRoleMenuDTO, throwable);
                return Result.fail();
            }
        };
    }
}
