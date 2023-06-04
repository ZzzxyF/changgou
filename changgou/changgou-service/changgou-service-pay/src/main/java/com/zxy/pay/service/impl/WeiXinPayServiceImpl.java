package com.zxy.pay.service.impl;

import com.github.wxpay.sdk.WXPayUtil;
import com.google.common.collect.Maps;
import com.zxy.entity.HttpClient;
import com.zxy.pay.service.WeiXinPayService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
@Service
@Transactional(rollbackFor = Exception.class)
public class WeiXinPayServiceImpl implements WeiXinPayService {

    @Value("${weixin.appid}")
    private String appid;

    @Value("${weixin.partner}")
    private String partner;

    @Value("${weixin.partnerkey}")
    private String partnerkey;

    @Value("${weixin.notifyurl}")
    private String notifyurl;
    @Override
    public Map createNative(Map<String,String> paraMaps) {
        String outTradeNo=paraMaps.get("out_trade_no");//编号
        String totalFee=paraMaps.get("total_fee");//金额
        try {
            //1、封装参数
            Map param = new HashMap();
            param.put("appid", appid);
            param.put("mch_id", partner);
            param.put("nonce_str", WXPayUtil.generateNonceStr());
            param.put("body", "畅购");
            param.put("out_trade_no",outTradeNo);
            param.put("total_fee", totalFee);
            param.put("spbill_create_ip", "127.0.0.1");
            param.put("notify_url", notifyurl);
            param.put("trade_type", "NATIVE");
            //发送数据转为xml报文格式
            String paramXml = WXPayUtil.generateSignedXml(param, partnerkey);
            //发送请求，生成对应的二维码
            HttpClient httpClient = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
            httpClient.setHttps(true);
            httpClient.setXmlParam(paramXml);
            httpClient.post();
            String content = httpClient.getContent();
            Map<String, String> stringMap = WXPayUtil.xmlToMap(content);
            System.out.println("stringMap:"+stringMap);
            Map<String,String> dataMap = new HashMap<>();
            //code_url是对应的二维码地址
            dataMap.put("code_url",stringMap.get("code_url"));
            dataMap.put("out_trade_no",outTradeNo);
            dataMap.put("total_fee",totalFee);
            return dataMap;
        } catch (Exception e) {
            Map errMap= Maps.newHashMap();
            errMap.put("error msg", e.getMessage());
            return errMap;
        }
    }

    @Override
    public Map queryPayStatus(String out_trade_no) {
        try {
            Map param = new HashMap();
            param.put("appid",appid);                            //应用ID
            param.put("mch_id",partner);                         //商户号
            param.put("out_trade_no",out_trade_no);              //商户订单编号
            param.put("nonce_str",WXPayUtil.generateNonceStr()); //随机字符
            String paramXml = WXPayUtil.generateSignedXml(param,partnerkey);
            HttpClient httpClient = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
            httpClient.setHttps(true);
            httpClient.setXmlParam(paramXml);
            httpClient.post();
            String content = httpClient.getContent();
            return WXPayUtil.xmlToMap(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
