package com.xdl.jjg.model.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 签约公司门店自提表
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
@Data
public class EsCompanyDeliveryDO implements Serializable {


    /**
     * 主键ID
     */
	private Long id;
    /**
     * 签约公司ID
     */
	private Long companyId;


}
