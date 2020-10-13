package com.xdl.jjg.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 导入异常会员实体
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
@Data
public class EsExceptionMemberVO implements Serializable {


    /**
     * 手机号码
     */
    private String mobile;
    /**
     * 会员登陆用户名
     */
    private String name;
    /**
     * 密码
     */
    private String password;
    /**
     * 企业标识符
     */
    private String companyCode;

}
