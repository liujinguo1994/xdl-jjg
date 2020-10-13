package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

/**
 * <p>
 * 会员站内消息
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
@Data
@TableName("es_member_notice_log")
public class EsMemberNoticeLog extends Model<EsMemberNoticeLog> {

    private static final long serialVersionUID = 1L;

    /**
     * 会员历史消息id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 会员id
     */
    @TableField("member_id")
    private Long memberId;
    /**
     * 站内信内容
     */
    private String content;
    /**
     * 发送时间
     */
    @TableField(value = "send_time")
    private Long sendTime;
    /**
     * 是否删除 ：1 删除   0  未删除
     */
    @TableField("is_del")
    private Integer isDel;
    /**
     * 是否已读 ：1已读   0 未读
     */
    @TableField("is_read")
    private Integer isRead;
    /**
     * 接收时间
     */
    @TableField(value = "receive_time")
    private Long receiveTime;
    /**
     * 标题
     */
    private String title;
    /**
     * 消息类型(系统、店铺)
     */
    @TableField("msg_type")
    private Integer msgType;


}
