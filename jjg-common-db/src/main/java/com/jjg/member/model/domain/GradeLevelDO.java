package com.xdl.jjg.model.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 好中差评论统计
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
@Data
public class GradeLevelDO implements Serializable {

    /**
     * 评论总是
     */
    private Integer num ;
    /**
     * 好评
     */
    private Integer goodNum;
    /**
     * 中评
     */
    private Integer commentNum;
    /**
     * 差评
     */
    private Integer badNum;
    /**
     * 带评论图片
     */
    private Integer pictureNum;


}
