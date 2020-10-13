package com.xdl.jjg.utils;

import com.shopx.trade.api.model.domain.dto.EsOrderDTO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Description: zhuox-shop-trade
 * <p>
 * Created by Administrator on 2019/6/29 16:48
 */
public class OrderStatusUtils {
    public static Boolean selectOrderStatus(List<EsOrderDTO> list, String status){

        List<String> ordersTemp = list.stream().filter(esOrder1 -> !StringUtils.equals(esOrder1.getOrderState(), status))
                .map(EsOrderDTO::getOrderState).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(ordersTemp)){
            // 筛选订单状态，如果不存在已完成的订单，
            return true;
        }
        return false;
    }
}
