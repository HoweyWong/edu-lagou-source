package com.lagou.edu.authority.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lagou.edu.authority.entity.po.Resource;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
@Mapper
public interface ResourceMapper extends BaseMapper<Resource> {

    /**
     * 根据角色id列表查询关联的资源
     * @param roleIds
     * @return
     */
    List<Resource> queryByRoleIds(@Param("roleIds") Set<Integer> roleIds);
}