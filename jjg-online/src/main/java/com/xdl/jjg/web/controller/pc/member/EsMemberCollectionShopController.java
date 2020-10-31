package com.xdl.jjg.web.controller.pc.member;


import com.jjg.member.model.domain.EsMemberCollectionShopDO;
import com.jjg.member.model.domain.EsMemberCollectionShopLabelDO;
import com.jjg.member.model.domain.EsMemberGoodsDO;
import com.jjg.member.model.domain.EsQueryCollectionShopDO;
import com.jjg.member.model.dto.EsMemberCollectionShopDTO;
import com.jjg.member.model.dto.EsQueryCollectShopDTO;
import com.jjg.member.model.dto.EsUpdateTopShopDTO;
import com.jjg.member.model.vo.EsMemberCollectionShopLabelVO;
import com.jjg.member.model.vo.EsMemberGoodsVO;
import com.jjg.member.model.vo.EsQueryCollectionShopVO;
import com.jjg.trade.model.form.EsUpdateTopShopForm;
import com.jjg.trade.model.form.ShopRemarksForm;
import com.jjg.trade.model.form.query.EsCollectShopQueryForm;
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
import com.xdl.jjg.web.service.feign.member.MemberCollectionShopService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 会员收藏店铺表 前端控制器
 * </p>
 *
 * @author HQL 236154186@qq.com
 * @since 2019-05-29
 */
@Api(value = "/memberShop", tags = "会员收藏店铺")
@RestController
@RequestMapping("/memberCollectionShop")
public class EsMemberCollectionShopController extends BaseController {

    @Reference(version = "${dubbo.application.version}", timeout = 10000,check = false)
    private MemberCollectionShopService memberCollectionShopService;

    @ApiOperation(value = "取消会员收藏店铺", notes = "依据传递的id取消店铺收藏")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "shopId", value = "店铺id", required = true, dataType = "long", paramType = "path", example = "1"),
    })
    @DeleteMapping("/collection/deleteShop/{shopId}")
    @ResponseBody
    public ApiResponse deleteShop(@PathVariable("shopId") Long shopId) {
        Long memberId = ShiroKit.getUser().getId();
        if (null == memberId) {
            return ApiResponse.fail(TradeErrorCode.NOT_LOGIN.getErrorCode(), TradeErrorCode.NOT_LOGIN.getErrorMsg());
        }
        DubboResult<EsMemberCollectionShopDO> result = memberCollectionShopService.deleteMemberCollectionShop(memberId, shopId);
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
            return ApiResponse.fail(TradeErrorCode.NOT_LOGIN.getErrorCode(), TradeErrorCode.NOT_LOGIN.getErrorMsg());
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
            return ApiResponse.fail(TradeErrorCode.NOT_LOGIN.getErrorCode(), TradeErrorCode.NOT_LOGIN.getErrorMsg());
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

    @ApiOperation(value = "收藏店铺列表数据", notes = "收藏店铺列表数据", response = EsQueryCollectionShopVO.class)
    @PostMapping("/getMemberCollectionShopList")
    @ResponseBody
    public ApiResponse getMemberCollectionShopList(EsCollectShopQueryForm form) {
        ShiroUser user = ShiroKit.getUser();
        if (null == user) {
            return ApiPageResponse.fail(TradeErrorCode.NOT_LOGIN.getErrorCode(), TradeErrorCode.NOT_LOGIN.getErrorMsg());
        }
        Long memberId = user.getId();
        EsQueryCollectShopDTO dto = new EsQueryCollectShopDTO();
        BeanUtil.copyProperties(form, dto);
        dto.setMemberId(memberId);
        DubboPageResult result = memberCollectionShopService.getMemberCollectionShopList(form.getPageNum(), form.getPageSize(), dto);
        if(result.isSuccess()){
            if(result.getData().getList().size() == 0){
                return ApiResponse.success(result.getData());
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
            return ApiResponse.success(shopVOList);
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }

    @ApiOperation(value = "热销、上新商品列表数据", notes = "收藏店铺列表数据", response = EsQueryCollectionShopVO.class)
    @PostMapping("/getMemberCollectionShopListNew")
    @ResponseBody
    public ApiResponse getMemberCollectionShopListNew(EsCollectShopQueryForm form) {
        ShiroUser user = ShiroKit.getUser();
        if (null == user) {
            return ApiPageResponse.fail(TradeErrorCode.NOT_LOGIN.getErrorCode(), TradeErrorCode.NOT_LOGIN.getErrorMsg());
        }
        Long memberId = user.getId();
        EsQueryCollectShopDTO dto = new EsQueryCollectShopDTO();
        BeanUtil.copyProperties(form, dto);
        dto.setMemberId(memberId);
        DubboPageResult result = memberCollectionShopService.getMemberCollectionShopListNew(form.getPageNum(), form.getPageSize(), dto);
        if(result.isSuccess()){
            if(result.getData().getList().size() == 0){
                return ApiPageResponse.success(result);
            }
            List<EsQueryCollectionShopDO> shopDOList = result.getData().getList();
            List<EsQueryCollectionShopVO> shopVOList = shopDOList.stream().map(e -> {
                EsQueryCollectionShopVO queryCollectionShopVO = new EsQueryCollectionShopVO();
                BeanUtil.copyProperties(e, queryCollectionShopVO);
                List<EsMemberGoodsDO> hotGoodList = e.getHotGoodList();
                List<EsMemberGoodsDO> newGoodList = e.getNewGoodList();
                if(CollectionUtils.isNotEmpty(hotGoodList)){
                    List<EsMemberGoodsVO> hotGoodsVOList = BeanUtil.copyList(hotGoodList, EsMemberGoodsVO.class);
                    queryCollectionShopVO.setHotGoodList(hotGoodsVOList);
                }
                if(CollectionUtils.isNotEmpty(newGoodList)){
                    List<EsMemberGoodsVO> newGoodsVOList = BeanUtil.copyList(newGoodList, EsMemberGoodsVO.class);
                    queryCollectionShopVO.setNewGoodList(newGoodsVOList);
                }
                return queryCollectionShopVO;
            }).collect(Collectors.toList());
            return ApiPageResponse.pageSuccess(result.getData().getTotal(), shopVOList);
        }
        return ApiPageResponse.fail(ApiStatus.wrapperException(result));
    }
}

