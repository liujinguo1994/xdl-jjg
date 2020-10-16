package com.xdl.jjg.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jjg.member.model.domain.EsCommentGalleryDO;
import com.xdl.jjg.entity.EsCommentGallery;

import java.util.List;

/**
 * <p>
 *  评论图片 Mapper 接口
 * </p>
 *
 * @author HQL 236154186@qq.com
 * @since 2019-05-29
 */
public interface EsCommentGalleryMapper extends BaseMapper<EsCommentGallery> {

    /**
     * 查询图片地址列表
     */
    List<EsCommentGalleryDO> getCommentGalleryList(Long commentId);

    /**
     * 查询图片地址列表
     */
    List<String> getCommentImageList(Long commentId);

}
