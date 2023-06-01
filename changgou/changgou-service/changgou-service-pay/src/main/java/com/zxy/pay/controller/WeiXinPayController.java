package com.zxy.pay.controller;

import com.alibaba.fastjson.JSON;
import com.github.wxpay.sdk.WXPayUtil;
import com.zxy.entity.Result;
import com.zxy.entity.StatusCode;
import com.zxy.pay.service.WeiXinPayService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author DanChe
 */
@RestController
@RequestMapping("/pay")
public class WeiXinPayController {
    @Autowired
    WeiXinPayService weiXinPayService;
    @Value("${mq.pay.exchange.order}")
    private String  exchange;
    @Value("${mq.pay.routing.key}")
    private String routing;
    @Autowired
    RabbitTemplate rabbitTemplate;

    /**
     * 支付生成二维码
     * @param outtradeno
     * @param money
     * @return
     */
    @RequestMapping(value = "/create/native")
    public Result createNative(String outtradeno, String money){
        Map<String,String> resultMap = weiXinPayService.createNative(outtradeno,money);
        return new Result(true, StatusCode.OK,"创建二维码预付订单成功！",resultMap);
    }

    /**
     * 查询状态
     * @param outtradeno
     * @return
     */
    @GetMapping(value = "/status/query")
    public Result queryStatus(String outtradeno){
        Map<String,String> resultMap = weiXinPayService.queryPayStatus(outtradeno);
        return new Result(true,StatusCode.OK,"查询状态成功！",resultMap);
    }



    /**
     *
     * @param request
     * @return    支付结果的接受url
     * @throws Exception
     */
    @RequestMapping(value = "/notify/url")
    public String notifyUrl(HttpServletRequest request) throws Exception {
        InputStream inputStream=request.getInputStream();
        ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, len);
        }
        outputStream.close();
        inputStream.close();

        String resultStr=new String(outputStream.toByteArray(),"utf-8");
        Map<String, String> map = WXPayUtil.xmlToMap(resultStr);
        String mapStr=JSON.toJSONString(map);
        rabbitTemplate.convertAndSend(exchange,routing,mapStr);
        //响应数据设置
        Map respMap = new HashMap();
        respMap.put("return_code","success");
        respMap.put("return_msg","OK");
        return WXPayUtil.mapToXml(respMap);
    }

}
