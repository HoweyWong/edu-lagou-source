package com.lagou.edu.common.color;

import java.util.Random;

/**
 * 颜色工具类
 */
public final class ColorUtils {

    /**
     * 获取随机的颜色
     *
     * @return 返回颜色, 如#00b38a
     */
    public static String getRandColorCode() {
        String r, g, b;
        Random random = new Random();
        r = Integer.toHexString(random.nextInt(256)).toUpperCase();
        g = Integer.toHexString(random.nextInt(256)).toUpperCase();
        b = Integer.toHexString(random.nextInt(256)).toUpperCase();

        r = r.length() == 1 ? "0" + r : r;
        g = g.length() == 1 ? "0" + g : g;
        b = b.length() == 1 ? "0" + b : b;

        return "#" + r + g + b;
    }
}
