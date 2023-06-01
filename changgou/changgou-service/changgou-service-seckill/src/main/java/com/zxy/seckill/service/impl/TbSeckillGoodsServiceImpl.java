package com.zxy.seckill.service.impl;

import com.zxy.entity.IdWorker;
import com.zxy.seckill.dao.TbSeckillGoodsDao;
import com.zxy.seckill.entity.TbSeckillGoods;
import com.zxy.seckill.entity.TbSeckillOrder;
import com.zxy.seckill.service.TbSeckillGoodsService;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

/**
 * (TbSeckillGoods)表服务实现类
 *
 * @author makejava
 * @since 2023-04-23 00:38:26
 */
@Service("tbSeckillGoodsService")
public class TbSeckillGoodsServiceImpl implements TbSeckillGoodsService {
    @Resource
    private TbSeckillGoodsDao tbSeckillGoodsDao;
    @Autowired
    RedisTemplate redisTemplate;
    @Value("${flashSaleKey}")
    public  String flashSaleKey;
    @Autowired
    public IdWorker idWorker;

    @Override
    public List<TbSeckillGoods> queryTbSeckillGoodsByRedis(String key) {
     /*   测试环境使用
     if(StringUtils.isNotEmpty(key)){
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date dateKey= null;
            try {
                dateKey = simpleDateFormat.parse(key);
                String extName = DateUtil.data2str(dateKey,DateUtil.PATTERN_YYYYMMDDHH);
               return   redisTemplate.boundHashOps(flashSaleKey+extName).values();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
*/
        return   redisTemplate.boundHashOps(flashSaleKey+key).values();

    }


    @Override
    public TbSeckillGoods one(String time, Long id){
        if(StringUtils.isNotEmpty(time)){
           throw new RuntimeException("时间段为空，参数异常");
        }
        if(ObjectUtils.isEmpty(id)){
            throw new RuntimeException("商品id为空，参数异常");
        }
       Object goodsObject= redisTemplate.opsForHash().get(flashSaleKey+time,id);
        if(!ObjectUtils.isEmpty(goodsObject)){
            TbSeckillGoods  TbSeckillGoods=(TbSeckillGoods)goodsObject;
            return  TbSeckillGoods;
        }
        throw new RuntimeException("商品id为空，参数异常");
    }

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public TbSeckillGoods queryById(Long id) {
        return this.tbSeckillGoodsDao.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param tbSeckillGoods 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public Page<TbSeckillGoods> queryByPage(TbSeckillGoods tbSeckillGoods, PageRequest pageRequest) {
        long total = this.tbSeckillGoodsDao.count(tbSeckillGoods);
        return new PageImpl<>(this.tbSeckillGoodsDao.queryAllByLimit(tbSeckillGoods, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param tbSeckillGoods 实例对象
     * @return 实例对象
     */
    @Override
    public TbSeckillGoods insert(TbSeckillGoods tbSeckillGoods) {
        this.tbSeckillGoodsDao.insert(tbSeckillGoods);
        return tbSeckillGoods;
    }

    /**
     * 修改数据
     *
     * @param tbSeckillGoods 实例对象
     * @return 实例对象
     */
    @Override
    public TbSeckillGoods update(TbSeckillGoods tbSeckillGoods) {
        this.tbSeckillGoodsDao.update(tbSeckillGoods);
        return this.queryById(tbSeckillGoods.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long id) {
        return this.tbSeckillGoodsDao.deleteById(id) > 0;
    }


}
