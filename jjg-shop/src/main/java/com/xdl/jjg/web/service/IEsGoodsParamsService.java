package com.xdl.jjg.web.service;


import com.jjg.shop.model.domain.EsBuyerGoodsParamsDO;
import com.jjg.shop.model.domain.EsGoodsParamsDO;
import com.jjg.shop.model.dto.EsGoodsParamsDTO;
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
public interface IEsGoodsParamsService {

    /**
     * 插入数据
     * @auther: wangaf 826988665@qq.com
     * @date: 2019/05/31 16:39:30
     * @param goodsParamsDTO    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsGoodsParamsDO>
     */
    DubboResult<EsGoodsParamsDO> insertGoodsParams(EsGoodsParamsDTO goodsParamsDTO);

    /**
     * 批量添加
     * @param goodsParamsDTO
     * @param goodsId
     * @return
     */
    DubboResult<EsGoodsParamsDO> insertGoodsParams(List<EsGoodsParamsDTO> goodsParamsDTO, Long goodsId);
    /**
     * 根据条件更新更新数据
     * @auther: wangaf 826988665@qq.com
     * @date: 2019/05/31 16:40:10
     * @param goodsParamsDTO    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsGoodsParamsDO>
     */
    DubboResult<EsGoodsParamsDO> updateGoodsParams(EsGoodsParamsDTO goodsParamsDTO, Long id);

    /**
     * 根据id获取数据
     * @auther: wangaf 826988665@qq.com
     * @date: 2019/05/31 16:37:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsGoodsParamsDO>
     */
    DubboResult<EsGoodsParamsDO> getGoodsParams(Long id);

    /**
     * 根据查询条件查询列表
     * @auther: wangaf 826988665@qq.com
     * @date: 2019/06/03 13:42:53
     * @param goodsParamsDTO  DTO
     * @param pageSize      页码
     * @param pageNum       页数
     * @return: com.shopx.common.model.result.DubboPageResult<EsGoodsParamsDO>
     */
    DubboPageResult<EsGoodsParamsDO> getGoodsParamsList(EsGoodsParamsDTO goodsParamsDTO, int pageSize, int pageNum);

    /**
     * 根据主键删除数据
     * @auther: wangaf 826988665@qq.com
     * @date: 2019/05/31 16:40:44
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsGoodsParamsDO>
     */
    DubboResult<EsGoodsParamsDO> deleteGoodsParams(Long id);

    /**
     * 根据商品ID获取商品参数
     * @param goodsId
     * @return
     */
    DubboPageResult<EsGoodsParamsDO> getGoodsParamsByGoodsId(Long goodsId);

    DubboPageResult<EsBuyerGoodsParamsDO>  queryGoodsParams(Long categoryId, Long goodsId);
}
