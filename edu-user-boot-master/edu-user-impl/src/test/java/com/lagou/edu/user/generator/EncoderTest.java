package com.lagou.edu.user.generator;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @Description
 * @Author huanghao
 * @Date 2022-3-25
 * @Version 1.0
 */
public class EncoderTest {
    public static void main(String[] args) {
        final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encode = encoder.encode("123456");
        boolean matches = encoder.matches("123456", "$2a$10$bCuZEtws7Rm39cxFypnCqeHbyoldwPkS6RknO9QLRM..xQWcIJD.y");
        System.out.println(encode);
        System.out.println(matches);
    }
}
