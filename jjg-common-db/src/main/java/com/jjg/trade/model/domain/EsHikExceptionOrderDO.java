package com.jjg.trade.model.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 *  异常订单表实体（海康用）
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2020-05-09
 */
@Data
public class EsHikExceptionOrderDO implements Serializable{

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;


    /**
     * 联系人姓名
     */
    private String name;

    /**
     * 创建时间（操作时间）
     */
    private Long createTime;

    /**
     * 更新时间
     */
    private Long updateTime;

    /**
     * 手机号码
     */
    private String mobile;

    /**
     * 订单号
     */
    private String orderSn;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 描述
     */
    private String description;

    /**
     * 备注
     */
    private String remark;


}
