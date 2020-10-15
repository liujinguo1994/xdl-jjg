package com.jjg.member.model.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * app端优惠券数量统计
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
@Data
public class EsComplaintStatDO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 待处理数量
     */
    private Integer waitCount;
    /**
     * 处理中数量
     */
    private Integer runningCount;
    /**
     * 已完成数量
     */
    private Integer finishCount;

}
