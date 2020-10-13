package com.xdl.jjg.web.controller.pc.member;

import com.shopx.common.exception.ArgumentException;
import com.shopx.common.model.result.ApiPageResponse;
import com.shopx.common.model.result.DubboPageResult;
import com.shopx.common.model.result.DubboResult;
import com.shopx.common.util.BeanUtil;
import com.shopx.common.web.BaseController;
import com.shopx.member.api.constant.MemberConstant;
import com.shopx.member.api.constant.MemberErrorCode;
import com.shopx.member.api.model.domain.EsCommentCategoryClassifyDO;
import com.shopx.member.api.model.domain.EsCommentInfoDO;
import com.shopx.member.api.model.domain.EsMemberCommentDetailDO;
import com.shopx.member.api.model.domain.GradeLevelDO;
import com.shopx.member.api.model.domain.dto.EsAddCommentDTO;
import com.shopx.member.api.model.domain.dto.EsCommentSupportDTO;
import com.shopx.member.api.model.domain.dto.EsMemberCommentCopyDTO;
import com.shopx.member.api.model.domain.dto.EsQueryDetailCommentDTO;
import com.shopx.member.api.model.domain.vo.EsCommentCategoryClassifyVO;
import com.shopx.member.api.model.domain.vo.EsCommentCategoryVO;
import com.shopx.member.api.model.domain.vo.EsMemberCommentDetailVO;
import com.shopx.member.api.model.domain.vo.GradeLevelVO;
import com.shopx.member.api.service.IEsAddCommentService;
import com.shopx.member.api.service.IEsCommentCategoryService;
import com.shopx.member.api.service.IEsCommentSupportService;
import com.shopx.member.api.service.IEsMemberCommentService;
import com.shopx.trade.api.constant.TradeErrorCode;
import com.shopx.trade.api.model.domain.EsBuyerOrderDO;
import com.shopx.trade.api.model.domain.EsBuyerOrderItemsDO;
import com.shopx.trade.api.model.domain.vo.*;
import com.shopx.trade.api.model.enums.CommentStatusEnum;
import com.shopx.trade.api.service.IEsOrderItemsService;
import com.shopx.trade.web.constant.ApiStatus;
import com.shopx.trade.web.request.CommentImageForm;
import com.shopx.trade.web.request.EsAddCommentForm;
import com.shopx.trade.web.request.EsCommentSupportForm;
import com.shopx.trade.web.request.EsMemberCommentCopyForm;
import com.shopx.trade.web.request.query.EsQueryDetailCommentForm;
import com.shopx.trade.web.shiro.oath.ShiroKit;
import com.shopx.trade.web.shiro.oath.ShiroUser;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 我的评论 前端控制器
 * </p>
 *
 * @author HQL 236154186@qq.com
 * @since 2019-05-29
 */
@Api(value = "/esMemberComment", tags = "评论相关接口")
@RestController
@RequestMapping("/zhuox/esMemberComment")
public class EsMemberCommentController extends BaseController {

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

