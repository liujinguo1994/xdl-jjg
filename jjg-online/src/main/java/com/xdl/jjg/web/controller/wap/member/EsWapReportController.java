package com.xdl.jjg.web.controller.wap.member;

import com.shopx.common.model.result.ApiPageResponse;
import com.shopx.common.model.result.ApiResponse;
import com.shopx.common.model.result.DubboPageResult;
import com.shopx.common.model.result.DubboResult;
import com.shopx.common.util.BeanUtil;
import com.shopx.member.api.model.domain.EsReportDO;
import com.shopx.member.api.model.domain.dto.EsComrImglDTO;
import com.shopx.member.api.model.domain.dto.EsReportDTO;
import com.shopx.member.api.model.domain.dto.ReportQueryParam;
import com.shopx.member.api.model.domain.vo.EsReportVO;
import com.shopx.member.api.service.IEsReportService;
import com.shopx.trade.web.constant.ApiStatus;
import com.shopx.trade.web.request.EsWapReportForm;
import com.shopx.trade.web.request.query.EsWapReportQueryForm;
import com.shopx.trade.web.shiro.oath.ShiroKit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 移动端-举报 前端控制器
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-03-16 09:28:26
 */
@Api(value = "/wap/member/report",tags = "移动端-举报")
@RestController
@RequestMapping("/wap/member/report")
public class EsWapReportController {

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsReportService reportService;

    @ApiOperation(value = "提交举报信息")
    @PostMapping("/addReport")
    @ResponseBody
    public ApiResponse addReport(@RequestBody @Valid EsWapReportForm form) {
        EsReportDTO dto=new EsReportDTO();
        BeanUtil.copyProperties(form,dto);
        dto.setMemberId(ShiroKit.getUser().getId());
        dto.setMemberName(ShiroKit.getUser().getName());
        dto.setComrImglDTOList(openImg(form.getImgStr()));
        DubboResult<EsReportDO> result = reportService.insertReport(dto);
        if (result !=null){
            return ApiResponse.success(result);
        }else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    private List<EsComrImglDTO> openImg(String imgStr){
        String[] array = imgStr.split(",");
        List<EsComrImglDTO> list = new ArrayList<>();
        for(int i = 0; i < array.length; i++){
            EsComrImglDTO comrImglDTO = new EsComrImglDTO();
            comrImglDTO.setOriginal(array[i]);
            comrImglDTO.setSort(i);
            comrImglDTO.setType(0);
            list.add(comrImglDTO);
        }
        return list;
    }

    @ApiOperation(value = "分页查询会员举报列表",response = EsReportVO.class)
    @GetMapping("/getReportList")
    @ResponseBody
    public ApiResponse getReportList(EsWapReportQueryForm form) {
        ReportQueryParam param = new ReportQueryParam();
        param.setMemberId(ShiroKit.getUser().getId());
        param.setState(form.getState());
        DubboPageResult<EsReportDO> result = reportService.getReportList(param,form.getPageSize(),form.getPageNum());
        if (result.isSuccess()) {
            List<EsReportDO> doList = result.getData().getList();
            List<EsReportVO> voList = BeanUtil.copyList(doList, EsReportVO.class);
            return ApiPageResponse.pageSuccess(result.getData().getTotal(), voList);
        }else {
            return ApiPageResponse.fail(ApiStatus.wrapperException(result));
        }
    }
}

