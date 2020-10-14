package com.xdl.jjg.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jjg.member.model.domain.EsCommentCategoryDO;
import com.xdl.jjg.entity.EsCommentCategory;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-24
 */
public interface EsCommentCategoryMapper extends BaseMapper<EsCommentCategory> {

    /**
     * 查询分类已绑定的标签和未绑定的标签
     *
     * @param categoryId
     * @return
     */
    List<EsCommentCategoryDO> getEsCommentCategoryBindList(Long categoryId);

    /**
     * 查询分类标签
     *
     * @param categoryId
     * @return
     */
    List<EsCommentCategoryDO> getEsCommentCategoryList(Long categoryId);
}
