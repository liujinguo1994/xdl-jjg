package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

/**
 * <p>
 * 评论
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
@Data
@TableName("es_member_comment")
public class EsMemberComment extends Model<EsMemberComment> {

    private static final long serialVersionUID = 1L;

    /**
     * 评论主键
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 商品id
     */
    @TableField("goods_id")
	private Long goodsId;
    /**
     * skuid
     */
    @TableField("sku_id")
	private Long skuId;
    /**
     * 会员id
     */
    @TableField("member_id")
	private Long memberId;
    /**
     * 卖家id
     */
    @TableField("shop_id")
	private Long shopId;
    /**
     * 会员名称
     */
    @TableField("member_name")
	private String memberName;
    /**
     * 会员头像
     */
    @TableField("member_face")
	private String memberFace;
    /**
     * 商品名称
     */
    @TableField("goods_name")
	private String goodsName;
    /**
     * 评论内容
     */
	private String content;
    /**
     * 评论时间
     */
	@TableField(value = "create_time", fill = FieldFill.INSERT)
	private Long createTime;
    /**
     * 是否有图片 1 有 0 没有
     */
    @TableField("have_image")
	private Integer haveImage;
    /**
     * 状态  0 正常 1 删除
     */
	private Integer state;
    /**
     * 好中差评
     */
	private String grade;
    /**
     * 订单明细编号
     */
    @TableField("order_sn")
	private String orderSn;
    /**
     * 是否回复 1 是 0 否
     */
    @TableField("reply_status")
	private Integer replyStatus;
    /**
     * 评论标签
     */
    private String labels;
    /**
     * 综合评分
     */
    @TableField("comment_score")
    private Double commentScore;
    /**
     * 发货速度评分
     */
    @TableField(exist = false)
    private Double deliveryScore;

    /**
     * 描述相符度评分
     */
    @TableField(exist = false)
    private Double descriptionScore;

    /**
     * 服务评分
     */
    @TableField(exist = false)
    private Double serviceScore;
    /**
     * 图片地址
     */
    @TableField(exist = false)
    private String original;
    /**
     * 商品编号
     */
    @TableField("goods_sn")
    private String goodsSn;
    /**
     * 是否匿名1否，2是
     */
    @TableField("is_anonymous")
    private Integer isAnonymous;
}
