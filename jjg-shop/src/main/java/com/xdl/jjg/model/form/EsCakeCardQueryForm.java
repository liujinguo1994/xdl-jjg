package com.xdl.jjg.model.form;


import com.xdl.jjg.response.web.QueryPageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 商品查询条件
 *
 * @author fk
 * @version v2.0
 * @since v7.0.0
 * 2018年3月21日 下午3:46:04
 */
@Data
@ApiModel
public class EsCakeCardQueryForm extends QueryPageForm {
    /**
     * 是否上架 0代表已下架，1代表已上架
     */
    @ApiModelProperty(name = "is_used", value = "是否可用，0正常，1用过")
    private Integer isUsed;
    /**
     * 卡券码
     */
    @ApiModelProperty(name = "code", value = "卡券码")
    private String code;

    /**
     * 订单号
     */
    @ApiModelProperty(name = "order_sn", value = "卡券码")
    private String orderSn;

}
