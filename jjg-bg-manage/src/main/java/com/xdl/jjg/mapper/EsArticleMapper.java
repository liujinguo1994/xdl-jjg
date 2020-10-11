package com.xdl.jjg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xdl.jjg.entity.EsArticle;
import com.xdl.jjg.model.domain.EsArticleDO;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 文章 Mapper 接口
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-07-24
 */
public interface EsArticleMapper extends BaseMapper<EsArticle> {

    //分页查询文章列表
    IPage<EsArticleDO> getAllList(Page page, @Param("articleName") String articleName);

    //根据分类分页查询文章列表
    IPage<EsArticleDO> getListByCategoryId(Page page, @Param("articleName") String articleName, @Param("categoryIds") Long[] categoryIds);


}
