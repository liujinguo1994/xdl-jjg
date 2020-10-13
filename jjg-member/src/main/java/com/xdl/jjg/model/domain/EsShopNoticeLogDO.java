package com.xdl.jjg.model.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 店铺站内消息(平台-店铺)
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
@Data
public class EsShopNoticeLogDO implements Serializable {


    /**
     * id
     */
	private Long id;
    /**
     * 店铺ID
     */
	private Long shopId;
    /**
     * 站内信内容
     */
	private String noticeContent;
    /**
     * 发送时间
     */
	private Long sendTime;
    /**
     * 是否删除 ：1 删除   0  未删除
     */
	private Integer isDel;
    /**
     * 是否已读 ：1已读   0 未读
     */
	private Integer isRead;
    /**
     * 消息类型
     */
	private String type;

    /**
     * 会员id
     */
    private long memberId;
    /**
     * 商品信息
     */
    private String goodsJson;



}
