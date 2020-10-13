package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

/**
 * <p>
 * 会员积分明细
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
@Data
@TableName("es_member_point_history")
public class EsMemberPointHistory extends Model<EsMemberPointHistory> {

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
     * 积分值
     */
    @TableField("grade_point")
	private Integer gradePoint;
    /**
     * 操作时间
     */
	@TableField(value = "create_time", fill = FieldFill.INSERT)
	private Long createTime;
    /**
     * 操作理由0：其他送 1：购物送 2：评论送
     */
    @TableField(value = "reason")
	private Integer reason;
    /**
     * 积分类型
     */
    @TableField("grade_point_type")
	private Integer gradePointType;
    /**
     * 操作者
     */
    @TableField(value = "operator")
	private String operator;

    /**
     * 当前积分
     */
    @TableField(value = "current_point")
    private Integer currentPoint;

    /**
     * 订单编号
     */
    @TableField(value = "order_sn")
    private String orderSn;

}
