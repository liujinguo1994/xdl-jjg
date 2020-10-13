package com.xdl.jjg.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-04
 */
@Data
@ToString
public class EsMemberCompanyDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 会员ID
     */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long memberId;

    /**
     * 公司ID
     */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long companyId;


}
