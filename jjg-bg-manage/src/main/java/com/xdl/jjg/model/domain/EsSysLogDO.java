package com.xdl.jjg.model.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 系统操作日志
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
@Data
public class EsSysLogDO implements Serializable {

    private static final long serialVersionUID = -492564198068611345L;
    /**
     * 主键ID
     */
    private Long id;
    /**
     * 创建时间
     */
    private Long createTime;
    /**
     * 更新时间
     */
    private Long updateTime;
    /**
     * 操作人ID
     */
    private Long operId;
    /**
     * 操作内容
     */
    private String operContent;
    /**
     * 操作类型
     */
    private Integer operType;
}
