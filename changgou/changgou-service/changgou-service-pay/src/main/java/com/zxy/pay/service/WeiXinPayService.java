package com.zxy.pay.service;

import java.util.Map;

public interface WeiXinPayService {
    /**
     * 订单号，订单金额
     * @return
     */
    public Map createNative(Map<String,String> paraMaps);


    /**
     * 查询订单状态
     * @param out_trade_no
     * @return
     */
    public Map queryPayStatus(String out_trade_no);
}
