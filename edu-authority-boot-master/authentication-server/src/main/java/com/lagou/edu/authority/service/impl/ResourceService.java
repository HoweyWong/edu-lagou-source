package com.lagou.edu.authority.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.lagou.edu.auth.client.dto.AllocateRoleResourceDTO;
import com.lagou.edu.auth.client.param.ResourceQueryParam;
import com.lagou.edu.authority.entity.po.Resource;
import com.lagou.edu.authority.entity.po.RoleResource;
import com.lagou.edu.authority.mapper.ResourceMapper;
import com.lagou.edu.authority.service.IResourceService;
import com.lagou.edu.authority.service.NewMvcRequestMatcher;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ResourceService extends ServiceImpl<ResourceMapper, Resource> implements IResourceService {

    @Autowired
    private HandlerMappingIntrospector mvcHandlerMappingIntrospector;

    @Autowired
    private ResourceMapper resourceMapper;

    @Autowired
    private RoleResourceService roleResourceService;

    /**
     * 系统中所有资源url转化成的RequestMatcher集合，用于匹配请求中的url
     */
    private static final Set<MvcRequestMatcher> resourceConfigAttributes = new HashSet<>();

    @Override
    public synchronized void loadResource() {
        List<Resource> resources = resourceMapper.selectList(new QueryWrapper<>());
        resources.stream().forEach(resource -> resourceConfigAttributes.add(this.newMvcRequestMatcher(resource.getUrl())));
        log.debug("init resourceConfigAttributes:{}", resourceConfigAttributes);
    }

    @Override
    public boolean matchRequestUrl(HttpServletRequest authRequest) {
        // 能找到匹配的url就返回true。不比对method域
        return resourceConfigAttributes.stream().filter(requestMatcher -> requestMatcher.matches(authRequest)).count() > 0;
    }

    @Override
    public List<Resource> queryByRoleIds(Set<Integer> roleIds) {
        return resourceMapper.queryByRoleIds(roleIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveOrUpdate(Resource resource) {
        boolean success = super.saveOrUpdate(resource);
        if (success) {
            // 更新缓存
            resourceConfigAttributes.add(this.newMvcRequestMatcher(resource.getUrl()));
        }
        return success;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteWithAssociation(Integer id) {
        Resource resource = this.getById(id);
        roleResourceService.removeByResourceId(id);
        boolean success = this.removeById(id);
        if (success) {
            resourceConfigAttributes.remove(this.newMvcRequestMatcher(resource.getUrl()));
        }
        return success;
    }

    @Override
    public Page<Resource> getResourcePages(ResourceQueryParam resourceQueryParam) {
        Page<Resource> page = new Page<>(resourceQueryParam.getCurrent(), resourceQueryParam.getSize());
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper
                .eq(Objects.nonNull(resourceQueryParam.getId()), "id", resourceQueryParam.getId())
                .like(StringUtils.isNotBlank(resourceQueryParam.getName()), "name", resourceQueryParam.getName())
                .like(StringUtils.isNotBlank(resourceQueryParam.getUrl()), "url", resourceQueryParam.getUrl())
                .eq(Objects.nonNull(resourceQueryParam.getCategoryId()), "category_id", resourceQueryParam.getCategoryId())
                .ge(Objects.nonNull(resourceQueryParam.getStartCreateTime()), "created_time", resourceQueryParam.getStartCreateTime())
                .le(Objects.nonNull(resourceQueryParam.getEndCreateTime()), "created_time", resourceQueryParam.getEndCreateTime())
                .orderByDesc("id");
        return this.page(page, queryWrapper);
    }

    @Override
    public boolean matchUserResources(Set<Integer> roleIds, HttpServletRequest request) {
        boolean existInResources = this.matchRequestUrl(request);
        if (!existInResources) {
            log.info("url未在资源池中找到，拒绝访问: url:{}", request.getServletPath());
            return false;
        }
        List<Resource> resources = this.queryByRoleIds(roleIds);
        for (Resource resource : resources) {
            NewMvcRequestMatcher matcher = this.newMvcRequestMatcher(resource.getUrl());
            if (matcher.matches(request)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Resource> getByCategoryId(Integer categoryId) {
        QueryWrapper<Resource> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category_id", categoryId);
        return this.list(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void allocateRoleResources(AllocateRoleResourceDTO allocateRoleResourceDTO) {
        if (CollectionUtils.isEmpty(allocateRoleResourceDTO.getResourceIdList())) {
            // 表示删除所有角色资源
            allocateRoleResourceDTO.setResourceIdList(Lists.newArrayList());
        }
        // 已分配的资源ID列表
        Set<Integer> roleResourceIds = roleResourceService.queryByRoleId(allocateRoleResourceDTO.getRoleId());
        // 当前准备分配的资源ID列表
        Set<Integer> allocateRoleResourceIds = Sets.newHashSet(allocateRoleResourceDTO.getResourceIdList());

        // 本次要删除的角色-资源关系
        Set<Integer> needToDel = roleResourceIds.stream().filter(resourceId -> !allocateRoleResourceIds.contains(resourceId)).collect(Collectors.toSet());
        // 本次要新增的角色-资源关系
        Set<Integer> needToInsert = allocateRoleResourceIds.stream().filter(resourceId -> !roleResourceIds.contains(resourceId)).collect(Collectors.toSet());

        if (CollectionUtils.isNotEmpty(needToDel)) {
            roleResourceService.removeByRoleIdAndResourceIds(allocateRoleResourceDTO.getRoleId(), needToDel);
        }

        if (CollectionUtils.isNotEmpty(needToInsert)) {
            List<RoleResource> roleResources = needToInsert.stream().map(resourceId -> {
                RoleResource roleResource = new RoleResource();
                roleResource.setRoleId(allocateRoleResourceDTO.getRoleId());
                roleResource.setResourceId(resourceId);
                roleResource.setCreatedBy(allocateRoleResourceDTO.getCreatedBy());
                roleResource.setUpdatedBy(allocateRoleResourceDTO.getUpdatedBy());
                roleResource.setCreatedTime(new Date());
                roleResource.setUpdatedTime(new Date());
                return roleResource;
            }).collect(Collectors.toList());
            roleResourceService.saveBatch(roleResources);
        }
    }

    /**
     * 创建RequestMatcher
     *
     * @param url
     * @return
     */
    private NewMvcRequestMatcher newMvcRequestMatcher(String url) {
        return new NewMvcRequestMatcher(mvcHandlerMappingIntrospector, url, null);
    }
}