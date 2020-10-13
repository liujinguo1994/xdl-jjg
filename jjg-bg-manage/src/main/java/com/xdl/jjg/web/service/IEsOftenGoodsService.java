package com.xdl.jjg.web.service;


import com.xdl.jjg.model.domain.EsOftenGoodsDO;
import com.xdl.jjg.model.dto.EsOftenGoodsDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 * 常买商品 服务类
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-05-06
 */
public interface IEsOftenGoodsService {

    /**
     * 插入数据
     *
     * @param oftenGoodsDTO 常买商品DTO
     * @author rm 2817512105@qq.com
     * @return: com.shopx.common.model.result.DubboResult<EsOftenGoodsDO>
     * @since 2020-05-06
     */
    DubboResult insertOftenGoods(EsOftenGoodsDTO oftenGoodsDTO);

    /**
     * 根据条件更新更新数据
     *
     * @param oftenGoodsDTO 常买商品DTO
     * @author rm 2817512105@qq.com
     * @return: com.shopx.common.model.result.DubboResult<EsOftenGoodsDO>
     * @since 2020-05-06
     */
    DubboResult updateOftenGoods(EsOftenGoodsDTO oftenGoodsDTO);

    /**
     * 根据id获取数据
     *
     * @param id 主键id
     * @author rm 2817512105@qq.com
     * @return: com.shopx.common.model.result.DubboResult<EsOftenGoodsDO>
     * @since 2020-05-06
     */
    DubboResult<EsOftenGoodsDO> getOftenGoods(Long id);

    /**
     * 根据查询条件查询列表
     *
     * @param oftenGoodsDTO 常买商品DTO
     * @param pageSize      行数
     * @param pageNum       页码
     * @author rm 2817512105@qq.com
     * @return: com.shopx.common.model.result.DubboPageResult<EsOftenGoodsDO>
     * @since 2020-05-06
     */
    DubboPageResult<EsOftenGoodsDO> getOftenGoodsList(EsOftenGoodsDTO oftenGoodsDTO, int pageSize, int pageNum);

    /**
     * 根据主键删除数据
     *
     * @param id 主键id
     * @author rm 2817512105@qq.com
     * @return: com.shopx.common.model.result.DubboResult<EsOftenGoodsDO>
     * @since 2020-05-06
     */
    DubboResult deleteOftenGoods(Long id);

    /**
     * 根据自定义分类ID查询列表
     */
    DubboPageResult<EsOftenGoodsDO> getListByCustomCategoryId(Long customCategoryId, int pageSize, int pageNum);

    /**
     * 根据自定义分类ID查询列表
     *
     * @param customCategoryId 分类id
     * @author yuanj 595831329@qq.com
     * @return: com.shopx.common.model.result.DubboResult<EsFindGoodsDO>
     * @since 2020-05-21
     */
    DubboPageResult<EsOftenGoodsDO> getByCustomCategoryId(Long customCategoryId);
}
