package com.jjg.trade.model.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 店铺详细
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2019-06-24
 */
@ApiModel(description = "结算单表头信息")
@Data
public class EsHeaderVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 组织名称
     */
    @ApiModelProperty(value = "组织名称")
    private String orgName;

    /**
     * 银行开户名
     */
    @ApiModelProperty(value = "银行开户名")
    private String bankAccountName;

    /**
     * 银行开户账号
     */
    @ApiModelProperty(value = "银行开户账号")
    private String bankNumber;

    /**
     * 开户银行支行名称
     */
    @ApiModelProperty(value = "开户银行支行名称")
    private String bankName;

    /**
     * 开户银行所在省名称
     */
    @ApiModelProperty(value = "开户银行所在省名称")
    private String bankProvince;

    /**
     * 开户银行所在市名称
     */
    @ApiModelProperty(value = "开户银行所在市名称")
    private String bankCity;

    /**
     * 开户银行所在县名称
     */
    @ApiModelProperty(value = "开户银行所在县名称")
    private String bankCounty;

    /**
     * 开户银行所在镇民名称
     */
    @ApiModelProperty(value = "开户银行所在镇民名称")
    private String bankTown;

    /**
     * 结算总金额
     */
    @ApiModelProperty(value = "结算总金额")
    private Double price;

    /**
     * 结算金额
     */
    @ApiModelProperty(value = "结算金额")
    private Double billMoney;

    /**
     * 佣金
     */
    @ApiModelProperty(value = "佣金")
    private Double commission;

    /**
     * 退款金额
     */
    @ApiModelProperty(value = "退款金额")
    private Double refundMoney;

    /**
     * 退还佣金
     */
    @ApiModelProperty(value = "退还佣金")
    private Double refundCommission;
}
