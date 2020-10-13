package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

/**
 * <p>
 * 购物车
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
@Data
@TableName("es_cart")
public class EsCart extends Model<EsCart> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 会员ID
     */
    @TableField("member_id")
	private Long memberId;
    /**
     * 创建时间
     */
	@TableField(value = "create_time", fill = FieldFill.INSERT)
	private Long createTime;
    /**
     * 修改时间
     */
	@TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
	private Long updateTime;
    /**
     * 提交时间
     */
    @TableField("submit_time")
	private Long submitTime;
    /**
     * 提交IP
     */
    @TableField("submit_ip")
	private String submitIp;
    /**
     * 备注
     */
	private String remake;
    /**
     * 手机MEI
     */
    @TableField("phone_imei")
	private String phoneImei;
    /**
     * 手机MAC
     */
    @TableField("phone_mac")
	private String phoneMac;



}
