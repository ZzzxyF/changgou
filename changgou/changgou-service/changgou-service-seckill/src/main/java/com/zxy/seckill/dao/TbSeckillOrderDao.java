package com.zxy.seckill.dao;

import com.zxy.seckill.entity.TbSeckillOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * (TbSeckillOrder)表数据库访问层
 *
 * @author makejava
 * @since 2023-04-23 00:38:26
 */
@Mapper
public interface TbSeckillOrderDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    TbSeckillOrder queryById(Long id);

    /**
     * 查询指定行数据
     *
     * @param tbSeckillOrder 查询条件
     * @param pageable         分页对象
     * @return 对象列表
     */
    List<TbSeckillOrder> queryAllByLimit(TbSeckillOrder tbSeckillOrder, @Param("pageable") Pageable pageable);

    /**
     * 统计总行数
     *
     * @param tbSeckillOrder 查询条件
     * @return 总行数
     */
    long count(TbSeckillOrder tbSeckillOrder);

    /**
     * 新增数据
     *
     * @param tbSeckillOrder 实例对象
     * @return 影响行数
     */
    int insert(TbSeckillOrder tbSeckillOrder);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<TbSeckillOrder> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<TbSeckillOrder> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<TbSeckillOrder> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<TbSeckillOrder> entities);

    /**
     * 修改数据
     *
     * @param tbSeckillOrder 实例对象
     * @return 影响行数
     */
    int update(TbSeckillOrder tbSeckillOrder);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);

}

