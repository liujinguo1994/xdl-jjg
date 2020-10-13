package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

/**
 * <p>
 * 会员登录日志
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
@Data
@TableName("es_member_login_log")
public class EsMemberLoginLog extends Model<EsMemberLoginLog> {

    private static final long serialVersionUID = 1L;

    /**
     * 登陆日志ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 登陆用户ID
     */
    @TableField("member_id")
	private Long memberId;
    /**
     * 用户名称
     */
    @TableField("member_name")
	private String memberName;
    /**
     * 登陆时间
     */
    @TableField("login_time")
	private Long loginTime;
    /**
     * 登陆IP
     */
    @TableField("login_ip")
	private String loginIp;
    /**
     * 登陆类型：0 app,1 pc ,2 h5
     */
    @TableField("login_type")
	private Integer loginType;
    /**
     * 登录状态
     */
	private Integer state;
    /**
     * 失败原因
     */
    @TableField("fail_reason")
	private String failReason;


}
