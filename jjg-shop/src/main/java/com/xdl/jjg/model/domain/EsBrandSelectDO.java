package com.jjg.member.model.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class EsBrandSelectDO implements Serializable {

    /**
     * 选择器ID
     */
    private Integer id;

    /**
     * 选择器内容
     */
    private String text;

    /**
     * 是否选中
     */
    private Boolean selected;
}
