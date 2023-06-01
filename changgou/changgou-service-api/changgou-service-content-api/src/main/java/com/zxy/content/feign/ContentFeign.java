package com.zxy.content.feign;

import com.zxy.content.pojo.Content;
import com.zxy.entity.Result;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name="content")
public interface ContentFeign {

    //根据分类id-->查询content
    @GetMapping(value = "/content/list/category/{id}")
    public Result<List<Content>> findByCategory(@PathVariable Long id);
}
