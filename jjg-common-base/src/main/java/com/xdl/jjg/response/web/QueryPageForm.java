package com.xdl.jjg.response.web;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: HQL 236154186@qq.com
 * @since: 2019/6/22 14:16
 */

@Data
public abstract class QueryPageForm implements Serializable {


    private static final long serialVersionUID = 3489707677494271887L;

    private static final int DEFAULT_PAGE_NUM = 1;
    private static final int DEFAULT_PAGE_SIZE = 10;


    @ApiModelProperty(value = "页码", name = "pageNum", dataType = "int", required = true, example = "1")
    private int pageNum = DEFAULT_PAGE_NUM;


    @ApiModelProperty(value = "页数", name = "pageSize", dataType = "int", required = true, example = "10")
    private int pageSize = DEFAULT_PAGE_SIZE;

}
