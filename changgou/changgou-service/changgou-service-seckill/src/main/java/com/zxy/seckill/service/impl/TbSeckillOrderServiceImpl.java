package com.zxy.seckill.service.impl;

import com.zxy.entity.IdWorker;
import com.zxy.seckill.controller.MultiThreadingCreateOrder;
import com.zxy.seckill.dao.TbSeckillGoodsDao;
import com.zxy.seckill.dao.TbSeckillOrderDao;
import com.zxy.seckill.entity.SeckillStatus;
import com.zxy.seckill.entity.TbSeckillOrder;
import com.zxy.seckill.service.TbSeckillOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * (TbSeckillOrder)表服务实现类
 *
 * @author makejava
 * @since 2023-04-23 00:38:26
 */
@Service("tbSeckillOrderService")
public class TbSeckillOrderServiceImpl implements TbSeckillOrderService {

  @Resource
  private TbSeckillOrderDao tbSeckillOrderDao;
  @Autowired
  RedisTemplate redisTemplate;

  @Autowired
  IdWorker idWorker;
  @Autowired
  MultiThreadingCreateOrder multiThreadingCreateOrder;

  @Value("${UserQueueCount}")
  public String UserQueueCount;

  @Value("${SeckillOrderQueue}")
  public String SeckillOrderQueue;

  @Value("${UserQueueStatus}")
  public String UserQueueStatus;

  @Value("${SeckillOrder}")
  public String SeckillOrder;


  @Override
  public TbSeckillOrder queryById(Long id) {
    return this.tbSeckillOrderDao.queryById(id);
  }


  @Override
  public Page<TbSeckillOrder> queryByPage(TbSeckillOrder tbSeckillOrder, PageRequest pageRequest) {
    long total = this.tbSeckillOrderDao.count(tbSeckillOrder);
    return new PageImpl<>(this.tbSeckillOrderDao.queryAllByLimit(tbSeckillOrder, pageRequest), pageRequest, total);
  }

  @Override
  public TbSeckillOrder insert(TbSeckillOrder tbSeckillOrder) {
    this.tbSeckillOrderDao.insert(tbSeckillOrder);
    return tbSeckillOrder;
  }


  @Override
  public TbSeckillOrder update(TbSeckillOrder tbSeckillOrder) {
    this.tbSeckillOrderDao.update(tbSeckillOrder);
    return this.queryById(tbSeckillOrder.getId());
  }

  @Override
  public boolean deleteById(Long id) {
    return this.tbSeckillOrderDao.deleteById(id) > 0;
  }


  @Override
  public Boolean add(Long id, String time, String username) {
    //遗留疑问：这里的防止重复下单，如果是多个商品，username是重复的，那只要一个商品下单了，其他商品就不能秒杀了
    //因为秒杀商品的下单是有顺序的，所以这里以redis的数组控制排队队列,用户点击一次下单，就将订单信息加入队列---》抢单有序

    //防止重复秒杀:一个用户在队列只能有一个
    Long userQueueCount = redisTemplate.opsForHash().increment(UserQueueCount, username, 1L);
    if (userQueueCount > 1) {
      throw new RuntimeException("存在重复抢购");
    }

    //商品信息封装
    SeckillStatus seckillStatus = new SeckillStatus(username, new Date(), 1, id, time);
    //放入队列---》将订单放入队列中
    redisTemplate.opsForList().leftPush(SeckillOrderQueue, seckillStatus);

    //更新抢单状态--》记录当前订单状态
    redisTemplate.opsForHash().put(UserQueueStatus, username, seckillStatus);

    //通过异步方法调用，支持多线程
    multiThreadingCreateOrder.createOrder();
    return true;
  }


  @Override
  public SeckillStatus queryStatus(String username) {
    return (SeckillStatus) redisTemplate.boundHashOps(UserQueueStatus).get(username);
  }


  @Override
  public void updatePayStatus(String out_trade_no, String transaction_id, String username) {
    //获取订单数据
    TbSeckillOrder seckillOrder=(TbSeckillOrder)redisTemplate.opsForHash().get(SeckillOrder,username);
    seckillOrder.setStatus("1");
    seckillOrder.setPayTime(new Date());
    tbSeckillOrderDao.insert(seckillOrder);
    //清空redis缓存
    {
    //排队
    redisTemplate.opsForHash().delete(UserQueueCount,username);
    //状态
    redisTemplate.opsForHash().delete(UserQueueStatus,username);
    //清空订单
    redisTemplate.opsForHash().delete(SeckillOrder,username);
    }

  }

  @Override
  public void closeOrder(String username) {
    TbSeckillOrder seckillOrder=(TbSeckillOrder)redisTemplate.opsForHash().get(SeckillOrder,username);
    SeckillStatus seckillStatus=(SeckillStatus)redisTemplate.opsForHash().get(UserQueueStatus,username);
    //如果Redis中有订单信息，说明用户未支付
    if(seckillStatus!=null && seckillOrder!=null){

    }


  }
}
