package com.jjg.trade.model.domain;/**
 * @author wangaf
 * @date 2019/11/15 13:57
 **/

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

/**
 @Author wangaf 826988665@qq.com
 @Date 2019/11/15
 @Version V1.0
 **/
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ESGoodsSelectDO implements Serializable {

    private Long goodsId;

    private Integer quantity;

    private Double money;

    private String goodsName;

    private String goodsSn;

    private String original;
}
