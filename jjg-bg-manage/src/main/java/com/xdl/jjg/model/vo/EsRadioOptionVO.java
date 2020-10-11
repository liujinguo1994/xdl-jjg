package com.xdl.jjg.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 单选选项vo
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
@Data
public class EsRadioOptionVO implements Serializable {

    private static final long serialVersionUID = -7929725832999922959L;
    /**
     * 选项
     */
    private String label;

    /**
     * 选项值
     */
    private Object value;

}
