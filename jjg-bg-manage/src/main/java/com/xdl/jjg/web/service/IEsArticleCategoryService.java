package com.xdl.jjg.web.service;

import com.xdl.jjg.model.domain.EsArticleCategoryDO;
import com.xdl.jjg.model.dto.EsArticleCategoryDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 * 文章分类 服务类
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-07-24
 */
public interface IEsArticleCategoryService {

    /**
     * 插入数据
     *
     * @param articleCategoryDTO 文章分类DTO
     * @author rm 2817512105@qq.com
     * @return: com.shopx.common.model.result.DubboResult<EsArticleCategoryDO>
     * @since 2019-07-24
     */
    DubboResult insertArticleCategory(EsArticleCategoryDTO articleCategoryDTO);

    /**
     * 根据条件更新更新数据
     *
     * @param articleCategoryDTO 文章分类DTO
     * @author rm 2817512105@qq.com
     * @return: com.shopx.common.model.result.DubboResult<EsArticleCategoryDO>
     * @since 2019-07-24
     */
    DubboResult updateArticleCategory(EsArticleCategoryDTO articleCategoryDTO);

    /**
     * 根据id获取数据
     *
     * @param id 主键id
     * @author rm 2817512105@qq.com
     * @return: com.shopx.common.model.result.DubboResult<EsArticleCategoryDO>
     * @since 2019-07-24
     */
    DubboResult<EsArticleCategoryDO> getArticleCategory(Long id);

    /**
     * 根据查询条件查询列表
     *
     * @param articleCategoryDTO 文章分类DTO
     * @param pageSize           行数
     * @param pageNum            页码
     * @author rm 2817512105@qq.com
     * @return: com.shopx.common.model.result.DubboPageResult<EsArticleCategoryDO>
     * @since 2019-07-24
     */
    DubboPageResult<EsArticleCategoryDO> getArticleCategoryList(EsArticleCategoryDTO articleCategoryDTO, int pageSize, int pageNum);

    /**
     * 根据主键删除数据
     *
     * @param id 主键id
     * @author rm 2817512105@qq.com
     * @return: com.shopx.common.model.result.DubboResult<EsArticleCategoryDO>
     * @since 2019-07-24
     */
    DubboResult deleteArticleCategory(Long id);

    //查询二级文章分类列表
    DubboPageResult<EsArticleCategoryDO> getChildren(Long id);

    //查询文章分类树
    DubboPageResult<EsArticleCategoryDO> getTree();
}
