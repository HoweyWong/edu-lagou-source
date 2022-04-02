package com.lagou.edu.pay.seata;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import io.seata.core.context.RootContext;
/**
 * @author: ma wei long
 * @date:   2020年7月28日 上午12:28:08
 */
@Component
public class RequestHeaderInterceptor implements RequestInterceptor {
	
    @Override
    public void apply(RequestTemplate template) {
        String xid = RootContext.getXID();
        if(StringUtils.isNotBlank(xid)){
            template.header("Fescar-Xid",xid);
        }
    }
}