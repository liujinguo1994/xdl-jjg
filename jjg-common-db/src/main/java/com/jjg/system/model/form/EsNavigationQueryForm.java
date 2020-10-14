package com.xdl.jjg.model.form;

import com.xdl.jjg.response.web.QueryPageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 导航菜单
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
@Data
@ApiModel
public class EsNavigationQueryForm extends QueryPageForm {

    private static final long serialVersionUID = 349771739453781318L;

    /**
     * 客户端类型
     */
    @ApiModelProperty(value = "客户端类型(PC,MOBILE)")
    private String clientType;

}
