package com.lagou.edu.authority.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lagou.edu.authority.entity.po.Resource;
import com.lagou.edu.authority.entity.po.ResourceCategory;
import com.lagou.edu.authority.mapper.ResourceCategoryMapper;
import com.lagou.edu.authority.service.IResourceCategoryService;
import com.lagou.edu.authority.service.IResourceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author : chenrg
 * @create 2020/7/14 19:43
 **/
@Service
@Slf4j
public class ResourceCategoryService extends ServiceImpl<ResourceCategoryMapper, ResourceCategory> implements IResourceCategoryService {

    @Autowired
    private IResourceService resourceService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteById(Integer id) {
        List<Resource> resources = resourceService.getByCategoryId(id);
        if (CollectionUtils.isNotEmpty(resources)) {
            throw new RuntimeException("资源分类下有资源信息，不允许删除!");
        }
        return this.removeById(id);
    }
}
