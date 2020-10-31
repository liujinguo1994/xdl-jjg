package com.xdl.jjg.web.controller.pc.member;


import com.jjg.member.model.domain.EsMemberCollectionGoodsDO;
import com.jjg.member.model.dto.EsMemberCollectionGoodsDTO;
import com.jjg.member.model.dto.EsQueryMemberCollectionGoodsDTO;
import com.jjg.member.model.vo.EsMemberCollectionGoodsSortStatisticsVO;
import com.jjg.member.model.vo.EsMemberCollectionGoodsVO;
import com.jjg.trade.model.form.query.EsMemberCollectionGoodsQueryForm;
import com.xdl.jjg.constant.ApiStatus;
import com.xdl.jjg.constant.TradeErrorCode;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.response.web.ApiPageResponse;
import com.xdl.jjg.response.web.ApiResponse;
import com.xdl.jjg.shiro.oath.ShiroKit;
import com.xdl.jjg.shiro.oath.ShiroUser;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.web.controller.BaseController;
import com.xdl.jjg.web.service.feign.member.MemberCollectionGoodsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 会员商品收藏 前端控制器
 * </p>
 *
 * @author HQL 236154186@qq.com
 * @since 2019-05-29
 */
@Api(value = "/collectionGoods", tags = "会员收藏商品")
@RestController
@RequestMapping("/collectionGoods")
public class EsMemberCollectionGoodsController extends BaseController {

    @Autowired
    private MemberCollectionGoodsService memberCollectionGoodsService;


