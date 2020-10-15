package com.jjg.trade.model.form.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName: GoodsQueryForm
 * @Description: 商品查询Form
 * @Author: libw  981087977@qq.com
 * @Date: 7/11/2019 13:24
 * @Version: 1.0
 */
@Data
public class SelectorQueryForm implements Serializable {

    /**
     * 输入值
     */
    @ApiModelProperty(value = "关键字")
    private String keyword;
    @ApiModelProperty(value = "品牌ID")
    private List<String> brandList;
    @ApiModelProperty(value = "分类Id")
    private List<String> categoryIdList;
    @ApiModelProperty(value = "搜索价格 例如50_100")
    private String price;
    @ApiModelProperty(name = "prop", value = "属性:参数名_参数值@参数名_参数值",example = "屏幕类型_LED@屏幕尺寸_15英寸")
    private String prop;

}
