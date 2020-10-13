package com.xdl.jjg.model.vo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 *  收藏判断
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2020-02-24
 */
@Data
@Api
public class EsCollVO implements Serializable {

    private static final long serialVersionUID = 8155378858596627368L;

    @ApiModelProperty(required = false,value = "是否收藏,1已经收藏，2没")
    private Integer state;



}
