package com.xdl.jjg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xdl.jjg.entity.EsAddComment;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  追加评论Mapper 接口
 * </p>
 *
 * @author LINS 1220316142@qq.com
 * @since 2019-10-08 15:19:23
 */
public interface EsAddCommentMapper extends BaseMapper<EsAddComment> {

    EsAddComment getAddCommentByCommentId(@Param("commentId") Long commentId);
}
