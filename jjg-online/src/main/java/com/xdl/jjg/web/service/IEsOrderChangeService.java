package com.xdl.jjg.web.service;

import com.shopx.common.model.result.DubboResult;

/**
 * Description: zhuox-shop-trade
 * 订单状态变化 接口
 * Created by LiuJG on 2019/6/29 10:26
 */

public interface IEsOrderChangeService {

    DubboResult updateOrderStatus(String value, String tradeSn);
}
