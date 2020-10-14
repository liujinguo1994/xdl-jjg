package com.xdl.jjg.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

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
public class EsUpdateMemberForm implements Serializable {
    /**
     * 主键id
     */
    @ApiModelProperty(required = true, value = "主键id", example = "1")
    @NotNull(message = "id不能为空")
    private Long id;
    /**
     * 会员登陆用户名
     */
    @ApiModelProperty(required = true, value = "会员登陆用户名")
    @NotBlank(message = "用户名不能为空")
    private String name;
    /**
     * 会员登陆密码
     */
    @ApiModelProperty(required = false, value = "会员登陆密码")
    private String password;
    /**
     * 昵称
     */
    @ApiModelProperty(required = true, value = "昵称")
    @NotBlank(message = "昵称不能为空")
    private String nickname;
    /**
     * 会员性别
     */
    @ApiModelProperty(value = "会员性别", example = "1")
    private Integer sex;
    /**
     * 会员生日
     */
    @ApiModelProperty(required = true, value = "会员生日", example = "1")
    @NotNull(message = "生日不能为空")
    private Long birthday;
    /**
     * 邮箱
     */
    @ApiModelProperty(required = true, value = "邮箱")
    @NotBlank(message = "邮箱不能为空")
    private String email;
    /**
     * 所属省份ID
     */
    @ApiModelProperty(value = "所属省份ID", example = "1")
    private Long provinceId;
    /**
     * 所属城市ID
     */
    @ApiModelProperty(value = "所属城市ID", example = "1")
    private Long cityId;
    /**
     * 所属省份名称
     */
    @ApiModelProperty(value = "所属省份名称")
    private String province;
    /**
     * 所属城市名称
     */
    @ApiModelProperty(value = "所属城市名称")
    private String city;
    /**
     * 手机号码
     */
    @ApiModelProperty(required = true, value = "手机号码")
    @NotBlank(message = "手机号码不能为空")
    private String mobile;
    /**
     * 座机号码
     */
    @ApiModelProperty(value = "座机号码")
    private String tel;
    /**
     * 企业标识符
     */
    @ApiModelProperty(value = "企业标识符")
    private String companyCode;

}
