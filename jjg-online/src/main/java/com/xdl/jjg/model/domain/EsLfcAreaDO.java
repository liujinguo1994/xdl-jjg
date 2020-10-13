package com.xdl.jjg.model.domain;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;

import java.io.Serializable;


/**
 * 人寿订单区域
 * @author yuanj
 * @version v2.0
 * @since v7.0.0
 * 2018-03-16 16:32:45
 */
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class EsLfcAreaDO implements Serializable {
			
    private static final long serialVersionUID = 9122931201151887L;
    
    /**主键*/
    private Integer id;
    /**区域id*/

    private String areaId ;
    /** 名称 */

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