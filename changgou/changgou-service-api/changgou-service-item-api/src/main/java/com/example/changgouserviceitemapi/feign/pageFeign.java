package com.example.changgouserviceitemapi.feign;

import com.zxy.entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("item")
public interface pageFeign {

    @GetMapping("/page/createHtml/{id}")
    public Result createHtml(@PathVariable("id") Long id);
}
