package com.jjg.trade.model.form;


import com.xdl.jjg.response.web.QueryPageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 发票管理
 * </p>
 *
 * @author LINS 1220316142@qq.com
 * @since 2019-10-08 15:19:25
 */
@Data
@ApiModel
@Accessors(chain = true)
public class QueryReceiptOrdersForm extends QueryPageForm {

    private static final long serialVersionUID = 1L;

    /**
     * 关键词
     */
	@ApiModelProperty(value = "关键词")
	private String keyword;

//    /**
//     * 子订单编号
//     */
//	@ApiModelProperty(value = "子订单编号")
//	private String orderSn;

}
