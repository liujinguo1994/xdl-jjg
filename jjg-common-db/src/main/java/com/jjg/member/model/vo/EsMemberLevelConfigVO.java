package com.jjg.member.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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
@ApiModel
public class EsMemberLevelConfigVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	@ApiModelProperty(required = false,value = "主键id",example = "1")
	private Long id;

    /**
     * 等级名称
     */
	@ApiModelProperty(required = false,value = "等级名称")
	private String level;

    /**
     * 成长值下线
     */
	@ApiModelProperty(required = false,value = "成长值下线",example = "1")
	private Integer underLine;

    /**
     * 创建时间
     */
	@ApiModelProperty(required = false,value = "创建时间",example = "1559303049597")
	private Long createTime;

    /**
     * 修改时间
     */
	@ApiModelProperty(required = false,value = "修改时间",example = "1559303049597")
	private Long updateTime;


}
