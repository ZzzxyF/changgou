package com.changgou.goods.service.impl;

import com.alibaba.fastjson.JSON;
import com.changgou.goods.dao.BrandMapper;
import com.changgou.goods.dao.CategoryMapper;
import com.changgou.goods.dao.SkuMapper;
import com.changgou.goods.dao.SpuMapper;

import com.changgou.goods.service.SpuService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zxy.entity.IdWorker;
import com.zxy.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/****
 * @Author:admin
 * @Description:Spu业务层接口实现类
 * @Date 2019/6/14 0:16
 *****/
@Service
public class SpuServiceImpl implements SpuService {

    @Autowired
    private SpuMapper spuMapper;

    @Autowired
    public IdWorker idWorker;

    @Autowired
    public CategoryMapper categoryMapper;

    @Autowired
    public BrandMapper brandMapper;
    @Autowired
    public SkuMapper skuMapper;


    /**
     * Spu条件+分页查询
     * @param spu 查询条件
     * @param page 页码
     * @param size 页大小
     * @return 分页结果
     */
    @Override
    public PageInfo<Spu> findPage(Spu spu, int page, int size){
        //分页
        PageHelper.startPage(page,size);
        //搜索条件构建
        Example example = createExample(spu);
        //执行搜索
        return new PageInfo<Spu>(spuMapper.selectByExample(example));
    }

    /**
     * Spu分页查询
     * @param page
     * @param size
     * @return
     */
    @Override
    public PageInfo<Spu> findPage(int page, int size){
        //静态分页
        PageHelper.startPage(page,size);
        //分页查询
        return new PageInfo<Spu>(spuMapper.selectAll());
    }

    /**
     * Spu条件查询
     * @param spu
     * @return
     */
    @Override
    public List<Spu> findList(Spu spu){
        //构建查询条件
        Example example = createExample(spu);
        //根据构建的条件查询数据
        return spuMapper.selectByExample(example);
    }


    /**
     * Spu构建查询对象
     * @param spu
     * @return
     */
    public Example createExample(Spu spu){
        Example example=new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        if(spu!=null){
            // 主键
            if(!StringUtils.isEmpty(spu.getId())){
                    criteria.andEqualTo("id",spu.getId());
            }
            // 货号
            if(!StringUtils.isEmpty(spu.getSn())){
                    criteria.andEqualTo("sn",spu.getSn());
            }
            // SPU名
            if(!StringUtils.isEmpty(spu.getName())){
                    criteria.andLike("name","%"+spu.getName()+"%");
            }
            // 副标题
            if(!StringUtils.isEmpty(spu.getCaption())){
                    criteria.andEqualTo("caption",spu.getCaption());
            }
            // 品牌ID
            if(!StringUtils.isEmpty(spu.getBrandId())){
                    criteria.andEqualTo("brandId",spu.getBrandId());
            }
            // 一级分类
            if(!StringUtils.isEmpty(spu.getCategory1Id())){
                    criteria.andEqualTo("category1Id",spu.getCategory1Id());
            }
            // 二级分类
            if(!StringUtils.isEmpty(spu.getCategory2Id())){
                    criteria.andEqualTo("category2Id",spu.getCategory2Id());
            }
            // 三级分类
            if(!StringUtils.isEmpty(spu.getCategory3Id())){
                    criteria.andEqualTo("category3Id",spu.getCategory3Id());
            }
            // 模板ID
            if(!StringUtils.isEmpty(spu.getTemplateId())){
                    criteria.andEqualTo("templateId",spu.getTemplateId());
            }
            // 运费模板id
            if(!StringUtils.isEmpty(spu.getFreightId())){
                    criteria.andEqualTo("freightId",spu.getFreightId());
            }
            // 图片
            if(!StringUtils.isEmpty(spu.getImage())){
                    criteria.andEqualTo("image",spu.getImage());
            }
            // 图片列表
            if(!StringUtils.isEmpty(spu.getImages())){
                    criteria.andEqualTo("images",spu.getImages());
            }
            // 售后服务
            if(!StringUtils.isEmpty(spu.getSaleService())){
                    criteria.andEqualTo("saleService",spu.getSaleService());
            }
            // 介绍
            if(!StringUtils.isEmpty(spu.getIntroduction())){
                    criteria.andEqualTo("introduction",spu.getIntroduction());
            }
            // 规格列表
            if(!StringUtils.isEmpty(spu.getSpecItems())){
                    criteria.andEqualTo("specItems",spu.getSpecItems());
            }
            // 参数列表
            if(!StringUtils.isEmpty(spu.getParaItems())){
                    criteria.andEqualTo("paraItems",spu.getParaItems());
            }
            // 销量
            if(!StringUtils.isEmpty(spu.getSaleNum())){
                    criteria.andEqualTo("saleNum",spu.getSaleNum());
            }
            // 评论数
            if(!StringUtils.isEmpty(spu.getCommentNum())){
                    criteria.andEqualTo("commentNum",spu.getCommentNum());
            }
            // 是否上架
            if(!StringUtils.isEmpty(spu.getIsMarketable())){
                    criteria.andEqualTo("isMarketable",spu.getIsMarketable());
            }
            // 是否启用规格
            if(!StringUtils.isEmpty(spu.getIsEnableSpec())){
                    criteria.andEqualTo("isEnableSpec",spu.getIsEnableSpec());
            }
            // 是否删除
            if(!StringUtils.isEmpty(spu.getIsDelete())){
                    criteria.andEqualTo("isDelete",spu.getIsDelete());
            }
            // 审核状态
            if(!StringUtils.isEmpty(spu.getStatus())){
                    criteria.andEqualTo("status",spu.getStatus());
            }
        }
        return example;
    }

