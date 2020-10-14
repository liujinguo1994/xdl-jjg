package com.jjg.member.model.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 后台管理评论列表
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
@Data
public class EsAdminManagerDO implements Serializable {


    /**
     * 评论主键
     */
	private Long id;
    /**
     * 会员头像
     */
    private String face;
    /**
     * 会员手机号
     */
    private String mobile;
    /**
     * 好中差评
     */
    private String grade;
    /**
     * 综合评分
     */
    private Double commentScore;
    /**
     * 评论时间
     */
    private Long createTime;
    /**
     * 商品id
     */
    private Long goodsId;
    /**
     * 评论图片路径
     */
    private List<String> commentsImage;
    /**
     * 商品标签
     */
    private String goodTag;
    /**
     * 商品编号
     */
    private String goodsSn;
    /**
     * 买家昵称
     */
    private String nickname;
    /**
     * 商品规格信息
     */
    private String specValues;
    /**
     * 评论内容
     */
    private String content;
    /**
     * 评论回复
     */
    private String replayContent;
    /**
     * 是否回复 1回复，2未回复
     */
    private Integer replyStatus;
    /**
     * 子订单号
     */
    private String orderSn;
    /**
     * 商品名称
     */
    private String goodsName;
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
     * 会员id
     */
    private Long memberId;
    /**
     * 店铺id
     */
    private Long shopId;
    /**
     * 商品规格
     */
    private List<EsMemberSpecValuesDO> esMemberSpecValuesDO;

}
