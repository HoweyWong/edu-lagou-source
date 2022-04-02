package com.lagou.edu.front.config;

import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BlowfishConfig {

    @Bean
    public SymmetricCrypto blowfish() {
        return new SymmetricCrypto(SymmetricAlgorithm.Blowfish, "blow_fish".getBytes());
    }

//    public static void main(String[] args) {
//        SymmetricCrypto crypto = new SymmetricCrypto(SymmetricAlgorithm.Blowfish, "blow_fish".getBytes());
//        String aaas = crypto.encryptBase64("399471");
//        System.out.println(aaas);
//        String decrypt = crypto.decryptStr(aaas);
//        System.out.println(decrypt);
//    }
}