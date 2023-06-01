package com.zxy.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author DanChe
 */
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients(basePackages = {"com.zxy.feign","com.zxy.users.feign"})
@MapperScan("com.zxy.order.dao")
public class OrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class,args);
    }
}
