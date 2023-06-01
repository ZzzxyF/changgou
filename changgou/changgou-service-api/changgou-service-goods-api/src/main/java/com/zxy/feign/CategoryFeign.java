package com.zxy.feign;


import com.zxy.entity.Result;
import com.zxy.pojo.Category;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("goods")
public interface CategoryFeign {
    @GetMapping("/category/{id}")
    public Result<Category> findById(@PathVariable Integer id);
}
