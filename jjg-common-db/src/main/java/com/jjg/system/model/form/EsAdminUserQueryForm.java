package com.xdl.jjg.model.form;

import com.xdl.jjg.response.web.QueryPageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * <p>
 * 管理员
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
@Data
@ApiModel(description = "管理员")
public class EsAdminUserQueryForm extends QueryPageForm {


    private static final long serialVersionUID = 8268515066038485283L;
    /**
     * 部门
     */
    @ApiModelProperty(value = "部门", example = "1")
    private Long department;

    /**
     * 关键字(用户名，用户真实姓名)
     */
    @ApiModelProperty(value = "关键字(用户名，用户真实姓名)")
    private String keyword;

}
