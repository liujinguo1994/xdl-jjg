package com.jjg.system.model.dto;


import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
@Data
@ToString
public class EsSecuritySettingsDTO implements Serializable {

    private static final long serialVersionUID = -7415822798174683152L;
    /**
     * 注册初始积分
     */
    private Integer integral;
    /**
     * 最大登录失败次数
     */
    private Integer failedNumber;
    /**
     * 未支付失败时间(以分钟为单位)
     */
    private Integer time;

}
