package com.xdl.jjg.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: lns 1220316142@qq.com
 * @since: 2019/5/29 9:55
 */
@Data
public class QueryCommentListDTO implements Serializable {

    /**
     * 关键字
     */
    private String keyword;
    /**
     * 是否回复 1 是 0 否
     */
    private Integer replyStatus;
}
