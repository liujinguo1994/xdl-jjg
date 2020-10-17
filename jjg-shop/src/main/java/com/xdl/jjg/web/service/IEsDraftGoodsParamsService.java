package com.xdl.jjg.web.service;


import com.jjg.shop.model.domain.EsDraftGoodsParamsDO;
import com.jjg.shop.model.dto.EsDraftGoodsParamsDTO;
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
public interface IEsDraftGoodsParamsService {

    /**
     * 插入数据
     * @auther: wangaf 826988665@qq.com
     * @date: 2019-06-03
     * @param draftGoodsParamsDTO    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsDraftGoodsParamsDO>
     */
    DubboResult<EsDraftGoodsParamsDO> insertDraftGoodsParams(EsDraftGoodsParamsDTO draftGoodsParamsDTO);
    /**
     * 批量添加
     * @param goodsParamsDTO
     * @param goodsId
     * @return
     */
    DubboResult<EsDraftGoodsParamsDO> insertDraftGoodsParams(List<EsDraftGoodsParamsDTO> goodsParamsDTO, Long goodsId);
    /**
     * 根据条件更新更新数据
     * @auther: wangaf 826988665@qq.com
     * @date: 2019-06-03
     * @param draftGoodsParamsDTO    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsDraftGoodsParamsDO>
     */
    DubboResult<EsDraftGoodsParamsDO> updateDraftGoodsParams(EsDraftGoodsParamsDTO draftGoodsParamsDTO, Long id);

    /**
     * 根据id获取数据
     * @auther: wangaf 826988665@qq.com
     * @date: 2019/05/31 16:37:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsDraftGoodsParamsDO>
     */
    DubboResult<EsDraftGoodsParamsDO> getDraftGoodsParams(Long id);

    /**
     * 根据查询条件查询列表
     * @auther: wangaf 826988665@qq.com
     * @date: 2019-06-03
     * @param draftGoodsParamsDTO  DTO
     * @param pageSize      页码
     * @param pageNum       页数
     * @return: com.shopx.common.model.result.DubboPageResult<EsDraftGoodsParamsDO>
     */
    DubboPageResult<EsDraftGoodsParamsDO> getDraftGoodsParamsList(EsDraftGoodsParamsDTO draftGoodsParamsDTO, int pageSize, int pageNum);

    /**
     * 根据主键删除数据
     * @auther: wangaf 826988665@qq.com
     * @date: 2019-06-03
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsDraftGoodsParamsDO>
     */
    DubboResult<EsDraftGoodsParamsDO> deleteDraftGoodsParams(Long id);
}
