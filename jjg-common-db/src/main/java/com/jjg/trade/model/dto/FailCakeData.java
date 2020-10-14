package com.jjg.member.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;


/**
 * describe: 导入传递参数
 * @author: Yj
 * @Date: 2019/3/21 11:16
 * @return:
 */
@ApiModel
public class FailCakeData implements Serializable {

    @ApiModelProperty(name = "name", value = "商品名称")
    private String name;
    @ApiModelProperty(name = "code", value = "卡券码")
    private String code;
    @ApiModelProperty(name = "password", value = "卡券码密码")
    private String password;



    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "FailCakeData{" +
                "name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}