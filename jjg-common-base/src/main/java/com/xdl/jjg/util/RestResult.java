package com.xdl.jjg.util;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author sanqi
 * @version 1.0
 * @data 2019年07月24日
 * @description 统一返回类
 */
@Data
@ApiModel(value = "统一返回类")
@SuppressWarnings("unchecked")
public class RestResult<T> implements Serializable {

    private static final long serialVersionUID = -8709615713843974588L;

    @ApiModelProperty(value = "返回的code，0成功，其他的失败")
    private Integer code;
    @ApiModelProperty(value = "错误时返回的错误信息")
    private String msg;
    @ApiModelProperty(value = "成功时返回的数据集")
    private T data;

    public RestResult() {
        this.code = 0;
        this.msg = "成功";
    }
    public RestResult(String msg) {
        this.code = -1;
        this.msg = msg;
    }
    public static RestResult ok() {
        return ok(null);
    }

    public static RestResult ok(Object data) {
        return ok("成功",data);
    }


    public static RestResult ok(String msg,Object data) {
        return set(0,msg,data);
    }

    public static RestResult fail() {
        return fail("失败");
    }
    public static RestResult fail(String msg) {
        return fail(-1, msg);
    }

    public static RestResult fail(Integer code, String msg) {
        return set(code,msg,null);
    }

    public static RestResult set(Integer code, String msg,Object data) {
        RestResult r = new RestResult();
        r.code = code;
        r.msg = msg;
        r.data = data;
        return r;
    }




}
