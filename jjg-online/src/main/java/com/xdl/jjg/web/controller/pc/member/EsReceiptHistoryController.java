package com.xdl.jjg.web.controller.pc.member;


import com.jjg.member.model.dto.EsReceiptHistoryDTO;
import com.jjg.trade.model.domain.EsBuyerOrderDO;
import com.jjg.trade.model.domain.EsBuyerOrderItemsDO;
import com.jjg.trade.model.dto.EsBuyerOrderQueryDTO;
import com.jjg.trade.model.form.EsReceiptHistoryForm;
import com.jjg.trade.model.form.QueryReceiptOrdersForm;
import com.jjg.trade.model.vo.EsBuyerOrderItemsVO;
import com.jjg.trade.model.vo.EsBuyerOrderVO;
import com.xdl.jjg.constant.ApiStatus;
import com.xdl.jjg.constant.TradeErrorCode;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.response.web.ApiPageResponse;
import com.xdl.jjg.response.web.ApiResponse;
import com.xdl.jjg.shiro.oath.ShiroKit;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.web.controller.BaseController;
import com.xdl.jjg.web.service.IEsOrderService;
import com.xdl.jjg.web.service.feign.member.ReceiptHistoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private ReceiptHistoryService iesReceiptHistoryService;
    @Autowired
    private IEsOrderService iEsOrderService;


    @ApiOperation(value = "新增会员发票", notes = "根据form表单数据提交")
    @PostMapping("/insertesReceiptHistory")
    public ApiResponse insertesReceiptHistory(EsReceiptHistoryForm esReceiptHistoryForm) {
        Long memberId = ShiroKit.getUser().getId();
        if (null == memberId) {
            return ApiResponse.fail(TradeErrorCode.NOT_LOGIN.getErrorCode(), TradeErrorCode.NOT_LOGIN.getErrorMsg());
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
            return ApiResponse.fail(TradeErrorCode.NOT_LOGIN.getErrorCode(), TradeErrorCode.NOT_LOGIN.getErrorMsg());
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

