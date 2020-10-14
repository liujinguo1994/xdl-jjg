package com.xdl.jjg.model.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 会员站内消息
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
@Data
public class EsMemberNoticeLogDO implements Serializable {


    /**
     * 会员历史消息id
     */
	private Long id;
    /**
     * 会员id
     */
	private Long memberId;
    /**
     * 站内信内容
     */
	private String content;
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
     * 接收时间
     */
	private Long receiveTime;
    /**
     * 标题
     */
	private String title;
    /**
     * 消息类型(系统、店铺)
     */
	private Integer msgType;



}
