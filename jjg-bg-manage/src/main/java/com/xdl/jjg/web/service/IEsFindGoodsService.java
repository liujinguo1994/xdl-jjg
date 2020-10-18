package com.xdl.jjg.web.service;


import com.jjg.system.model.domain.EsFindGoodsDO;
import com.jjg.system.model.dto.EsFindGoodsDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 * 发现好货 服务类
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-05-07
 */
public interface IEsFindGoodsService {

    /**
     * 插入数据
     *
     * @param findGoodsDTO 发现好货DTO
     * @author rm 2817512105@qq.com
     * @return: com.shopx.common.model.result.DubboResult<EsFindGoodsDO>
     * @since 2020-05-07
     */
    DubboResult insertFindGoods(EsFindGoodsDTO findGoodsDTO);

    /**
     * 根据条件更新更新数据
     *
     * @param findGoodsDTO 发现好货DTO
     * @author rm 2817512105@qq.com
     * @return: com.shopx.common.model.result.DubboResult<EsFindGoodsDO>
     * @since 2020-05-07
     */
    DubboResult updateFindGoods(EsFindGoodsDTO findGoodsDTO);

    /**
     * 根据id获取数据
     *
     * @param id 主键id
     * @author rm 2817512105@qq.com
     * @return: com.shopx.common.model.result.DubboResult<EsFindGoodsDO>
     * @since 2020-05-07
     */
    DubboResult<EsFindGoodsDO> getFindGoods(Long id);

    /**
     * 根据查询条件查询列表
     *
     * @param findGoodsDTO 发现好货DTO
     * @param pageSize     行数
     * @param pageNum      页码
     * @author rm 2817512105@qq.com
     * @return: com.shopx.common.model.result.DubboPageResult<EsFindGoodsDO>
     * @since 2020-05-07
     */
    DubboPageResult<EsFindGoodsDO> getFindGoodsList(EsFindGoodsDTO findGoodsDTO, int pageSize, int pageNum);

    /**
     * 根据主键删除数据
     *
     * @param id 主键id
     * @author rm 2817512105@qq.com
     * @return: com.shopx.common.model.result.DubboResult<EsFindGoodsDO>
     * @since 2020-05-07
     */
    DubboResult deleteFindGoods(Long id);

    /**
     * 根据自定义分类ID查询列表
     */
    DubboPageResult<EsFindGoodsDO> getListByCustomCategoryId(Long customCategoryId, int pageSize, int pageNum);


    /**
     * 根据自定义分类ID查询列表
     *
     * @param customCategoryId 分类id
     * @author yuanj 595831329@qq.com
     * @return: com.shopx.common.model.result.DubboResult<EsFindGoodsDO>
     * @since 2020-05-21
     */
    DubboPageResult<EsFindGoodsDO> getByCustomCategoryId(Long customCategoryId);
}
