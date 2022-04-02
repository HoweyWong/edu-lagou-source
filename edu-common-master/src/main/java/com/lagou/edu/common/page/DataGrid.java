package com.lagou.edu.common.page;

import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: ma wei long
 * @date:   2020年6月30日 下午8:58:30
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("分页数据结果集 VO")
public class DataGrid<T>{

    @ApiModelProperty("总记录数")
    private Long total = 0L;
    
    @ApiModelProperty("总页数数")
    private Long totalPages = 0L;
    
    @ApiModelProperty("总页数数")
    private Long currentPage = 0L;

    @ApiModelProperty("本页数据记录")
    private List<T> rows = new ArrayList<>();
}
