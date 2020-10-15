package com.jjg.member.model.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 咨询(手机端做)
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
@Data
public class EsMemberAskDO implements Serializable {


    /**
     * 主键
     */
	private Long id;
    /**
     * 商品id
     */
	private Long goodsId;
    /**
     * 会员id
     */
	private Long memberId;
    /**
     * 咨询内容
     */
	private String content;
    /**
     * 咨询时间
     */
	private Long createTime;
    /**
     * 卖家id
     */
	private Long shopId;
    /**
     * 回复内容
     */
	private String reply;
    /**
     * 回复时间
     */
	private Long replyTime;
    /**
     * 回复状态 1 已回复 0未回复
     */
	private Integer replyStatus;
    /**
     * 状态
     */
	private Integer state;
    /**
     * 卖家名称
     */
	private String memberName;
    /**
     * 商品名称
     */
	private String goodsName;
    /**
     * 会员头像
     */
	private String memberFace;



}
