package com.xdl.jjg.model.dto;
/**
 * <p>
 * 会员活跃查询
 * </p>
 *
 * @author LINS 1220316142@qq.com
 * @since 2019-07-02 19:50:03
 */

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class EsQueryConditionActiveInfoDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 会员类型
     */
    private Integer  memberType;
    /**
     * 搜索内容
     */
    private String coent;
    /**
     * 下单开始时间
     */
    private Long beginTime;
    /**
     * 下单结束时间
     */
    private Long endTime;
    /**
     *店铺id
     */
    private Long shopId;
}
