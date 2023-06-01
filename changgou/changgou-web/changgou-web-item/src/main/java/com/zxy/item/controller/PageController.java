package com.zxy.item.controller;

import com.zxy.entity.Result;
import com.zxy.entity.StatusCode;
import com.zxy.item.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/page")
public class PageController {
    @Autowired
    private PageService pageService;


    @GetMapping("/createHtml/{id}")
    public Result createHtml(@PathVariable("id") Long id){
        pageService.createPageHtml(id);
        return new Result(true, StatusCode.OK,"ok");
    }

}
