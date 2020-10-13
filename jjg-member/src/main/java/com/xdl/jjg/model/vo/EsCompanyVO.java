package com.xdl.jjg.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 签约公司
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
@Data
public class EsCompanyVO implements Serializable {


    /**
     * 主键ID
     */
	private Long id;
    /**
     * 公司名称
     */
	private String companyName;
    /**
     * 公司编号
     */
	private String companyCode;
    /**
     * 是否有效
     */
	private Integer isDel;
    /**
     * 创建时间
     */
	private Long createTime;
    /**
     * 更新时间
     */
	private Long updateTime;
    /**
     * 结束时间
     */
    private Long endTime;

    /**
     * 是否有效0为正常，1为禁用
     */
    private Integer state;


}
