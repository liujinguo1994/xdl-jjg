package com.xdl.jjg.web.service;


import com.xdl.jjg.model.domain.EsArticleDO;
import com.xdl.jjg.model.dto.EsArticleDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 * 文章 服务类
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-07-24
 */
public interface IEsArticleService {

    /**
     * 插入数据
     *
     * @param articleDTO 文章DTO
     * @author rm 2817512105@qq.com
     * @return: com.shopx.common.model.result.DubboResult<EsArticleDO>
     * @since 2019-07-24
     */
    DubboResult insertArticle(EsArticleDTO articleDTO);

    /**
     * 根据条件更新更新数据
     *
     * @param articleDTO 文章DTO
     * @author rm 2817512105@qq.com
     * @return: com.shopx.common.model.result.DubboResult<EsArticleDO>
     * @since 2019-07-24
     */
    DubboResult updateArticle(EsArticleDTO articleDTO);

    /**
     * 根据id获取数据
     *
     * @param id 主键id
     * @author rm 2817512105@qq.com
     * @return: com.shopx.common.model.result.DubboResult<EsArticleDO>
     * @since 2019-07-24
     */
    DubboResult<EsArticleDO> getArticle(Long id);

    /**
     * 根据查询条件查询列表
     *
     * @param articleDTO 文章DTO
     * @param pageSize   行数
     * @param pageNum    页码
     * @author rm 2817512105@qq.com
     * @return: com.shopx.common.model.result.DubboPageResult<EsArticleDO>
     * @since 2019-07-24
     */
    DubboPageResult<EsArticleDO> getArticleList(EsArticleDTO articleDTO, int pageSize, int pageNum);

    /**
     * 根据主键删除数据
     *
     * @param id 主键id
     * @author rm 2817512105@qq.com
     * @return: com.shopx.common.model.result.DubboResult<EsArticleDO>
     * @since 2019-07-24
     */
    DubboResult deleteArticle(Long id);

    //获取帮助页面总数
    DubboResult<Integer> articleCount();

    //分页查询文章列表
    DubboPageResult<EsArticleDO> getList(int pageSize, int pageNum);

    // 根据分类id查询文章列表
    DubboPageResult<EsArticleDO> getByCategoryId(Long categoryId);
}
