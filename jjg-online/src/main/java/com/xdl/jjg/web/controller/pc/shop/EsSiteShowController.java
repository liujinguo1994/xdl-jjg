package com.xdl.jjg.web.controller.pc.shop;/**
 * @author wangaf
 * @date 2020/1/9 10:37
 **/

import com.jjg.system.model.domain.EsSettingsDO;
import com.jjg.system.model.domain.EsSiteSettingDO;
import com.jjg.system.model.enums.SettingGroup;
import com.xdl.jjg.constant.ApiStatus;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.response.web.ApiResponse;
import com.xdl.jjg.util.JsonUtil;
import com.xdl.jjg.web.controller.BaseController;
import com.xdl.jjg.web.service.feign.system.SettingsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private SettingsService esSettingsService;

    @GetMapping
    @ResponseBody
    @ApiOperation(value = "获取站点设置", response = EsSettingsDO.class)
    public ApiResponse getSiteSetting() {
        DubboResult<EsSettingsDO> result = esSettingsService.getByCfgGroup(SettingGroup.SITE.name());
        if(result.isSuccess()){
            EsSettingsDO esSettingsDO = result.getData();
            String siteJson = esSettingsDO.getCfgValue();
            EsSiteSettingDO esSiteSettingDO = JsonUtil.jsonToObject(siteJson, EsSiteSettingDO.class);
            logger.info("站点设置:"+siteJson);
            logger.info("站点设置:"+esSiteSettingDO.getSiteon());
            return ApiResponse.success(esSiteSettingDO);
        }else{
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }
}
