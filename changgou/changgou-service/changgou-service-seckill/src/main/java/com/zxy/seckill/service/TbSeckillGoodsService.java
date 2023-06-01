package com.zxy.seckill.service;

import com.zxy.seckill.entity.TbSeckillGoods;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

/**
 * (TbSeckillGoods)表服务接口
 *
 * @author makejava
 * @since 2023-04-23 00:38:25
 */
public interface TbSeckillGoodsService {
    /**
     * 从redis中获取秒杀商品信息
     * @param key
     * @return
     */
    List<TbSeckillGoods> queryTbSeckillGoodsByRedis(String key);

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    TbSeckillGoods queryById(Long id);

    /**
     * 分页查询
     *
     * @param tbSeckillGoods 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<TbSeckillGoods> queryByPage(TbSeckillGoods tbSeckillGoods, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param tbSeckillGoods 实例对象
     * @return 实例对象
     */
    TbSeckillGoods insert(TbSeckillGoods tbSeckillGoods);

    /**
     * 修改数据
     *
     * @param tbSeckillGoods 实例对象
     * @return 实例对象
     */
    TbSeckillGoods update(TbSeckillGoods tbSeckillGoods);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Long id);

    /**
     *
     * @param time 时间段
     * @param id  商品id
     * @return
     */
    TbSeckillGoods one(String time ,Long id);

}
