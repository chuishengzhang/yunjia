package com.zcs.yunjia.order.service.impl;

import com.zcs.yunjia.common.pojo.OrderInfo;
import com.zcs.yunjia.common.pojo.RequestResult;
import com.zcs.yunjia.mapper.TbOrderItemMapper;
import com.zcs.yunjia.mapper.TbOrderMapper;
import com.zcs.yunjia.mapper.TbOrderShippingMapper;
import com.zcs.yunjia.order.redis.JedisClient;
import com.zcs.yunjia.order.service.OrderService;
import com.zcs.yunjia.pojo.TbOrder;
import com.zcs.yunjia.pojo.TbOrderItem;
import com.zcs.yunjia.pojo.TbOrderShipping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private JedisClient jedisClient;
    @Value("${ORDER_ID_KEY}")
    private  String ORDER_ID_KEY;
    @Value("${ORDER_ITEM_ID_KEY}")
    private String ORDER_ITEM_ID_KEY;
    @Autowired
    private TbOrderMapper orderMapper;
    @Autowired
    private TbOrderItemMapper orderItemMapper;
    @Autowired
    private TbOrderShippingMapper orderShippingMapper;

    @Override
    public RequestResult createOrder(OrderInfo orderInfo) {
        RequestResult result = new RequestResult();
        String orderId = "";
        try {
            //生产orderId 使用redis的incr
            if (!jedisClient.exists(ORDER_ID_KEY)) {
                //第一次创建订单 设置初始值
                jedisClient.set(ORDER_ID_KEY, "5201314");
            }
            orderId = jedisClient.incr(ORDER_ID_KEY).toString();
            //添加到order表
            TbOrder order = new TbOrder();
            order.setOrderId(orderId);
            order.setPayment(orderInfo.getPayment());
            order.setPaymentType(orderInfo.getPaymentType());
            order.setStatus(1);//默认为未付款
            order.setCreateTime(new Date());
            order.setUpdateTime(new Date());
            orderMapper.insert(order);
            //添加到orderItem表
            List<TbOrderItem> orderItems = orderInfo.getOrderItems();
            if (!jedisClient.exists(ORDER_ITEM_ID_KEY)) {
                jedisClient.set(ORDER_ITEM_ID_KEY, "1");
            }
            for (TbOrderItem toi : orderItems) {
                TbOrderItem orderItem = new TbOrderItem();
                orderItem.setId(jedisClient.incr(ORDER_ITEM_ID_KEY).toString());
                orderItem.setItemId(toi.getItemId());
                orderItem.setOrderId(orderId);
                orderItem.setNum(toi.getNum());
                orderItem.setPicPath(toi.getPicPath());
                orderItem.setPrice(toi.getPrice());
                orderItem.setTitle(toi.getTitle());
                orderItem.setTotalFee(toi.getTotalFee());
                orderItemMapper.insert(orderItem);
            }
            //添加到orderShipping表
            TbOrderShipping orderShipping = orderInfo.getOrderShipping();
            orderShipping.setOrderId(orderId);
            orderShippingMapper.insert(orderShipping);
        }catch (Exception e){
            e.printStackTrace();
            result.setStatus(400);
            result.setData(null);
            return result;
        }
        result.setStatus(200);
        result.setData(orderId);
        return result;
    }
}
