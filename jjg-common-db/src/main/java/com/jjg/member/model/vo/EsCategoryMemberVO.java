package com.jjg.member.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author xiaoLin
 * @ClassName EsCategoryVO
 * @Description 类目VO
 * @create 2019/12/17 14:01
 */
@Data
@JsonIgnoreProperties(ignoreUnknown=true)
public class EsCategoryMemberVO implements Serializable {
    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID")
    private Long id;

    /**
     * 分类名称
     */
    @ApiModelProperty(value = "分类名称")
    private String name;

    /**
     * 收藏数量
     */
    @ApiModelProperty(value = "收藏数量")
    private Integer num;
}
