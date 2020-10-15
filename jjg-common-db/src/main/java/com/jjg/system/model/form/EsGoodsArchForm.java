package com.jjg.system.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 商品档案
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
@Data
@ApiModel
public class EsGoodsArchForm implements Serializable {

    private static final long serialVersionUID = 3786624402204048912L;

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id", example = "1")
    private Long id;

    /**
     * 供应商ID
     */
    @ApiModelProperty(required = true, value = "供应商ID", example = "1")
    @NotNull(message = "供应商ID不能为空")
    private Long supplierId;
    /**
     * 供应商名称
     */
    @ApiModelProperty(required = true, value = "供应商名称")
    @NotBlank(message = "供应商名称不能为空")
    private String supplierName;
    /**
     * 负责人姓名
     */
    @ApiModelProperty(required = true, value = "负责人姓名")
    @NotBlank(message = "负责人姓名不能为空")
    private String chargePerson;

    /**
     * 负责人联系方式
     */
    @ApiModelProperty(required = true, value = "负责人联系方式")
    @NotBlank(message = "负责人联系方式不能为空")
    private String chargeMobile;

    /**
     * 负责人邮箱
     */
    @ApiModelProperty(required = true, value = "负责人邮箱")
    @NotBlank(message = "负责人邮箱不能为空")
    private String email;

    /**
     * 对接采购经理
     */
    @ApiModelProperty(required = true, value = "对接采购经理")
    @NotBlank(message = "对接采购经理不能为空")
    private String purchaseManager;

    /**
     * 采购人联系方式
     */
    @ApiModelProperty(required = true, value = "采购人联系方式")
    @NotBlank(message = "采购人联系方式不能为空")
    private String purchaseMobile;

    /**
     * 采购人邮箱
     */
    @ApiModelProperty(required = true, value = "采购人邮箱")
    @NotBlank(message = "采购人邮箱不能为空")
    private String purchaseEmail;

    /**
     * 商品编号
     */
    @ApiModelProperty(required = true, value = "商品编号")
    @NotBlank(message = "商品编号不能为空")
    private String goodsSn;

    /**
     * 商品名称
     */
    @ApiModelProperty(required = true, value = "商品名称")
    @NotBlank(message = "商品名称不能为空")
    private String goodsName;
    /**
     * 是否生鲜（1生鲜，2非生鲜）
     */
    @ApiModelProperty(required = true, value = "是否生鲜（1生鲜，2非生鲜）")
    @NotNull(message = "是否生鲜不能为空")
    private Integer isFresh;

    /**
     * SKU集合
     */
    @ApiModelProperty(required = true, value = "SKU集合")
    @Valid
    @NotNull(message = "SKU集合不能为空")
    @Size(min = 1, message = "至少有一个sku信息")
    private List<EsSkuForm> skuList;

    /**
     * 是否是赠品（1是，2不是）
     */
    @ApiModelProperty(required = true, value = "是否是赠品（1是，2不是）")
    @NotNull(message = "是否是赠品不能为空")
    private Integer isGifts;

    /**
     * 税率
     */
    @ApiModelProperty(required = true, value = "税率", example = "1")
    @NotNull(message = "税率不能为空")
    private Double taxRate;
    /**
     * 分类编码
     */
    @ApiModelProperty(required = true, value = "分类编码")
    @NotBlank(message = "分类编码不能为空")
    private String cateCode;
    /**
     * 商品单位
     */
    @ApiModelProperty(required = true, value = "商品单位")
    @NotBlank(message = "商品单位不能为空")
    private String unit;

}
