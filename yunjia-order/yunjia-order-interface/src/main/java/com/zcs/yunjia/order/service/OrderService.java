package com.zcs.yunjia.order.service;

import com.zcs.yunjia.common.pojo.OrderInfo;
import com.zcs.yunjia.common.pojo.RequestResult;

public interface OrderService {

    /**
     * 创建订单
     * @return
     */
    public RequestResult createOrder(OrderInfo orderInfo);
}
