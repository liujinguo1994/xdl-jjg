package com.xdl.jjg.plugin;

import com.shopx.common.exception.ArgumentException;
import com.shopx.trade.api.constant.TradeErrorCode;
import com.shopx.trade.api.model.domain.dto.EsPromotionGoodsDTO;

import java.util.List;

/**
 * 参数验证
 *
 * @author Snow create in 2018/3/30
 * @version v2.0
 * @since v7.0.0
 */
public class PromotionValid {

    public PromotionValid(){

    }

    /**
     * 参数验证
     * 1、活动起始时间必须大于当前时间
     * 2、验证活动开始时间是否大于活动结束时间
     * <p>
     * 无返回值，如有错误直接抛异常
     *
     * @param startTime 活动开始时间
     * @param endTime   活动结束时间
     * @param rangeType 是否全部商品参与
     * @param goodsList 选择的商品
     */
    public static void paramValid(Long startTime, Long endTime, int rangeType, List<EsPromotionGoodsDTO> goodsList) {

        long nowTime = System.currentTimeMillis();

        //如果活动起始时间小于现在时间
        if (startTime< nowTime) {
            throw new ArgumentException(TradeErrorCode.PARAM_ERROR.getErrorCode(), "活动起始时间必须大于当前时间");
        }

        // 开始时间不能大于结束时间
        if (startTime > endTime) {
            throw new ArgumentException(TradeErrorCode.PARAM_ERROR.getErrorCode(), "活动起始时间不能大于活动结束时间");
        }

        // 部分商品
        int part = 2;

        // 如果促销活动选择的是部分商品参加活动
        if (rangeType == part) {
            // 商品id组不能为空
            if (goodsList == null) {
                throw new ArgumentException(TradeErrorCode.NO_ACTIVE_GOODS.getErrorCode(), "请选择要参与活动的商品");
            }
        }
    }
}
