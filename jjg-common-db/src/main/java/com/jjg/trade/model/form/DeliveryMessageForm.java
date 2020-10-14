package com.xdl.jjg.model.form;/**
 * Description: zhuox-shop-trade
 * <p>
 * Created by Administrator on 2019/10/15 14:54
 */

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @ClassName DeliveryMessageForm
 * @Description: TODO
 * @Author Administrator
 * @Date 2019/10/15 
 * @Version V1.0
 **/
@Data
@ApiModel
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeliveryMessageForm implements Serializable {
     /**
     * 主键ID
     */

    @ApiModelProperty(value = "自提点ID")
    @NotNull(message = "自提点ID不能为空")
    private Long DeliveryId;


    @ApiModelProperty(value = "自提点日期ID")
    @NotNull(message = "日期ID不能为空")
    private Long DateId;


    @ApiModelProperty(value = "自提时间点ID")
    @NotNull(message = "时间ID不能为空")
    private Long TimeId;

//    @ApiModelProperty(value = "收货人姓名")
////    @NotBlank(message = "收货人姓名必填")
//    private String receiverName;
//
//    @ApiModelProperty(value = "收货人手机")
////    @NotBlank(message = "收货人手机必填")
//    private String receiverMobile;
}
