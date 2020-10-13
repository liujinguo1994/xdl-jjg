package com.xdl.jjg.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: FormVO
 * @Description: 支付请求返回的form
 * @Author: libw  981087977@qq.com
 * @Date: 6/11/2019 15:37
 * @Version: 1.0
 */
@ApiModel
@Data
public class FormVO implements Serializable {

    @ApiModelProperty(value = "状态:0成功,1失败")
    private Integer code;

    @ApiModelProperty(value = "返回结果")
    private String data;

    @ApiModelProperty(value = "微信返回结果")
    private Map<String,String> wechatData;

    @ApiModelProperty("结果描述")
    private String msg;

    @ApiModelProperty(value = "表单请求地址")
    private String gatewayUrl;

    @ApiModelProperty(value = "表单请求内容")
    private List<FormItemVO> formItems;

    public String getGatewayUrl() {
        return gatewayUrl;
    }

    public void setGatewayUrl(String gatewayUrl) {
        this.gatewayUrl = gatewayUrl;
    }

    public List<FormItemVO> getFormItems() {
        return formItems;
    }

    public void setFormItems(List<FormItemVO> formItems) {
        this.formItems = formItems;
    }
}
