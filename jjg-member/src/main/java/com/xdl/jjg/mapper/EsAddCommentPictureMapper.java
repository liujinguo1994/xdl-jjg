package com.xdl.jjg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xdl.jjg.entity.EsAddCommentPicture;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  追加评论图片 Mapper 接口
 * </p>
 *
 * @author LBW 981087977@qq.com
 * @since 2019-11-12 14:44:44
 */
public interface EsAddCommentPictureMapper extends BaseMapper<EsAddCommentPicture> {

    EsAddCommentPicture addPictureBatch(@Param("commentId") Long commentId, @Param("list") List<String> list);
}
