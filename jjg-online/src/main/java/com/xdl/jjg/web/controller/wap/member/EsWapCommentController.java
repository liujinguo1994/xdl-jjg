package com.xdl.jjg.web.controller.wap.member;

import com.beust.jcommander.internal.Lists;
import com.shopx.common.exception.ArgumentException;
import com.shopx.common.model.result.ApiPageResponse;
import com.shopx.common.model.result.ApiResponse;
import com.shopx.common.model.result.DubboPageResult;
import com.shopx.common.model.result.DubboResult;
import com.shopx.common.util.BeanUtil;
import com.shopx.member.api.constant.MemberConstant;
import com.shopx.member.api.constant.MemberErrorCode;
import com.shopx.member.api.model.domain.EsCommentCategoryClassifyDO;
import com.shopx.member.api.model.domain.EsCommentCountDO;
import com.shopx.member.api.model.domain.EsCommentInfoDO;
import com.shopx.member.api.model.domain.EsMemberTokenDO;
import com.shopx.member.api.model.domain.dto.*;
import com.shopx.member.api.model.domain.vo.EsCommentCategoryClassifyVO;
import com.shopx.member.api.model.domain.vo.EsCommentCategoryVO;
import com.shopx.member.api.model.domain.vo.EsMemberCommentDetailVO;
import com.shopx.member.api.model.domain.vo.wap.EsCommentCountVO;
import com.shopx.member.api.service.*;
import com.shopx.trade.api.constant.TradeErrorCode;
import com.shopx.trade.api.model.domain.EsBuyerOrderDO;
import com.shopx.trade.api.model.domain.EsBuyerOrderItemsDO;
import com.shopx.trade.api.model.domain.EsOrderCommDO;
import com.shopx.trade.api.model.domain.vo.*;
import com.shopx.trade.api.model.enums.CommentStatusEnum;
import com.shopx.trade.api.service.IEsOrderItemsService;
import com.shopx.trade.web.constant.ApiStatus;
import com.shopx.trade.web.request.EsAddCommentForm;
import com.shopx.trade.web.request.EsCommentSupportForm;
import com.shopx.trade.web.request.EsWapCommentForm;
import com.shopx.trade.web.request.query.EsQueryDetailCommentForm;
import com.shopx.trade.web.request.query.EsWapCommentQueryForm;
import com.shopx.trade.web.shiro.oath.ShiroKit;
import com.shopx.trade.web.shiro.oath.ShiroUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

/**
 * <p>
 * 移动端-评论
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-02-17 09:28:26
 */
@Api(value = "/wap/member/comment",tags = "移动端-评论")
@RestController
@RequestMapping("/wap/member/comment")
public class EsWapCommentController  {

    @Reference(version = "${dubbo.application.version}", timeout = 5000, check = false)
    private IEsMemberCommentService iesMemberCommentService;
    @Reference(version = "${dubbo.application.version}", timeout = 5000,check = false)
    private IEsOrderItemsService iEsOrderItemsService;
    @Reference(version = "${dubbo.application.version}", timeout = 5000, check = false)
    private IEsCommentSupportService commentSupportService;
    @Reference(version = "${dubbo.application.version}", timeout = 5000, check = false)
    private IEsAddCommentService addCommentService;
    @Reference(version = "${dubbo.application.version}", timeout = 5000, check = false)
    IEsCommentCategoryService commentCategoryService;
    @Reference(version = "${dubbo.application.version}",timeout = 5000)
    private IEsMemberTokenService ieMemberUserTokenService;

