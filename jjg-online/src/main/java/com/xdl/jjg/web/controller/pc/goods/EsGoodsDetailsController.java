package com.xdl.jjg.web.controller.pc.goods;

import com.shopx.common.model.result.ApiResponse;
import com.shopx.common.model.result.DubboResult;
import com.shopx.goods.api.service.IEsGoodsService;
import com.shopx.member.api.service.IEsMemberCollectionGoodsService;
import com.shopx.member.api.service.IEsMemberCommentService;
import com.shopx.member.api.service.IEsMemberCouponService;
import com.shopx.trade.api.model.domain.vo.GoodsDetailMessageVO;
import com.shopx.trade.api.service.IEsCouponService;
import com.shopx.trade.web.shiro.oath.ShiroKit;
import com.shopx.trade.web.shiro.oath.ShiroUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(value = "/zhuox/goodsDetail",tags = "商城商品详情页面相关接口")
@RestController
@RequestMapping("/zhuox/goodsDetail")
public class EsGoodsDetailsController {

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsCouponService iEsCouponService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsMemberCollectionGoodsService esMemberCollectionGoodsService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsGoodsService esGoodsService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000, check = false)
    private IEsMemberCouponService iesMemberCouponService;
    @Reference(version = "${dubbo.application.version}", timeout = 5000, check = false)
    private IEsMemberCommentService iesMemberCommentService;


    // TODO 商品详情页面待优化
    @GetMapping(value = "/collection/{goodsId}")
    @ResponseBody
    @ApiOperation(value = "查看商品是否收藏", notes = "查看商品是否收藏")
    @ApiImplicitParam(name = "goodsId", value = "商品ID", required = true, paramType = "path", dataType = "long")
    public ApiResponse visitGoods(@PathVariable("goodsId") Long goodsId) {

        // 通过商品 查询商品是否已收藏
        ShiroUser user = ShiroKit.getUser();

        if (user != null){
            DubboResult<Boolean> isMemberCollection = esMemberCollectionGoodsService.getIsMemberCollection(goodsId, user.getId());
            if (isMemberCollection.getData()){
                return ApiResponse.success(true);
            }else {
                return ApiResponse.success(false);
            }
        }else {
            return ApiResponse.success(false);
        }

    }

    // V2
    @GetMapping(value = "/goodsDetailMessage/{goodsId}")
    @ResponseBody
    @ApiOperation(value = "商品详情页面参数",response = GoodsDetailMessageVO.class, notes = "商品详情页面参数,")
    @ApiImplicitParam(name = "goodsId", value = "商品ID", required = true, paramType = "path", dataType = "long")
    public ApiResponse getGoodsDetailMessage(@PathVariable("goodsId") Long goodsId) {

        // 通过商品 查询商品是否已收藏
        ShiroUser user = ShiroKit.getUser();
        // 好评率
        DubboResult<Double> result1 = this.iesMemberCommentService.getGoodCommentRate(goodsId);
        // 商品印象
        DubboResult<List<Map<String, Object>>> result2 = iesMemberCommentService.getLabelsGroup(goodsId);
        GoodsDetailMessageVO goodsDetailMessageVO = new GoodsDetailMessageVO();
        if (user != null){
            DubboResult<Boolean> isMemberCollection = esMemberCollectionGoodsService.getIsMemberCollection(goodsId, user.getId());
            goodsDetailMessageVO.setIsCollection(isMemberCollection.getData());
            goodsDetailMessageVO.setGoodCommentRate(result1.getData());
            goodsDetailMessageVO.setLabelsGroup(result2.getData());
            return ApiResponse.success(goodsDetailMessageVO);
        }else {
            goodsDetailMessageVO.setIsCollection(false);
            goodsDetailMessageVO.setGoodCommentRate(result1.getData());
            goodsDetailMessageVO.setLabelsGroup(result2.getData());
            return ApiResponse.success(goodsDetailMessageVO);
        }

    }


}
