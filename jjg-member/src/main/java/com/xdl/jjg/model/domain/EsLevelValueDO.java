package com.xdl.jjg.model.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 等级值
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-29 14:18:12
 */
@Data
public class EsLevelValueDO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 等级最低值
     */
    private Integer min;
    /**
     * 等级最大值
     */
    private Integer max;


}