    @ApiOperation(value = "订单评论详情",response = OrderCommentVO.class)
    @ApiImplicitParam(name = "orderSn",value = "订单编号",required = true,dataType = "String",paramType = "query")
    @ResponseBody
    @GetMapping("/getCommentDetailsList")
    public ApiResponse getCommentDetailsList(@RequestParam(value = "orderSn",required = false) String orderSn){
        Long memberId = ShiroKit.getUser().getId();
        if (null == memberId) {
            return  ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }
        DubboResult<EsBuyerOrderDO> result = iEsOrderItemsService.getBuyerCommentGoodsList(memberId, orderSn, CommentStatusEnum.FINISHED.value());

        if (!result.isSuccess()) {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }

        EsBuyerOrderDO esBuyerOrderDO = result.getData();
        OrderCommentVO orderCommentVO = new OrderCommentVO();
        BeanUtil.copyProperties(esBuyerOrderDO, orderCommentVO);
        List<EsBuyerOrderItemsDO> itemsDOList = esBuyerOrderDO.getEsBuyerOrderItemsDO();
        List<EsBuyerOrderItemsVO> itemArray = new ArrayList<>();
        if(!StringUtils.isBlank(orderSn) && CollectionUtils.isNotEmpty(itemsDOList)) {
            for(EsBuyerOrderItemsDO itemDO : itemsDOList){
                DubboResult<EsCommentInfoDO> resultComment = iesMemberCommentService.getPCCommentInfoByGoodsIdAndOrderSn(itemDO.getGoodsId(), orderSn,itemDO.getSkuId());
                if (!resultComment.isSuccess()) {
                    throw new ArgumentException(resultComment.getCode(), resultComment.getMsg());
                }
                EsBuyerOrderItemsVO buyerOrderItemsVO = new EsBuyerOrderItemsVO();
                BeanUtil.copyProperties(itemDO, buyerOrderItemsVO);
                EsCommentInfoVO esCommentInfoVO = null;
                if (null != resultComment.getData()) {
                    esCommentInfoVO = new EsCommentInfoVO();
                    EsCommentInfoDO commentInfoDO = resultComment.getData();
                    BeanUtil.copyProperties(commentInfoDO, esCommentInfoVO);
                    List <CommentImageVO> commentImageVOList = new ArrayList<>();
                    if (CollectionUtils.isNotEmpty(commentInfoDO.getCommentImageVOList())){

                        List<com.shopx.member.api.model.domain.vo.CommentImageVO> imageVOList = commentInfoDO.getCommentImageVOList();
                        imageVOList.forEach(commentImageVO1 -> {
                            CommentImageVO commentImageVO = new CommentImageVO();
                            commentImageVO.setUrl(commentImageVO1.getUrl());
                            commentImageVOList.add(commentImageVO);
                        });

                    }

                    esCommentInfoVO.setCommentImageVO(commentImageVOList);
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
                    if(commentCategoryClassifyDO.getExpressLabels() != null){
                        map.put("expressLabels", BeanUtil.copyList(commentCategoryClassifyDO.getExpressLabels(), EsCommentCategoryCopyVO.class));
                    }

                    if(commentCategoryClassifyDO.getGoodLabels() != null){
                        map.put("goodLabels", BeanUtil.copyList(commentCategoryClassifyDO.getGoodLabels(), EsCommentCategoryCopyVO.class));
                    }

                    if(commentCategoryClassifyDO.getServiceLabels()!= null){
                        map.put("serviceLabels", BeanUtil.copyList(commentCategoryClassifyDO.getServiceLabels(), EsCommentCategoryCopyVO.class));
                    }
                }
                buyerOrderItemsVO.setCommentCategoryClassifyVO(map);
                itemArray.add(buyerOrderItemsVO);
            }
        }
        orderCommentVO.setEsBuyerOrderItemsVO(itemArray);
        return ApiResponse.success(orderCommentVO);
    }