    @ApiOperation(value = "添加评价")
    @PostMapping("/insertComment")
    @ResponseBody
    public ApiResponse insertComment(@RequestBody @Valid EsWapCommentForm form) {
        ShiroUser user = ShiroKit.getUser();
        if (null == user) {
            return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }
        Long userId = user.getId();
        EsMemberCommentCopyDTO esMemberCommentCopyDTO = new EsMemberCommentCopyDTO();
        BeanUtil.copyProperties(form,esMemberCommentCopyDTO);
        esMemberCommentCopyDTO.setMemberId(userId);
        if(StringUtils.isNotBlank(form.getOriginal())){
            esMemberCommentCopyDTO.setOriginal(Lists.newArrayList(StringUtils.split(form.getOriginal(),",")));
        }
        esMemberCommentCopyDTO.setIsAnonymous(form.getIsAnonymous() == true ? 1: 2);
        DubboResult result =iesMemberCommentService.insertMemberComment(esMemberCommentCopyDTO);
        if (result.isSuccess()) {
            return ApiResponse.success(result.getData());
        }
        return ApiResponse.fail(result.getCode(), result.getMsg());
    }

//    @ApiOperation(value = "根据商品id查询评论列表", response = EsCommentCategoryClassifyVO.class)
//    @ApiImplicitParam(name = "goodId", value = "商品Id", required = true, dataType = "long", paramType = "path")
//    @GetMapping("/getEsCommentCategoryList/{goodId}")
//    @ResponseBody
//    public ApiResponse getEsCommentCategoryList(@PathVariable Long goodId) {
//        DubboResult<EsCommentCategoryClassifyDO> result = commentCategoryService.getEsCommentCategoryList(goodId);
//        if(result.isSuccess()){
//            EsCommentCategoryClassifyVO commentCategoryClassifyVO = new EsCommentCategoryClassifyVO();
//            EsCommentCategoryClassifyDO commentCategoryClassifyDO =  result.getData();
//            if(CollectionUtils.isNotEmpty(commentCategoryClassifyDO.getExpressLabels())){
//                commentCategoryClassifyVO.setExpressLabels(BeanUtil.copyList(commentCategoryClassifyDO.getExpressLabels(), EsCommentCategoryVO.class));
//            }
//            if(CollectionUtils.isNotEmpty(commentCategoryClassifyDO.getGoodLabels())){
//                commentCategoryClassifyVO.setGoodLabels(BeanUtil.copyList(commentCategoryClassifyDO.getGoodLabels(), EsCommentCategoryVO.class));
//            }
//            if(CollectionUtils.isNotEmpty(commentCategoryClassifyDO.getServiceLabels())){
//                commentCategoryClassifyVO.setServiceLabels(BeanUtil.copyList(commentCategoryClassifyDO.getServiceLabels(), EsCommentCategoryVO.class));
//            }
//
//            return ApiResponse.success(commentCategoryClassifyVO);
//        }
//        return ApiResponse.fail(ApiStatus.wrapperException(result));
//    }


