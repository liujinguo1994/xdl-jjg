package com.xdl.jjg.web.controller;

import com.xdl.jjg.web.service.IEsReceiptHistoryService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器-发票历史
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
@RestController
@RequestMapping("/esReceiptHistory")
@Api(value = "/esReceiptHistory", tags = "发票历史")
public class EsReceiptHistoryController {

    @Autowired
    private IEsReceiptHistoryService receiptHistoryService;


   /* @ApiOperation(value = "分页获取发票历史")
    @GetMapping(value = "/getReceiptHistoryList")
    @ResponseBody
    public ApiResponse getReceiptHistoryList(EsQueryPageForm form){
        EsReceiptHistoryDTO dto = new EsReceiptHistoryDTO();
        DubboPageResult<EsReceiptHistoryDO> result = receiptHistoryService.getReceiptHistoryList(dto, form.getPageSize(), form.getPageNum());
        if(result.isSuccess()){
            List<EsReceiptHistoryDO> data = result.getData().getList();
            List<EsReceiptHistoryVO> list = (List<EsReceiptHistoryVO>) BeanUtil.copyList(data, new EsReceiptHistoryVO().getClass());
            return ApiPageResponse.pageSuccess(result.getData().getTotal(),list);
        }else{
            return ApiPageResponse.fail(ApiStatus.wrapperException(result));
        }
    }*/

   /* @ApiOperation(value = "查询历史发票明细")
    @GetMapping(value = "/getDetail/{historyId}")
    @ApiImplicitParam(name = "historyId", value = "历史发票主键id", dataType = "long", paramType = "path")
    @ResponseBody
    public ApiResponse getDetail(@PathVariable Long historyId){
        receiptHistoryService.getByHistoryId(historyId);

    }*/


}
