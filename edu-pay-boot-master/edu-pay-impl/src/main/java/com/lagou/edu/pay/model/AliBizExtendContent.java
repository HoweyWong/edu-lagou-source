package com.lagou.edu.pay.model;

import lombok.Builder;
import lombok.Data;

/**
 * @author MrPro
 * @description: 阿里支付中的扩展内容，目前用于信贷信息的扩展
 * @date 2020-02-20 21:20:00
 */
@Data
@Builder
public class AliBizExtendContent {

    // 花呗分期的期数
    private String hb_fq_num;
    // 花呗手续费的承担方
    private String hb_fq_seller_percent;

}
