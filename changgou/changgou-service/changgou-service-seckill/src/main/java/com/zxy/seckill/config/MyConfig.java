package com.zxy.seckill.config;

import com.zxy.entity.IdWorker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author DanChe
 */
@Configuration
public class MyConfig {
    @Bean
    public IdWorker idWorker(){
        return new IdWorker(1,1);
    }
}
