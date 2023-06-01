package com.zxy.seckill.controller;

import com.zxy.entity.Result;
import com.zxy.entity.StatusCode;
import com.zxy.entity.TokenDecode;
import com.zxy.seckill.entity.SeckillStatus;
import com.zxy.seckill.entity.TbSeckillOrder;
import com.zxy.seckill.service.TbSeckillOrderService;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * (TbSeckillOrder)表控制层
 *
 * @author makejava
 * @since 2023-04-23 00:38:26
 */
@RestController
@RequestMapping("tbSeckillOrder")
public class TbSeckillOrderController {
    /**
     * 服务对象
     */
    @Resource
    private TbSeckillOrderService tbSeckillOrderService;
    @Autowired
    TokenDecode tokenDecode;


    /**
     * 添加订单
     * @param time
     * @param id
     * @return
     */
    @RequestMapping(value = "/add")
    public Result add(String time ,Long id){
        String username= tokenDecode.getUserInfo().get("username");
        //调用Service增加订单
        Boolean bo = tbSeckillOrderService.add(id, time, username);
        try {
            if(bo){
                return new Result(true, StatusCode.OK,"下单成功！");
            }
        } catch (java.lang.Exception exception) {
            return new Result(true,StatusCode.ERROR,"服务器繁忙，请稍后再试");
        }
        return new Result(true,StatusCode.ERROR,"服务器繁忙，请稍后再试");
    }


    /****
     * 查询抢购
     * @return
     */
    @RequestMapping(value = "/query")
    public Result queryStatus(){
        //获取用户名
        String username = tokenDecode.getUserInfo().get("username");

        //根据用户名查询用户抢购状态
        SeckillStatus seckillStatus = tbSeckillOrderService.queryStatus(username);

        if(seckillStatus!=null){
            return new Result(true,seckillStatus.getStatus(),"抢购状态");
        }
        //NOTFOUNDERROR =20006,没有对应的抢购数据
        return new Result(false,StatusCode.NOTFOUNDERROR,"没有抢购信息");
    }

    /**
     * 分页查询
     *
     * @param tbSeckillOrder 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @GetMapping
    public ResponseEntity<Page<TbSeckillOrder>> queryByPage(TbSeckillOrder tbSeckillOrder, PageRequest pageRequest) {
        return ResponseEntity.ok(this.tbSeckillOrderService.queryByPage(tbSeckillOrder, pageRequest));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public ResponseEntity<TbSeckillOrder> queryById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(this.tbSeckillOrderService.queryById(id));
    }

    /**
     * 新增数据
     *
     * @param tbSeckillOrder 实体
     * @return 新增结果
     */
    @PostMapping
    public ResponseEntity<TbSeckillOrder> add(TbSeckillOrder tbSeckillOrder) {
        return ResponseEntity.ok(this.tbSeckillOrderService.insert(tbSeckillOrder));
    }

    /**
     * 编辑数据
     *
     * @param tbSeckillOrder 实体
     * @return 编辑结果
     */
    @PutMapping
    public ResponseEntity<TbSeckillOrder> edit(TbSeckillOrder tbSeckillOrder) {
        return ResponseEntity.ok(this.tbSeckillOrderService.update(tbSeckillOrder));
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @DeleteMapping
    public ResponseEntity<Boolean> deleteById(Long id) {
        return ResponseEntity.ok(this.tbSeckillOrderService.deleteById(id));
    }

}

