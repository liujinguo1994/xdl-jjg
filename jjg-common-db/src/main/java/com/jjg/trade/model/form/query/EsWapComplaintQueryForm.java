package com.jjg.trade.model.form.query;

import com.xdl.jjg.response.web.QueryPageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;


/**
 * <p>
 * 移动端-投诉信息
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-01-09 09:28:26
 */
@Data
@ApiModel
public class EsWapComplaintQueryForm extends QueryPageForm {

	private static final long serialVersionUID = 5101287271156760430L;

	/**
	 * 处理状态:WAIT待处理,APPLYING处理中,APPLYED已处理
	 *
	 */
	@ApiModelProperty(value = "处理状态:WAIT待处理,APPLYING处理中,APPLYED已处理")
	@NotBlank(message = "处理状态不能为空")
	private String state;
}
