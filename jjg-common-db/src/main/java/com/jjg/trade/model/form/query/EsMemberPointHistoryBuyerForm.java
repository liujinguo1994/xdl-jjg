package com.jjg.trade.model.form.query;

import com.xdl.jjg.response.web.QueryPageForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 会员积分明细
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-04
 */
@Data
@Api
public class EsMemberPointHistoryBuyerForm extends QueryPageForm {

    private static final long serialVersionUID = 1L;

    /**
     * 积分类型1为增加，0为消费
     */
    @ApiModelProperty(required = false,value = "积分类型1为增加，0为消费",example = "0")
    private Integer gradePointType;

    /**
     * 操作理由0：其他送 1：购物送 2：评论送
     */
    @ApiModelProperty(required = false, value = "操作理由0：其他送 1：购物送 2：评论送")
    private String reason;

}
