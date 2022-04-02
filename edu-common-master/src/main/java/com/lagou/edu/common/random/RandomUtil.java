
package com.lagou.edu.common.random;

import java.util.Random;

/**
 * @Description: 随机工具类 
 * @Author Cookie
 * @Date 2014年12月12日 下午2:25:00 
 */
public class RandomUtil {
    /**
     * 随机字符串(数字和字母)
     * 
     * @param size
     * @return
     */
    public static String getRandomString(int size) {
        char[] c = { '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', 
                     'q', 'w', 'e', 'r', 't', 'y', 'u', 'i', 'o', 'p', 'a', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'z', 'x', 'c', 'v', 'b', 'n', 'm' };
        Random random = new Random(); // 初始化随机数产生器
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < size; i++) {
            sb.append(c[Math.abs(random.nextInt()) % c.length]);
        }
        return sb.toString();
    }

    /**
     * 随机字符串(数字)
     * 
     * @param size
     * @return
     */
    public static String getRandomNumber(int size) {
        char[] c = { '1', '2', '3', '4', '5', '6', '7', '8', '9', '0' };
        Random random = new Random(); // 初始化随机数产生器
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < size; i++) {
            sb.append(c[Math.abs(random.nextInt()) % c.length]);
        }
        return sb.toString();
    }
}
