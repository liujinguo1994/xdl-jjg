package com.jjg.member.model.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
@Data
public class EsMemberCompanyDO implements Serializable {


    /**
     * 会员ID
     */
	private Long memberId;
    /**
     * 公司ID
     */
	private Long companyId;



}
