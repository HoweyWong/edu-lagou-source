package com.lagou.edu.front.user.vo.request;

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
@ApiModel("用户登录请求VO")
public class LoginRequestVo implements java.io.Serializable {

    @ApiModelProperty(name = "phone", value = "手机号", required = true)
    private String phone;
    @ApiModelProperty(name = "password", value = "密码")
    private String password;
    @ApiModelProperty(name = "code", value = "验证码")
    private String code;
}
