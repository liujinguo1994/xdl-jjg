package com.jjg.member.model.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * <p>
 * 导入失败会员详情
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-03-27
 */
@Data
@ApiModel
public class EsImportMemberFailDataVO implements Serializable {

    private static final long serialVersionUID = -3589717115274123659L;

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "用户名")
    private String name;

    @ApiModelProperty(value = "企业标识符")
    private String companyCode;
}