package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

/**
 * <p>
 * 会员店铺关联表
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
@Data
@TableName("es_member_shop")
public class EsMemberShop extends Model<EsMemberShop> {

    private static final long serialVersionUID = 1L;

    /**
     * 会员ID
     */
    @TableField("member_id")
	private Long memberId;
    /**
     * 店铺ID
     */
    @TableField("shop_id")
	private Long shopId;



}
