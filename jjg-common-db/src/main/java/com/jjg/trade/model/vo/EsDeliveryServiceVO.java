package com.jjg.trade.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jjg.trade.model.domain.EsSelfDateDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 自提点信息维护VO
 * </p>
 *
 * @author libw 981087977@qq.com
 * @since 2019-06-04
 */
@Data
@ApiModel
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsDeliveryServiceVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	@ApiModelProperty(value = "主键ID")
	private Long id;

	/**
	 * 自提点名称
	 */
	@ApiModelProperty(value = "自提点名称")
	private String deliveryName;
	/**
	 * 有效状态
	 */
	@ApiModelProperty(value = "有效状态,0 有效 1 无效")
	private Integer state;

    /**
     * 门店地址
     */
	@ApiModelProperty(value = "门店地址")
	private String address;

    /**
     * 联系电话
     */
	@ApiModelProperty(value = "卖家联系电话")
	private String phone;
	/**
	 * 签约公司ID
	 */
	@ApiModelProperty(value = "签约公司ID")
	private Long companyId;
	/**
	 * 签约公司名称
	 */
	@ApiModelProperty(value = "签约公司名称")
	private String companyName;
	/**
	 * 自提日期id集合
	 */
	@ApiModelProperty(value = "自提日期集合")
	private List<EsSelfDateDO> selfDateDOList;

	@ApiModelProperty(value = "收货人姓名")
	private String receiverName;

	@ApiModelProperty(value = "收货人手机")
	private String receiverMobile;

	/**
	 * 签约公司code
	 */
	private String companyCode;
    /**
     * 店铺ID
     */
//	@JsonSerialize(using = ToStringSerializer.class)
//	@ApiModelProperty(value = "店铺ID")
//	private Long shopId;

	protected Serializable pkVal() {
		return this.id;
	}

}
