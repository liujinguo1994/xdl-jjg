package com.jjg.member.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 评论图片对象
 *
 * @author Snow create in 2018/7/30
 * @version v2.0
 * @since v7.0.0
 */
@Data
public class CommentImageVO implements Serializable {

    @ApiModelProperty(value = "APP评价图片")
    private List<String> imageUrl;

    @ApiModelProperty(value = "PC评价图片")
    private String url;
}
