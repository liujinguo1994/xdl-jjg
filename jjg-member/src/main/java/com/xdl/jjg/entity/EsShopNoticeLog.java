package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

/**
 * <p>
 * 店铺站内消息(平台-店铺)
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
@Data
@TableName("es_shop_notice_log")
public class EsShopNoticeLog extends Model<EsShopNoticeLog> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 店铺ID
     */
    @TableField("shop_id")
	private Long shopId;
    /**
     * 站内信内容
     */
    @TableField("notice_content")
	private String noticeContent;
    /**
     * 发送时间
     */
    @TableField("send_time")
	private Long sendTime;
    /**
     * 是否删除 ：1 删除   0  未删除
     */
    @TableField("is_del")
    @TableLogic(value = "0",delval = "1")
	private Integer isDel;
    /**
     * 是否已读 ：1已读   0 未读
     */
    @TableField("is_read")
	private Integer isRead;
    /**
     * 消息类型
     */
	private String type;

    /**
     * 会员id
     */
    @TableField("member_id")
    private Long memberId;
    /**
     * 商品信息
     */
    private String goodsJson;



}
