package com.xdl.jjg.web.controller.pc.after;

import com.shopx.common.model.result.ApiResponse;
import com.shopx.common.model.result.DubboPageResult;
import com.shopx.common.model.result.DubboResult;
import com.shopx.common.web.BaseController;
import com.shopx.trade.api.model.domain.EsRefundDO;
import com.shopx.trade.api.model.domain.dto.EsReFundQueryDTO;
import com.shopx.trade.api.model.domain.dto.EsRefundDTO;
import com.shopx.trade.api.model.domain.vo.AfterSaleTypeVO;
import com.shopx.trade.api.model.domain.vo.EsRefundVO;
import com.shopx.trade.api.service.IEsRefundService;
import com.shopx.trade.web.constant.ApiStatus;
import com.shopx.trade.web.request.EsRefundForm;
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
 *  退款单表 前端控制器
 * </p>
 *
 * @author LiuJG344009799@qq.com
 * @since 2019-05-29
 */
@Api(value = "/esRefund",tags = "退（货）款表接口")
@RestController
@RequestMapping("/esRefund")
public class EsRefundController extends BaseController {

    @Reference(version = "${dubbo.application.version}",timeout = 5000)
    private IEsRefundService iesRefundService;

    /**
     * 插入退款单信息
     * @param esRefundForm
     * @return
     */
    @PostMapping
    @ApiOperation(value = "插入退款单信息",notes = "根据前台传入的Form")
    @ApiImplicitParam(name = "esRefundForm",value = "Form表单")
    @ResponseBody
    public ApiResponse insertRefund(EsRefundForm esRefundForm){

        EsRefundDTO esRefundDTO = new EsRefundDTO();

        BeanUtils.copyProperties(esRefundForm,esRefundDTO);

        DubboResult result = iesRefundService.insertRefund(esRefundDTO);

        if(result.isSuccess()){
            return ApiResponse.success(result.getData());
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }

    /**
     * 根据Id删除退款单信息
     * @param id
     * @return
     */
    @DeleteMapping(value = "/{id}")
    @ApiOperation(value = "删除退款单信息",notes = "根据Id删除退款单信息")
    @ApiImplicitParam(name = "id", value = "主键id")
    @ResponseBody
    public ApiResponse deleteRefund(@PathVariable Long id){

        DubboResult result = iesRefundService.deleteRefund(id);

        if(result.isSuccess()){
            return ApiResponse.success(result.getData());
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }

    /**
     * 更新退款单信息
     * @param esRefundForm
     * @return
     */
    @PutMapping
    @ApiOperation(value = "更新退款单信息",notes = "根据Form修改退款单信息")
    @ResponseBody
    @ApiImplicitParam(name = "esRefundForm", value = "Form表单")
    public ApiResponse updateRefund(EsRefundForm esRefundForm){

        EsRefundDTO esRefundDTO = new EsRefundDTO();

        BeanUtils.copyProperties(esRefundForm,esRefundDTO);

        DubboResult result = iesRefundService.updateRefund(esRefundDTO);

        if(result.isSuccess()){
            return ApiResponse.success(result.getData());
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }

    /**
     * 获取退款单信息
     * @param id
     * @return
     */
    @GetMapping(value = "/{id}")
    @ResponseBody
    @ApiOperation(value = "获取退款单信息",notes = "通过id查询退款单信息")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "主键id", required = true, dataType = "long", paramType = "query", example = "1")})
    public ApiResponse<EsRefundVO> getRefund(@PathVariable Long id){

        DubboResult<EsRefundDO> result = iesRefundService.getRefund(id);

        if (result.isSuccess()){
            return ApiResponse.success(result.getData());
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));

    }

    /**
     * 获取退款单信息列表
     * @param esRefundForm
     * @param pageSize
     * @param pageNum
     * @return
     */
    @GetMapping
    @ResponseBody
    @ApiOperation(value = "获取退款单信息列表",notes = "跟据form表单数据提交")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "esRefundGoodsForm", value = "查询Form表单"),
            @ApiImplicitParam(name = "pageSize", value = "页数", dataType = "int", paramType = "query", required = true, example = "10"),
            @ApiImplicitParam(name = "pageNum", value = "页码", dataType = "int", paramType = "query", required = true, example = "1")})
    public ApiResponse getOrderLogList(EsRefundForm esRefundForm, @ApiIgnore @NotEmpty(message = "页数不能为空") int pageSize,
                                       @ApiIgnore @NotEmpty(message = "页码不能为空") int pageNum){

        EsReFundQueryDTO esReFundQueryDTO = new EsReFundQueryDTO();

        BeanUtils.copyProperties(esRefundForm,esReFundQueryDTO);

        DubboPageResult<EsRefundDO> result = iesRefundService.getRefundList(esReFundQueryDTO, pageSize, pageNum);

        if (result.isSuccess()){
            List<EsRefundDO> esRefundDOList = result.getData().getList();
            List<EsRefundVO> EsRefundDOVOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(esRefundDOList)){
                EsRefundDOVOList = esRefundDOList.stream().map(EsRefund ->{
                    EsRefundVO esRefundVO = new EsRefundVO();
                    BeanUtils.copyProperties(EsRefund,esRefundVO);
                    return esRefundVO;
                }).collect(Collectors.toList());
            }
            return ApiResponse.success(DubboPageResult.success(EsRefundDOVOList).getData());
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }

    @PostMapping("/getAfterSaleType")
    @ResponseBody
    @ApiOperation(value = "获取售后类型")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "esRefundGoodsForm", value = "查询Form表单"),
            @ApiImplicitParam(name = "pageSize", value = "页数", dataType = "int", paramType = "query", required = true, example = "10"),
            @ApiImplicitParam(name = "pageNum", value = "页码", dataType = "int", paramType = "query", required = true, example = "1")})
    public ApiResponse getAfterSaleType(String orderSn, Long goodsId){
        AfterSaleTypeVO afterSaleType = new AfterSaleTypeVO();

        // 判断是否可退款
        
        // 判断是否可退货

        // 判断是否可换货

        // 判断是否可维修
        return null;
    }
}

