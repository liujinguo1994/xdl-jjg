package com.xdl.jjg.model.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 评论分类
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-29 14:18:13
 */
@Data
@ToString
public class EsCommentConfigDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 好中差评设置集合
     */
   List<EsCommentSortConfigDTO> commentSortConfigDTOList;
    /**
     * 权重设置集合
     */
   List<EsGradeWeightConfigDTO> gradeWeightConfigDTOList;



}
