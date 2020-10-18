package com.xdl.jjg.web.service;


import com.jjg.system.model.domain.EsCustomCategoryDO;
import com.jjg.system.model.dto.EsCustomCategoryDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 * 自定义分类 服务类
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-05-06
 */
public interface IEsCustomCategoryService {

    /**
     * 插入数据
     *
     * @param customCategoryDTO 自定义分类DTO
     * @author rm 2817512105@qq.com
     * @return: com.shopx.common.model.result.DubboResult<EsCustomCategoryDO>
     * @since 2020-05-06
     */
    DubboResult insertCustomCategory(EsCustomCategoryDTO customCategoryDTO);

    /**
     * 根据条件更新更新数据
     *
     * @param customCategoryDTO 自定义分类DTO
     * @author rm 2817512105@qq.com
     * @return: com.shopx.common.model.result.DubboResult<EsCustomCategoryDO>
     * @since 2020-05-06
     */
    DubboResult updateCustomCategory(EsCustomCategoryDTO customCategoryDTO);

    /**
     * 根据id获取数据
     *
     * @param id 主键id
     * @author rm 2817512105@qq.com
     * @return: com.shopx.common.model.result.DubboResult<EsCustomCategoryDO>
     * @since 2020-05-06
     */
    DubboResult<EsCustomCategoryDO> getCustomCategory(Long id);

    /**
     * 根据查询条件查询列表
     *
     * @param customCategoryDTO 自定义分类DTO
     * @param pageSize          行数
     * @param pageNum           页码
     * @author rm 2817512105@qq.com
     * @return: com.shopx.common.model.result.DubboPageResult<EsCustomCategoryDO>
     * @since 2020-05-06
     */
    DubboPageResult<EsCustomCategoryDO> getCustomCategoryList(EsCustomCategoryDTO customCategoryDTO, int pageSize, int pageNum);

    /**
     * 根据主键删除数据
     *
     * @param id 主键id
     * @author rm 2817512105@qq.com
     * @return: com.shopx.common.model.result.DubboResult<EsCustomCategoryDO>
     * @since 2020-05-06
     */
    DubboResult deleteCustomCategory(Long id);
}
