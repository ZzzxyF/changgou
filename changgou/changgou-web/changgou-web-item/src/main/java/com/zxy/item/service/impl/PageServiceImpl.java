package com.zxy.item.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.zxy.entity.Result;
import com.zxy.feign.CategoryFeign;
import com.zxy.feign.SkuFeign;
import com.zxy.feign.SpuFeign;
import com.zxy.item.service.PageService;
import com.zxy.pojo.Sku;
import com.zxy.pojo.Spu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

@Service
public class PageServiceImpl implements PageService {
    @Autowired
    private SpuFeign spuFeign;
    @Autowired
    private SkuFeign skuFeign;
    @Autowired
    private CategoryFeign categoryFeign;

    @Autowired
    private TemplateEngine templateEngine;
    @Value("${pageUrl}")
    private  String pageUrl;

    private Map<String,Object> buildDataModel(Long spuId){
        Map<String,Object> map= Maps.newHashMap();
        //获取spu数据
        Result<Spu> result=spuFeign.findById(spuId);
        Spu spu=result.getData();
        //获取分类数据
        map.put("category1",categoryFeign.findById(spu.getCategory1Id()));
        map.put("category2",categoryFeign.findById(spu.getCategory2Id()));
        map.put("category3",categoryFeign.findById(spu.getCategory3Id()));
        if(spu.getImages()!=null) {
            map.put("imageList", spu.getImages().split(","));
        }
        //获取spec规格参数所有信息
        map.put("specificationList", JSON.parseObject(spu.getSpecItems(),Map.class));
        map.put("spu",spu);
        //获取sku数据
        Sku sku=new Sku();
        sku.setSpuId(spuId);

        Result<List<Sku>> listResult= skuFeign.findList(sku);
        map.put("skuList",listResult);
        return map;
    }


    @Override
    public void createPageHtml(Long spuId) {
        Context context=new Context();
        //获取数据集
        Map<String,Object> mapData=buildDataModel(spuId);
        context.setVariables(mapData );
        //准备生成文件
        File dir=new File(pageUrl);
        if(!dir.exists()){
            dir.mkdirs();
        }
        File dest=new File(dir,spuId+".html");
        // 3.生成页面
        try (PrintWriter writer = new PrintWriter(dest, "UTF-8")) {
            templateEngine.process("item", context, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
