package com.zxy.pay.service;

import java.util.Map;

public interface WeiXinPayService {
    /**
     * 订单号，订单金额
     * @param out_trade_no
     * @param total_fee
     * @return
     */
    public Map createNative(String out_trade_no, String total_fee);


    /**
     * 查询订单状态
     * @param out_trade_no
     * @return
     */
    public Map queryPayStatus(String out_trade_no);
}
