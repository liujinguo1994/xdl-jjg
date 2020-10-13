package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

/**
 * <p>
 * 评论回复(店家回复)
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
@Data
@TableName("es_comment_reply")
public class EsCommentReply extends Model<EsCommentReply> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 评论id
     */
    @TableField("comment_id")
	private Long commentId;
    /**
     * 回复父id
     */
    @TableField("parent_id")
	private Long parentId;
    /**
     * 回复内容
     */
	private String content;
    /**
     * 创建时间
     */
	@TableField(value = "create_time", fill = FieldFill.INSERT)
	private Long createTime;
    /**
     * 商家或者买家
     */
	private String role;
    /**
     * 父子路径0|10|
     */
	private String path;

    /**
     * 店铺id
     */
    @TableField("shop_id")
    private Long shopId;


}
