package com.example.changgouserviceitemapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class ChanggouServiceItemApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChanggouServiceItemApiApplication.class, args);
    }

}
