package com.xdl.jjg.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2020-03-09
 */
@Data
@ToString
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsTabCountVO implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 全部数量
     */
	@ApiModelProperty(value = "全部数量")
	private Integer allCount;
    /**
     *
     */
	@ApiModelProperty(value = "降价数量")
	private Integer priceDownCount;



}
