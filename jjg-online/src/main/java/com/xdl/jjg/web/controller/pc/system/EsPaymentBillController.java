package com.xdl.jjg.web.controller.pc.system;

import com.shopx.common.model.result.ApiResponse;
import com.shopx.common.model.result.DubboPageResult;
import com.shopx.common.model.result.DubboResult;
import com.shopx.common.web.BaseController;
import com.shopx.trade.api.model.domain.EsPaymentBillDO;
import com.shopx.trade.api.model.domain.dto.EsPaymentBillDTO;
import com.shopx.trade.api.model.domain.vo.EsPaymentBillVO;
import com.shopx.trade.api.service.IEsPaymentBillService;
import com.shopx.trade.web.constant.ApiStatus;
import com.shopx.trade.web.request.EsPaymentBillForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 会员支付帐单-es_payment_bill 前端控制器
 * </p>
 *
 * @author LiuJG344009799@qq.com
 * @since 2019-05-29
 */
@Api(value = "/esPaymentBill",tags = "会员支付账单信息接口")
@RestController
@RequestMapping("/esPaymentBill")
public class EsPaymentBillController extends BaseController {

    @Reference(version = "${dubbo.application.version}",timeout = 5000)
    private IEsPaymentBillService iesPaymentBillService;


    /**
     * 插入会员支付账单信息
     * @param esPaymentBillForm
     * @return
     */
    @PostMapping
    @ApiOperation(value = "插入会员支付账单信息",notes = "根据前台传入的Form")
    @ResponseBody
    @ApiImplicitParam(name = "esPaymentBillForm", value = "Form表单")
    public ApiResponse insertPaymentBill(EsPaymentBillForm esPaymentBillForm){

        EsPaymentBillDTO esPaymentBillDTO = new EsPaymentBillDTO();

        BeanUtils.copyProperties(esPaymentBillForm,esPaymentBillDTO);

        DubboResult result = iesPaymentBillService.insertPaymentBill(esPaymentBillDTO);

        if(result.isSuccess()){
            return ApiResponse.success(result.getData());
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }

    /**
     * 根据Id会员支付账单信息
     * @param id
     * @return
     */
    @DeleteMapping(value = "/{id}")
    @ApiOperation(value = "删除会员支付账单信息",notes = "根据Id删除会员支付账单信息")
    @ResponseBody
    @ApiImplicitParam(name = "id", value = "主键id")
    public ApiResponse deletePaymentBill(@PathVariable Long id){

        DubboResult result = iesPaymentBillService.deletePaymentBill(id);

        if(result.isSuccess()){
            return ApiResponse.success(result.getData());
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }

    /**
     * 更新会员支付账单信息
     * @param esPaymentBillForm
     * @return
     */
    @PutMapping
    @ApiOperation(value = "更新会员支付账单信息",notes = "根据Form修改会员支付账单信息")
    @ResponseBody
    @ApiImplicitParam(name = "esPaymentBillForm", value = "Form表单")
    public ApiResponse updatePromotionGoods(EsPaymentBillForm esPaymentBillForm){

        EsPaymentBillDTO esPaymentBillDTO = new EsPaymentBillDTO();

        BeanUtils.copyProperties(esPaymentBillForm,esPaymentBillDTO);

        DubboResult result = iesPaymentBillService.updatePaymentBill(esPaymentBillDTO);

        if(result.isSuccess()){
            return ApiResponse.success(result.getData());
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }

    /**
     * 获取会员支付账单信息
     * @param id
     * @return
     */
    @GetMapping(value = "/{id}")
    @ApiOperation(value = "获取会员支付账单信息",notes = "通过id获取会员支付账单信息")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "主键id", required = true, dataType = "long", paramType = "query", example = "1")})
    @ResponseBody
    public ApiResponse<EsPaymentBillVO> getPaymentBill(@PathVariable Long id){

        DubboResult<EsPaymentBillDO> result = iesPaymentBillService.getPaymentBill(id);

        if (result.isSuccess()){
            return ApiResponse.success(result.getData());
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));

    }

    /**
     * 获取会员支付账单信息列表
     * @param esPaymentBillForm
     * @param pageSize
     * @param pageNum
     * @return
     */
    @GetMapping
    @ApiOperation(value = "获取会员支付账单信息列表",notes = "跟据form表单提交的数据查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "esPaymentBillForm", value = "查询Form表单"),
            @ApiImplicitParam(name = "pageSize", value = "页数", dataType = "int", paramType = "query", required = true, example = "10"),
            @ApiImplicitParam(name = "pageNum", value = "页码", dataType = "int", paramType = "query", required = true, example = "1")})
    @ResponseBody
    public ApiResponse getPaymentBillList(EsPaymentBillForm esPaymentBillForm, @ApiIgnore @NotEmpty(message = "页数不能为空") int pageSize,
                                       @ApiIgnore @NotEmpty(message = "页码不能为空") int pageNum){

        EsPaymentBillDTO esPaymentBillDTO = new EsPaymentBillDTO();

        BeanUtils.copyProperties(esPaymentBillForm,esPaymentBillDTO);

        DubboPageResult<EsPaymentBillDO> result = iesPaymentBillService.getPaymentBillList(esPaymentBillDTO, pageSize, pageNum);

        if (result.isSuccess()){
            List<EsPaymentBillDO> esPaymentBillDOList = result.getData().getList();
            List<EsPaymentBillVO> EsPaymentBillVOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(esPaymentBillDOList)){
                EsPaymentBillVOList = esPaymentBillDOList.stream().map(EsPaymentBill ->{
                    EsPaymentBillVO esPaymentBillVO = new EsPaymentBillVO();
                    BeanUtils.copyProperties(EsPaymentBill,esPaymentBillVO);
                    return esPaymentBillVO;
                }).collect(Collectors.toList());
            }
            return ApiResponse.success(DubboPageResult.success(EsPaymentBillVOList).getData());
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }

}

