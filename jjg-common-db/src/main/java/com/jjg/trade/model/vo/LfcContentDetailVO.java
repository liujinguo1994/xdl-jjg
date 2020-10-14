package com.jjg.member.model.vo;
import io.swagger.annotations.ApiModel;
import lombok.Data;


/**
 * 快递轨迹详情
 *
 * @author zh
 * @version v7.0.0
 * @since v7.0.0
 * 2018-07-11 14:42:50
 */
@ApiModel
@Data
public class LfcContentDetailVO {

    /**
     * 快递轨迹
     */
    private String desc;
    /**
     * 快递时间点
     */
    private String time;

}