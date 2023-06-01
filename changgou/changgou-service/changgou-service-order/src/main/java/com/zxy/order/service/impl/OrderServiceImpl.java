package com.zxy.order.service.impl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zxy.entity.IdWorker;
import com.zxy.feign.SkuFeign;
import com.zxy.order.dao.OrderItemMapper;
import com.zxy.order.dao.OrderMapper;
import com.zxy.order.pojo.Order;
import com.zxy.order.pojo.OrderItem;
import com.zxy.order.service.CartService;
import com.zxy.order.service.OrderService;
import com.zxy.users.feign.UserFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

/****
 * @Author:admin
 * @Description:Order业务层接口实现类
 * @Date 2019/6/14 0:16
 *****/
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private CartService cartService;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    SkuFeign skuFeign;
    @Autowired
    UserFeign userFeign;

    private static String CartKey = "Cart_";

    private static String OrderKey = "Order";


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Order add(Order order) {
        //根据用户信息获取所有购物车的订单信息---》购物车redis中的数据
        List<OrderItem> orderItems = cartService.orderList(order.getUsername());
        int totalMoney = 0;//总金额
        int totalPayMoney = 0;//支付金额
        int num = 0;
        for (OrderItem orderItem : orderItems) {
            totalMoney += orderItem.getMoney();
            totalPayMoney += orderItem.getPayMoney();
            num += orderItem.getNum();
        }
        order.setTotalNum(num);
        order.setTotalMoney(totalMoney);
        order.setPayMoney(totalPayMoney);
        order.setPreMoney(totalMoney - totalPayMoney);//优惠
        order.setCreateTime(new Date());
        order.setUpdateTime(order.getCreateTime());
        order.setBuyerRate("0");        //0:未评价，1：已评价
        order.setSourceType("1");       //来源，1：WEB
        order.setOrderStatus("0");      //0:未完成,1:已完成，2：已退货
        order.setPayStatus("0");        //0:未支付，1：已支付，2：支付失败
        order.setConsignStatus("0");    //0:未发货，1：已发货，2：已收货
        order.setId("NO." + idWorker.nextId());//设置订单id
        //订单信息
        orderMapper.insert(order);
        //订单详情
        for (OrderItem orderItem : orderItems) {
            orderItem.setId("NO." + idWorker.nextId());
            orderItem.setIsReturn("0");
            orderItem.setOrderId(order.getId());
            orderItemMapper.insertSelective(orderItem);
        }
        //减少库存
        skuFeign.decrCount(order.getUsername());
        //增加用户积分
        userFeign.addPoint(10);
        //线上支付，记录订单
      //  if (order.getPayType().equalsIgnoreCase("1")) {
            //将支付记录存入到Reids namespace  key  value
            redisTemplate.boundHashOps(OrderKey).put(order.getId(), order);
        //}
        //不清空购物车是为了回滚商品库存
        //清空redis购物车
        //redisTemplate.delete(CartKey+order.getUsername());
        return order;
    }

    /**
     * Order条件+分页查询
     *
     * @param order 查询条件
     * @param page  页码
     * @param size  页大小
     * @return 分页结果
     */
    @Override
    public PageInfo<Order> findPage(Order order, int page, int size) {
        //分页
        PageHelper.startPage(page, size);
        //搜索条件构建
        Example example = createExample(order);
        //执行搜索
        return new PageInfo<Order>(orderMapper.selectByExample(example));
    }

    /**
     * Order分页查询
     *
     * @param page
     * @param size
     * @return
     */
    @Override
    public PageInfo<Order> findPage(int page, int size) {
        //静态分页
        PageHelper.startPage(page, size);
        //分页查询
        return new PageInfo<Order>(orderMapper.selectAll());
    }

    /**
     * Order条件查询
     *
     * @param order
     * @return
     */
    @Override
    public List<Order> findList(Order order) {
        //构建查询条件
        Example example = createExample(order);
        //根据构建的条件查询数据
        return orderMapper.selectByExample(example);
    }


    /**
     * Order构建查询对象
     *
     * @param order
     * @return
     */
    public Example createExample(Order order) {
        Example example = new Example(Order.class);
        Example.Criteria criteria = example.createCriteria();
        if (order != null) {
            // 订单id
            if (!StringUtils.isEmpty(order.getId())) {
                criteria.andEqualTo("id", order.getId());
            }
            // 数量合计
            if (!StringUtils.isEmpty(order.getTotalNum())) {
                criteria.andEqualTo("totalNum", order.getTotalNum());
            }
            // 金额合计
            if (!StringUtils.isEmpty(order.getTotalMoney())) {
                criteria.andEqualTo("totalMoney", order.getTotalMoney());
            }
            // 优惠金额
            if (!StringUtils.isEmpty(order.getPreMoney())) {
                criteria.andEqualTo("preMoney", order.getPreMoney());
            }
            // 邮费
            if (!StringUtils.isEmpty(order.getPostFee())) {
                criteria.andEqualTo("postFee", order.getPostFee());
            }
            // 实付金额
            if (!StringUtils.isEmpty(order.getPayMoney())) {
                criteria.andEqualTo("payMoney", order.getPayMoney());
            }
            // 支付类型，1、在线支付、0 货到付款
            if (!StringUtils.isEmpty(order.getPayType())) {
                criteria.andEqualTo("payType", order.getPayType());
            }
            // 订单创建时间
            if (!StringUtils.isEmpty(order.getCreateTime())) {
                criteria.andEqualTo("createTime", order.getCreateTime());
            }
            // 订单更新时间
            if (!StringUtils.isEmpty(order.getUpdateTime())) {
                criteria.andEqualTo("updateTime", order.getUpdateTime());
            }
            // 付款时间
            if (!StringUtils.isEmpty(order.getPayTime())) {
                criteria.andEqualTo("payTime", order.getPayTime());
            }
            // 发货时间
            if (!StringUtils.isEmpty(order.getConsignTime())) {
                criteria.andEqualTo("consignTime", order.getConsignTime());
            }
            // 交易完成时间
            if (!StringUtils.isEmpty(order.getEndTime())) {
                criteria.andEqualTo("endTime", order.getEndTime());
            }
            // 交易关闭时间
            if (!StringUtils.isEmpty(order.getCloseTime())) {
                criteria.andEqualTo("closeTime", order.getCloseTime());
            }
            // 物流名称
            if (!StringUtils.isEmpty(order.getShippingName())) {
                criteria.andEqualTo("shippingName", order.getShippingName());
            }
            // 物流单号
            if (!StringUtils.isEmpty(order.getShippingCode())) {
                criteria.andEqualTo("shippingCode", order.getShippingCode());
            }
            // 用户名称
            if (!StringUtils.isEmpty(order.getUsername())) {
                criteria.andLike("username", "%" + order.getUsername() + "%");
            }
            // 买家留言
            if (!StringUtils.isEmpty(order.getBuyerMessage())) {
                criteria.andEqualTo("buyerMessage", order.getBuyerMessage());
            }
            // 是否评价
            if (!StringUtils.isEmpty(order.getBuyerRate())) {
                criteria.andEqualTo("buyerRate", order.getBuyerRate());
            }
            // 收货人
            if (!StringUtils.isEmpty(order.getReceiverContact())) {
                criteria.andEqualTo("receiverContact", order.getReceiverContact());
            }
            // 收货人手机
            if (!StringUtils.isEmpty(order.getReceiverMobile())) {
                criteria.andEqualTo("receiverMobile", order.getReceiverMobile());
            }
            // 收货人地址
            if (!StringUtils.isEmpty(order.getReceiverAddress())) {
                criteria.andEqualTo("receiverAddress", order.getReceiverAddress());
            }
            // 订单来源：1:web，2：app，3：微信公众号，4：微信小程序  5 H5手机页面
            if (!StringUtils.isEmpty(order.getSourceType())) {
                criteria.andEqualTo("sourceType", order.getSourceType());
            }
            // 交易流水号
            if (!StringUtils.isEmpty(order.getTransactionId())) {
                criteria.andEqualTo("transactionId", order.getTransactionId());
            }
            // 订单状态,0:未完成,1:已完成，2：已退货
            if (!StringUtils.isEmpty(order.getOrderStatus())) {
                criteria.andEqualTo("orderStatus", order.getOrderStatus());
            }
            // 支付状态,0:未支付，1：已支付，2：支付失败
            if (!StringUtils.isEmpty(order.getPayStatus())) {
                criteria.andEqualTo("payStatus", order.getPayStatus());
            }
            // 发货状态,0:未发货，1：已发货，2：已收货
            if (!StringUtils.isEmpty(order.getConsignStatus())) {
                criteria.andEqualTo("consignStatus", order.getConsignStatus());
            }
            // 是否删除
            if (!StringUtils.isEmpty(order.getIsDelete())) {
                criteria.andEqualTo("isDelete", order.getIsDelete());
            }
        }
        return example;
    }

    /**
     * 删除
     *
     * @param id
     */
    @Override
    public void delete(String id) {
        orderMapper.deleteByPrimaryKey(id);
    }

    /**
     * 修改Order
     *
     * @param order
     */
    @Override
    public void update(Order order) {
        //订单支付成功后，修改订单状态
        //同时删除redis中订单信息
        orderMapper.updateByPrimaryKey(order);

    }


    /**
     * 根据ID查询Order
     *
     * @param id
     * @return
     */
    @Override
    public Order findById(String id) {
        return orderMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询Order全部数据
     *
     * @return
     */
    @Override
    public List<Order> findAll() {
        return orderMapper.selectAll();
    }

    @Override
    public void UpdateStatus(String orderId, String transactionId) {
        //支付成功时，修改订单信息
        //订单修改sql数据
        Order order = orderMapper.selectByPrimaryKey(orderId);
        Date date = new Date();
        order.setUpdateTime(new Date());
        order.setPayTime(date);
        order.setTransactionId(transactionId);
        order.setPayStatus("1");//支付
        orderMapper.updateByPrimaryKeySelective(order);
        //删除Redis中的订单记录
        redisTemplate.boundHashOps(OrderKey).delete(orderId);
        //todo 不需要删除此时购物车中的信息么（）
        //redisTemplate.delete(CartKey+order.getUsername());

    }


    @Override
    public void deleteOrder(String orderId) {
        //支付失败
        //修改订单状态--》数据库
        //回滚商品数量信息
        Order order = (Order) redisTemplate.opsForHash().get(OrderKey, orderId);
        order.setUpdateTime(new Date());
        order.setPayStatus("2");//失败
        orderMapper.updateByPrimaryKeySelective(order);
        //回滚商品数据
        skuFeign.inCrCount(order.getUsername());
        //删除redis中的订单数据
        redisTemplate.opsForHash().delete(OrderKey, orderId);
        //todo 不需要删除此时购物车中的信息么（）
        //redisTemplate.delete(CartKey+order.getUsername());
    }
}
