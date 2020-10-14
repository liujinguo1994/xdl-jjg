package com.jjg.member.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 静态页设置
 */
@Data
public class EsPageSettingDTO implements Serializable {
    private static final long serialVersionUID = 694651540478317187L;
    /**
     * pc静态页地址
     */
    private String pcAddress;
    /**
     * wap静态页地址
     */
    private String wapAddress;

}
