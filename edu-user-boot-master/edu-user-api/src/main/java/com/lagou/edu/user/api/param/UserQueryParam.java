package com.lagou.edu.user.api.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserQueryParam implements Serializable {
    private Integer currentPage;
    private Integer pageSize;
    private String phone;
    private Integer userId;
    private Date startCreateTime;
    private Date endCreateTime;
}
