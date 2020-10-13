package com.xdl.jjg.web.controller.pc.member;

import com.shopx.common.model.result.ApiPageResponse;
import com.shopx.common.model.result.ApiResponse;
import com.shopx.common.model.result.DubboPageResult;
import com.shopx.common.model.result.DubboResult;
import com.shopx.common.util.BeanUtil;
import com.shopx.common.web.BaseController;
import com.shopx.member.api.constant.MemberErrorCode;
import com.shopx.member.api.model.domain.dto.EsReceiptHistoryDTO;
import com.shopx.member.api.service.IEsReceiptHistoryService;
import com.shopx.trade.api.model.domain.EsBuyerOrderDO;
import com.shopx.trade.api.model.domain.EsBuyerOrderItemsDO;
import com.shopx.trade.api.model.domain.dto.EsBuyerOrderQueryDTO;
import com.shopx.trade.api.model.domain.vo.EsBuyerOrderItemsVO;
import com.shopx.trade.api.model.domain.vo.EsBuyerOrderVO;
import com.shopx.trade.api.service.IEsOrderService;
import com.shopx.trade.web.constant.ApiStatus;
import com.shopx.trade.web.request.EsReceiptHistoryForm;
import com.shopx.trade.web.request.QueryReceiptOrdersForm;
import com.shopx.trade.web.shiro.oath.ShiroKit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 发票历史 前端控制器
 * </p>
 *
 * @author HQL 236154186@qq.com
 * @since 2019-05-29
 */
@Api(value = "/esReceiptHistory", tags = "会员发票历史")
@RestController
@RequestMapping("/esReceiptHistory")
public class EsReceiptHistoryController extends BaseController {

    @Reference(version = "${dubbo.application.version}", timeout = 5000,check = false)
    private IEsReceiptHistoryService iesReceiptHistoryService;
    @Reference(version = "${dubbo.application.version}", timeout = 5000, check = false)
    private IEsOrderService iEsOrderService;


    @ApiOperation(value = "新增会员发票", notes = "根据form表单数据提交")
    @PostMapping("/insertesReceiptHistory")
    public ApiResponse insertesReceiptHistory(EsReceiptHistoryForm esReceiptHistoryForm) {
        Long memberId = ShiroKit.getUser().getId();
        if (null == memberId) {
            return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }
        EsReceiptHistoryDTO esReceiptHistoryDTO = new EsReceiptHistoryDTO();
        BeanUtil.copyProperties(esReceiptHistoryForm, esReceiptHistoryDTO);
        esReceiptHistoryDTO.setMemberId(memberId);
        DubboResult result = this.iesReceiptHistoryService.insertReceiptHistory(esReceiptHistoryDTO);
        if (result.isSuccess()) {
            return ApiResponse.success(result.getData());
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }

    @ApiOperation(value = "查询订单和发票", notes = "查询订单和发票", response = EsBuyerOrderVO.class)
    @GetMapping("/getOrdersAndReceipt")
    @ResponseBody
    public ApiResponse getOrdersAndReceipt(QueryReceiptOrdersForm queryReceiptOrdersForm) {
        Long userId = ShiroKit.getUser().getId();
        if (null == userId) {
            return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }
        EsBuyerOrderQueryDTO esBuyerOrderQueryDTO = new EsBuyerOrderQueryDTO();
//        if(!StringUtils.isBlank(queryReceiptOrdersForm.getGoodsName())){
//            esBuyerOrderQueryDTO.setGoodsName(queryReceiptOrdersForm.getGoodsName());
//        }
//        if(!StringUtils.isBlank(queryReceiptOrdersForm.getOrderSn())){
//            esBuyerOrderQueryDTO.setOrderSn(queryReceiptOrdersForm.getOrderSn());
//        }
        esBuyerOrderQueryDTO.setMemberId(userId);
        esBuyerOrderQueryDTO.setKeyword(queryReceiptOrdersForm.getKeyword());
        DubboPageResult<EsBuyerOrderDO> result =this.iEsOrderService.getEsMemberReceiptList(esBuyerOrderQueryDTO,
                queryReceiptOrdersForm.getPageSize(),queryReceiptOrdersForm.getPageNum());
        if (result.isSuccess()) {
            List<EsBuyerOrderVO> list = new ArrayList<>();
            List<EsBuyerOrderDO> listDO = result.getData().getList();
            list = listDO.stream().map(buyerOrder -> {
                List<EsBuyerOrderItemsDO> buyerOrderItemsDOs = buyerOrder.getEsBuyerOrderItemsDO();
                List<EsBuyerOrderItemsVO> orderItemsVOList = BeanUtil.copyList(buyerOrderItemsDOs, EsBuyerOrderItemsVO.class);
                EsBuyerOrderVO buyerOrderVO = new EsBuyerOrderVO();
                BeanUtil.copyProperties(buyerOrder, buyerOrderVO);
                buyerOrderVO.setEsBuyerOrderItemsVO(orderItemsVOList);
                return buyerOrderVO;
            }).collect(Collectors.toList());
            return ApiPageResponse.pageSuccess(result.getData().getTotal(),list);
        }
        return ApiPageResponse.fail(ApiStatus.wrapperException(result));
    }


}

