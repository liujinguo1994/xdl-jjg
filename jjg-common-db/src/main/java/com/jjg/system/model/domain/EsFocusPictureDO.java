package com.jjg.system.model.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
@Data
public class EsFocusPictureDO implements Serializable {


    private static final long serialVersionUID = 1673433962372561502L;
    /**
     * 主键id
     */
    private Long id;
    /**
     * 图片地址
     */
    private String picUrl;
    /**
     * 操作类型
     */
    private String operationType;
    /**
     * 操作参数
     */
    private String operationParam;
    /**
     * 操作地址
     */
    private String operationUrl;
    /**
     * 客户端类型 APP/WAP/PC
     */
    private String clientType;
    /**
     * 背景色
     */
    private String operationColor;

}
