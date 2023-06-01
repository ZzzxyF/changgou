package com.zxy.order.service;

import com.github.pagehelper.PageInfo;
import com.zxy.order.pojo.Order;

import java.util.List;

/****
 * @Author:admin
 * @Description:Order业务层接口
 * @Date 2019/6/14 0:16
 *****/
public interface OrderService {

    /***
     * Order多条件分页查询
     * @param order
     * @param page
     * @param size
     * @return
     */
    PageInfo<Order> findPage(Order order, int page, int size);

    /***
     * Order分页查询
     * @param page
     * @param size
     * @return
     */
    PageInfo<Order> findPage(int page, int size);

    /***
     * Order多条件搜索方法
     * @param order
     * @return
     */
    List<Order> findList(Order order);

    /***
     * 删除Order
     * @param id
     */
    void delete(String id);

    /***
     * 修改Order数据
     * @param order
     */
    void update(Order order);

    /***
     * 新增Order
     * @param order
     */
    Order add(Order order);

    /**
     * 根据ID查询Order
     * @param id
     * @return
     */
     Order findById(String id);

    /***
     * 查询所有Order
     * @return
     */
    List<Order> findAll();

    /**
     * 修改订单支付后的状态，订单编号，支付流水号
     */
    void UpdateStatus(String orderId,String transactionId);

    /**
     * 当订单支付失败，需要删除order
     */
    void deleteOrder(String orderId);
}

