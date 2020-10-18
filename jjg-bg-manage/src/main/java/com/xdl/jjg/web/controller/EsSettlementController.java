package com.xdl.jjg.web.controller;

import com.jjg.system.model.form.*;
import com.jjg.trade.model.domain.*;
import com.jjg.trade.model.dto.EsBillDTO;
import com.jjg.trade.model.dto.EsBillDetailDTO;
import com.jjg.trade.model.vo.EsBillDetailVO;
import com.jjg.trade.model.vo.EsBillVO;
import com.jjg.trade.model.vo.EsHeaderVO;
import com.jjg.trade.model.vo.EsSettlementDetailVO;
import com.xdl.jjg.constant.ApiStatus;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.response.web.ApiPageResponse;
import com.xdl.jjg.response.web.ApiResponse;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.web.service.feign.trade.BillService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * <p>
 * 前端控制器-结算
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-06-10
 */
@RestController
@RequestMapping("/esSettlement")
@Api(value = "/esSettlement", tags = "结算")
public class EsSettlementController {

    @Autowired
    private BillService billService;

    /**
     * 分页查询账单列表
     */
    @ApiOperation(value = "分页查询账单列表", response = EsBillVO.class)
    @GetMapping(value = "/getBillList")
    @ResponseBody
    public ApiResponse getBillList(@Valid EsBillQueryForm form) {
        EsBillDTO dto = new EsBillDTO();
        BeanUtil.copyProperties(form, dto);
        DubboPageResult<EsBillDO> result = billService.getBillList(dto, form.getPageSize(), form.getPageNum());
        if (result.isSuccess()) {
            List<EsBillDO> data = result.getData().getList();
            List<EsBillVO> voList = BeanUtil.copyList(data, EsBillVO.class);
            return ApiPageResponse.pageSuccess(result.getData().getTotal(), voList);
        } else {
            return ApiPageResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    /**
     * 分页查询账单详情列表
     */
    @ApiOperation(value = "分页查询账单详情列表", response = EsBillDetailVO.class)
    @GetMapping(value = "/getBillDetail")
    @ResponseBody
    public ApiResponse getBillDetail(@Valid EsBillDetailQueryForm form) {
        EsBillDetailDTO dto = new EsBillDetailDTO();
        BeanUtil.copyProperties(form, dto);
        DubboPageResult<EsBillDetailDO> result = billService.getBillDetail(dto, form.getPageSize(), form.getPageNum());
        if (result.isSuccess()) {
            List<EsBillDetailDO> data = result.getData().getList();
            List<EsBillDetailVO> voList = BeanUtil.copyList(data, EsBillDetailVO.class);
            return ApiPageResponse.pageSuccess(result.getData().getTotal(), voList);
        } else {
            return ApiPageResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    /**
     * 获取结算单头部信息
     */
    @ApiOperation(value = "获取结算单头部信息", response = EsHeaderVO.class)
    @GetMapping(value = "/getSettlementInfo")
    @ResponseBody
    public ApiResponse getSettlementInfo(@Valid EsSettlementHeadQueryForm form) {
        DubboResult result = billService.getSettlementInfo(form.getSettlementId(), form.getType());
        if (result.isSuccess()) {
            EsHeaderDO data = (EsHeaderDO) result.getData();
            EsHeaderVO vo = new EsHeaderVO();
            BeanUtil.copyProperties(data, vo);
            return ApiResponse.success(vo);
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    /**
     * 获取结算单总订单明细
     */
    @ApiOperation(value = "获取结算单总订单明细", response = EsSettlementDetailVO.class)
    @GetMapping(value = "/getSettlementOrderDetail")
    @ResponseBody
    public ApiResponse getSettlementOrderDetail(@Valid EsSettlementDetailQueryForm form) {
        DubboPageResult<EsSettlementDetailDO> result = billService.getSettlementOrderDetail(form.getSettlementId(), form.getType(), form.getPageSize(), form.getPageNum());
        if (result.isSuccess()) {
            List<EsSettlementDetailDO> data = result.getData().getList();
            List<EsSettlementDetailVO> voList = BeanUtil.copyList(data, EsSettlementDetailVO.class);
            return ApiPageResponse.pageSuccess(result.getData().getTotal(), voList);
        } else {
            return ApiPageResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    /**
     * 获取结算单退款订单明细
     */
    @ApiOperation(value = "获取结算单退款订单明细", response = EsSettlementDetailVO.class)
    @GetMapping(value = "/getSettlementRefundOrderDetail")
    @ResponseBody
    public ApiResponse getSettlementRefundOrderDetail(@Valid EsSettlementDetailQueryForm form) {
        DubboPageResult<EsSettlementDetailDO> result = billService.getSettlementRefundOrderDetail(form.getSettlementId(), form.getType(), form.getPageSize(), form.getPageNum());
        if (result.isSuccess()) {
            List<EsSettlementDetailDO> data = result.getData().getList();
            List<EsSettlementDetailVO> voList = BeanUtil.copyList(data, EsSettlementDetailVO.class);
            return ApiPageResponse.pageSuccess(result.getData().getTotal(), voList);
        } else {
            return ApiPageResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    /**
     * 更改结算状态
     */
    @ApiOperation(value = "更改结算状态")
    @PutMapping(value = "/updateStatus")
    @ResponseBody
    public ApiResponse updateStatus(@Valid @RequestBody @ApiParam(name = "更改结算状态form对象", value = "form") EsSettlementUpdateStatusForm form) {
        DubboResult result = billService.updateStatus(form.getSettlementId(), form.getType(), form.getStatus());
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    /**
     * 手动结算
     */
/*    @ApiOperation(value = "手动结算")
    @PutMapping(value = "/manualSettlement")
    @ResponseBody
    public ApiResponse manualSettlement(@Valid @RequestBody @ApiParam(name="手动结算form对象",value="form") EsManualSettlementForm form){
        if (form.getType() == 3 && form.getCompanyId() == null){
            throw new ArgumentException(ErrorCode.COMPANY_ID_IS_NULL.getErrorCode(), "签约公司不能为空");
        }
        DubboResult result = billService.manualSettlement();
        if (result.isSuccess()) {
            return ApiResponse.success();
        }else{
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }*/


    /**
     * 店铺及签约公司导出EXCEL
     */
    @ApiOperation(value = "店铺及签约公司导出EXCEL")
    @GetMapping(value = "/export")
    public ApiResponse export(@Valid EsExportSettlementForm form, HttpServletResponse response) {
        DubboResult<ExcelDO> result = billService.exportExcel(form.getSettlementId(), form.getType());
        if (result.isSuccess()) {
            ExcelDO excelDO = result.getData();

            response.setContentType("application/msexcel;charset=UTF-8");
            try {
                response.setHeader("Content-disposition", "attachment; filename=" + new String(excelDO.getExcelName().getBytes("UTF-8"), "ISO8859-1") + ".xls");
            } catch (UnsupportedEncodingException e) {
                return ApiResponse.fail(101, "设置响应头出错!");
            }
            try {
                OutputStream out = new BufferedOutputStream(response.getOutputStream());
                excelDO.getWorkbook().write(out);
                out.flush();
                out.close();
            } catch (Exception e) {
                return ApiResponse.fail(102, "文档操作错误!");
            }
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

}
