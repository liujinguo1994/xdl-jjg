package com.jjg.member.model.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-06-04
 */
@Data
@ToString
public class EsFocusPictureDTO implements Serializable {

    private static final long serialVersionUID = 1L;

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
