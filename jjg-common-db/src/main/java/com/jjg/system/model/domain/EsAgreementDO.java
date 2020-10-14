package com.xdl.jjg.model.domain;

import lombok.Data;

import java.io.Serializable;


/**
 * <p>
 * 协议维护
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
@Data
public class EsAgreementDO implements Serializable {
    private static final long serialVersionUID = 3809591295032230596L;
    /**
     * 主键ID
     */
    private Long id;
    /**
     * 协议编号
     */
    private String agrNo;
    /**
     * 协议内容
     */
    private String content;
    /**
     * 创建时间
     */
    private Long createTime;
    /**
     * 更新时间
     */
    private Long updateTime;
    /**
     * 协议名称
     */
    private String agrName;
    /**
     * 协议版本
     */
    private String version;

}
