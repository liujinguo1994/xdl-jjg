package com.xdl.jjg.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;


/**
 * describe: 导入传递参数
 * @author: Yj
 * @Date: 2019/3/21 11:16
 * @return:
 */
@ApiModel
public class EsCakeImportDTO implements Serializable {

    @ApiModelProperty(name = "money", value = "总导入个数")
    private Integer totalNum;

    @ApiModelProperty(name = "money", value = "导入成功个数")
    private Integer successNum;

    @ApiModelProperty(name = "money", value = "导入失败个数")
    private Integer failNum;

    @ApiModelProperty(name = "fail_data", value = "失败手机号")
    private List<FailCakeData> failData;

    public Integer getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Integer totalNum) {
        this.totalNum = totalNum;
    }

    public Integer getSuccessNum() {
        return successNum;
    }

    public void setSuccessNum(Integer successNum) {
        this.successNum = successNum;
    }

    public Integer getFailNum() {
        return failNum;
    }

    public void setFailNum(Integer failNum) {
        this.failNum = failNum;
    }

    public List<FailCakeData> getFailData() {
        return failData;
    }

    public void setFailData(List<FailCakeData> failData) {
        this.failData = failData;
    }

    @Override
    public String toString() {
        return "CakeImportDTO{" +
                "totalNum=" + totalNum +
                ", successNum=" + successNum +
                ", failNum=" + failNum +
                ", failData=" + failData +
                '}';
    }
}