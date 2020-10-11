package com.xdl.jjg.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 配置文件映射实体
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
@Data
public class EsConfigItemVO implements Serializable {
    /**
     * 配置文件name值
     */
    private String name;
    /**
     * 配置文件name映射文本值
     */
    private String text;
    /**
     * 配置文件显示在浏览器时，input的type属性
     */
    private String type;
    /**
     * 配置的值
     */
    private Object value;
    /**
     * 如果是select 是需要将可选项传递到前端
     */
    private List<EsRadioOptionVO> options;
}
