package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

/**
 * <p>
 * 签约公司
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2019-06-28
 */
@Data
@TableName("es_company")
public class EsCompany extends Model<EsCompany> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 公司名称
     */
    @TableField("company_name")
	private String companyName;
    /**
     * 公司编号
     */
    @TableField("company_code")
	private String companyCode;
    /**
     * 是否删除0为正常，1为禁用
     */
    @TableLogic(value = "0", delval = "1")
    @TableField("is_del")
	private Integer isDel;
    /**
     * 是否有效0为正常，1为禁用
     */
    @TableField("state")
    private Integer state;
    /**
     * 开始时间
     */
	@TableField(value = "create_time", fill = FieldFill.INSERT)
	private Long createTime;
    /**
     * 结束时间
     */
    @TableField(value = "end_time", fill = FieldFill.INSERT)
    private Long endTime;
    /**
     * 更新时间
     */
	@TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
	private Long updateTime;



}
