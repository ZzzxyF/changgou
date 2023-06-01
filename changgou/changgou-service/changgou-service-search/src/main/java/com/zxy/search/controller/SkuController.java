package com.zxy.search.controller;

import com.zxy.entity.Result;
import com.zxy.entity.StatusCode;
import com.zxy.search.service.SkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

//SKUcontroller
@RestController
@CrossOrigin
@RequestMapping("/search")
public class SkuController {

    @Autowired
    private SkuService skuService;
    //导入数据
    @GetMapping("/import")
    public Result search(){
        skuService.importSku();
        return new Result(true, StatusCode.OK,"导入成功");
    }

   @GetMapping
    public Map searchSkuInfo(@RequestParam(required = false) Map searchMap) throws IOException {
       Object pageNum = searchMap.get("pageNum");
       if(pageNum==null){
           searchMap.put("pageNum","1");
       }
       if(pageNum instanceof Integer){
           searchMap.put("pageNum",pageNum.toString());
       }
        return   skuService.search(searchMap);
    }
}
