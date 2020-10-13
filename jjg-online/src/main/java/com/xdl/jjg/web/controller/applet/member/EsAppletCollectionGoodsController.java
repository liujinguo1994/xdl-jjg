package com.xdl.jjg.web.controller.applet.member;

import com.shopx.common.model.result.ApiPageResponse;
import com.shopx.common.model.result.ApiResponse;
import com.shopx.common.model.result.DubboPageResult;
import com.shopx.common.model.result.DubboResult;
import com.shopx.common.util.BeanUtil;
import com.shopx.member.api.constant.MemberErrorCode;
import com.shopx.member.api.model.domain.EsMemberCollectionGoodsDO;
import com.shopx.member.api.model.domain.EsMemberDO;
import com.shopx.member.api.model.domain.dto.EsMemberCollectionGoodsDTO;
import com.shopx.member.api.model.domain.dto.EsQueryMemberCollectionGoodsDTO;
import com.shopx.member.api.model.domain.vo.EsMemberCollectionGoodsSortStatisticsVO;
import com.shopx.member.api.model.domain.vo.EsMemberCollectionGoodsVO;
import com.shopx.member.api.service.IEsMemberCollectionGoodsService;
import com.shopx.member.api.service.IEsMemberService;
import com.shopx.trade.web.constant.ApiStatus;
import com.shopx.trade.web.request.EsAppletDelCollectionForm;
import com.shopx.trade.web.request.EsAppletGoodsCollectionForm;
import com.shopx.trade.web.request.query.EsAppletCollectionGoodsQueryForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 小程序-商品收藏 前端控制器
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-07-14
 */
@Api(value = "/applet/member/collectGoods",tags = "小程序-商品收藏")
@RestController
@RequestMapping("/applet/member/collectGoods")
public class EsAppletCollectionGoodsController {


    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsMemberCollectionGoodsService memberCollectionGoodsService;
    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsMemberService memberService;


