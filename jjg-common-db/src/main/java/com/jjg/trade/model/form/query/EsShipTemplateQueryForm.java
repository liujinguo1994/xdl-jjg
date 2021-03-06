package com.jjg.trade.model.form.query;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 运费模板表QueryForm
 * </p>
 *
 * @author LiuJG 344009799@qq.com
 * @since 2019-06-05 09:25:43
 */
@Data
@ApiModel
@Accessors(chain = true)
public class EsShipTemplateQueryForm implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 模板名称
     */
	@ApiModelProperty(value = "模板名称")
	private String modeName;

    /**
     * 创建时间
     */
	@ApiModelProperty(value = "创建时间")
	private Long createTime;

    /**
     * 修改时间
     */
	@ApiModelProperty(value = "修改时间")
	private Long updateTime;

    /**
     * 是否删除(0 不删除，1删除)
     */
	@ApiModelProperty(value = "是否删除(0 不删除，1删除)")
	private Integer isDel;

    /**
     * 是否生鲜（0生鲜，1非生鲜）
     */
	@ApiModelProperty(value = "是否生鲜（0生鲜，1非生鲜）")
	private Integer isFresh;

    /**
     * 店铺id
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "店铺id")
	private Long shopId;

    /**
     * 物流公司ID
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "物流公司ID")
	private Long logiId;

    /**
     * 物流公司名称
     */
	@ApiModelProperty(value = "物流公司名称")
	private String logiName;

    /**
     * 是否是活动(0是活动，1不是活动)
     */
	@ApiModelProperty(value = "是否是活动(0是活动，1不是活动)")
	private Integer sign;

}
