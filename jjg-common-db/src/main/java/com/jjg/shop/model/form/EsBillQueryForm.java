package com.jjg.shop.model.form;
/**
 * @author wangaf
 * @date 2019/10/16 9:08
 **/

import com.xdl.jjg.response.web.QueryPageForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 @Author wangaf 826988665@qq.com
 @Date 2019/10/16
 @Version V1.0
 **/
@Api
@Data
public class EsBillQueryForm extends QueryPageForm {

    /**
     * 结算状态 0 已结算 1 未结算
     */
    @ApiModelProperty(value = "结算状态 0 已结算 1 未结算")
    private Integer state;

    /**
     *
     */
    @ApiModelProperty(value = "结算单号")
    private String billSn;
}
