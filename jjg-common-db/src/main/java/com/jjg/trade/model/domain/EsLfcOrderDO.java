package com.jjg.trade.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.xdl.jjg.util.PropertyUpperStrategy;
import io.swagger.annotations.ApiModelProperty;
import springfox.documentation.annotations.ApiIgnore;

import java.io.Serializable;
import java.util.List;

/**
 * 中国人寿订单
 * @author Snow create in 2018/5/14
 * @version v2.0
 * @since v7.0.0
 */
@ApiIgnore
@JsonNaming(value = PropertyUpperStrategy.UpperCamelCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsLfcOrderDO implements Serializable {

    @ApiModelProperty(value = "订单号")
    private String orderId;

    @ApiModelProperty(value = "用户下单时间")
    private String submitTime;

    @ApiModelProperty(value = "收件人姓名")
    private String receiverName;

    @ApiModelProperty(value = "收件人电话")
    private String receiverPhone;

    @ApiModelProperty(value = "收件人省名称")
    private String receiverProvinceName;

    @ApiModelProperty(value = "收件人市名称")
    private String receiverCityName;

    @ApiModelProperty(value = "收件人区名称")
    private String receiverDistrictName;

    @ApiModelProperty(value = "收件人详细地址")
    private String receiverAddressDetail;

//    @ApiModelProperty(value = "商品id")
//    private String presentId;

    @ApiModelProperty(value = "订单详情")
    private List<EsLfcOrderSkuDO> orderSkus;


    @ApiModelProperty(value = "费用")
    private Double expFee;

    @ApiModelProperty(value = "支付方式")
    private String payMethod;

    @ApiModelProperty(value = "来源")
    private String sourceId;

    @ApiModelProperty(value = "支付时间")
    private String payTime;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(String submitTime) {
        this.submitTime = submitTime;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public String getReceiverProvinceName() {
        return receiverProvinceName;
    }

    public void setReceiverProvinceName(String receiverProvinceName) {
        this.receiverProvinceName = receiverProvinceName;
    }

    public String getReceiverCityName() {
        return receiverCityName;
    }

    public void setReceiverCityName(String receiverCityName) {
        this.receiverCityName = receiverCityName;
    }

    public String getReceiverDistrictName() {
        return receiverDistrictName;
    }

    public void setReceiverDistrictName(String receiverDistrictName) {
        this.receiverDistrictName = receiverDistrictName;
    }

    public String getReceiverAddressDetail() {
        return receiverAddressDetail;
    }

    public void setReceiverAddressDetail(String receiverAddressDetail) {
        this.receiverAddressDetail = receiverAddressDetail;
    }

    public Double getExpFee() {
        return expFee;
    }

    public void setExpFee(Double expFee) {
        this.expFee = expFee;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public List<EsLfcOrderSkuDO> getOrderSkus() {
        return orderSkus;
    }

    public void setOrderSkus(List<EsLfcOrderSkuDO> orderSkus) {
        this.orderSkus = orderSkus;
    }

    @Override
    public String toString() {
        return "LfcOrder{" +
                "orderId='" + orderId + '\'' +
                ", submitTime='" + submitTime + '\'' +
                ", receiverName='" + receiverName + '\'' +
                ", receiverPhone='" + receiverPhone + '\'' +
                ", receiverProvinceName='" + receiverProvinceName + '\'' +
                ", receiverCityName='" + receiverCityName + '\'' +
                ", receiverDistrictName='" + receiverDistrictName + '\'' +
                ", receiverAddressDetail='" + receiverAddressDetail + '\'' +
                ", orderSkus=" + orderSkus +
                ", expFee=" + expFee +
                ", payMethod='" + payMethod + '\'' +
                ", sourceId='" + sourceId + '\'' +
                ", payTime='" + payTime + '\'' +
                '}';
    }
}
