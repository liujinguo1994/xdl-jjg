package com.xdl.jjg.model.domain;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 自提点信息维护
 * </p>
 *
 * @author libw 981087977@qq.com
 * @since 2019-05-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class EsDeliveryServiceDO extends Model<EsDeliveryServiceDO> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	private Long id;
	/**
	 * 自提点名称
	 */
	private String deliveryName;
	/**
	 * 有效状态
	 */
	private Integer state;

    /**
     * 门店地址
     */
	private String address;
	/**
	 * 自提日期id集合
	 */
	private List<EsSelfDateDO> selfDateDOList;
    /**
     * 联系电话
     */
	private String phone;
	/**
	 * 签约公司ID
	 */
	private Long companyId;
	/**
	 * 签约公司名称
	 */
	private String companyName;

	/**
	 * 签约公司code
	 */
	private String companyCode;

    /**
     * 店铺ID
     */
//	private Long shopId;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
}
