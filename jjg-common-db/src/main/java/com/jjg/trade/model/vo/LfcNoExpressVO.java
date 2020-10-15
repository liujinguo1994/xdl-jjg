package com.jjg.trade.model.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;


/**
 * 快递无物流信息
 *
 * @author zh
 * @version v7.0.0
 * @since v7.0.0
 * 2018-07-11 14:42:50
 */
@ApiModel
@Data
public class LfcNoExpressVO {

    /**
     * 快递单号
     */
    private String number;
    /**
     * 快递单号
     */
    private String company;
    /**
     * 物流详细信息
     */
    private String content;


}