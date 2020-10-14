package com.xdl.jjg.model.form.query;

import com.shopx.common.model.result.QueryPageForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 会员积分明细
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-04
 */
@Data
@Api
public class EsMemberPointHistoryForm  extends QueryPageForm {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @ApiModelProperty(required = false, value = "主键ID", example = "1")
    private Long id;

    /**
     * 会员ID
     */
    @ApiModelProperty(required = false,value = "会员ID",example = "1")
    private Long memberId;

    /**
     * 积分值
     */
    @ApiModelProperty(required = false,value = "积分值",example = "1")
    private Integer gradePoint;

    /**
     * 操作时间
     */
    @ApiModelProperty(required = false,value = "操作时间",example = "1559303049597")
    private Long createTime;

    /**
     * 操作理由
     */
    @ApiModelProperty(required = false, value = "操作理由")
    private String reason;

    /**
     * 积分类型
     */
    @ApiModelProperty(required = false,value = "积分类型",example = "1")
    private Integer gradePointType;

    /**
     * 操作者
     */
    @ApiModelProperty(required = false, value = "操作者")
    private String operator;

}
