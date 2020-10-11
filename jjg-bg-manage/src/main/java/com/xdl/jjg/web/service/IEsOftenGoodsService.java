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
     * @author rm 2817512105@qq.com
     * @since 2020-05-06
     * @param oftenGoodsDTO    常买商品DTO
     * @return: com.shopx.common.model.result.DubboResult<EsOftenGoodsDO>
     */
    DubboResult insertOftenGoods(EsOftenGoodsDTO oftenGoodsDTO);

    /**
     * 根据条件更新更新数据
     * @author rm 2817512105@qq.com
     * @since 2020-05-06
     * @param oftenGoodsDTO    常买商品DTO
     * @return: com.shopx.common.model.result.DubboResult<EsOftenGoodsDO>
     */
    DubboResult updateOftenGoods(EsOftenGoodsDTO oftenGoodsDTO);

    /**
     * 根据id获取数据
     * @author rm 2817512105@qq.com
     * @since 2020-05-06
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsOftenGoodsDO>
     */
    DubboResult<EsOftenGoodsDO> getOftenGoods(Long id);

    /**
     * 根据查询条件查询列表
     * @author rm 2817512105@qq.com
     * @since 2020-05-06
     * @param oftenGoodsDTO  常买商品DTO
     * @param pageSize  行数
     * @param pageNum   页码
     * @return: com.shopx.common.model.result.DubboPageResult<EsOftenGoodsDO>
     */
    DubboPageResult<EsOftenGoodsDO> getOftenGoodsList(EsOftenGoodsDTO oftenGoodsDTO, int pageSize, int pageNum);

    /**
     * 根据主键删除数据
     * @author rm 2817512105@qq.com
     * @since 2020-05-06
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsOftenGoodsDO>
     */
    DubboResult deleteOftenGoods(Long id);

    /**
     * 根据自定义分类ID查询列表
     */
    DubboPageResult<EsOftenGoodsDO> getListByCustomCategoryId(Long customCategoryId, int pageSize, int pageNum);

    /**
     * 根据自定义分类ID查询列表
     * @author yuanj 595831329@qq.com
     * @since 2020-05-21
     * @param customCategoryId    分类id
     * @return: com.shopx.common.model.result.DubboResult<EsFindGoodsDO>
     */
    DubboPageResult<EsOftenGoodsDO> getByCustomCategoryId(Long customCategoryId);
}
