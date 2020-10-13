package com.xdl.jjg.converter;


import com.xdl.jjg.model.vo.GoodsSkuVO;
import com.xdl.jjg.model.vo.TradeConvertGoodsSkuVO;
import com.xdl.jjg.util.BeanUtil;

/**
 * 转换goods包下，此包使用到的model及字段
 *
 * @author Snow create in 2018/3/20
 * @version v2.0
 * @since v7.0.0
 */
public class TradeGoodsConverter {

//    /**
//     * 转换goodsVO对象
//     * @return
//     */
//    public static TradeConvertGoodsSkuVO goodsVOConverter(CacheGoods goods){
//        TradeConvertGoodsSkuVO goodsVO = new TradeConvertGoodsSkuVO();
//        goodsVO.setIsAuth(goods.getIsAuth());
//        goodsVO.setTemplateId(goods.getTemplateId());
//        goodsVO.setLastModify(goods.getLastModify());
//        return goodsVO;
//    }

    /**
     * 转换goodsSkuVO对象
     * @return
     */
    public static TradeConvertGoodsSkuVO goodsSkuVOConverter(GoodsSkuVO goodsSkuVO){
        TradeConvertGoodsSkuVO skuVO = new TradeConvertGoodsSkuVO();
        BeanUtil.copyProperties(goodsSkuVO,skuVO);
        return skuVO;
    }
}
