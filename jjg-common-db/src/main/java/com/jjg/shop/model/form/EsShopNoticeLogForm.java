package com.jjg.shop.model.form;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@ApiModel
public class EsShopNoticeLogForm implements Serializable {
    /**
     * 站内信内容
     */
    @ApiModelProperty(value = "消息内容")
    @NotBlank(message = "消息内容不能为空")
    private String noticeContent;

    /**
     * 会员ids
     */
    @ApiModelProperty(value = "会员集合")
    @NotNull(message = "会员集合不能为空")
    private Long [] memberIds;
    /**
     * 商品信息
     */
    @ApiModelProperty(value = "商品信息集合")
    private List<ESGoodsNoticeForm> goodsNoticeDTOList;
}