    @ApiOperation(value = "查询商品详情评论",notes = "查询商品详情评论",response = EsMemberCommentDetailVO.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页码", required = true, dataType = "int", paramType = "query", example = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页显示数量", required = true, dataType = "int", paramType = "query", example = "1")

    })
    @GetMapping("/getDetailCommentList")
    @ResponseBody
    public ApiResponse getMemberDetailCommentList(@Valid EsQueryDetailCommentForm esQueryDetailCommentForm, int pageSize, int pageNum){

        // shiro 框架问题，此处用token自行获取当前用户id
        Long memberId = 0L;
        try{
            ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = servletRequestAttributes.getRequest();
            String token = request.getHeader("token");
            if(StringUtils.isNotBlank(token)){
                EsMemberTokenDTO userTokenDTO = new EsMemberTokenDTO();
                userTokenDTO.setToken(token);
                DubboResult<EsMemberTokenDO> tokenResult = ieMemberUserTokenService.getMemberTokenInfo(userTokenDTO);
                if(tokenResult.isSuccess()){
                    memberId = tokenResult.getData().getMemberId();
                }
            }
        }catch (Throwable ignore){}

        EsQueryDetailCommentDTO esQueryDetailCommentDTO = new EsQueryDetailCommentDTO();
        BeanUtil.copyProperties(esQueryDetailCommentForm,esQueryDetailCommentDTO);
        DubboPageResult result = this.iesMemberCommentService.getMemberDetailCommentList(esQueryDetailCommentDTO,memberId, pageSize,pageNum);
        if(result.isSuccess()){
            return ApiResponse.success(DubboPageResult.success(result.getData().getTotal(), result.getData().getList()));
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }

    @ApiOperation(value = "分页查询评论数据", response = OrderCommentVO.class)
    @GetMapping("/getOrdersAndCommentList")
    @ResponseBody
    public ApiPageResponse getOrdersAndCommentList(@Valid EsWapCommentQueryForm form) {
        Long memberId = ShiroKit.getUser().getId();
        if (null == memberId) {
            return (ApiPageResponse) ApiPageResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }
        DubboPageResult<EsBuyerOrderDO> result;
        if (form.getType() == MemberConstant.commented) {
            result = iEsOrderItemsService.getBuyerOrderCommentGoodsList(memberId,form.getOrderSn() ,CommentStatusEnum.FINISHED.value(), form.getPageSize(), form.getPageNum());
        } else {
            result = iEsOrderItemsService.getBuyerOrderCommentGoodsList(memberId, form.getOrderSn(),CommentStatusEnum.UNFINISHED.value(), form.getPageSize(), form.getPageNum());
        }
        if (!result.isSuccess()) {
            return (ApiPageResponse) ApiPageResponse.fail(ApiStatus.wrapperException(result));
        }
        List<OrderCommentVO> resultList = new ArrayList<>();
        List<EsBuyerOrderDO> list1 = result.getData().getList();
        if (CollectionUtils.isNotEmpty(list1)){
            for(EsBuyerOrderDO buyerOrderDO : list1){
                OrderCommentVO orderCommentVO = new OrderCommentVO();
                BeanUtil.copyProperties(buyerOrderDO, orderCommentVO);
                String orderSn = buyerOrderDO.getOrderSn();
                List<EsBuyerOrderItemsDO> itemsDOList = buyerOrderDO.getEsBuyerOrderItemsDO();
                List<EsBuyerOrderItemsVO> itemArray = new ArrayList<>();
                if(!StringUtils.isBlank(orderSn) && CollectionUtils.isNotEmpty(itemsDOList)) {
                    for(EsBuyerOrderItemsDO itemDO : itemsDOList){
                        DubboResult<EsCommentInfoDO> resultComment = iesMemberCommentService.getCommentInfoByGoodsIdAndOrderSn(itemDO.getGoodsId(), orderSn,itemDO.getSkuId());
                        if (!resultComment.isSuccess()) {
                            throw new ArgumentException(resultComment.getCode(), resultComment.getMsg());
                        }
                        EsBuyerOrderItemsVO buyerOrderItemsVO = new EsBuyerOrderItemsVO();
                        BeanUtil.copyProperties(itemDO, buyerOrderItemsVO);
                        EsCommentInfoVO esCommentInfoVO = new EsCommentInfoVO();
                        if (null != resultComment.getData()) {
                            EsCommentInfoDO commentInfoDO = resultComment.getData();
                            BeanUtil.copyProperties(commentInfoDO, esCommentInfoVO);
                            if(resultComment.getData().getAddContentDO() != null){
                                EsAddCommentVO addCommentVO = new EsAddCommentVO();
                                BeanUtil.copyProperties(commentInfoDO.getAddContentDO(), addCommentVO);
                                esCommentInfoVO.setAddContentVO(addCommentVO);
                            }
                        }
                        buyerOrderItemsVO.setEsCommentInfoVO(esCommentInfoVO);

                        DubboResult<EsCommentCategoryClassifyDO> categoryClassifyDOResult = commentCategoryService.getEsCommentCategoryList(itemDO.getGoodsId());
                        Map<String, Object> map = new HashMap<>();
                        map.put("expressLabels", Arrays.asList());
                        map.put("goodLabels", Arrays.asList());
                        map.put("serviceLabels", Arrays.asList());
                        if(categoryClassifyDOResult.isSuccess() && categoryClassifyDOResult.getData() != null){
                            EsCommentCategoryClassifyDO commentCategoryClassifyDO =  categoryClassifyDOResult.getData();
                            if(commentCategoryClassifyDO.getExpressLabels() == null){
                                map.put("expressLabels", BeanUtil.copyList(commentCategoryClassifyDO.getExpressLabels(), EsCommentCategoryCopyVO.class));
                            }

                            if(commentCategoryClassifyDO.getGoodLabels() != null){
                                map.put("goodLabels", BeanUtil.copyList(commentCategoryClassifyDO.getGoodLabels(), EsCommentCategoryCopyVO.class));
                            }

                            if(commentCategoryClassifyDO.getServiceLabels() == null){
                                map.put("serviceLabels", BeanUtil.copyList(commentCategoryClassifyDO.getServiceLabels(), EsCommentCategoryCopyVO.class));
                            }
                        }
                        buyerOrderItemsVO.setCommentCategoryClassifyVO(map);
                        itemArray.add(buyerOrderItemsVO);
                    }
                }
                orderCommentVO.setEsBuyerOrderItemsVO(itemArray);
                resultList.add(orderCommentVO);
            }
        }
        return ApiPageResponse.pageSuccess(result.getData().getTotal(), resultList);
    }

    @ApiOperation(value = "统计好评率")
    @ApiImplicitParam(name = "goodId", value = "商品Id", required = true, dataType = "long", paramType = "path")
    @GetMapping("/getGoodCommentRate/{goodsId}")
    @ResponseBody
    public ApiResponse getGoodCommentRate(@PathVariable Long goodsId) {
//        Long userId = ShiroKit.getUser().getId();
//        if (null == userId) {
//            return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
//        }
        DubboResult<Double> result = this.iesMemberCommentService.getGoodCommentRate(goodsId);
        if (result.isSuccess()) {
            return ApiResponse.success(DubboResult.success(result.getData()));
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }


    @ApiOperation(value = "添加追评内容")
    @PostMapping("/insertAddComment")
    @ResponseBody
    public ApiResponse insertAddComment(@RequestBody @Valid EsAddCommentForm esAddCommentForm) {
        EsAddCommentDTO esAddCommentDTO = new EsAddCommentDTO();
        BeanUtil.copyProperties(esAddCommentForm, esAddCommentDTO);
        DubboResult result = addCommentService.insertAddComment(esAddCommentDTO);
        if (result.isSuccess()) {
            return ApiResponse.success(result.getData());
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }

    @ApiOperation(value = "根据商品id查询标签列表", notes = "根据商品id查询标签列表", response = EsCommentCategoryClassifyVO.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "goodId", value = "商品Id", required = true, dataType = "Long", paramType = "query", example = "1")
    })
    @GetMapping("/getEsCommentCategoryList")
    @ResponseBody
    public ApiResponse getEsCommentCategoryList(@Valid Long goodId) {
        DubboResult<EsCommentCategoryClassifyDO> result = commentCategoryService.getEsCommentCategoryList(goodId);
        if(result.isSuccess()){
            EsCommentCategoryClassifyVO commentCategoryClassifyVO = new EsCommentCategoryClassifyVO();
            EsCommentCategoryClassifyDO commentCategoryClassifyDO =  result.getData();
            if(CollectionUtils.isNotEmpty(commentCategoryClassifyDO.getExpressLabels())){
                commentCategoryClassifyVO.setExpressLabels(BeanUtil.copyList(commentCategoryClassifyDO.getExpressLabels(), EsCommentCategoryVO.class));
            }
            if(CollectionUtils.isNotEmpty(commentCategoryClassifyDO.getGoodLabels())){
                commentCategoryClassifyVO.setGoodLabels(BeanUtil.copyList(commentCategoryClassifyDO.getGoodLabels(), EsCommentCategoryVO.class));
            }
            if(CollectionUtils.isNotEmpty(commentCategoryClassifyDO.getServiceLabels())){
                commentCategoryClassifyVO.setServiceLabels(BeanUtil.copyList(commentCategoryClassifyDO.getServiceLabels(), EsCommentCategoryVO.class));
            }

            return ApiResponse.success(commentCategoryClassifyVO);
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }

    @ApiOperation(value = "评论点赞", notes = "根据form表单数据提交")
    @PostMapping("/insertSupport")
    public ApiResponse add(@RequestBody @Valid EsCommentSupportForm esCommentSupportForm) {
        Long userId = ShiroKit.getUser().getId();
        if (null == userId) {
            return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }
        EsCommentSupportDTO esCommentSupportDTO = new EsCommentSupportDTO();
        BeanUtil.copyProperties(esCommentSupportForm, esCommentSupportDTO);
        esCommentSupportDTO.setMemberId(userId);
        esCommentSupportDTO.setCommentId(esCommentSupportForm.getCommentId());
        DubboResult result = commentSupportService.insertCommentSupport(esCommentSupportDTO);
        if (result.isSuccess()) {
            return ApiResponse.success();
        }
        return ApiResponse.fail(TradeErrorCode.ALREADY_SUPPORT.getErrorCode(), TradeErrorCode.ALREADY_SUPPORT.getErrorMsg());
    }

    @ApiOperation(value = "取消点赞", notes = "根据form表单数据提交")
    @DeleteMapping("/deleteSupport")
    public ApiResponse deleteSupport(@Valid EsCommentSupportForm esCommentSupportForm) {
        Long userId = ShiroKit.getUser().getId();
        if (null == userId) {
            return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }
        EsCommentSupportDTO esCommentSupportDTO = new EsCommentSupportDTO();
        BeanUtil.copyProperties(esCommentSupportForm, esCommentSupportDTO);
        esCommentSupportDTO.setMemberId(userId);
        esCommentSupportDTO.setCommentId(esCommentSupportForm.getId());
        DubboResult result = commentSupportService.updateCommentSupport(esCommentSupportDTO,userId);
        if (result.isSuccess()) {
            return ApiResponse.success();
        }
        return ApiResponse.fail(TradeErrorCode.ALREADY_CANCEL.getErrorCode(), TradeErrorCode.ALREADY_CANCEL.getErrorMsg());
    }


    @ApiOperation(value = "个人中心评论数量", response = EsOrderCommVO.class)
    @GetMapping("/getOrdersAndCommentCount")
    @ResponseBody
    public ApiResponse getOrdersAndCommentCount() {
        Long memberId = ShiroKit.getUser().getId();
        if (null == memberId) {
            return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }

        DubboResult<EsOrderCommDO> count = iEsOrderItemsService.getCount(memberId);
        if (count.isSuccess()){
            EsOrderCommVO commVO=new EsOrderCommVO();
            BeanUtil.copyProperties(count.getData(),commVO);
            return ApiResponse.success(commVO);
        }
        return ApiResponse.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
    }

    @ApiOperation(value = "查询商品评论数",notes = "查询商品评论数",response = EsCommentCountVO.class)
    @GetMapping("/getGoodCommentCount/{goodsId}")
    @ResponseBody
    public ApiResponse getCommentCount(@PathVariable("goodsId") Long goodsId){
        DubboResult<EsCommentCountDO> result = this.iesMemberCommentService.getCommentCount(goodsId);
        EsCommentCountVO commentCountVO=new EsCommentCountVO();
        if(result.isSuccess()){
            BeanUtil.copyProperties(result.getData(),commentCountVO);
            return ApiResponse.success(commentCountVO);
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }

}

