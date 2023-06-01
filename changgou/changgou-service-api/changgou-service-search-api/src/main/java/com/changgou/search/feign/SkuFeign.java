package com.changgou.search.feign;

import com.zxy.entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@FeignClient(name="search")
@RequestMapping("/search")
public interface SkuFeign {
   @GetMapping
    public Map searchSkuInfo(@RequestParam(required = false) Map searchMap) throws IOException ;
}
