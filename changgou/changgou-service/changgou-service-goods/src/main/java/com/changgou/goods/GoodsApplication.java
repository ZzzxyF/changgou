package com.changgou.goods;

import com.zxy.entity.IdWorker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import tk.mybatis.spring.annotation.MapperScan;

@EnableEurekaClient
@SpringBootApplication
@MapperScan(basePackages = {"com.changgou.goods.dao"})
public class GoodsApplication {

    @Bean
    public IdWorker idWorker(){
     return new IdWorker(0,0);
    }

    public static void main(String[] args) {
        SpringApplication.run(GoodsApplication.class);
    }
}
