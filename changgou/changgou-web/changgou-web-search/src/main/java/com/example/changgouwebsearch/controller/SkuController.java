package com.example.changgouwebsearch.controller;

import com.changgou.search.feign.SkuFeign;
import com.changgou.search.pojo.SkuInfo;
import com.zxy.entity.Page;
import com.zxy.entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@Controller
@RequestMapping("/search")
public class SkuController {
    @Autowired
    SkuFeign skuFeign;
    @GetMapping(value = "/list")
    public String search(@RequestParam(required = false) Map searchMap, Model model){
        try {
            Map  skuData=  skuFeign.searchSkuInfo(searchMap);
            model.addAttribute("result", skuData);//搜索返回的结果集合
            model.addAttribute("searchMap",searchMap);//搜索条件集合
            //记录这次搜索的url给下次使用
            model.addAttribute("url",url(searchMap));
            Page<SkuInfo> page=new Page<SkuInfo>(Long.parseLong(skuData.get("totalPages").toString()),Integer.parseInt(skuData.get("pageNumber").toString()),Integer.parseInt(skuData.get("pageSize").toString()));
            model.addAttribute("page",page);//设置分页实现
            } catch (IOException e) {
            e.printStackTrace();
        }
        return "search";
    }


  /*  private String url(Map<String, String> searchMap) {// { spec_网络:"移动4G","keywords":"华为"}
        String url = "/search/list"; // a/b?id=1&
        if (searchMap != null) {
            url += "?";
            for (Map.Entry<String, String> stringStringEntry : searchMap.entrySet()) {
                //如果是排序 则 跳过 拼接排序的地址 因为有数据
                if(stringStringEntry.getKey().equals("sortField") || stringStringEntry.getKey().equals("sortRule")){
                    continue;
                }
                url += stringStringEntry.getKey() + "=" + stringStringEntry.getValue() + "&";

            }
            if(url.lastIndexOf("&")!=-1)
                url = url.substring(0, url.lastIndexOf("&"));
        }
        return url;
    }*/
    //记录这次的url
    private String url(Map<String, String> searchMap) {
        String url = "/search/list";
        if(searchMap!=null && searchMap.size()>0){
            url+="?";
            for (Map.Entry<String, String> stringStringEntry : searchMap.entrySet()) {
                String key = stringStringEntry.getKey();// keywords / brand  / category
                String value = stringStringEntry.getValue();//华为  / 华为  / 笔记本
                if(key.equals("pageNum")){
                    continue;
                }
                url+=key+"="+value+"&";
            }

            //去掉多余的&
            if(url.lastIndexOf("&")!=-1){
                url =  url.substring(0,url.lastIndexOf("&"));
            }

        }
        return url;
    }
}
