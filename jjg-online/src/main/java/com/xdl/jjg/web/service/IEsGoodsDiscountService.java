package com.xdl.jjg.web.service;

import com.shopx.common.model.result.DubboPageResult;
import com.shopx.common.model.result.DubboResult;
import com.shopx.trade.api.model.domain.EsGoodsDiscountDO;
import com.shopx.trade.api.model.domain.dto.EsGoodsDiscountDTO;

/**
 * <p>
 * 商品折扣活动表 服务类
 * </p>
 *
 * @author LBW 981087977@qq.com
 * @since 2019-06-28 14:26:01
 */
public interface IEsGoodsDiscountService {

    /**
     * 插入数据
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:39:30
     * @param goodsDiscountDTO    商品折扣活动表DTO
     * @return: com.shopx.common.model.result.DubboResult<EsGoodsDiscountDO>
     */
    DubboResult insertGoodsDiscount(EsGoodsDiscountDTO goodsDiscountDTO);

    /**
     * 根据条件更新更新数据
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:40:10
     * @param goodsDiscountDTO   商品折扣活动表DTO
     * @param id                            主键id
     * @return: com.shopx.common.model.result.DubboResult<EsGoodsDiscountDO>
     */
    DubboResult updateGoodsDiscount(EsGoodsDiscountDTO goodsDiscountDTO, Long id);

    /**
     * 根据id获取数据
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:37:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsGoodsDiscountDO>
     */
    DubboResult<EsGoodsDiscountDO> getGoodsDiscount(Long id);

    /**
     * 根据查询条件查询列表
     * @auther: libw 981087977@qq.com
     * @date: 2019/06/03 13:42:53
     * @param goodsDiscountDTO  商品折扣活动表DTO
     * @param pageSize  行数
     * @param pageNum   页码
     * @return: com.shopx.common.model.result.DubboPageResult<EsGoodsDiscountDO>
     */
    DubboPageResult<EsGoodsDiscountDO> getGoodsDiscountList(EsGoodsDiscountDTO goodsDiscountDTO, int pageSize, int pageNum);

    /**
     * 根据主键删除数据
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:40:44
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsGoodsDiscountDO>
     */
    DubboResult deleteGoodsDiscount(Long id);

    /**
     * 从缓存里根据id获取商品打折活动id
     *
     * @param id 商品打折活动id
     * @author: libw 981087977@qq.com
     * @date: 2019/06/19 16:21:21
     * @return: com.shopx.common.model.result.DubboResult
     */
    DubboResult getGoodsDiscountForCache(Long id);

    DubboResult<EsGoodsDiscountDO> getSellerGoodsDiscount(Long id);

    DubboResult<EsGoodsDiscountDO> getGoodsDiscountByTime(Long shopId);
}
