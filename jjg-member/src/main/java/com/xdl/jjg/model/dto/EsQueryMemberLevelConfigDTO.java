package com.xdl.jjg.model.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 会员等级配置
 * </p>
 *
 * @author LINS 1220316142@qq.com
 * @since 2019-07-04 09:42:04
 */
@Data
@ToString
public class EsQueryMemberLevelConfigDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 等级名称
     */
	private String level;

    /**
     * 开始时间
     */
	private Long createTimeStart;

    /**
     * 结束时间
     */
    private Long createTimeEnd;


}