    @ApiOperation(value = "我的评论", notes = "我的评论", response = OrderCommentVO.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页码", required = true, dataType = "int", paramType = "query", example = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页显示数量", required = true, dataType = "int", paramType = "query", example = "1"),
            @ApiImplicitParam(name = "type", value = "2待评价，1已评价", required = true, dataType = "String", paramType = "query", example = "1"),
            @ApiImplicitParam(name = "orderSn", value = "订单编号", required = false, dataType = "String", paramType = "query", example = "")
    })
    @GetMapping("/getOrdersAndCommentList")
    @ResponseBody
    public ApiPageResponse getOrdersAndCommentList(@ApiIgnore @RequestParam(value = "pageNum",defaultValue = "1",required = false) int pageNum, @ApiIgnore @RequestParam(value = "pageSize",defaultValue = Integer.MAX_VALUE+"",required = false)int pageSize, @ApiIgnore String type, @ApiIgnore @RequestParam(value = "orderSn",required = false) String reqOrderSn) {
        Long memberId = ShiroKit.getUser().getId();
        if (null == memberId) {
            return (ApiPageResponse) ApiPageResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }
        DubboPageResult<EsBuyerOrderDO> result;
        if (null == type){
            type = "2";
        }
        if (Integer.parseInt(type) == MemberConstant.commented) {
            result = iEsOrderItemsService.getBuyerOrderCommentGoodsList(memberId,reqOrderSn, CommentStatusEnum.FINISHED.value(), pageSize, pageNum);
        } else {
            result = iEsOrderItemsService.getBuyerOrderCommentGoodsList(memberId,reqOrderSn, CommentStatusEnum.UNFINISHED.value(), pageSize, pageNum);
        }
        if (!result.isSuccess()) {
            return (ApiPageResponse) ApiPageResponse.fail(ApiStatus.wrapperException(result));
        }
        List<OrderCommentVO> resultList = new ArrayList<>();
        List<EsBuyerOrderDO> list1 = result.getData().getList();
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
//                            if(CollectionUtils.isEmpty(addCommentVO.getOriginal()) || StringUtils.equals(addCommentVO.getOriginal().get(0),"")){
//                                addCommentVO.setOriginal(new ArrayList<>());
//                            }
                            esCommentInfoVO.setAddContentVO(addCommentVO);
                        }
                        if(CollectionUtils.isEmpty(commentInfoDO.getOriginals())){
                            esCommentInfoVO.setOriginals(new ArrayList<>());
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
                        if(commentCategoryClassifyDO.getExpressLabels() != null){
                            map.put("expressLabels", BeanUtil.copyList(commentCategoryClassifyDO.getExpressLabels(), EsCommentCategoryCopyVO.class));
                        }

                        if(commentCategoryClassifyDO.getGoodLabels() != null){
                            map.put("goodLabels", BeanUtil.copyList(commentCategoryClassifyDO.getGoodLabels(), EsCommentCategoryCopyVO.class));
                        }

                        if(commentCategoryClassifyDO.getServiceLabels()!= null){
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
            return ApiPageResponse.pageSuccess(result.getData().getTotal(), resultList);
    }

    // TODO 商品详情页面待优化
    @ApiOperation(value = "统计好评率", notes = "统计好评率")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "goodsId", value = "商品Id", required = true, dataType = "Long", paramType = "query", example = "1")

    })
    @GetMapping("/getGoodCommentRate/{goodsId}")
    @ResponseBody
    public ApiResponse getGoodCommentRate(@PathVariable("goodsId") Long goodsId) {
        DubboResult<Double> result = this.iesMemberCommentService.getGoodCommentRate(goodsId);
        if (result.isSuccess()) {
            return ApiResponse.success(DubboResult.success(result.getData()));
        }
       return ApiResponse.fail(ApiStatus.wrapperException(result));
    }

    @ApiOperation(value = "查询商品详情评论",notes = "查询商品详情评论",response = EsMemberCommentDetailVO.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页码", required = true, dataType = "int", paramType = "query", example = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页显示数量", required = true, dataType = "int", paramType = "query", example = "1")

    })
    @GetMapping("/getDetailCommentList")
    @ResponseBody
    public ApiResponse getMemberDetailCommentList(@Valid EsQueryDetailCommentForm esQueryDetailCommentForm, int pageSize, int pageNum){
        ShiroUser user = ShiroKit.getUser();
        Long memberId = null;
        if(user != null){
            memberId = user.getId();
        }
        EsQueryDetailCommentDTO esQueryDetailCommentDTO = new EsQueryDetailCommentDTO();
        BeanUtil.copyProperties(esQueryDetailCommentForm,esQueryDetailCommentDTO);
        DubboPageResult<EsMemberCommentDetailDO> result = this.iesMemberCommentService.getMemberDetailCommentList(esQueryDetailCommentDTO, memberId, pageSize, pageNum);
        if(result.isSuccess()){
//            List<EsMemberCommentDetailVO> resultVo = BeanUtil.copyList(result.getData().getList(), EsMemberCommentDetailVO.class);
            return ApiResponse.success(DubboPageResult.success(result.getData().getTotal(), result.getData().getList()));
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }

    @ApiOperation(value = "统计商品评论数量",notes = "统计商品评论数量")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "goodsId", value = "商品Id", required = true, dataType = "Long", paramType = "query", example = "1")

    })
    @GetMapping("/getCountComment/{goodsId}")
    @ResponseBody
    public ApiResponse getCountComment(@PathVariable("goodsId") Long goodsId){
        DubboResult<GradeLevelDO> result = this.iesMemberCommentService.getCountComment(goodsId);
        if(result.isSuccess()){
            GradeLevelVO gradeLevelVO = new GradeLevelVO();
            BeanUtil.copyProperties(result.getData(),gradeLevelVO);
            return ApiResponse.success(gradeLevelVO);
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }

    @ApiOperation(value = "添加评价", notes = "添加评价")
    @PostMapping("/insertComment")
    @ResponseBody
    public ApiResponse insertComment(@RequestBody @ApiParam(name = "esMemberCommentCopyForm",value = "评论对象",required = true) @Valid EsMemberCommentCopyForm esMemberCommentCopyForm) {
        ShiroUser user = ShiroKit.getUser();
        if (null == user) {
            return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }
        Long userId = user.getId();
        EsMemberCommentCopyDTO esMemberCommentCopyDTO = new EsMemberCommentCopyDTO();
        BeanUtil.copyProperties(esMemberCommentCopyForm,esMemberCommentCopyDTO);
        esMemberCommentCopyDTO.setMemberId(userId);
        esMemberCommentCopyDTO.setIsAnonymous(esMemberCommentCopyForm.getIsAnonymous() == true ? 1: 2);
        DubboResult result =iesMemberCommentService.insertMemberComment(esMemberCommentCopyDTO);
        if (result.isSuccess()) {
            return ApiResponse.success(result.getData());
        }
        return ApiResponse.fail(result.getCode(), result.getMsg());
    }
    @ApiOperation(value = "批量添加评价", notes = "批量添加评价")
    @PostMapping("/batchInsertComment")
    @ResponseBody
    public ApiResponse batchInsertComment(@RequestBody List<EsMemberCommentCopyForm> esMemberCommentCopyForms) {
        ShiroUser user = ShiroKit.getUser();
        if (null == user) {
            return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }
        esMemberCommentCopyForms = esMemberCommentCopyForms.stream().filter(o -> o.getGoodsId() != null).collect(Collectors.toList());

        for (EsMemberCommentCopyForm comment : esMemberCommentCopyForms) {
            Long userId = user.getId();
            EsMemberCommentCopyDTO esMemberCommentCopyDTO = new EsMemberCommentCopyDTO();
            BeanUtil.copyProperties(comment, esMemberCommentCopyDTO);
            if (CollectionUtils.isNotEmpty(comment.getCommentImageForm())){
                List<CommentImageForm> commentImageForm = comment.getCommentImageForm();
                List<String> list = new ArrayList<>();
                commentImageForm.forEach(commentImageForm1 -> {
                    list.add(commentImageForm1.getUrl());
                });
                esMemberCommentCopyDTO.setOriginal(list);
                esMemberCommentCopyDTO.setHaveImage(1);
            }else {
                esMemberCommentCopyDTO.setHaveImage(0);
            }
            esMemberCommentCopyDTO.setMemberId(userId);

            esMemberCommentCopyDTO.setIsAnonymous(comment.getIsAnonymous() == true ? 1: 2);
            DubboResult dubboResult = iesMemberCommentService.insertMemberComment(esMemberCommentCopyDTO);
            if(!dubboResult.isSuccess()){
                return ApiResponse.fail(dubboResult.getCode(), dubboResult.getMsg());
            }
        }
        return ApiResponse.success();
    }

    @ApiOperation(value = "评论点赞", notes = "根据form表单数据提交")
    @PostMapping("/insertSupport")
    public ApiResponse add(@Valid EsCommentSupportForm esCommentSupportForm) {
        Long userId = ShiroKit.getUser().getId();
        if (null == userId) {
            return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }
        EsCommentSupportDTO esCommentSupportDTO = new EsCommentSupportDTO();
        BeanUtil.copyProperties(esCommentSupportForm, esCommentSupportDTO);
        esCommentSupportDTO.setMemberId(userId);
        esCommentSupportDTO.setCommentId(esCommentSupportForm.getId());
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

    @ApiOperation(value = "添加追评内容", notes = "依据前端传递表单值新增追评内容")
    @PostMapping("/insertAddComment")
    @ResponseBody
    public ApiResponse insertAddComment(@RequestBody @ApiParam(name = "esAddCommentForm",value = "追加评论对象",required = true) @Valid EsAddCommentForm esAddCommentForm) {
        EsAddCommentDTO esAddCommentDTO = new EsAddCommentDTO();
        BeanUtil.copyProperties(esAddCommentForm, esAddCommentDTO);
        DubboResult result = addCommentService.insertAddComment(esAddCommentDTO);
        if (result.isSuccess()) {
            return ApiResponse.success(result.getData());
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }
    // TODO 商品详情页面待优化
    @ApiOperation(value = "商品印象", notes = "商品印象")
    @PostMapping("/getLabelsGroup/{goodsId}")
    @ResponseBody
    public ApiResponse getLabelsGroup(@PathVariable("goodsId") Long goodsId) {
        DubboResult result = iesMemberCommentService.getLabelsGroup(goodsId);
        if(result.isSuccess()){
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
}

