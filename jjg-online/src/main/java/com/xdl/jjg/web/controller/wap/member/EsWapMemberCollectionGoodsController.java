package com.xdl.jjg.web.controller.wap.member;

import com.shopx.common.model.result.ApiPageResponse;
import com.shopx.common.model.result.ApiResponse;
import com.shopx.common.model.result.DubboPageResult;
import com.shopx.common.model.result.DubboResult;
import com.shopx.common.util.BeanUtil;
import com.shopx.member.api.constant.MemberErrorCode;
import com.shopx.member.api.model.domain.EsMemberCollectionGoodsDO;
import com.shopx.member.api.model.domain.dto.EsMemberCollectionGoodsDTO;
import com.shopx.member.api.model.domain.dto.EsQueryMemberCollectionGoodsDTO;
import com.shopx.member.api.model.domain.vo.EsMemberCollectionGoodsSortStatisticsVO;
import com.shopx.member.api.model.domain.vo.EsMemberCollectionGoodsVO;
import com.shopx.member.api.service.IEsMemberCollectionGoodsService;
import com.shopx.trade.web.constant.ApiStatus;
import com.shopx.trade.web.request.query.EsMemberCollectionGoodsQueryForm;
import com.shopx.trade.web.shiro.oath.ShiroKit;
import com.shopx.trade.web.shiro.oath.ShiroUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * <p>
 * 移动端-商品收藏 前端控制器
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2020-02-24 09:28:26
 */
@Api(value = "/wap/member/collectGoods",tags = "移动端-商品收藏")
@RestController
@RequestMapping("/wap/member/collectGoods")
public class EsWapMemberCollectionGoodsController {


    @Reference(version = "${dubbo.application.version}", timeout = 10000, check = false)
    private IEsMemberCollectionGoodsService memberCollectionGoodsService;


