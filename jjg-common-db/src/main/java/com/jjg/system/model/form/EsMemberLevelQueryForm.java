package com.jjg.system.model.form;

import com.xdl.jjg.response.web.QueryPageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 会员等级配置
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-07-04 09:42:04
 */
@Data
@ApiModel
public class EsMemberLevelQueryForm extends QueryPageForm {


    private static final long serialVersionUID = -621962003154697757L;
    /**
     * 等级名称
     */
    @ApiModelProperty(value = "等级名称")
    private String level;

    /**
     * 创建时间开始
     */
    @ApiModelProperty(value = "创建时间开始", example = "1")
    private Long createTimeStart;

    /**
     * 创建时间结束
     */
    @ApiModelProperty(value = "创建时间结束", example = "1")
    private Long createTimeEnd;


}
