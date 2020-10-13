package com.xdl.jjg.model.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 会员店铺关联表
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
@Data
public class EsMemberShopDO implements Serializable {


    /**
     * 会员ID
     */
	private Long memberId;
    /**
     * 店铺ID
     */
	private Long shopId;



}
