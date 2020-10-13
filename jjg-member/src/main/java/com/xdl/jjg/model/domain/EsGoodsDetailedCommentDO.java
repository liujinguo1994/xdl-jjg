package com.xdl.jjg.model.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 商品详情页评论
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
@Data
public class EsGoodsDetailedCommentDO implements Serializable {


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
     * 是否有图片 1 有 0 没有
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
     * 综合评分
     */
    private Double commentScore;
    /**
     * 订单明细编号
     */
	private String orderSn;
    /**
     * 是否回复 1 是 0 否
     */
	private Integer replyStatus;
    /**
     * 图片信息
     */
    private List<String> images;



}
