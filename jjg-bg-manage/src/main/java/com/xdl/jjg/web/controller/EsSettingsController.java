package com.xdl.jjg.web.controller;

import com.xdl.jjg.constant.ApiStatus;
import com.xdl.jjg.model.domain.EsSettingsDO;
import com.xdl.jjg.model.dto.*;
import com.xdl.jjg.model.enums.SettingGroup;
import com.xdl.jjg.model.form.*;
import com.xdl.jjg.model.vo.*;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.response.web.ApiResponse;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.util.JsonUtil;
import com.xdl.jjg.web.service.IEsSettingsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 前端控制器-系统配置
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-06-20 15:30:00
 */
@Api(value = "/esSettings", tags = "系统配置")
@RestController
@RequestMapping("/esSettings")
public class EsSettingsController {

    @Autowired
    private IEsSettingsService iEsSettingsService;

    @GetMapping(value = "/getSecuritySetting")
    @ApiOperation(value = "获取安全设置", response = EsSecuritySettingsVO.class)
    @ResponseBody
    public ApiResponse getSecuritySetting() {
        DubboResult<EsSettingsDO> result = iEsSettingsService.getByCfgGroup(SettingGroup.SECURITY.name());
        if (result.isSuccess()) {
            EsSettingsDO data = result.getData();
            String value = data.getCfgValue();
            EsSecuritySettingsVO vo = new EsSecuritySettingsVO();
            if (!StringUtil.isEmpty(value)) {
                vo = JsonUtil.jsonToObject(value, EsSecuritySettingsVO.class);
            }
            return ApiResponse.success(vo);
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @PutMapping(value = "/updateSecuritySetting")
    @ApiOperation(value = "修改安全设置")
    @ResponseBody
    public ApiResponse updateSecuritySetting(@Valid @RequestBody @ApiParam(name = "安全设置form对象", value = "form") EsSecuritySettingsForm form) {
        EsSecuritySettingsDTO esSecuritySettingsDTO = new EsSecuritySettingsDTO();
        BeanUtil.copyProperties(form, esSecuritySettingsDTO);
        DubboResult result = iEsSettingsService.updateSecuritySetting(SettingGroup.SECURITY, esSecuritySettingsDTO);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @PutMapping(value = "/saveOrderSetting")
    @ApiOperation(value = "保存订单设置")
    @ResponseBody
    public ApiResponse saveOrderSetting(@Valid @RequestBody @ApiParam(name = "订单设置form对象", value = "form") EsOrderSettingForm form) {
        EsOrderSettingDTO orderSettingDTO = new EsOrderSettingDTO();
        BeanUtil.copyProperties(form, orderSettingDTO);
        DubboResult result = iEsSettingsService.saveOrderSetting(SettingGroup.TRADE, orderSettingDTO);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @GetMapping(value = "/OrderSetting")
    @ApiOperation(value = "获取订单设置", response = EsOrderSettingVO.class)
    @ResponseBody
    public ApiResponse OrderSetting() {
        DubboResult<EsSettingsDO> result = iEsSettingsService.getByCfgGroup(SettingGroup.TRADE.name());
        if (result.isSuccess()) {
            EsSettingsDO data = result.getData();
            String value = data.getCfgValue();
            EsOrderSettingVO vo = new EsOrderSettingVO();
            if (!StringUtil.isEmpty(value)) {
                vo = JsonUtil.jsonToObject(value, EsOrderSettingVO.class);
            }
            return ApiResponse.success(vo);
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation("静态页地址参数保存")
    @PostMapping(value = "/saveStaticPageAddress")
    @ResponseBody
    public ApiResponse saveStaticPageAddress(@Valid EsPageSettingForm form) {
        EsPageSettingDTO dto = new EsPageSettingDTO();
        BeanUtil.copyProperties(form, dto);
        DubboResult result = iEsSettingsService.saveStaticPageAddress(SettingGroup.PAGE, dto);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @GetMapping(value = "/getStaticPageAddress")
    @ApiOperation(value = "查询静态页地址", response = EsPageSettingVO.class)
    @ResponseBody
    public ApiResponse getStaticPageAddress() {
        DubboResult<EsSettingsDO> result = iEsSettingsService.getByCfgGroup(SettingGroup.PAGE.name());
        if (result.isSuccess()) {
            EsSettingsDO data = result.getData();
            String value = data.getCfgValue();
            EsPageSettingVO vo = new EsPageSettingVO();
            if (!StringUtil.isEmpty(value)) {
                vo = JsonUtil.jsonToObject(value, EsPageSettingVO.class);
            }
            return ApiResponse.success(vo);
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @PutMapping(value = "/saveClearingCycle")
    @ApiOperation(value = "保存结算周期设置")
    @ResponseBody
    public ApiResponse saveClearingCycle(@RequestBody @ApiParam(name = "结算周期设置form对象", value = "form") EsClearingCycleSettingsForm form) {
        EsClearingCycleSettingsDTO dto = new EsClearingCycleSettingsDTO();
        BeanUtil.copyProperties(form, dto);
        DubboResult result = iEsSettingsService.saveClearingCycle(SettingGroup.CLEARING, dto);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @GetMapping(value = "/getClearingCycle")
    @ApiOperation(value = "查询结算周期设置", response = EsClearingCycleVO.class)
    @ResponseBody
    public ApiResponse getClearingCycle() {
        DubboResult<EsSettingsDO> result = iEsSettingsService.getByCfgGroup(SettingGroup.CLEARING.name());
        if (result.isSuccess()) {
            EsSettingsDO data = result.getData();
            String value = data.getCfgValue();
            List<EsClearingCycleVO> list = new ArrayList<>();
            if (!StringUtil.isEmpty(value)) {
                list = JsonUtil.jsonToList(value, EsClearingCycleVO.class);
            }
            return ApiResponse.success(list);
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @PutMapping(value = "/saveSite")
    @ApiOperation(value = "保存站点设置")
    @ResponseBody
    public ApiResponse saveSite(@RequestBody @ApiParam(name = "站点设置form对象", value = "form") EsSiteForm form) {
        EsSiteDTO dto = new EsSiteDTO();
        BeanUtil.copyProperties(form, dto);
        DubboResult result = iEsSettingsService.saveSite(SettingGroup.SITE, dto);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @GetMapping(value = "/getSite")
    @ApiOperation(value = "查询站点设置", response = EsSiteVO.class)
    @ResponseBody
    public ApiResponse getSite() {
        DubboResult<EsSettingsDO> result = iEsSettingsService.getByCfgGroup(SettingGroup.SITE.name());
        if (result.isSuccess()) {
            EsSettingsDO data = result.getData();
            String value = data.getCfgValue();
            EsSiteVO vo = new EsSiteVO();
            if (!StringUtil.isEmpty(value)) {
                vo = JsonUtil.jsonToObject(value, EsSiteVO.class);
            }
            return ApiResponse.success(vo);
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

}
