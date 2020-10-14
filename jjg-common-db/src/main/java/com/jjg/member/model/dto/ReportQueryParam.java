package com.jjg.member.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 投诉条件
 * </p>
 *
 * @author yuanj 595831328@qq.com
 * @since 2019-08-13
 */
@Data
public class ReportQueryParam implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 状态
     */
    private String state;

    /**
     * 会员ID
     */
    private Long memberId;


}
