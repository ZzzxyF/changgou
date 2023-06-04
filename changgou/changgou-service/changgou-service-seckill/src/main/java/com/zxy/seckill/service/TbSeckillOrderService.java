package com.zxy.seckill.service;

import com.zxy.seckill.entity.SeckillStatus;
import com.zxy.seckill.entity.TbSeckillOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 * (TbSeckillOrder)表服务接口
 *
 * @author makejava
 * @since 2023-04-23 00:38:26
 */
public interface TbSeckillOrderService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    TbSeckillOrder queryById(Long id);

    /**
     * 分页查询
     *
     * @param tbSeckillOrder 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<TbSeckillOrder> queryByPage(TbSeckillOrder tbSeckillOrder, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param tbSeckillOrder 实例对象
     * @return 实例对象
     */
    TbSeckillOrder insert(TbSeckillOrder tbSeckillOrder);

    /**
     * 修改数据
     *
     * @param tbSeckillOrder 实例对象
     * @return 实例对象
     */
    TbSeckillOrder update(TbSeckillOrder tbSeckillOrder);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Long id);

    /***
     * 添加秒杀订单
     * @param id:商品ID
     * @param time:商品秒杀开始时间
     * @param username:用户登录名
     * @return
     */
    Boolean add(Long id, String time, String username);

    /***
     * 抢单状态查询
     * @param username
     */
    SeckillStatus queryStatus(String username);

    /**
     * zf成功修改状态
     * @param out_trade_no
     * @param transaction_id
     * @param username
     */
     void updatePayStatus(String out_trade_no,String transaction_id,String username);

    /**
     * 关闭、回滚数据
     */
    void closeOrder(String  username);

}
