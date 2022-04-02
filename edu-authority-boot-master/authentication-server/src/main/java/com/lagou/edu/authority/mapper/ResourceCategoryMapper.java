package com.lagou.edu.authority.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lagou.edu.authority.entity.po.ResourceCategory;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author : chenrg
 * @create 2020/7/14 19:42
 **/
@Repository
@Mapper
public interface ResourceCategoryMapper extends BaseMapper<ResourceCategory> {

}
