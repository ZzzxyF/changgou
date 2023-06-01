package com.changgou.getway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * 网关---》日志，路由，过滤，监听
 * @author DanChe
 */
@SpringBootApplication
@EnableEurekaClient
public class GetWayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GetWayApplication.class,args);
    }
}
