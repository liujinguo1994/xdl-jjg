package com.xdl.jjg.model.co;/**
 * @author wangaf
 * @date 2019/10/29 10:26
 **/

import com.xdl.jjg.constant.PropSelector;
import com.xdl.jjg.model.vo.EsSearchGoodsVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 @Author wangaf 826988665@qq.com
 @Date 2019/10/29
 @Version V1.0
 **/
@Data
@Api
public class EsSelectSearchCO implements Serializable {

    @ApiModelProperty(value = "分类")
    private List<SearchSelector> cat;
    @ApiModelProperty(value = "已选择分类")
    private List<SearchSelector> selectedCat;
    @ApiModelProperty(value = "品牌")
    private List<SearchSelector> brand;
    @ApiModelProperty(value = "参数")
    private List<PropSelector> prop;
    @ApiModelProperty("猜你喜欢")
    private List<EsSearchGoodsVO> likeGoods;
}