    /**
     * 删除
     * @param id
     */
    @Override
    public void delete(Long id){
            Spu spu = spuMapper.selectByPrimaryKey(id);
            //检查是否被逻辑删除  ,必须先逻辑删除后才能物理删除
            if(!spu.getIsDelete().equals("1")){
                throw new RuntimeException("此商品不能删除！");
            }
            spuMapper.deleteByPrimaryKey(id);

    }

    /**
     * 修改Spu
     * @param spu
     */
    @Override
    public void update(Spu spu){
        spuMapper.updateByPrimaryKey(spu);
    }

    /**
     * 增加Spu
     * @param spu
     */
    @Override
    public void add(Spu spu){
        spuMapper.insert(spu);
    }

    /**
     * 根据ID查询Spu
     * @param id
     * @return
     */
    @Override
    public Spu findById(Long id){
        return  spuMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询Spu全部数据
     * @return
     */
    @Override
    public List<Spu> findAll() {
        return spuMapper.selectAll();
    }

    @Override
    public void saveGoods(Goods goods) {
      //增加spu
        Spu spu=goods.getSpu();
        if(ObjectUtils.isEmpty(spu.getId())){//新增
            spu.setId(idWorker.nextId());
            spuMapper.insert(spu);
        }else{//修改
            spuMapper.updateByPrimaryKey(spu);
            //删除之前的sku
            Long spuId=spu.getId();
            Sku sku=new Sku();
            sku.setSpuId(spuId);
            skuMapper.delete(sku);
        }
      //增加sku
        Date date=new Date();
        Category category=categoryMapper.selectByPrimaryKey(spu.getCategory3Id());
        Brand brand=brandMapper.selectByPrimaryKey(spu.getBrandId());
       for(Sku sku:goods.getSku()){
           //构建SKU名称，采用SPU+规格值组装
           if(StringUtils.isEmpty(sku.getSpec())){
               sku.setSpec("{}");
           }
           //获取Spu的名字
           String name = spu.getName();
           //将规格转换成Map
           Map<String,String> specMap = JSON.parseObject(sku.getSpec(), Map.class);
           //循环组装Sku的名字
           for (Map.Entry<String, String> entry : specMap.entrySet()) {
               name+="  "+entry.getValue();
           }
           sku.setName(name);
           //ID
           sku.setId(idWorker.nextId());
           //SpuId
           sku.setSpuId(spu.getId());
           //创建日期
           sku.setCreateTime(date);
           //修改日期
           sku.setUpdateTime(date);
           //商品分类ID
           sku.setCategoryId(spu.getCategory3Id());
           //分类名字
           sku.setCategoryName(category.getName());
           //品牌名字
           sku.setBrandName(brand.getName());
           //增加
           skuMapper.insertSelective(sku);
      }
    }

    @Override
    public Goods findGoodsById(Long spuId) {
        Spu spu=spuMapper.selectByPrimaryKey(spuId);
        Sku sku=new Sku();
        sku.setSpuId(spu.getId());
        List<Sku> skuList=skuMapper.select(sku);
        //封装goods
        Goods goods=new Goods();
        goods.setSpu(spu);
        goods.setSku(skuList);
        return goods;
    }

    @Override
    public void audit(Long spuId) {
    Spu spu=spuMapper.selectByPrimaryKey(spuId);
    //判断商品是否删除
        if(spu.getIsDelete().equals("1")){
        throw new  RuntimeException("商品已被删除");
        }
    //未删除的情况下，修改商品审批状态和上架状态（审核通过后才可以上架）
        spu.setStatus("1");//审核状态
        spu.setIsMarketable("1");//上架
        spuMapper.updateByPrimaryKey(spu);
    }

    @Override
    public void pull(Long spuId) {
        Spu spu=spuMapper.selectByPrimaryKey(spuId);
        if(spu.getIsDelete().equals("1")){
            throw  new  RuntimeException("商品已被删除");
        }
        spu.setIsMarketable("0");//下架
        spuMapper.updateByPrimaryKey(spu);
    }

    @Override
    public void put(Long spuId) {
    Spu spu=spuMapper.selectByPrimaryKey(spuId);
    if(spu.getIsDelete().equals("1")){
        throw new RuntimeException("商品已被删除");
    }
    //商品是否审核
    if(spu.getStatus().equals("0")){
        throw new  RuntimeException("商品未审核通过");
    }
      spu.setIsMarketable("1");//上架
        spuMapper.updateByPrimaryKey(spu);
    }

    //批量上架
    @Override
    public int putMany(Long[] ids) {
        Spu spu=new Spu();
        spu.setIsMarketable("1");//上架
        //批量修改
        Example example=new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("id", Arrays.asList(ids));//id
        //下架
        criteria.andEqualTo("isMarketable","0");
        //审核通过的
        criteria.andEqualTo("status","1");
        //非删除的
        criteria.andEqualTo("isDelete","0");
        return spuMapper.updateByExampleSelective(spu, example);
    }

    //批量下架
    @Override
    public int pullMany(Long[] ids) {
        Spu spu=new Spu();
        spu.setIsMarketable("0");//下架
        //批量修改
        Example example=new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("id", Arrays.asList(ids));//id
        //上架的
        criteria.andEqualTo("isMarketable","1");
        //审核通过的
        criteria.andEqualTo("status","1");
        //非删除的
        criteria.andEqualTo("isDelete","0");
        return spuMapper.updateByExampleSelective(spu, example);
    }

    //逻辑删除
    @Override
    public void logicDelete(Long spuId) {
       Spu spu=spuMapper.selectByPrimaryKey(spuId);
       //判断是否下架，必须是下架商品才能删除
        if(!spu.getIsMarketable().equals("0")){
            throw new  RuntimeException("商品需先下架再删除");
        }
        spu.setStatus("0");//修改审核状态
        spu.setIsDelete("1");
        spuMapper.updateByPrimaryKey(spu);
    }

    //还原商品
    @Override
    public void restore(Long spuId) {
    Spu spu=spuMapper.selectByPrimaryKey(spuId);
    if(!spu.getIsDelete().equals("1")){
    throw new RuntimeException("此商品未删除");
    }
    spu.setIsDelete("0");//未删除
    spu.setStatus("0");//未审核
    spuMapper.updateByPrimaryKey(spu);
    }
}
