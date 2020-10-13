package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

/**
 * <p>
 * 咨询(手机端做)
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
@Data
@TableName("es_member_ask")
public class EsMemberAsk extends Model<EsMemberAsk> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 商品id
     */
    @TableField("goods_id")
	private Long goodsId;
    /**
     * 会员id
     */
    @TableField("member_id")
	private Long memberId;
    /**
     * 咨询内容
     */
	private String content;
    /**
     * 咨询时间
     */
	@TableField(value = "create_time", fill = FieldFill.INSERT)
	private Long createTime;
    /**
     * 卖家id
     */
    @TableField("shop_id")
	private Long shopId;
    /**
     * 回复内容
     */
	private String reply;
    /**
     * 回复时间
     */
    @TableField("reply_time")
	private Long replyTime;
    /**
     * 回复状态 1 已回复 0未回复
     */
    @TableField("reply_status")
	private Integer replyStatus;
    /**
     * 状态
     */
	private Integer state;
    /**
     * 卖家名称
     */
    @TableField("member_name")
	private String memberName;
    /**
     * 商品名称
     */
    @TableField("goods_name")
	private String goodsName;
    /**
     * 会员头像
     */
    @TableField("member_face")
	private String memberFace;



}
