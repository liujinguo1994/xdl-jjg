package com.xdl.jjg.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 运费模板表
 * </p>
 *
 * @author LiuJG344009799@qq.com
 * @since 2019-05-29
 */
@Data
@ToString
@ApiModel
@Accessors(chain = true)
public class EsShipTemplateForm implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键自增
     */
	@ApiModelProperty(value = "主键自增")
	private Long id;
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
	@ApiModelProperty(value = "店铺id")
	private Long shopId;
    /**
     * 物流公司ID
     */
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


	protected Serializable pkVal() {
		return this.id;
	}

}
