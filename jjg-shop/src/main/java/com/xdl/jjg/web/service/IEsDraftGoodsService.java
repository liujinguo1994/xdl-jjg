package com.xdl.jjg.web.service;


import com.jjg.shop.model.domain.EsDraftGoodsDO;
import com.jjg.shop.model.domain.EsSellerDraftGoodsDO;
import com.jjg.shop.model.dto.EsDraftGoodsDTO;
import com.jjg.shop.model.dto.EsDraftGoodsQueryDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wangaf 826988665@qq.com
 * @since 2019-06-03
 */
public interface IEsDraftGoodsService {

    /**
     * 插入数据
     * @auther: wangaf 826988665@qq.com
     * @date: 2019-06-03
     * @param draftGoodsDTO    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsDraftGoodsDO>
     */
    DubboResult<EsDraftGoodsDO> insertDraftGoods(EsDraftGoodsDTO draftGoodsDTO);

    /**
     * 根据条件更新更新数据
     * @auther: wangaf 826988665@qq.com
     * @date: 2019-06-03
     * @param draftGoodsDTO    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsDraftGoodsDO>
     */
    DubboResult<EsDraftGoodsDO> updateDraftGoods(EsDraftGoodsDTO draftGoodsDTO, Long id);

    /**
     * 根据id获取数据
     * @auther: wangaf 826988665@qq.com
     * @date: 2019/05/31 16:37:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsDraftGoodsDO>
     */
    DubboResult<EsDraftGoodsDO> getDraftGoods(Long id);

    /**
     * 根据id获取数据
     * @auther: wangaf 826988665@qq.com
     * @date: 2019/05/31 16:37:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsDraftGoodsDO>
     */
    DubboResult<EsSellerDraftGoodsDO> getSellerDraftGoods(Long id);

    /**
     * 根据查询条件查询列表
     * @auther: wangaf 826988665@qq.com
     * @date: 2019-06-03
     * @param draftGoodsDTO  DTO
     * @param pageSize      页码
     * @param pageNum       页数
     * @return: com.shopx.common.model.result.DubboPageResult<EsDraftGoodsDO>
     */
    DubboPageResult<EsDraftGoodsDO> getDraftGoodsList(EsDraftGoodsQueryDTO draftGoodsDTO, Long shopId, int pageSize, int pageNum);

    /**
     * 根据主键删除数据
     * @auther: wangaf 826988665@qq.com
     * @date: 2019-06-03
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsDraftGoodsDO>
     */
    DubboResult<EsDraftGoodsDO> deleteDraftGoods(Long id);

    /**
     * 草稿商品信息发布（同步到商品表）
     * @param draftGoodsDTO
     * @return
     */
    DubboResult<EsDraftGoodsDO> syncEsGoods(EsDraftGoodsDTO draftGoodsDTO);
}
