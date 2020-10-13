package com.xdl.jjg.model.dto;

import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 会员站内消息
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-04
 */
@Data
@ToString
public class EsMemberNoticeLogDTO implements Serializable {

    private static final long serialVersionUID = 1L;

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
    @TableLogic
	private Integer isDel;

    /**
     * 是否已读 ：1已读   0 未读 3全部
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
