package com.jjg.system.model.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 站点设置FORM
 */
@Data
@ToString
public class EsSiteDTO implements Serializable {

    private static final long serialVersionUID = -1549672177047538309L;

    /**
     * 站点名称
     */
    private String siteName;
    /**
     * 网站标题
     */
    private String title;
    /**
     * 网站关键字
     */
    private String keywords;
    /**
     * 网站描述
     */
    private String descript;
    /**
     * 网站logo
     */
    private String logo;
    /**
     * 默认图片
     */
    private String defaultImg;
}
