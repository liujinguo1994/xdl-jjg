package com.xdl.jjg.model.vo.wap;

import com.shopx.member.api.model.domain.vo.EsMyFootprintVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 足迹返回
 *
 * @author yuanj create in 2020/04/07
 * @version v2.0
 * @since v7.0.0
 */
@ApiModel(description = "我的足迹")
@Data
public class WapMyFootVO implements Serializable {


    @ApiModelProperty(value = "日期" )
    private String time;


    @ApiModelProperty(value = "足迹" )
    private List<EsMyFootprintVO> footprintVOList;





}
