package com.zxy.seckill.config;

import com.google.common.collect.Maps;
import com.zxy.entity.DateUtil;
import com.zxy.seckill.dao.TbSeckillGoodsDao;
import com.zxy.seckill.entity.TbSeckillGoods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author DanChe
 *
 */
@Configuration
public class SeckillCornTask {

    /**
     * 定时任务将秒杀商品加入redis中
     * 30s执行一次
     */
    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    private TbSeckillGoodsDao seckillGoodsMapper;
    @Value("${flashSaleKey}")
    private String flashSaleKey;

    @Value("${SeckillGoodsCountList}")
    public String SeckillGoodsCountList;
    @Bean
    @Scheduled(cron = "0/30 * * * * ?")
    public void loadGoodsPushRedis(){
        List<Date> dateMenus= DateUtil.getDateMenus();
        //根据时间段查询每个时间段的商品集合
        for(Date startTime:dateMenus){
            String extName = DateUtil.data2str(startTime,DateUtil.PATTERN_YYYYMMDDHH);
            Map<String,Object> paraMap= Maps.newHashMap();
            paraMap.put("status",1);
            paraMap.put("stockCount",0);//库存>0
            paraMap.put("startTime",startTime);
            paraMap.put("endTime",DateUtil.addDateHour(startTime,2));
            //清空以前加载到redis中商品数据-->清空之前时间段这个定时的商品数据，查询之前的商品id
            Set keys=redisTemplate.boundHashOps(flashSaleKey + extName).keys();

            if(keys!=null&&keys.size()>0){
                paraMap.put("ids",keys);
            }
            List<TbSeckillGoods> SeckillGoodsList=seckillGoodsMapper.querySeckillGoods(paraMap);
            //将商品存入redis
            for(TbSeckillGoods seckillGood : SeckillGoodsList){
                //map   "SeckillGoods_"+extName    id,商品
                redisTemplate.boundHashOps(flashSaleKey+extName).put(seckillGood.getId(),seckillGood);
                redisTemplate.expireAt(flashSaleKey+extName,DateUtil.addDateHour(startTime, 2));

                //为了避免多线程并发下单，商品异常问题，需要根据商品数量创建队列，将商品信息放置于队列中
                //有多少个商品就放这个商品多少次id
                Long[] countArr=   pushGoodsArray(seckillGood.getId(),seckillGood.getStockCount());
                redisTemplate.opsForList().leftPushAll(SeckillGoodsCountList+seckillGood.getId(),countArr);
            }
        }

    }
    public Long[]  pushGoodsArray(long id,int number){
        Long[] arr=new Long[number];
        for(int i=0;i<number;i++){
            arr[i]=id;
        }
            return arr;
    }
}
