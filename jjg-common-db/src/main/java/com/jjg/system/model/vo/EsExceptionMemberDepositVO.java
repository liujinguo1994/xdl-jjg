package com.jjg.system.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 导入异常会员余额实体
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
@Data
public class EsExceptionMemberDepositVO implements Serializable {


    /**
     * 手机号码
     */
    private String mobile;
    /**
     * 余额
     */
    private Double memberBalance;

}
