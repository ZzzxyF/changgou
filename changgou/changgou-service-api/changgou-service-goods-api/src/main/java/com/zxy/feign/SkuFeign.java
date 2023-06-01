package com.zxy.feign;

import com.zxy.entity.Result;
import com.zxy.pojo.Sku;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient("goods")
public interface SkuFeign {
    //根据审核状态查询sku信息
    @GetMapping("/sku/status/{status}")
    public Result<List<Sku>> findByStatus(@PathVariable String status);

    @PostMapping(value = "/sku/search" )
    public Result<List<Sku>> findList(@RequestBody(required = false)  Sku sku);

    @GetMapping("/sku/{id}")
    public Result<Sku> findById(@PathVariable Long id);

    @PostMapping(value = "/sku/decr/count")
    public Result decrCount(@RequestParam(value = "username") String username);

    //增加库存
    @PostMapping("sku/incr/count")
    public Result inCrCount(@RequestParam("username") String username);
}
