package com.zxy.users;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import tk.mybatis.spring.annotation.MapperScan;


@SpringBootApplication
@EnableEurekaClient
@MapperScan("com.zxy.users.dao")
public class UsersApplication {

    public static void main(String[] args) {
        SpringApplication.run(UsersApplication.class,args);
    }
}
