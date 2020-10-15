package com.jjg.member.model.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 店铺导航管理
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
@Data
public class EsNavigationDO implements Serializable {


    /**
     * 导航id
     */
	private Long id;
    /**
     * 名称
     */
	private String name;
    /**
     * 是否显示
     */
	private Integer isDel;
    /**
     * 排序
     */
	private Integer sort;
    /**
     * 导航内容
     */
	private String contents;
    /**
     * URL
     */
	private String navUrl;
    /**
     * 新窗口打开
     */
	private String target;
    /**
     * 店铺id
     */
	private Long shopId;



}
