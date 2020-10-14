package com.jjg.member.model.dto;

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
public class EsMemberLevelConfigDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	private Long id;

    /**
     * 等级名称
     */
	private String level;

    /**
     * 成长值下线
     */
	private Integer underLine;

    /**
     * 创建时间
     */
	private Long createTime;

    /**
     * 修改时间
     */
	private Long updateTime;


}