    @ApiOperation(value = "查询会员商品收藏列表", response = EsMemberCollectionGoodsVO.class)
    @GetMapping("/getCollectionGoodList")
    @ResponseBody
    public ApiResponse getCollectionGoodList(@Valid EsAppletCollectionGoodsQueryForm form) {
        DubboResult<EsMemberDO> dubboResult = memberService.getMemberBySkey(form.getSkey());
        if (!dubboResult.isSuccess() || dubboResult.getData() == null) {
            return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }
        EsQueryMemberCollectionGoodsDTO dto = new EsQueryMemberCollectionGoodsDTO();
        BeanUtil.copyProperties(form, dto);
        dto.setUserId(dubboResult.getData().getId());
        DubboPageResult result = memberCollectionGoodsService.getMemberCollectionGoodListBuyer( dto);
        if(result.isSuccess() && result.getData() != null){
            List<EsMemberCollectionGoodsVO> list = BeanUtil.copyList(result.getData().getList(), EsMemberCollectionGoodsVO.class);
            return ApiPageResponse.pageSuccess(result.getData().getTotal(), list);
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }

    @ApiOperation(value = "收藏商品列表统计", response = EsMemberCollectionGoodsSortStatisticsVO.class)
    @PostMapping("/getCollectionGoodNum/{skey}")
    @ApiImplicitParam(name = "skey", value = "登录态标识", required =true, dataType = "String" ,paramType="path")
    @ResponseBody
    public ApiResponse getCollectionGoodNum(@PathVariable String skey) {
        DubboResult<EsMemberDO> dubboResult = memberService.getMemberBySkey(skey);
        if (!dubboResult.isSuccess() || dubboResult.getData() == null) {
            return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }
        Long userId = dubboResult.getData().getId();
        DubboResult result = memberCollectionGoodsService.getMemberCollectionGoodNumBuyer(userId);
        if(result.isSuccess()){
            EsMemberCollectionGoodsSortStatisticsVO statisticsVO = new EsMemberCollectionGoodsSortStatisticsVO();
            BeanUtil.copyProperties(result.getData(), statisticsVO);
            return ApiResponse.success(statisticsVO);
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }


    @ApiOperation(value = "添加会员商品收藏")
    @PostMapping("/addCollection")
    @ResponseBody
    public ApiResponse addCollection(@RequestBody @Valid EsAppletGoodsCollectionForm form) {
        DubboResult<EsMemberDO> dubboResult = memberService.getMemberBySkey(form.getSkey());
        if (!dubboResult.isSuccess() || dubboResult.getData() == null) {
            return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }
        EsMemberCollectionGoodsDTO esMemberCollectionGoodsDTO = new EsMemberCollectionGoodsDTO();
        esMemberCollectionGoodsDTO.setGoodsId(form.getGoodsId());
        esMemberCollectionGoodsDTO.setMemberId(dubboResult.getData().getId());
        DubboResult<EsMemberCollectionGoodsDO> result = memberCollectionGoodsService.insertMemberCollectionGood(esMemberCollectionGoodsDTO);
        if (result.isSuccess()) {
            return ApiResponse.success(result.getData());
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }


    @ApiOperation(value = "判断会员是否收藏该商品")
    @PostMapping("/hasCollection")
    @ResponseBody
    public ApiResponse hasCollection(@RequestBody @Valid EsAppletGoodsCollectionForm form) {
        DubboResult<EsMemberDO> dubboResult = memberService.getMemberBySkey(form.getSkey());
        if (!dubboResult.isSuccess() || dubboResult.getData() == null) {
            return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }
        Long userId = dubboResult.getData().getId();
        DubboResult<Boolean> result = memberCollectionGoodsService.getIsMemberCollection(form.getGoodsId(),userId);
        if (result.isSuccess()) {
            return ApiResponse.success(result.getData());
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }


    @DeleteMapping(value = "/deleteGoods")
    @ApiOperation(value = "删除会员商品收藏")
    @ResponseBody
    public ApiResponse deleteGoods(@Valid EsAppletGoodsCollectionForm form) {
        DubboResult<EsMemberDO> dubboResult = memberService.getMemberBySkey(form.getSkey());
        if (!dubboResult.isSuccess() || dubboResult.getData() == null) {
            return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }
        Long userId = dubboResult.getData().getId();
        DubboResult<EsMemberCollectionGoodsDO> result = memberCollectionGoodsService.deleteMemberCollectionGood(userId, form.getGoodsId());
        if (result.isSuccess()) {
            return ApiResponse.success(result.getData());
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }

    @DeleteMapping(value = "/batchDeleteGoods")
    @ApiOperation(value = "批量删除会员商品收藏")
    @ResponseBody
    public ApiResponse batchDeleteGoods(@Valid EsAppletDelCollectionForm form) {
        DubboResult<EsMemberDO> dubboResult = memberService.getMemberBySkey(form.getSkey());
        if (!dubboResult.isSuccess() || dubboResult.getData() == null) {
            return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }
        Long userId = dubboResult.getData().getId();
        DubboResult<EsMemberCollectionGoodsDO> result = memberCollectionGoodsService.deleteMemberCollectionGoodBatch(userId,form.getGoodIds());
        if (result.isSuccess()) {
            return ApiResponse.success(result.getData());
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }


    @ApiOperation(value = "设置降价提醒")
    @PostMapping("/setRemind")
    @ResponseBody
    public ApiResponse setRemind(@RequestBody @Valid EsAppletGoodsCollectionForm form) {
        DubboResult<EsMemberDO> dubboResult = memberService.getMemberBySkey(form.getSkey());
        if (!dubboResult.isSuccess() || dubboResult.getData() == null) {
            return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }
        Long userId = dubboResult.getData().getId();
        DubboResult result = memberCollectionGoodsService.updateRemind(form.getGoodsId(), userId);
        if(result.isSuccess()){
            return ApiResponse.success();
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }

    @ApiOperation(value = "取消降价提醒")
    @PostMapping("/cancelRemind")
    @ResponseBody
    public ApiResponse cancelRemind(@RequestBody @Valid EsAppletGoodsCollectionForm form) {
        DubboResult<EsMemberDO> dubboResult = memberService.getMemberBySkey(form.getSkey());
        if (!dubboResult.isSuccess() || dubboResult.getData() == null) {
            return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }
        Long userId = dubboResult.getData().getId();
        DubboResult result = memberCollectionGoodsService.deleteRemind(form.getGoodsId(), userId);
        if(result.isSuccess()){
            return ApiResponse.success();
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }
}