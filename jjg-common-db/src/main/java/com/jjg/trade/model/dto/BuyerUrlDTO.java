package com.jjg.member.model.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 退款申请VO
 * @author zjp
 * @version v7.0
 * @since v7.0 上午10:33 2018/5/2
 */
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
@Data
public class BuyerUrlDTO implements Serializable{
    private static final long serialVersionUID = 758087208773569549L;


    @ApiModelProperty(name = "url", value = "图片路径",required = true)
    private String url;

}
