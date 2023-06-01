package com.zxy.seckill.entity;

import java.util.Date;
import java.io.Serializable;

/**
 * (TbSeckillOrder)实体类
 *
 * @author makejava
 * @since 2023-04-23 00:38:26
 */
public class TbSeckillOrder implements Serializable {
    private static final long serialVersionUID = -39461591483207930L;
    /**
     * 主键
     */
    private Long id;
    /**
     * 秒杀商品ID
     */
    private Long seckillId;
    /**
     * 支付金额
     */
    private Double money;
    /**
     * 用户
     */
    private String userId;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 支付时间
     */
    private Date payTime;
    /**
     * 状态，0未支付，1已支付
     */
    private String status;
    /**
     * 收货人地址
     */
    private String receiverAddress;
    /**
     * 收货人电话
     */
    private String receiverMobile;
    /**
     * 收货人
     */
    private String receiver;
    /**
     * 交易流水
     */
    private String transactionId;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(Long seckillId) {
        this.seckillId = seckillId;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public String getReceiverMobile() {
        return receiverMobile;
    }

    public void setReceiverMobile(String receiverMobile) {
        this.receiverMobile = receiverMobile;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

}

