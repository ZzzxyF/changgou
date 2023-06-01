package com.zxy.order.service;

import com.zxy.entity.Result;
import com.zxy.order.pojo.OrderItem;

import java.util.List;

public interface CartService {
    //添加购物车
    public Result add(Integer number, Long id, String username);
    //查询购物车orderItem
    public List<OrderItem> orderList(String username);

}
