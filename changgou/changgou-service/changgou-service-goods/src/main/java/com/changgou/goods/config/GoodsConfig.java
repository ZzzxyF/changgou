package com.changgou.goods.config;

import com.zxy.entity.FeignInterceptor;
import feign.Request;
import feign.Request.Options;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration
public class GoodsConfig {
   @Bean
     public   FeignInterceptor  feignInterceptor(){
      return new FeignInterceptor();
  }

  //修改feign超时时间设置
  @Bean
  public Request.Options options(){
     return new Options(6000,10000);
  }

  @Bean
  public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
    // 创建 RedisTemplate 对象
    RedisTemplate<String, Object> template = new RedisTemplate<>();
    // 设置 RedisConnection 工厂。 它就是实现多种 Java Redis 客户端接入的秘密工厂
    template.setConnectionFactory(factory);
    // 使用 String 序列化方式，序列化 KEY 。
    template.setKeySerializer(RedisSerializer.string());
    // 使用 JSON 序列化方式（库是 Jackson ），序列化 VALUE 。
    template.setValueSerializer(RedisSerializer.json());
    return template;
  }

}
