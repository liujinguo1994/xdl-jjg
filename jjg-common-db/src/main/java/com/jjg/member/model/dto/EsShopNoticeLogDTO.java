package com.jjg.member.model.dto;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 店铺站内消息(平台-店铺)
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2019-06-27
 */
@Data
@ToString
public class EsShopNoticeLogDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
	private Long id;

    /**
     * 店铺ID
     */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long shopId;

    /**
     * 站内信内容
     */
	private String noticeContent;

    /**
     * 发送时间
     */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long sendTime;

    /**
     * 是否删除 ：1 删除   0  未删除
     */
    @TableLogic
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
	 * 订单号
	 */
	private String orderSn;

	/**
	 * 会员ids
	 */
	private Long memberIds;
	/**
	 * 商品信息
	 */
	private List<GoodsNoticeDTO> goodsNoticeDTOList;

	protected Serializable pkVal() {
		return this.id;
	}

}
