package com.jjg.trade.model.form.query;

import com.xdl.jjg.response.web.QueryPageForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @ClassName: GoodsQueryForm
 * @Description: 商品查询Form
 * @Author: libw  981087977@qq.com
 * @Date: 7/11/2019 13:24
 * @Version: 1.0
 */
@Data
public class GoodsQueryForm extends QueryPageForm {

    /**
     *原图路径
     */
    private String thumbnail;

    /**
     * 店铺ID
     */
    private Integer shopId;

    /**
     *店铺名称
     */
    private String shopName;

    /**
     * 评论数量
     */
    private Integer commentNum;

    /**
     *好评率
     */
    private Double grade;

    /**
     *商品价格
     */
    private Double money;

    /**
     * 品牌
     */
//    private Integer brand;

    /**
     *分类ID
     */
    private Long cat;

    /**
     * 分类路径
     */
    private String categoryPath;

    /**
     * 删除状态
     */
    private Integer isDel;

    /**
     * 上架状态
     */
    private Integer marketEnable;

    /**
     *审核状态
     */
    private Integer isAuth;

    /**
     * 输入值
     */
    @ApiModelProperty(value = "关键字")
    private String keyword;

    /**
     *商品价格区间
     */
    private String price;

    @ApiModelProperty(name = "prop", value = "属性:参数名_参数值@参数名_参数值",example = "屏幕类型_LED@屏幕尺寸_15英寸")
    private String prop;

    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private String sort = "def";
    @ApiModelProperty(value = "品牌ID")
    private List<String> brand;
//    @ApiModelProperty(value = "分类Id")
//    private List<String> cat;
    @ApiModelProperty(value = "商品Id")
    private List<String> goodsList;
}
