package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
@Data
@TableName("es_email")
public class EsEmail extends Model<EsEmail> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 邮件标题
     */
	private String title;
    /**
     * 邮件类型
     */
	private String type;
    /**
     * 是否成功
     */
    @TableField("is_success")
	private Integer isSuccess;
    /**
     * 邮件接收者
     */
	private String email;
    /**
     * 邮件内容
     */
	private String content;
    /**
     * 错误次数
     */
    @TableField("error_num")
	private Integer errorNum;
    /**
     * 最后发送时间
     */
	@TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
	private Long updateTime;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
