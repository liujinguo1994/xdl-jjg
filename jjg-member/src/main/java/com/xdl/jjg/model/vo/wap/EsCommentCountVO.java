package com.xdl.jjg.model.vo.wap;

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
 * @since 2020-06-02
 */
@Data
@ToString
@Accessors(chain = true)
public class EsCommentCountVO implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 好评
     */
	@ApiModelProperty(value = "好评")
	private Integer highCount;
    /**
     *中评
     */
	@ApiModelProperty(value = "中评")
	private Integer commCount;


	/**
	 * 差评
	 */
	@ApiModelProperty(value = "差评")
	private Integer lowCount;
	/**
	 * 有图
	 */
	@ApiModelProperty(value = "有图")
	private Integer hasImageCount;

	@ApiModelProperty(value = "最近")
	private Integer recentCount;
	@ApiModelProperty(value = "追加")
	private Integer addCount;


}
