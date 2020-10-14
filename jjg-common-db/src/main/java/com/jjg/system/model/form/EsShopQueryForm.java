package com.xdl.jjg.model.form;

import com.xdl.jjg.response.web.QueryPageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 店铺
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-06-04
 */
@Data
@ApiModel(description = "店铺")
public class EsShopQueryForm extends QueryPageForm {

    private static final long serialVersionUID = 8688095128077626938L;
    /**
     * 店铺名称
     */
    @ApiModelProperty(value = "店铺名称")
    private String shopName;


    /**
     * 会员名称
     */
    @ApiModelProperty(value = "会员名称")
    private String memberName;

    /**
     * 店铺状态(开启中：OPEN 店铺关闭：CLOSED 申请开店：APPLY 审核拒绝：REFUSED 申请中：APPLYING)
     */
    @ApiModelProperty(value = "店铺状态(开启中：OPEN 店铺关闭：CLOSED 申请开店：APPLY 审核拒绝：REFUSED 申请中：APPLYING)")
    private String state;

    /**
     * 手机号码
     */
    @ApiModelProperty(value = "手机号码")
    private String mobile;

    /**
     * 店铺创建时间开始
     */
    @ApiModelProperty(value = "店铺创建时间开始", example = "1")
    private Long shopCreatetimeStart;

    /**
     * 店铺创建时间结束
     */
    @ApiModelProperty(value = "店铺创建时间结束", example = "1")
    private Long shopCreatetimeEnd;


}
