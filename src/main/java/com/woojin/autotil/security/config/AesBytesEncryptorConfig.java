package com.woojin.autotil.security.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.encrypt.AesBytesEncryptor;

@Configuration
public class AesBytesEncryptorConfig {
    @Value("${ENCRYPT_KEY}")
    private String encryptKey;
    @Value("${ENCRYPT_SALT}")
    private String encryptSalt;

    @Bean
    public AesBytesEncryptor aesBytesEncryptor(){
        return new AesBytesEncryptor(encryptKey,encryptSalt);
    }
}
