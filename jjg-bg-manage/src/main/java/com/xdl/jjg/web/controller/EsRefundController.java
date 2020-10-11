package com.xdl.jjg.web.controller;


import com.xdl.jjg.constant.ApiStatus;
import com.xdl.jjg.model.form.EsReFundQueryForm;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.response.web.ApiPageResponse;
import com.xdl.jjg.util.BeanUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器-售后订单
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
@RestController
@RequestMapping("/esRefund")
@Api(value="/esRefund", tags="售后订单")
public class EsRefundController {

    @Autowired
    private IEsRefundService refundService;

    @ApiOperation(value = "售后订单分页查询",response = EsRefundVO.class)
    @GetMapping(value = "/getRefundList")
    @ResponseBody
    public ApiResponse getRefundList(EsReFundQueryForm form){
        EsReFundQueryDTO queryDTO = new EsReFundQueryDTO();
        BeanUtil.copyProperties(form,queryDTO);
        DubboPageResult result = refundService.getRefundList(queryDTO, form.getPageSize(), form.getPageNum());
        if(result.isSuccess()){
            List<EsRefundDO> data = result.getData().getList();
            List<EsRefundVO> list =BeanUtil.copyList(data,EsRefundVO.class);
            return ApiPageResponse.pageSuccess(result.getData().getTotal(),list);
        }else{
            return ApiPageResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "获取售后订单详情",response = EsRefundVO.class)
    @GetMapping("/getRefund/{sn}")
    @ApiImplicitParam(name = "sn",value ="维权单号" ,dataType = "String",paramType = "path",required = true)
    @ResponseBody
    public ApiResponse getRefund(@PathVariable("sn") String sn){
        DubboResult result = refundService.getAdminRefundBySn(sn);
        if(result.isSuccess()){
            EsRefundDO data = (EsRefundDO) result.getData();
            EsRefundVO esRefundVO = new EsRefundVO();
            BeanUtil.copyProperties(data,esRefundVO);
            return ApiPageResponse.success(esRefundVO);
        }else{
            return ApiPageResponse.fail(ApiStatus.wrapperException(result));
        }
    }
}
