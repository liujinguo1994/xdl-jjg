package com.xdl.jjg.web.controller.wap.member;

import com.shopx.common.model.result.ApiPageResponse;
import com.shopx.common.model.result.ApiResponse;
import com.shopx.common.model.result.DubboPageResult;
import com.shopx.common.model.result.DubboResult;
import com.shopx.common.util.BeanUtil;
import com.shopx.member.api.constant.MemberErrorCode;
import com.shopx.member.api.model.domain.*;
import com.shopx.member.api.model.domain.dto.EsMemberCollectionShopDTO;
import com.shopx.member.api.model.domain.dto.EsUpdateTopShopDTO;
import com.shopx.member.api.model.domain.vo.EsCollectionShopInfoVO;
import com.shopx.member.api.service.IEsMemberCollectionShopService;
import com.shopx.trade.web.constant.ApiStatus;
import com.shopx.trade.web.request.EsUpdateTopShopForm;
import com.shopx.trade.web.request.ShopRemarksForm;
import com.shopx.trade.web.request.query.EsQueryPageForm;
import com.shopx.trade.web.shiro.oath.ShiroKit;
import com.shopx.trade.web.shiro.oath.ShiroUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>
 * 移动端-店铺收藏 前端控制器
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2020-02-24 10:28:26
 */
@Api(value = "/wap/member/collectShop",tags = "移动端-店铺收藏")
@RestController
@RequestMapping("/wap/member/collectShop")
public class EsWapMemberCollectionShopController {


    @Reference(version = "${dubbo.application.version}", timeout = 10000,check = false)
    private IEsMemberCollectionShopService memberCollectionShopService;


