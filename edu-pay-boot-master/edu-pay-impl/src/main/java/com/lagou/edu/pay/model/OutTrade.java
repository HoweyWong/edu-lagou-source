package com.lagou.edu.pay.model;

import java.util.Date;
import java.util.Map;

import com.lagou.edu.pay.api.enums.Status;

import lombok.Builder;
import lombok.Data;

/**
 * @author Piaoxu
 * @since 2019/4/1-15:25
 **/

@Data
@Builder
public class OutTrade {
    private String orderNo;
    private String outTradeNo;
    private Status status;
    private String msg;
    private String buyId;
    private Date payTime;
    private Map<String, String> extra;
}
