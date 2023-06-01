package com.zxy.order.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;
/**
 * @author DanChe
 */ //加上这个类后，所有的请求都会被验证是否有令牌，在底层根据令牌信息核对，对应的changgou\changgou key和value只有验证后才能进入controller
@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true,securedEnabled = true)
public class ResourceServerConfigure extends ResourceServerConfigurerAdapter {
    //公钥
    public static String public_key="public.key";

    //jwt钥匙池
    @Bean
    public TokenStore tokenStore(JwtAccessTokenConverter  jwtAccessTokenConverter){
        return new JwtTokenStore(jwtAccessTokenConverter);
    }


    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter(){
        JwtAccessTokenConverter converter=new JwtAccessTokenConverter();
        converter.setVerifierKey(getPubKey());
        return converter;
    }
    //获取公钥的key值
    private String getPubKey(){
        Resource resource=new ClassPathResource(public_key);
        try {
            InputStreamReader reader=new InputStreamReader(resource.getInputStream());
            BufferedReader bf=new BufferedReader(reader);
            return  bf.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    //认证地址配置
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest()
                .authenticated();
    }
}
