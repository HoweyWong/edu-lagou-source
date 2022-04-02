package com.lagou.edu.front.course.model.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class PurchasedRecordForAppResult {
    @ApiModelProperty("课程购买记录列表")
    List<AllCoursePurchasedRecordRespVo> allCoursePurchasedRecord;
}
