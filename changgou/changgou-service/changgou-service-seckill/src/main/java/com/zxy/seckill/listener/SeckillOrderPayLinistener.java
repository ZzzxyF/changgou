package com.zxy.seckill.listener;

import com.alibaba.fastjson.JSON;
import com.zxy.seckill.service.TbSeckillOrderService;
import java.util.Map;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

//注入对象缺失

/**
 * 监听用户zf情况
 */
@Component
@RabbitListener(queues = "${mq.pay.queue.order}")
public class SeckillOrderPayLinistener {

  @Autowired
  private TbSeckillOrderService tbSeckillOrderService;

  //@PayLoad ;获取监听信息body
  @RabbitHandler
  public void consumeMessage(@Payload String message) {
    Map<String, String> paramMaps = JSON.parseObject(message, Map.class);

    String returnCode = paramMaps.get("return_code");
    String resultCode = paramMaps.get("result_code");

    if(returnCode.equalsIgnoreCase("success")){
      String outTradeNo=paramMaps.get("out_trade_no");

      Map<String,String> data=JSON.parseObject(paramMaps.get("attach").toString(),Map.class);
      //支付成功处理
      if(resultCode.equalsIgnoreCase("success")){
        //修改状态
        tbSeckillOrderService.updatePayStatus(outTradeNo,data.get("transaction_id"),data.get("username"));
      }else{
        //支付失败--》删除订单
        //回滚库存


      }
    }

  }


}
