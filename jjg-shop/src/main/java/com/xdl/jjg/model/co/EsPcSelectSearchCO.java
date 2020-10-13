package com.xdl.jjg.model.co;/**
 * @author wangaf
 * @date 2019/10/29 10:26
 **/

import com.shopx.goods.api.constant.PropSelector;
import com.shopx.goods.api.model.domain.vo.EsSearchGoodsVO;
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
public class EsPcSelectSearchCO implements Serializable {



    private String paramName;
    private String paramNameText;
    @ApiModelProperty(value = "参数List")
    private List<Object> paramsList;

    @ApiModelProperty(value = "参数")
    private List<PropSelector> prop;
    @ApiModelProperty("猜你喜欢")
    private List<EsSearchGoodsVO> likeGoods;
}
