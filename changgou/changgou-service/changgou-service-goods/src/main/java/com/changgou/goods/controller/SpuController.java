package com.changgou.goods.controller;


import com.changgou.goods.service.SpuService;
import com.github.pagehelper.PageInfo;
import com.zxy.entity.Result;
import com.zxy.entity.StatusCode;
import com.zxy.pojo.Goods;
import com.zxy.pojo.Spu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/****
 * @Author:admin
 * @Description:
 * @Date 2019/6/14 0:18
 *****/

@RestController
@RequestMapping("/spu")
@CrossOrigin
public class SpuController {

    @Autowired
    private SpuService spuService;

    //条件分页查询
    @PostMapping(value = "/search/{page}/{size}" )
    public Result<PageInfo> findPage(@RequestBody(required = false)  Spu spu, @PathVariable  int page, @PathVariable  int size){
        //调用SpuService实现分页条件查询Spu
        PageInfo<Spu> pageInfo = spuService.findPage(spu, page, size);
        return new Result(true,StatusCode.OK,"查询成功",pageInfo);
    }

    //分页查询
    @GetMapping(value = "/search/{page}/{size}" )
    public Result<PageInfo> findPage(@PathVariable  int page, @PathVariable  int size){
        //调用SpuService实现分页查询Spu
        PageInfo<Spu> pageInfo = spuService.findPage(page, size);
        return new Result<PageInfo>(true,StatusCode.OK,"查询成功",pageInfo);
    }

    /***
     * 多条件搜索品牌数据
     * @param spu
     * @return
     */
    @PostMapping(value = "/search" )
    public Result<List<Spu>> findList(@RequestBody(required = false)  Spu spu){
        //调用SpuService实现条件查询Spu
        List<Spu> list = spuService.findList(spu);
        return new Result<List<Spu>>(true,StatusCode.OK,"查询成功",list);
    }

    /***
     * 根据ID删除品牌数据
     * @param id
     * @return
     */
    @DeleteMapping(value = "/{id}" )
    public Result delete(@PathVariable Long id){
        //调用SpuService实现根据主键删除
        spuService.delete(id);
        return new Result(true,StatusCode.OK,"删除成功");
    }

    /***
     * 修改Spu数据
     * @param spu
     * @param id
     * @return
     */
    @PutMapping(value="/{id}")
    public Result update(@RequestBody  Spu spu,@PathVariable Long id){
        //设置主键值
        spu.setId(id);
        //调用SpuService实现修改Spu
        spuService.update(spu);
        return new Result(true,StatusCode.OK,"修改成功");
    }

    /***
     * 新增Spu数据
     * @param spu
     * @return
     */
    @PostMapping
    public Result add(@RequestBody   Spu spu){
        //调用SpuService实现添加Spu
        spuService.add(spu);
        return new Result(true,StatusCode.OK,"添加成功");
    }

    /***
     * 根据ID查询Spu数据
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<Spu> findById(@PathVariable Long id){
        //调用SpuService实现根据主键查询Spu
        Spu spu = spuService.findById(id);
        return new Result<Spu>(true,StatusCode.OK,"查询成功",spu);
    }

    @GetMapping
    public Result<List<Spu>> findAll(){
        //调用SpuService实现查询所有Spu
        List<Spu> list = spuService.findAll();
        return new Result<List<Spu>>(true, StatusCode.OK,"查询成功",list) ;
    }

    //新增商品
    @PostMapping("/save")
    public Result  save(@RequestBody Goods goods){
        spuService.saveGoods(goods);
        return new Result(true, StatusCode.OK,"新增成功") ;
    }

    @GetMapping("/goods/{id}")
    public Result<Goods>  findBySpuId(@PathVariable("id") Long id){
    //根据id查询Goods（spu+sku）信息
      Goods goods=  spuService.findGoodsById(id);
        return new Result<Goods>(true, StatusCode.OK,"新增成功",goods) ;
    }

    //审核：审核通过的商品，自动上架
    @GetMapping("/audit/{id}")
    public Result  audit(@PathVariable("id") Long id){
        spuService.audit(id);
        return new Result(true, StatusCode.OK,"审核通过") ;
    }

    //下架--->先判断是否删除
    @GetMapping("/pull/{id}")
    public Result pull(@PathVariable("id") Long id){
        spuService.pull(id);
        return new Result(true, StatusCode.OK,"删除成功") ;
    }

    //上架---》下架后的商品再次上架
    @PutMapping("/put/{id}")
    public Result put(@PathVariable Long id){
        spuService.put(id);
        return new Result(true, StatusCode.OK,"上架成功") ;
    }

    /**
     *  批量上架
     * @param ids
     * @return
     */
    @PutMapping("/put/many")
    public Result putMany(@RequestBody Long[] ids){
        int count = spuService.putMany(ids);
        return new Result(true,StatusCode.OK,"上架"+count+"个商品");
    }

    //批量下架
    @PutMapping("/pull/many")
    public Result pullMany(@RequestBody Long[] ids){
        int count= spuService.pullMany(ids);
        return new Result(true,StatusCode.OK,"上架"+count+"个商品");
    }

    //逻辑删除商品
    @DeleteMapping("/logic/delete/{id}")
    public Result logicDelete(@PathVariable Long id){
        spuService.logicDelete(id);
        return new Result(true,StatusCode.OK,"删除成功");
    }

    //还原商品
    @PutMapping("/restore/{id}")
    public Result restore(@PathVariable Long id){
        spuService.restore(id);
        return new Result(true,StatusCode.OK,"数据恢复成功");
    }

    //物理删除
    @DeleteMapping("/physics/delete/{id}")
     public  Result physicsDelete(@PathVariable Long id){
         spuService.delete(id);
         return new Result(true,StatusCode.OK,"物理删除成功");
     }


}
