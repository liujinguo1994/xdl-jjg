package com.xdl.jjg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xdl.jjg.entity.EsCommentReply;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 评论回复(店家回复) Mapper 接口
 * </p>
 *
 * @author HQL 236154186@qq.com
 * @since 2019-05-29
 */
public interface EsCommentReplyMapper extends BaseMapper<EsCommentReply> {

    EsCommentReply getCommentReplyByCommentId(@Param("commentId") Long commentId);
}
