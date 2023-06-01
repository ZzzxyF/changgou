package com.changgou.getway.config;


import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

//配置限流区分的key,当前限流key--》每个访问的Ip
@Configuration
public class KeyResoverConfig {

    @Bean(name="ipKeyResolver")
    public KeyResolver userKeyResolver(){
        KeyResolver keyResolver=new KeyResolver() {
            @Override
            public Mono<String> resolve(ServerWebExchange exchange) {
                String ip=exchange.getRequest().getRemoteAddress().getAddress().getHostAddress();
                System.out.println("ip====="+ip);
                return Mono.just(ip);
            }
        };
        return keyResolver;
    }
}
