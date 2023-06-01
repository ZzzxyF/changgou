package com.zxy.order.service.impl;

import com.zxy.entity.Result;
import com.zxy.entity.StatusCode;
import com.zxy.feign.SkuFeign;
import com.zxy.feign.SpuFeign;
import com.zxy.order.pojo.OrderItem;
import com.zxy.order.service.CartService;
import com.zxy.pojo.Sku;
import com.zxy.pojo.Spu;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class CartServiceImpl implements CartService {
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    SkuFeign skuFeign;
    @Autowired
    SpuFeign spuFeign;

    private static String CartKey="Cart_";

    //添加购物车的方法

    /**
     * @param number   商品数量
     * @param id       商品id
     * @param username 用户
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result add(Integer number, Long id, String username) {
        OrderItem orderItem=null;
        if (number == null || id == null || StringUtils.isEmpty(username)) {
            throw new RuntimeException("参数异常");
        }
        //商品删除
        if(number<=0){
            redisTemplate.opsForHash().delete(CartKey+username,id);
            return new Result(true,StatusCode.OK,"删除购物车成功");
        }
        try {
            Result<Sku> skuResult = skuFeign.findById(id);
            if (skuResult != null && skuResult.isFlag()) {
                Sku sku = skuResult.getData();
                Result<Spu> spuResult = spuFeign.findById(sku.getSpuId());
                 orderItem=sku2ToOrderItem(sku,spuResult.getData(),number);
            }
        } catch (Exception e) {
           return new Result(false, StatusCode.ERROR,"添加购物车异常");
        }
        if(!ObjectUtils.isEmpty(orderItem)){
            redisTemplate.opsForHash().put(CartKey+username,id,orderItem);
        }
        return new Result(true,StatusCode.OK,"添加购物车成功");
    }

    @Override
    public List<OrderItem> orderList(String username) {
       List<OrderItem> orderItems= Lists.newArrayList();
        if(!StringUtils.isEmpty(username)){
            orderItems=redisTemplate.opsForHash().values(CartKey+username);
        }
        return orderItems;
    }


    private OrderItem sku2ToOrderItem(Sku sku, Spu spu, Integer number) {
        OrderItem orderItem = new OrderItem();
        orderItem.setSkuId(sku.getId());
        orderItem.setSpuId(spu.getId());
        orderItem.setName(sku.getName());
        orderItem.setPrice(sku.getPrice());
        orderItem.setNum(number);
        orderItem.setMoney(number* orderItem.getPrice());//设置商品金额
        orderItem.setPayMoney(number*orderItem.getPrice());//设置支付金额
        orderItem.setImage(sku.getImage());
        orderItem.setWeight(sku.getWeight()*number);
        orderItem.setCategoryId1(spu.getCategory1Id());
        orderItem.setCategoryId2(spu.getCategory2Id());
        orderItem.setCategoryId3(spu.getCategory3Id());
        return orderItem;
    }
}
