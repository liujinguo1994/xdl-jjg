package com.xdl.jjg.model.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


/**
 * 快递平台实体
 *
 * @author zh
 * @version v7.0.0
 * @since v7.0.0
 * 2018-07-11 14:42:50
 */

@Data
public class ExpressDetailsDO implements Serializable {

    /**
     * 快递名称
     */
    private String name;
    /**
     * 快递单号
     */
    private String courierNum;
    /**
     * 快递单号里的商品图片
     */
    private List<String> image;
    /**
     * 物流详细信息
     */
    private List<Map> data;


}