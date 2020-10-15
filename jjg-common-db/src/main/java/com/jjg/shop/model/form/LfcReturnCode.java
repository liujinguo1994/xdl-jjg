package com.jjg.shop.model.form;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.xdl.jjg.util.PropertyUpperStrategy;
import io.swagger.annotations.ApiModelProperty;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 中国人寿返回
 * @author yuanj create in 2019/10/08
 * @version v2.0
 * @since v7.0.0
 */
@ApiIgnore
@JsonNaming(value = PropertyUpperStrategy.UpperCamelCaseStrategy.class)
public class LfcReturnCode {

    @ApiModelProperty(value = "状态码")
    private String code;

    @ApiModelProperty(value = "信息")
    private String msg;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "LfcReturnCode{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}
