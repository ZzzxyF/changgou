package com.zxy.seckill.controller;

import com.zxy.entity.DateUtil;
import com.zxy.seckill.entity.TbSeckillGoods;
import com.zxy.seckill.service.TbSeckillGoodsService;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * (TbSeckillGoods)表控制层
 *
 * @author makejava
 * @since 2023-04-23 00:38:20
 */
@RestController
@CrossOrigin
@RequestMapping("tbSeckillGoods")
public class TbSeckillGoodsController {
    /**
     * 服务对象
     */
    @Resource
    private TbSeckillGoodsService tbSeckillGoodsService;

    /**
     * 秒杀时间菜单
     * @return
     */
    @RequestMapping(value = "/menus")
    public List<Date> dateMenu(){
      return DateUtil.getDateMenus();
    }

    /**
     * 秒杀商品列表：从redis中获取当前秒杀商品列表
     * @param time
     * @return
     */
    @RequestMapping(value = "/list")
    public List<TbSeckillGoods> list(String time){
      return   tbSeckillGoodsService.queryTbSeckillGoodsByRedis(time);
    }

    /**
     * 查看商品详情
     * @param time
     * @param id
     * @return
     */
    @RequestMapping(value = "/one")
    public TbSeckillGoods one(String time,Long id){
        //调用Service查询商品详情
        return tbSeckillGoodsService.one(time,id);
    }




}

