package com.xdl.jjg.web.controller.wap.goods;


import com.shopx.common.model.result.DubboPageResult;
import com.shopx.common.model.result.DubboResult;
import com.shopx.common.util.StringUtil;
import com.shopx.goods.api.model.domain.cache.EsCategoryCO;
import com.shopx.goods.api.model.domain.vo.EsCategoryVO;
import com.shopx.goods.api.service.IEsCategoryService;
import com.shopx.member.api.model.domain.EsShopDetailDO;
import com.shopx.member.api.service.IEsShopDetailService;
import com.shopx.trade.web.constant.ApiStatus;
import io.swagger.annotations.*;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;


/**
 * <p>
 * 移动端-店铺分类
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2020-04-21 09:11:26
 */
@Api(value = "/wap/goods/shopCa",tags = "移动端-店铺分类")
@RestController
@RequestMapping("/wap/goods/shopCa")
public class EsWapShopController {

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsShopDetailService shopDetailService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsCategoryService categoryService;


    @ApiOperation(value = "查询店铺分类",response = EsCategoryVO.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "shopId", value = "店铺id", required = true, dataType = "long", paramType = "path",example = "1")})
    @GetMapping(value = "/getCategoryChildren/{shopId}")
    @ResponseBody
    public ApiResponse getCategoryChildren(@ApiIgnore @PathVariable("shopId") Long shopId) {

        DubboResult<EsShopDetailDO> result = shopDetailService.getByShopId(shopId);

        DubboPageResult<EsCategoryCO> resultCa=null;
        if (result.isSuccess()) {
            EsShopDetailDO shopDetailDO = result.getData();
            if (!StringUtil.isEmpty(shopDetailDO.getGoodsManagementCategory())) {
                resultCa = categoryService.getShopCategory(shopDetailDO.getGoodsManagementCategory());
                if (!resultCa.isSuccess()) {
                    return ApiResponse.fail(ApiStatus.wrapperException(result));
                }
            }
            return ApiResponse.success(resultCa.getData());
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }

    }



}