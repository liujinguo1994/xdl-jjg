package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
@Data
@TableName("es_member_company")
public class EsMemberCompany extends Model<EsMemberCompany> {

    private static final long serialVersionUID = 1L;

    /**
     * 会员ID
     */
    @TableField("member_id")
	private Long memberId;
    /**
     * 公司ID
     */
    @TableField("company_id")
	private Long companyId;



}
