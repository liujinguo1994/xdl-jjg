package com.xdl.jjg.model.domain;

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
public class EsMemberCouponStatDO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 可使用优惠券数量
     */
    private Integer enabledCount;
    /**
     * 已使用优惠券数量
     */
    private Integer usedCount;
    /**
     * 失效优惠券数量
     */
    private Integer failureCount;

}