    @ApiOperation(value = "添加会员收藏店铺", notes = "依据传递的id添加店铺收藏")
    @ApiImplicitParam(name = "shopId", value = "店铺id", required = true, dataType = "long", paramType = "path", example = "1")
    @PostMapping(value = "/insertShop/{shopId}")
    @ResponseBody
    public ApiResponse insertShop(@PathVariable("shopId") Long shopId) {
        Long memberId = ShiroKit.getUser().getId();
        if (null == memberId) {
            return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }
        DubboResult<EsMemberCollectionShopDO> result = memberCollectionShopService.insertMemberCollectionShop(shopId, memberId);
        if (result.isSuccess()) {
            return ApiResponse.success(result.getData());
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }

    @ApiOperation(value = "取消会员收藏店铺", notes = "依据传递的id取消店铺收藏")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "shopId", value = "店铺id", required = true, dataType = "long", paramType = "path", example = "1"),
    })
    @DeleteMapping("/collection/deleteShop/{shopId}")
    @ResponseBody
    public ApiResponse deleteShop(@PathVariable("shopId") Long shopId) {
        Long memberId = ShiroKit.getUser().getId();
        if (null == memberId) {
            return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }
        DubboResult<EsMemberCollectionShopDO> result = memberCollectionShopService.deleteMemberCollectionShop(memberId, shopId);
        if (result.isSuccess()) {
            return ApiResponse.success(result.getData());
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }

    @ApiOperation(value = "判断会员是否收藏该店铺")
    @PostMapping("/collection/hasColl/{shopId}")
    public ApiResponse hasCollection(@PathVariable("shopId") Long shopId) {
        Long userId = ShiroKit.getUser().getId();
        if (null == userId) {
            return ApiResponse.success(false);
        }
        DubboResult<Boolean> result = memberCollectionShopService.hasCollection(shopId,userId);
        if (result.isSuccess()) {
            return ApiResponse.success(result.getData());
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }


    @ApiOperation(value = "修改店铺备注", notes = "修改店铺备注")
    @PutMapping("/collection/updateRemarks")
    @ResponseBody
    public ApiResponse updateRemarks(@Valid ShopRemarksForm remarksForm) {
        ShiroUser user = ShiroKit.getUser();
        if (null == user) {
            return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }
        EsMemberCollectionShopDTO esMemberCollectionShopDTO = new EsMemberCollectionShopDTO();
        if (null != remarksForm.getMark()) {
            esMemberCollectionShopDTO.setMark(remarksForm.getMark());
        }
        esMemberCollectionShopDTO.setId(remarksForm.getId());
        DubboResult<EsMemberCollectionShopDO> result = memberCollectionShopService.insertRemarks(esMemberCollectionShopDTO);
        if (result.isSuccess()) {
            return ApiResponse.success(result.getData());
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }

    @ApiOperation(value = "店铺置顶操作", notes = "店铺置顶操作")
    @PostMapping("/collection/upDateTop")
    @ResponseBody
    public ApiResponse deleteShop(@Valid EsUpdateTopShopForm esUpdateTopShopForm) {
        ShiroUser user = ShiroKit.getUser();
        if (null == user) {
            return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }
        Long memberId = user.getId();
        EsUpdateTopShopDTO esUpdateTopShopDTO = new EsUpdateTopShopDTO();
        BeanUtil.copyProperties(esUpdateTopShopForm, esUpdateTopShopDTO);
        DubboResult<EsMemberCollectionShopDO> result = memberCollectionShopService.updateShopTop(esUpdateTopShopDTO, memberId);
        if (result.isSuccess()) {
            return ApiResponse.success(result.getData());
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }

  /*  @ApiOperation(value = "收藏店铺列表数据", notes = "收藏店铺列表数据", response = EsQueryCollectionShopVO.class)
    @PostMapping("/getMemberCollectionShopList")
    @ResponseBody
    public ApiResponse getMemberCollectionShopList(EsCollectShopQueryForm form) {
        ShiroUser user = ShiroKit.getUser();
        if (null == user) {
            return ApiPageResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }
        Long memberId = user.getId();
        EsQueryCollectShopDTO dto = new EsQueryCollectShopDTO();
        BeanUtil.copyProperties(form, dto);
        dto.setMemberId(memberId);
        DubboPageResult result = memberCollectionShopService.getMemberCollectionShopList(form.getPageNum(), form.getPageSize(), dto);
        if(result.isSuccess()){
            if(result.getData().getList().size() == 0){
                return ApiPageResponse.success(result);
            }
            List<EsQueryCollectionShopDO> shopDOList = result.getData().getList();
            List<EsQueryCollectionShopVO> shopVOList = shopDOList.stream().map(e -> {
                EsQueryCollectionShopVO queryCollectionShopVO = new EsQueryCollectionShopVO();
                BeanUtil.copyProperties(e, queryCollectionShopVO);
                List<EsMemberCollectionShopLabelDO> labelDOList = e.getCollectionShopLabelDOList();
                if(labelDOList != null && labelDOList.size() > 0){
                    List<EsMemberCollectionShopLabelVO> shopLabelVOS = labelDOList.stream().map(n -> {
                        EsMemberCollectionShopLabelVO shopLabelVO = new EsMemberCollectionShopLabelVO();
                        BeanUtil.copyProperties(n, shopLabelVO);
                        List<EsMemberGoodsDO> goodsDOList =  n.getMemberGoodsDO();
                        List<EsMemberGoodsVO> goodsVOList = BeanUtil.copyList(goodsDOList, EsMemberGoodsVO.class);
                        shopLabelVO.setMemberGoodsVO(goodsVOList);
                        return shopLabelVO;
                    }).collect(Collectors.toList());
                    queryCollectionShopVO.setCollectionShopLabelVOList(shopLabelVOS);
                }
                return queryCollectionShopVO;
            }).collect(Collectors.toList());
            return ApiPageResponse.pageSuccess(result.getData().getTotal(), shopVOList);
        }
        return ApiPageResponse.fail(ApiStatus.wrapperException(result));
    }*/

    @ApiOperation(value = "收藏店铺列表", response = EsCollectionShopInfoVO.class)
    @GetMapping("/getCollectionShopList")
    @ResponseBody
    public ApiResponse getCollectionShopList(EsQueryPageForm form) {
        ShiroUser user = ShiroKit.getUser();
        if (null == user) {
            return ApiPageResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }
        Long memberId = user.getId();
        DubboPageResult<EsCollectionShopInfoDO> result = memberCollectionShopService.getCollectionShopList(memberId, form.getPageSize(), form.getPageNum());
        if(result.isSuccess()){
            List<EsCollectionShopInfoDO> data = result.getData().getList();
            List<EsCollectionShopInfoVO> voList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(data)){
                 voList = BeanUtil.copyList(data, EsCollectionShopInfoVO.class);
            }
            return ApiPageResponse.pageSuccess(result.getData().getTotal(), voList);
        }else {
            return ApiPageResponse.fail(ApiStatus.wrapperException(result));
        }
    }

}