package com.xdl.jjg.model.form;

import com.xdl.jjg.response.web.QueryPageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 角色
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
@Data
@ApiModel(description = "角色")
public class EsRoleQueryForm extends QueryPageForm {

    private static final long serialVersionUID = 2545884328132568794L;
    /**
     * 权限名称
     */
    @ApiModelProperty(value = "权限名称")
    private String roleName;

}
