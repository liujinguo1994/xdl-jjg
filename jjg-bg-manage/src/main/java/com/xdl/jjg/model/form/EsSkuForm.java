package com.xdl.jjg.model.form;

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
 * 档案管理-商品sku信息
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
@Data
@ApiModel
public class EsSkuForm implements Serializable {

    private static final long serialVersionUID = 7684774151707155683L;

    /**
     * skuId
     */
    @ApiModelProperty(value = "skuId", example = "1")
    private Long id;

    /**
     * sku编号
     */
    @ApiModelProperty(required = true, value = "sku编号")
    @NotBlank(message = "sku编号不能为空")
    private String skuSn;
    /**
     * 条形码
     */
    @ApiModelProperty(required = true, value = "条形码")
    @NotBlank(message = "条形码不能为空")
    private String barCode;
    /**
     * 重量
     */
    @ApiModelProperty(required = true, value = "重量", example = "1")
    @NotNull(message = "重量不能为空")
    private Double weight;
    /**
     * 质检状态
     */
    @ApiModelProperty(value = "质检状态", example = "1")
    private Integer qualityState;

    /**
     * 质检报告
     */
    @ApiModelProperty(value = "质检报告")
    private String qualityReport;

    /**
     * 长
     */
    @ApiModelProperty(required = true, value = "长", example = "1")
    @NotNull(message = "长不能为空")
    private String skuLong;

    /**
     * 宽
     */
    @ApiModelProperty(required = true, value = "宽", example = "1")
    @NotNull(message = "宽不能为空")
    private String wide;

    /**
     * 高
     */
    @ApiModelProperty(required = true, value = "高", example = "1")
    @NotNull(message = "高不能为空")
    private String high;
    /**
     * 规格文本 前端缓存
     */
    @ApiModelProperty(value = "规格文本 前端缓存")
    private String specText;

    /**
     * 商品规格集合
     */
    @ApiModelProperty(required = true, value = "商品规格集合")
    @Valid
    @NotNull(message = "商品规格集合不能为空")
    @Size(min = 1, message = "至少有一个规格信息")
    private List<EsSpecForm> specList;

}
