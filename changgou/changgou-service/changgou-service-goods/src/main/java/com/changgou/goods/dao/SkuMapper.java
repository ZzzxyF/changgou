package com.changgou.goods.dao;

import com.zxy.order.pojo.OrderItem;
import com.zxy.pojo.Sku;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

/****
 * @Author:admin
 * @Description:Sku的Dao
 * @Date 2019/6/14 0:12
 *****/
public interface SkuMapper extends Mapper<Sku> {
    //增加商品库存减少方法
    @Update("update tb_sku set num=num-#{num},sale_num=sale_num+#{num} where id=#{skuId} and num>=#{num}")
    int decrCount(OrderItem orderItem);
    //增加商品库存方法
    @Update("update tb_sku set num=num+#{num},sale_num=sale_num-#{num} where id=#{skuId}")
    int inCount(OrderItem orderItem);
}
