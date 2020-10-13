package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.io.Serializable;


/**
 * 品牌实体
 * @author fk
 * @version v2.0
 * @since v7.0.0
 * 2018-03-16 16:32:45
 */
@TableName("es_cake_card")
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
@Data
public class EsCakeCard implements Serializable {
			
    private static final long serialVersionUID = 9122931201151887L;
    
    /**卡券主键*/
    @TableId(value="id", type= IdType.AUTO)
    private Integer id;
    /**卡券名称*/


    private String name;
    /**卡券码*/

    private String code;
    /**卡券码密码*/


    private String password;


    private String mobile;


    /**订单号*/
    @TableId(value = "order_sn")

    private String orderSn;
    /**是否可用，0正常，1用过*/
    @TableId(value = "is_used")
    private Integer isUsed;

    /**创建时间*/
    @TableId(value = "create_time")
    private Long createTime;

    /**更新时间*/
    @TableId(value = "update_time")
    private Long updateTime;

}