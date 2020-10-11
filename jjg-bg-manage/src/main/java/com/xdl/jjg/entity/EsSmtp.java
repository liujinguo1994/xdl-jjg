package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("es_smtp")
public class EsSmtp extends Model<EsSmtp> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 主机
     */
	private String host;
    /**
     * 用户名
     */
	private String username;
    /**
     * 密码
     */
	private String password;
    /**
     * 最后发信时间
     */
	private Long lastSendTime;
    /**
     * 已发数
     */
    @TableField("send_count")
	private Integer sendCount;
    /**
     * 最大发信数
     */
    @TableField("max_count")
	private Integer maxCount;
    /**
     * 发信邮箱
     */
    @TableField("mail_from")
	private String mailFrom;
    /**
     * 端口
     */
	private Integer port;
    /**
     * ssl是否开启
     */
    @TableField("open_ssl")
	private Integer openSsl;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
