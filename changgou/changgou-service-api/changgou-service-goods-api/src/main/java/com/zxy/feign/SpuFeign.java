package com.zxy.feign;

import com.zxy.entity.Result;
import com.zxy.pojo.Spu;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("goods")
public interface SpuFeign {
    @GetMapping("/spu/{id}")
    public Result<Spu> findById(@PathVariable Long id);
}
