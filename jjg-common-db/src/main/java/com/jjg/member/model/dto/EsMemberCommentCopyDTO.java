package com.jjg.member.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 评论
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-04
 */
@Data
@ToString
public class EsMemberCommentCopyDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 评论主键
     */
	private Long id;

    /**
     * 商品id
     */
	private Long goodsId;

    /**
     * skuid
     */
	private Long skuId;

    /**
     * 会员id
     */
	private Long memberId;

    /**
     * 卖家id
     */
	private Long shopId;

    /**
     * 会员名称
     */
	private String memberName;

    /**
     * 会员头像
     */
	private String memberFace;

    /**
     * 商品名称
     */
	private String goodsName;

    /**
     * 评论内容
     */
	private String content;

    /**
     * 评论时间
     */
	private Long createTime;

    /**
     * 是否有图片 1 有 2没有
     */
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
	private String orderSn;

    /**
     * 是否回复 1 是 0 否
     */
	private Integer replyStatus;
    /**
     * 评论标签
     */
    private String labels;
    /**
     * 发货速度评分
     */
    private Double deliveryScore;

    /**
     * 描述相符度评分
     */
    private Double descriptionScore;

    /**
     * 服务评分
     */
    private Double serviceScore;
    /**
     * 图片地址
     */
//    private String original;
    @ApiModelProperty(value = "评价图片")
    private List<String> original;
    /**
     * 是否匿名 1否，2是
     */
    private Integer isAnonymous;

    /**
     * 物流等级
     */
    private Integer logisticsLevel;

    /**
     * 商品等级
     */
    private Integer goodLevel;

    /**
     * 服务等级
     */
    private Integer serviceLevel;
}
