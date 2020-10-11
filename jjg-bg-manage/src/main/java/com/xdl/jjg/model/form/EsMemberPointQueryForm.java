package com.xdl.jjg.model.form;

import com.xdl.jjg.response.web.QueryPageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 会员积分
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
@Data
@ApiModel
public class EsMemberPointQueryForm extends QueryPageForm {

    private static final long serialVersionUID = 2924709986165161279L;
    /**
     * 关键字(用户名，手机号)
     */
    @ApiModelProperty(value = "关键字(用户名，手机号)")
    private String keyword;

    /**
     * 注册时间开始
     */
    @ApiModelProperty(value = "注册时间开始")
    private Long createTimeStart;

    /**
     * 注册时间结束
     */
    @ApiModelProperty(value = "注册时间结束")
    private Long createTimeEnd;

}
