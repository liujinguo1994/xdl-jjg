package com.jjg.system.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


/**
 * <p>
 * 部门
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-06-18
 */
@Data
@ApiModel
public class EsDepartmentVO implements Serializable {


    private static final long serialVersionUID = 6866624277342490515L;
    /**
     * 主键ID
     */

    @ApiModelProperty(value = "主键ID", example = "1")
    private Long id;

    /**
     * 部门名称
     */

    @ApiModelProperty(value = "部门名称")
    private String departmentName;

    /**
     * 是否删除
     */

    @ApiModelProperty(value = "是否删除", example = "1")
    private Integer isDel;

    /**
     * 创建时间
     */

    @ApiModelProperty(value = "创建时间", example = "1")
    private Long createTime;

    /**
     * 修改时间
     */

    @ApiModelProperty(value = "修改时间", example = "1")
    private Long updateTime;

    /**
     * 父id
     */
    @ApiModelProperty(value = "父id", example = "1")
    private Long parentId;

    /**
     * 父子路径
     */
    @ApiModelProperty(value = "父子路径")
    private String path;

    /**
     * 子部门
     */
    @ApiModelProperty(value = "子部门")
    private List<EsDepartmentVO> children;

}
