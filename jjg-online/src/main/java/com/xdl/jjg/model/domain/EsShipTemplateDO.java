package com.xdl.jjg.model.domain;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 运费模板表
 * </p>
 *
 * @author LiuJG344009799@qq.com
 * @since 2019-05-29
 */
@Data
public class EsShipTemplateDO extends Model<EsShipTemplateDO> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键自增
     */
	private Long id;
    /**
     * 模板名称
     */
	private String modeName;
    /**
     * 创建时间
     */
	private Long createTime;
    /**
     * 修改时间
     */
	private Long updateTime;
    /**
     * 是否删除(0 不删除，1删除)
     */
	private Integer isDel;
    /**
     * 是否生鲜（0生鲜，1非生鲜）
     */
	private Integer isFresh;
    /**
     * 店铺id
     */
	private Long shopId;
    /**
     * 物流公司ID
     */
	private Long logiId;
    /**
     * 物流公司名称
     */
	private String logiName;
    /**
     * 是否是活动(0是活动，1不是活动)
     */
	private Integer sign;

	private List<EsSellerFreightTemplateDetailDO> detailList;

	private Long isNg;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
