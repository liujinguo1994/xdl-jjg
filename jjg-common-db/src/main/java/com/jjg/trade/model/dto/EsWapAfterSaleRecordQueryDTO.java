package com.jjg.member.model.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 *  前端控制器-移动端-售后订单查询参数
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-03-03
 */
@Data
@ToString
public class EsWapAfterSaleRecordQueryDTO implements Serializable {

    private static final long serialVersionUID = -7349392509774931388L;

    /**
     * 买家端 售后列表 会员ID
     */
    private Long memberId;

    /**
     * 状态(1处理中，2已完成)
     */
    private Integer status;

}
