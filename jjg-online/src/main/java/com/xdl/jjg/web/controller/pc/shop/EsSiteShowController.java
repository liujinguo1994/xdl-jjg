package com.xdl.jjg.web.controller.pc.shop;/**
 * @author wangaf
 * @date 2020/1/9 10:37
 **/

import com.shopx.common.model.result.ApiResponse;
import com.shopx.common.model.result.DubboResult;
import com.shopx.common.util.JsonUtil;
import com.shopx.common.web.BaseController;
import com.shopx.system.api.model.domain.EsSettingsDO;
import com.shopx.system.api.model.domain.EsSiteSettingDO;
import com.shopx.system.api.model.enums.SettingGroup;
import com.shopx.system.api.service.IEsSettingsService;
import com.shopx.trade.web.constant.ApiStatus;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 @Author wangaf 826988665@qq.com
 @Date 2020/1/9
 @Version V1.0
 **/
@Api(value = "/site-show",tags = "站点展示")
@RestController
@RequestMapping("/site-show")
public class EsSiteShowController extends BaseController {

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsSettingsService esSettingsService;

    @GetMapping
    @ResponseBody
    @ApiOperation(value = "获取站点设置", response = EsSettingsDO.class)
    public ApiResponse getSiteSetting() {
        DubboResult<EsSettingsDO> result = esSettingsService.getByCfgGroup(SettingGroup.SITE.name());
        if(result.isSuccess()){
            EsSettingsDO esSettingsDO = result.getData();
            String siteJson = esSettingsDO.getCfgValue();
            EsSiteSettingDO  esSiteSettingDO = JsonUtil.jsonToObject(siteJson, EsSiteSettingDO.class);
            logger.info("站点设置:"+siteJson);
            logger.info("站点设置:"+esSiteSettingDO.getSiteon());
            return ApiResponse.success(esSiteSettingDO);
        }else{
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }
}
