package com.xdl.jjg.model.form;

import com.xdl.jjg.response.web.QueryPageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 会员
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
@Data
@ApiModel
public class EsMemberQueryForm extends QueryPageForm {

    private static final long serialVersionUID = 2924709986165161279L;
    /**
     * 关键字(会员id,用户名，手机号，邮箱)
     */
    @ApiModelProperty(value = "关键字(会员id,用户名，手机号，邮箱)")
    private String keyword;

    /**
     * 会员等级id
     */
    @ApiModelProperty(value = "会员等级id")
    private Long memberLevelId;

    /**
     * 企业标识符
     */
    @ApiModelProperty(value = "企业标识符")
    private String companyCode;

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

    /**
     * 会员状态(0,正常 1，禁用)
     */
    @ApiModelProperty(value = "会员状态(0,正常 1，禁用)", example = "1")
    private Integer state;

}
