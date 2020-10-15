package com.jjg.system.model.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-06-18
 */
@Data
@ToString
public class EsDepartmentDTO implements Serializable {


    private static final long serialVersionUID = 2542793686366988900L;
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 部门名称
     */
    private String departmentName;

    /**
     * 有效状态
     */
    private Integer status;

    /**
     * 是否删除
     */
    private Integer isDel;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 修改时间
     */
    private Long updateTime;

    /**
     * 父id
     */
    private Long parentId;

    /**
     * 父子路径
     */
    private String path;

}
