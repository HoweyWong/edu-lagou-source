package com.lagou.edu.pay.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Piaoxu
 * @since 2019/4/2-19:13
 **/
public class UrlUtils {

    public static String encode(Map<String,String> map){
        return map.keySet().stream().map(k->{
            try {
                String v = URLEncoder.encode(map.get(k),"UTF-8");
                return k+"="+v;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return null;
        }).collect(Collectors.joining("&"));
    }
}