    @ApiOperation(value = "查询会员商品收藏列表", notes = "根据页码分页分类展示商品收藏数据", response = EsMemberCollectionGoodsVO.class)
    @PostMapping("/getMemberCollectionGoodListBuyer")
    @ResponseBody
    public ApiResponse getMemberCollectionGoodListBuyer(EsMemberCollectionGoodsQueryForm queryMemberCollectionGoodsForm) {
        ShiroUser user = ShiroKit.getUser();
        if (null == user) {
            return ApiResponse.fail(TradeErrorCode.NOT_LOGIN.getErrorCode(), TradeErrorCode.NOT_LOGIN.getErrorMsg());
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
            return ApiResponse.fail(TradeErrorCode.NOT_LOGIN.getErrorCode(), TradeErrorCode.NOT_LOGIN.getErrorMsg());
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


    @ApiOperation(value = "设置降价提醒", notes = "设置降价提醒")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "goodId", value = "商品id", required = true, dataType = "long", paramType = "path",example = "1")})
    @PostMapping("/setRemind/{goodId}")
    @ResponseBody
    public ApiResponse setRemind(@PathVariable Long goodId) {
        ShiroUser user = ShiroKit.getUser();
        if (null == user) {
            return ApiResponse.fail(TradeErrorCode.NOT_LOGIN.getErrorCode(), TradeErrorCode.NOT_LOGIN.getErrorMsg());
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
            return ApiResponse.fail(TradeErrorCode.NOT_LOGIN.getErrorCode(), TradeErrorCode.NOT_LOGIN.getErrorMsg());
        }
        Long userId = user.getId();
        DubboResult result = memberCollectionGoodsService.deleteRemind(goodId, userId);
        if(result.isSuccess()){
            return ApiResponse.success();
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }

//    @ApiOperation(value = "统计收藏商品的分类个数", notes = "统计收藏商品的分类个数")
//    @GetMapping("/collectionByType/getGoodSort")
//    @ResponseBody
//    public ApiResponse getGoodSort() {
//        Long userId = ShiroKit.getUser().getId();
//        if (null == userId) {
//            return ApiResponse.fail(TradeErrorCode.NOT_LOGIN.getErrorCode(), TradeErrorCode.NOT_LOGIN.getErrorMsg());
//        }
//        DubboPageResult<EsCollectCateryNumDO> result = this.iesMemberCollectionGoodsService.getGoodSort(userId);
//        List<EsCollectCateryNumVO> list;
//        if (result.isSuccess()) {
//            list = BeanUtil.copyList(result.getData().getList(), EsCollectCateryNumVO.class);
//            return ApiResponse.success(list);
//        }
//        return ApiResponse.fail(ApiStatus.wrapperException(result));
//    }
//
//    @ApiOperation(value = "统计失效和降价商品数量", notes = "统计失效和降价商品数量")
//    @GetMapping("/collectionByType/cutAndEffetNum")
//    @ResponseBody
//    public ApiResponse getCutAndEffetNum() {
//        Long userId = ShiroKit.getUser().getId();
//        if (null == userId) {
//            return ApiResponse.fail(TradeErrorCode.NOT_LOGIN.getErrorCode(), TradeErrorCode.NOT_LOGIN.getErrorMsg());
//        }
//        DubboResult<EsCutAndEffectDO> result = this.iesMemberCollectionGoodsService.getCutAndEffetNum(userId);
//        if (result.isSuccess()) {
//            EsCutAndEffectVO esCutAndEffectVo = new EsCutAndEffectVO();
//            BeanUtil.copyProperties(result.getData(), esCutAndEffectVo);
//            return ApiResponse.success(esCutAndEffectVo);
//        }
//        return ApiResponse.fail(ApiStatus.wrapperException(result));
//    }
//
//
//    @ApiOperation(value = "统计商品收藏个数", notes = "统计商品收藏个数")
//    @GetMapping("/getCountGoodsNum")
//    @ResponseBody
//    public ApiResponse getCountGoodsNum() {
//        Long userId = ShiroKit.getUser().getId();
//        if (null == userId) {
//            return ApiResponse.fail(TradeErrorCode.NOT_LOGIN.getErrorCode(), TradeErrorCode.NOT_LOGIN.getErrorMsg());
//        }
//        DubboResult<Integer> result = this.iesMemberCollectionGoodsService.getCountGoodsNum(userId);
//        if (result.isSuccess()) {
//            return ApiResponse.success(DubboResult.success(result.getData()));
//        }
//        return ApiResponse.fail(ApiStatus.wrapperException(result));
//    }
//
//
//    @ApiOperation(value = "添加会员商品收藏", notes = "根据form表单数据提交")
//    @PostMapping("/collection/insertGoods")
//    public ApiResponse add(@Valid EsMemberCollectionGoodsForm esCollectionGoodsForm) {
//        Long userId = ShiroKit.getUser().getId();
//        if (null == userId) {
//            return ApiResponse.fail(TradeErrorCode.NOT_LOGIN.getErrorCode(), TradeErrorCode.NOT_LOGIN.getErrorMsg());
//        }
//        EsMemberCollectionGoodsDTO esMemberCollectionGoodsDTO = new EsMemberCollectionGoodsDTO();
//        BeanUtil.copyProperties(esCollectionGoodsForm, esMemberCollectionGoodsDTO);
//        esMemberCollectionGoodsDTO.setMemberId(userId);
//        DubboResult<EsMemberCollectionGoodsDO> result = iesMemberCollectionGoodsService.insertMemberCollectionGood(esMemberCollectionGoodsDTO);
//        if (result.isSuccess()) {
//            return ApiResponse.success(result.getData());
//        }
//        return ApiResponse.fail(ApiStatus.wrapperException(result));
//    }
//
//    @ApiOperation(value = "清空所有失效商品", notes = "清空所有失效商品")
//    @DeleteMapping("/collection/deleteAllEfect")
//    public ApiResponse deleteAllEfect() {
//        Long userId = ShiroKit.getUser().getId();
//        if (null == userId) {
//            return ApiResponse.fail(TradeErrorCode.NOT_LOGIN.getErrorCode(), TradeErrorCode.NOT_LOGIN.getErrorMsg());
//        }
//        DubboResult<EsMemberCollectionGoodsDO> result = iesMemberCollectionGoodsService.deleteAllEfect(userId);
//        if (result.isSuccess()) {
//            return ApiResponse.success(result.getData());
//        }
//        return ApiResponse.fail(ApiStatus.wrapperException(result));
//    }

    @DeleteMapping(value = "/collection/deleteGoods/{goodsId}")
    @ApiOperation(value = "删除会员商品收藏")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "goodsId", value = "商品id", required = true, dataType = "Long", paramType = "path", example = "1")
    })
    public ApiResponse deleteGoods( @PathVariable("goodsId") Long goodsId) {
        ShiroUser user = ShiroKit.getUser();
        if (null == user) {
            return ApiResponse.fail(TradeErrorCode.NOT_LOGIN.getErrorCode(), TradeErrorCode.NOT_LOGIN.getErrorMsg());
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
            return ApiResponse.fail(TradeErrorCode.NOT_LOGIN.getErrorCode(), TradeErrorCode.NOT_LOGIN.getErrorMsg());
        }
        DubboResult<EsMemberCollectionGoodsDO> result = memberCollectionGoodsService.deleteMemberCollectionGoodBatch(userId, goodIds);
        if (result.isSuccess()) {
            return ApiResponse.success(result.getData());
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }

    @PostMapping(value = "/collection/insertGoods/{goodsId}")
    @ApiOperation(value = "添加商品收藏")
    @ApiImplicitParam(name = "goodsId",value = "商品ID",dataType = "long",paramType = "path",required = true,example = "1")
    public ApiResponse insertGoods(@PathVariable("goodsId") Long goodsId){
        Long userId = ShiroKit.getUser().getId();
        if (null == userId) {
            return ApiResponse.fail(TradeErrorCode.NOT_LOGIN.getErrorCode(), TradeErrorCode.NOT_LOGIN.getErrorMsg());
        }
        EsMemberCollectionGoodsDTO esMemberCollectionGoodsDTO = new EsMemberCollectionGoodsDTO();
        esMemberCollectionGoodsDTO.setGoodsId(goodsId);
        esMemberCollectionGoodsDTO.setMemberId(userId);
        DubboResult<EsMemberCollectionGoodsDO> esMemberCollectionGoodsDODubboResult = memberCollectionGoodsService.insertMemberCollectionGood(esMemberCollectionGoodsDTO);
        if(esMemberCollectionGoodsDODubboResult.isSuccess()){
            return ApiResponse.success(esMemberCollectionGoodsDODubboResult.getData());
        }
        return ApiResponse.fail(ApiStatus.wrapperException(esMemberCollectionGoodsDODubboResult));
    }

//
//    @PutMapping(value = "/collection/upDateRemind/{goodsId}")
//    @ApiImplicitParam(name = "goodsId", value = "商品id", required = true, dataType = "long", paramType = "path", example = "1")
//    @ApiOperation(value = "设置降价提醒")
//    public ApiResponse upDateRemind(@PathVariable("goodsId") Long goodsId) {
//        Long userId = ShiroKit.getUser().getId();
//        if (null == userId) {
//            return ApiResponse.fail(TradeErrorCode.NOT_LOGIN.getErrorCode(), TradeErrorCode.NOT_LOGIN.getErrorMsg());
//        }
//        DubboResult<EsMemberCollectionGoodsDO> result = this.iesMemberCollectionGoodsService.updateRemind(goodsId, userId);
//        if (result.isSuccess()) {
//            return ApiResponse.success(result.getData());
//        }
//        return ApiResponse.fail(ApiStatus.wrapperException(result));
//    }
//
//    @PutMapping(value = "/collection/deleteRemind/{goodsId}")
//    @ApiImplicitParam(name = "goodsId", value = "商品id", required = true, dataType = "long", paramType = "path", example = "1")
//    @ApiOperation(value = "取消降价提醒")
//    public ApiResponse deleteRemind(@PathVariable("goodsId") Long goodsId) {
//        Long userId = ShiroKit.getUser().getId();
//        if (null == userId) {
//            return ApiResponse.fail(TradeErrorCode.NOT_LOGIN.getErrorCode(), TradeErrorCode.NOT_LOGIN.getErrorMsg());
//        }
//        DubboResult<EsMemberCollectionGoodsDO> result = this.iesMemberCollectionGoodsService.deleteRemind(goodsId, userId);
//        if (result.isSuccess()) {
//            return ApiResponse.success(result.getData());
//        }
//        return ApiResponse.fail(ApiStatus.wrapperException(result));
//    }

}


