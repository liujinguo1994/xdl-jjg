package com.xdl.jjg.web.service;


import com.jjg.trade.model.dto.EsRefundGoodsDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 * 退货商品 服务类
 * </p>
 *
 * @author LiuJG 344009799@qq.com
 * @since 2019-06-03
 */
public interface IEsRefundGoodsService {

    /**
     * 插入数据
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:39:30
     * @param refundGoodsDTO    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsRefundGoodsDO>
     */
    DubboResult insertRefundGoods(EsRefundGoodsDTO refundGoodsDTO);

    /**
     * 根据条件更新更新数据
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:40:10
     * @param refundGoodsDTO    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsRefundGoodsDO>
     */
    DubboResult updateRefundGoods(EsRefundGoodsDTO refundGoodsDTO);

    /**
     * 根据id获取数据
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:37:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsRefundGoodsDO>
     */
    DubboResult getRefundGoods(Long id);

    /**
     * 根据查询条件查询列表
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/06/03 13:42:53
     * @param refundGoodsDTO  DTO
     * @param pageSize      页码
     * @param pageNum       页数
     * @return: com.shopx.common.model.result.DubboPageResult<EsRefundGoodsDO>
     */
    DubboPageResult getRefundGoodsList(EsRefundGoodsDTO refundGoodsDTO, int pageSize, int pageNum);

    /**
     * 根据主键删除数据
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:40:44
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsRefundGoodsDO>
     */
    DubboResult deleteRefundGoods(Long id);

    /**
     * 卖家端
     * 根据退货款编号查询退货单下的商品列表
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/07/05 10:36
     * @return: com.shopx.common.model.result.DubboPageResult<EsRefundGoodsDO>
     */
    DubboPageResult getRefundGoodsByRefundSn(String refundSn);
}
