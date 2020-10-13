package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;


/**
 * 人寿订单区域
 * @author yuanj
 * @version v2.0
 * @since v7.0.0
 * 2018-03-16 16:32:45
 */
@TableName("es_lfc_area")
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class EsLfcArea implements Serializable {
			
    private static final long serialVersionUID = 9122931201151887L;
    
    /**主键*/
    @TableId("id")
    @ApiModelProperty(hidden=true)
    private Integer id;
    /**区域id*/
    @TableField(value = "area_id")
    @ApiModelProperty(value="区域id",required=true)
    private String areaId ;
    /** 名称 */
    @TableField(value = "name")
    @ApiModelProperty(name = "name", value = "名称", required = false)
    private String  name;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "LfcAreaDO{" +
                "id=" + id +
                ", areaId='" + areaId + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}