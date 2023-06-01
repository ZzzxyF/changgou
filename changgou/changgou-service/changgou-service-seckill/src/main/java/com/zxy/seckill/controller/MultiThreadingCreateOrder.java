package com.zxy.seckill.controller;

import com.zxy.entity.IdWorker;
import com.zxy.seckill.dao.TbSeckillGoodsDao;
import com.zxy.seckill.entity.SeckillStatus;
import com.zxy.seckill.entity.TbSeckillGoods;
import com.zxy.seckill.entity.TbSeckillOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.Date;

@Component
public class MultiThreadingCreateOrder {

  @Autowired
  IdWorker idWorker;

  @Autowired
  RedisTemplate redisTemplate;

  @Autowired
  private TbSeckillGoodsDao tbSeckillGoodsDao;

  @Value("${UserQueueCount}")
  public String UserQueueCount;

  @Value("${SeckillOrderQueue}")
  public String SeckillOrderQueue;

  @Value("${UserQueueStatus}")
  public String UserQueueStatus;

  @Value("${SeckillGoodsCountList}")
  public String SeckillGoodsCountList;

  //测试并发下单
  @Async
  public void createOrder(){

    //从队列中获取订单信息
    SeckillStatus seckillStatus=(SeckillStatus)redisTemplate.opsForList().rightPop("SeckillOrderQueue");
    //从商品的数据队列取一个商品，如果能取到说明还有库存
    Object sgood=redisTemplate.opsForList().rightPop("SeckillGoodsCountList_"+seckillStatus.getGoodsId());
    if(ObjectUtils.isEmpty(sgood)){
      //清空排队信息
      //清空,防止重复排队的key,当前商品已经抢购完了，可以其他商品抢购
      redisTemplate.opsForHash().delete(UserQueueCount,seckillStatus.getUsername());
      //清空订单状态
      redisTemplate.opsForHash().delete(UserQueueStatus,seckillStatus.getUsername());
      return;
    }

    if(seckillStatus!=null){
      //时间区间
      String time = seckillStatus.getTime();
      //用户登录名
      String username=seckillStatus.getUsername();
      //用户抢购商品
      Long id = seckillStatus.getGoodsId();
      //从redis获取秒杀商品信息
      TbSeckillGoods goods = (TbSeckillGoods) redisTemplate.boundHashOps("SeckillGoods_" + time).get(id);
      //判断是否有库存
      if(goods==null||goods.getStockCount() <=0){
        throw new RuntimeException("库存不足");
      }

      //存在库存，创建订单，存入redis,减少数据库的库存数量
      TbSeckillOrder seckillOrder = new TbSeckillOrder();
      seckillOrder.setId(idWorker.nextId());
      seckillOrder.setSeckillId(id);
      seckillOrder.setMoney(goods.getCostPrice());
      seckillOrder.setUserId(username);
      seckillOrder.setCreateTime(new Date());
      seckillOrder.setStatus("0");
      //创建商品订单到redis
      redisTemplate.opsForHash().put("SeckillOrder",username,seckillOrder);

      //这里是判断秒杀时间段商品是否还有库存，如果没有库存需要将对应时间段的商品删除。如果有更新商品信息
      //mysql数据同步不精确，因为此时可能多个线程数据过来拿到的StockCount都是一样的
      //场景如下，当线程1拿到stockCount是2 ，线程2拿到的stockCount也是2，此时正常情况stockCount-2=0,但是在这里还是1，不会进行数据同步
     /* goods.setStockCount(goods.getStockCount()-1);
      if(goods.getStockCount()<=0){
        //更新数据库数据
        tbSeckillGoodsDao.update(goods);
        //删除redis中对应时间段的商品
        redisTemplate.opsForHash().delete("SeckillGoods_"+time,id);
      }else{
        //当库存还有，更新此时redis商品库存信息
        redisTemplate.boundHashOps("SeckillGoods_" + time).put(id,goods);
      }*/
      //修改如下：根据商品的数量队列进行判断，如果队列数量为0，那么说明可以进行数据库同步了
    Long goodsSize=redisTemplate.boundListOps(SeckillGoodsCountList+seckillStatus.getGoodsId()).size();
    if(goodsSize<=0){
      //更新数据库数据
      goods.setStockCount(goodsSize.intValue());
      tbSeckillGoodsDao.update(goods);
      //删除redis中对应时间段的商品
      redisTemplate.opsForHash().delete("SeckillGoods_"+time,id);
    }else{
      //当库存还有，更新此时redis商品库存信息
      redisTemplate.boundHashOps("SeckillGoods_" + time).put(id,goods);
    }

      //抢单成功，更新抢单状态,排队->等待支付->待支付状态
      seckillStatus.setStatus(2);
      seckillStatus.setOrderId(seckillOrder.getId());
      seckillStatus.setMoney(seckillOrder.getMoney().floatValue());
      redisTemplate.boundHashOps(UserQueueStatus).put(username,seckillStatus);

    }
  }

}
