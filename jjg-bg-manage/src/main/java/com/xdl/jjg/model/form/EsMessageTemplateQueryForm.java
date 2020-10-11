package com.xdl.jjg.model.form;

import com.xdl.jjg.response.web.QueryPageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * <p>
 *  消息模板
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
@Data
@ApiModel
public class EsMessageTemplateQueryForm extends QueryPageForm {


    private static final long serialVersionUID = -9095192739060176953L;

    /**
     * 类型(会员 ,店铺 ,其他)
     */
    @ApiModelProperty(required = true, value = "类型(MEMBER:会员 ,SHOP:店铺)")
    @NotBlank(message = "类型不能为空")
    private String type;




}
