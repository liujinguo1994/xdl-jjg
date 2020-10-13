package com.xdl.jjg.model.domain;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


/**
 * 公司运费模版实体
 *
 * @author zjp
 * @version v7.0.0
 * @since v7.0.0
 * 2018-03-28 21:44:49
 */
@Data
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ShipCompanyTemplateDO implements Serializable {


    /**
     * 模版id
     */
    @ApiParam(hidden = true, value = "模版id")
    private Integer id;

    @ApiParam("商家id")
    private Integer sellerId;

    @ApiParam("名字")
    private String name;

    @ApiParam("模版类型，1 公司普通商品模板 2 生鲜模板")
    private Integer type;

    @ApiParam("模版详情：前端无需传递")
    private String detail;


    @ApiParam("模版详情：前端无需传递")
    private List<ShipCompanyTemplateChild> items;

    public Integer getSellerId() {
        return sellerId;
    }

    public void setSellerId(Integer sellerId) {
        this.sellerId = sellerId;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<ShipCompanyTemplateChild> getItems() {
        if (detail != null) {
            Gson gson = new Gson();
            this.items = gson.fromJson(detail, new TypeToken<List<ShipCompanyTemplateChild>>() {
            }.getType());
        }
        return items;
    }

    public void setItems(List<ShipCompanyTemplateChild> items) {
        this.items = items;
    }

    public void initDetail() {
        if (items != null) {
            Gson gson = new Gson();
            this.setDetail(gson.toJson(items));
        }
    }


    /**
     * 1 重量算运费 2 计件算运费
     *
     * @return
     */
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        if (items != null) {
            Gson gson = new Gson();
            this.detail = gson.toJson(items);
        }
        this.detail = detail;
    }

    @Override
    public String toString() {
        return "ShipTemplateDO{" +
                "id=" + id +
                ", sellerId=" + sellerId +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", detail='" + detail + '\'' +
                ", items=" + items +
                '}';
    }
}