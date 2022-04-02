package com.lagou.edu.front.user.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel("用户登录响应VO")
public class LoginResponeVo implements java.io.Serializable {

    @ApiModelProperty(name = "token", value = "token", required = true)
    private String token;
}
