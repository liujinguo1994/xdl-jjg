package com.xdl.jjg.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: HQL 236154186@qq.com
 * @since: 2019/5/29 9:55
 */
@Data
public class MemberUserDTO implements Serializable {
    private static final long serialVersionUID = 2883382561235637056L;
    private String name;
    private Long userId;
    private int currentPage;
    private int size;
}
