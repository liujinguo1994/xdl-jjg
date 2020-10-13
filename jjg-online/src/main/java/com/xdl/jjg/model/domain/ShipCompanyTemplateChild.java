package com.xdl.jjg.model.domain;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.shopx.common.util.JsonUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


/**
 * 公司模版详细配置
 *
 * @author fk
 * @version v1.0
 * @since v7.0.0
 * 2018-08-22 15:10:51
 */
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ShipCompanyTemplateChild implements Serializable {

    /**
     * <p>
     * Title:
     * </p>
     * <p>
     * Description:
     * </p>
     */

    @ApiParam("价格区间低价")
    private Double lowerPrice;
    @ApiParam("价格区间高价")
    private Double highPrice;

    @ApiParam("首重")
    private Double firstCompany;

    @ApiParam("运费")
    private Double firstPrice;

    @ApiParam("续重／需件")
    private Double continuedCompany;

    @ApiParam("续费")
    private Double continuedPrice;

    @ApiParam("地区‘，‘分隔   示例参数：北京，山西，天津，上海")
    private String area;

    @ApiParam("地区id‘，‘分隔  示例参数：1，2，3，4 ")
    private String areaId;

    @ApiParam("地区Json  示例参数：[山西，太原，天津，上海]  这个参数应该是用于前端地区选择器填充选择的数据的")
    private String areaJson;

    @ApiParam("模版类型，1 公司普通商品模板 2 生鲜模板")
    private String type;

    public Double getLowerPrice() {
        return lowerPrice;
    }

    public void setLowerPrice(Double lowerPrice) {
        this.lowerPrice = lowerPrice;
    }

    public Double getHighPrice() {
        return highPrice;
    }

    public void setHighPrice(Double highPrice) {
        this.highPrice = highPrice;
    }

    public Double getFirstCompany() {
        return firstCompany;
    }

    public void setFirstCompany(Double firstCompany) {
        this.firstCompany = firstCompany;
    }

    public Double getFirstPrice() {
        return firstPrice;
    }

    public void setFirstPrice(Double firstPrice) {
        this.firstPrice = firstPrice;
    }

    public Double getContinuedCompany() {
        return continuedCompany;
    }

    public void setContinuedCompany(Double continuedCompany) {
        this.continuedCompany = continuedCompany;
    }

    public Double getContinuedPrice() {
        return continuedPrice;
    }

    public void setContinuedPrice(Double continuedPrice) {
        this.continuedPrice = continuedPrice;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }


    public String getAreaJson() {
        return areaJson;
    }

    public void setAreaJson(String areaJson) {
        this.areaJson = areaJson;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void putArea(String area) {
        List<Map<String, Object>> list = JsonUtil.toList(area);
        StringBuffer _area = new StringBuffer("");
        StringBuffer _area_id = new StringBuffer("");
        for (Map<String, Object> map : list) {
            _area.append(map.get("local_name") + ",");
            _area_id.append(map.get("region_id") + ",");
        }

        this.area = _area.substring(0, _area.length() - 1).toString();
        this.areaId = _area_id.substring(0, _area_id.length() - 1).toString();
    }


}