    @ApiOperation(value = "查询会员商品收藏列表", notes = "根据页码分页分类展示商品收藏数据", response = EsMemberCollectionGoodsVO.class)
    @GetMapping("/getMemberCollectionGoodListBuyer")
    @ResponseBody
    public ApiResponse getMemberCollectionGoodListBuyer(EsMemberCollectionGoodsQueryForm queryMemberCollectionGoodsForm) {
        ShiroUser user = ShiroKit.getUser();
        if (null == user) {
            return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }
        Long userId = user.getId();
        EsQueryMemberCollectionGoodsDTO dto = new EsQueryMemberCollectionGoodsDTO();
        BeanUtil.copyProperties(queryMemberCollectionGoodsForm, dto);
        dto.setUserId(userId);
        DubboPageResult result = memberCollectionGoodsService.getMemberCollectionGoodListBuyer( dto);
        if(result.isSuccess() && result.getData() != null){
            BeanUtil.copyList(result.getData().getList(), EsMemberCollectionGoodsVO.class);
            List<EsMemberCollectionGoodsVO> list = BeanUtil.copyList(result.getData().getList(), EsMemberCollectionGoodsVO.class);
            return ApiPageResponse.pageSuccess(result.getData().getTotal(), list);
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }

    @ApiOperation(value = "收藏商品列表统计", notes = "收藏商品列表统计", response = EsMemberCollectionGoodsSortStatisticsVO.class)
    @PostMapping("/getMemberCollectionGoodNumBuyer")
    @ResponseBody
    public ApiResponse getMemberCollectionGoodNumBuyer() {
        ShiroUser user = ShiroKit.getUser();
        if (null == user) {
            return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }
        Long userId = user.getId();
        DubboResult result = memberCollectionGoodsService.getMemberCollectionGoodNumBuyer(userId);
        if(result.isSuccess()){
            EsMemberCollectionGoodsSortStatisticsVO statisticsVO = new EsMemberCollectionGoodsSortStatisticsVO();
            BeanUtil.copyProperties(result.getData(), statisticsVO);
            return ApiResponse.success(statisticsVO);
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }


    @ApiOperation(value = "添加会员商品收藏")
    @PostMapping("/collection/insertGoods/{goodsId}")
    public ApiResponse addGoods(@PathVariable("goodsId") Long goodsId) {
        Long userId = ShiroKit.getUser().getId();
        if (null == userId) {
            return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }
        EsMemberCollectionGoodsDTO esMemberCollectionGoodsDTO = new EsMemberCollectionGoodsDTO();
        esMemberCollectionGoodsDTO.setGoodsId(goodsId);
        esMemberCollectionGoodsDTO.setMemberId(userId);
        DubboResult<EsMemberCollectionGoodsDO> result = memberCollectionGoodsService.insertMemberCollectionGood(esMemberCollectionGoodsDTO);
        if (result.isSuccess()) {
            return ApiResponse.success(result.getData());
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }


    @ApiOperation(value = "判断会员是否收藏该商品")
    @PostMapping("/collection/hasColl/{goodsId}")
    public ApiResponse hasCollection(@PathVariable("goodsId") Long goodsId) {
        Long userId = ShiroKit.getUser().getId();
        if (null == userId) {
            return ApiResponse.success(false);
        }
        DubboResult<Boolean> result = memberCollectionGoodsService.getIsMemberCollection(goodsId,userId);
        if (result.isSuccess()) {
            return ApiResponse.success(result.getData());
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }


    @DeleteMapping(value = "/collection/deleteGoods/{goodsId}")
    @ApiOperation(value = "删除会员商品收藏")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "goodsId", value = "商品id", required = true, dataType = "Long", paramType = "path", example = "1")
    })
    public ApiResponse deleteGoods(@PathVariable("goodsId") Long goodsId) {
        ShiroUser user = ShiroKit.getUser();
        if (null == user) {
            return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }
        Long userId = user.getId();
        DubboResult<EsMemberCollectionGoodsDO> result = memberCollectionGoodsService.deleteMemberCollectionGood(userId, goodsId);
        if (result.isSuccess()) {
            return ApiResponse.success(result.getData());
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }

    @DeleteMapping(value = "/collection/{goodIds}")
    @ApiOperation(value = "批量删除会员商品收藏")
    @ApiImplicitParam(name = "goodIds",value ="商品ID数组" ,dataType = "long",paramType = "path",required = true,example = "1" ,allowMultiple = true)
    public ApiResponse batchDeleteGoods(@PathVariable(value = "goodIds") List<Long> goodIds) {
        Long userId = ShiroKit.getUser().getId();
        if (null == userId) {
            return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }
        DubboResult<EsMemberCollectionGoodsDO> result = memberCollectionGoodsService.deleteMemberCollectionGoodBatch(userId, goodIds);
        if (result.isSuccess()) {
            return ApiResponse.success(result.getData());
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }


    @ApiOperation(value = "设置降价提醒", notes = "设置降价提醒")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "goodId", value = "商品id", required = true, dataType = "long", paramType = "path",example = "1")})
    @PostMapping("/setRemind/{goodId}")
    @ResponseBody
    public ApiResponse setRemind(@PathVariable Long goodId) {
        ShiroUser user = ShiroKit.getUser();
        if (null == user) {
            return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }
        Long userId = user.getId();
        DubboResult result = memberCollectionGoodsService.updateRemind(goodId, userId);
        if(result.isSuccess()){
            return ApiResponse.success();
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }

    @ApiOperation(value = "取消降价提醒", notes = "取消降价提醒")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "goodId", value = "商品id", required = true, dataType = "long", paramType = "path",example = "1")})
    @PostMapping("/cancelRemind/{goodId}")
    @ResponseBody
    public ApiResponse cancelRemind(@PathVariable Long goodId) {
        ShiroUser user = ShiroKit.getUser();
        if (null == user) {
            return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }
        Long userId = user.getId();
        DubboResult result = memberCollectionGoodsService.deleteRemind(goodId, userId);
        if(result.isSuccess()){
            return ApiResponse.success();
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }




}