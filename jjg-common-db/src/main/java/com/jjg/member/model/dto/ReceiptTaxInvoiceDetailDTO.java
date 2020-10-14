package com.jjg.member.model.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 电子发票列表传值
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-04
 */
@Data
@ToString
public class ReceiptTaxInvoiceDetailDTO implements Serializable {

    /**
     * 规格型号
     */
    private String specType;
    /**
     * 不含税金额。红票为负。不含税金额、税额、含税金额任何一个不传时，会根据传入的单价，数量进行计算，可能和实际数值存在误差，建议都传入
     */
    private String taxExcludedAmount;
    /**
     * 发票行性质:0,正常行;1,折扣行;2,被折扣行
     */
    private String invoiceLineProperty;
    /**
     * 增值税特殊管理（优惠政策名称）,当favouredPolicyFlag为1时，此项必填
     */
    private String favouredPolicyName;
    /**
     * 数量（冲红时项目数量为负数）
     */
    private String num;
    /**
     * 单价含税标志，0:不含税,1:含税
     */
    private String withTaxFlag;
    /**
     * 税额，[不含税金额] * [税率] = [税额]；税额允许误差为 0.06。红票为负。不含税金额、税额、含税金额任何一个不传时，会根据传入的单价，数量进行计算，可能和实际数值存在误差，建议都传入
     */
    private String tax;
    /**
     * 优惠政策标识:0,不使用;1,使用
     */
    private String favouredPolicyFlag;
    /**
     * 税率
     */
    private String taxRate;
    /**
     * 单位
     */
    private String unit;
    /**
     * 扣除额。差额征收时填写，目前只支持填写一项
     */
    private String deduction;
    /**
     * 单价,当商品单价(price)为空时，商品数量(num)也必须为空；同时(price)为空时，含税金额(taxIncludedAmount)、不含税金额(taxExcludedAmount)、税额(tax)都不能为空
     */
    private String price;
    /**
     * 零税率标识:空,非零税率;1,免税;2,不征税;3,普通零税率
     */
    private String zeroRateFlag;
    /**
     * 商品编码（商品税收分类编码开发者自行填写）
     */
    private String goodsCode;
    /**
     * 自行编码（可不填）
     */
    private String selfCode;
    /**
     * 商品名称（如invoiceLineProperty =1，则此商品行为折扣行，折扣行不允许多行折扣，折扣行必须紧邻被折扣行，商品名称必须与被折扣行一致）
     */
    private String goodsName;
    /**
     * 含税金额，[不含税金额] + [税额] = [含税金额]，红票为负。不含税金额、税额、含税金额任何一个不传时，会根据传入的单价，数量进行计算，可能和实际数值存在误差，建议都传入
     */
    private String taxIncludedAmount;
}
