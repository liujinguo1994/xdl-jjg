package com.xdl.jjg.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

/**
 * @author xiaoLin
 * @ClassName EsCategoryO
 * @Description 类目DO
 * @create 2019/12/17 13:57
 */
@Data
@JsonIgnoreProperties(ignoreUnknown=true)
public class EsCategoryMemberDO implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;
    /**
     * 分类名称
     */
    private String name;

    private Integer num;

    protected Serializable pkVal() {
        return this.id;
    }
}
