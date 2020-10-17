package com.xdl.jjg.web.service;


import com.jjg.shop.model.domain.EsDraftGoodsSkuDO;
import com.jjg.shop.model.dto.EsDraftGoodsDTO;
import com.jjg.shop.model.dto.EsDraftGoodsSkuDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wangaf 826988665@qq.com
 * @since 2019-06-03
 */
public interface IEsDraftGoodsSkuService {

    /**
     * 插入数据
     * @auther: wangaf 826988665@qq.com
     * @date: 2019-06-03
     * @param draftGoodsSkuDTO    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsDraftGoodsSkuDO>
     */
    DubboResult<EsDraftGoodsSkuDO> insertDraftGoodsSku(List<EsDraftGoodsSkuDTO> draftGoodsSkuDTO, EsDraftGoodsDTO draftGoodsDTO);

    /**
     * 根据条件更新更新数据
     * @auther: wangaf 826988665@qq.com
     * @date: 2019-06-03
     * @param draftGoodsSkuDTO    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsDraftGoodsSkuDO>
     */
    DubboResult<EsDraftGoodsSkuDO> updateDraftGoodsSku(List<EsDraftGoodsSkuDTO> draftGoodsSkuDTO, EsDraftGoodsDTO draftGoodsDTO);

    /**
     * 根据id获取数据
     * @auther: wangaf 826988665@qq.com
     * @date: 2019/05/31 16:37:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsDraftGoodsSkuDO>
     */
    DubboResult<EsDraftGoodsSkuDO> getDraftGoodsSku(Long id);
    /**
     * 根据id获取数据
     * @auther: wangaf 826988665@qq.com
     * @date: 2019/05/31 16:37:16
     * @param goodsId    商品ID
     * @return: com.shopx.common.model.result.DubboResult<EsDraftGoodsSkuDO>
     */
    DubboPageResult<EsDraftGoodsSkuDO> getDraftGoodsSkuList(Long goodsId);

    /**
     * 根据查询条件查询列表
     * @auther: wangaf 826988665@qq.com
     * @date: 2019-06-03
     * @param draftGoodsSkuDTO  DTO
     * @param pageSize      页码
     * @param pageNum       页数
     * @return: com.shopx.common.model.result.DubboPageResult<EsDraftGoodsSkuDO>
     */
    DubboPageResult<EsDraftGoodsSkuDO> getDraftGoodsSkuList(EsDraftGoodsSkuDTO draftGoodsSkuDTO, int pageSize, int pageNum);

    /**
     * 根据 商品Id删除数据
     * @auther: wangaf 826988665@qq.com
     * @date: 2019-06-03
     * @param goodsId    商品ID
     * @return: com.shopx.common.model.result.DubboResult<EsDraftGoodsSkuDO>
     */
    DubboResult<EsDraftGoodsSkuDO> deleteDraftGoodsSku(Long goodsId);
}
