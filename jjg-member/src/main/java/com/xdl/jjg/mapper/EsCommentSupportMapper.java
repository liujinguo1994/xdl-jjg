package com.xdl.jjg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xdl.jjg.entity.EsCommentSupport;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author LINS 1220316142@qq.com
 * @since 2019-09-07 16:56:00
 */
public interface EsCommentSupportMapper extends BaseMapper<EsCommentSupport> {

    void delete(@Param("commentId") Long commentId, @Param("memberId") Long memberId);

    EsCommentSupport judgeCommentSupport(@Param("commentId") Long commentId, @Param("memberId") Long memberId);

}
