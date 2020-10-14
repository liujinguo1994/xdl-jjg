package com.jjg.member.model.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 查询后台会员列表
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
@Data
@ToString
public class EsQueryAdminMemberDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 会员等级id
     */
    private Long memberLevelId;
    /**
     * 签约公司
     */
    private String companyCode;
    /**
     * 会员信息查询
     */
    private String keyword;
    /**
     * 注册开始时间
     */
    private Long createTimeStart;
    /**
     * 注册结束时间
     */
    private Long createTimeEnd;
    /**
     * 会员状态
     */
    private String state;
    /**
     * 成长值最小
     */
    private Integer min;
    /**
     * 成长值最大
     */
    private Integer max;


}
