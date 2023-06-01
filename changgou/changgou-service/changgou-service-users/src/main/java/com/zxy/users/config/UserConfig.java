package com.zxy.users.config;

import com.zxy.entity.TokenDecode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserConfig {
    @Bean
    public TokenDecode tokenDecode(){
     return new TokenDecode();
    }
}
