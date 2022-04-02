package com.lagou.edu.common.uuid;

import java.util.UUID;

/**
 * @Description: UUID生成器
 * @Author zhang
 * @Date 2014-12-23 下午4:52:10
 */
public class UUIDHexGenerator {

    public static String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

}
