package com.jjg.trade.model.form.query;

import com.shopx.common.model.result.QueryPageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 余额明细查询
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-07-17 10:29:43
 */
@Data
@ApiModel
public class EsMemberDepositQueryForm extends QueryPageForm {

    private static final long serialVersionUID = 1L;

    /**
     * 查询类型1三个月内，2三个月以前
     */
    @ApiModelProperty(required = false,value = "查询类型1三个月内，2三个月以前，不传为查询全部")
    private String threeMonth;

    /**
     * 操作类型(0收入,1支出)
     */
    @ApiModelProperty(required = false,value = "操作类型(0收入,1支出)")
    private String type;


}
