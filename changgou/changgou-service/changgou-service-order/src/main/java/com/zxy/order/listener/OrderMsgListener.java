package com.zxy.order.listener;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import com.zxy.order.service.OrderService;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author zxy
 */
@Component
@RabbitListener(queues = "${mq.pay.queue.order}")
public class OrderMsgListener {
    @Resource
    private OrderService orderService;

    @RabbitHandler
    public void consumeMsg(String msg, Channel channel){
        Map<String,String> result = JSON.parseObject(msg,Map.class);
        channel.getChannelNumber();
        //业务结果 result_code=SUCCESS/FAIL，修改订单状态
        if(result.get("return_code").equalsIgnoreCase("success") ){
            //支付成功--》订单编号，事务流水号
            orderService.UpdateStatus(result.get("out_trade_no"), result.get("transaction_id"));
        }else{
            //支付失败--》订单删除
            orderService.deleteOrder(result.get("out_trade_no"));
        }
    }
}
