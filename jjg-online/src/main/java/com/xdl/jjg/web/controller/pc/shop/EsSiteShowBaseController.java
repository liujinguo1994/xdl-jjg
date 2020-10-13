package com.xdl.jjg.web.controller.pc.shop;

import com.shopx.common.model.result.ApiPageResponse;
import com.shopx.common.model.result.ApiResponse;
import com.shopx.common.model.result.DubboResult;
import com.shopx.common.util.JsonUtil;
import com.shopx.common.web.BaseController;
import com.shopx.system.api.model.domain.EsSettingsDO;
import com.shopx.system.api.model.enums.SettingGroup;
import com.shopx.system.api.service.IEsSettingsService;
import com.shopx.trade.api.model.domain.vo.SiteSettingVO;
import com.shopx.trade.web.constant.ApiStatus;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


/**
 * <p>
 *  站点信息首页展示控制器
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2020-02-20 14:20:15
 */
@Api(value = "/siteShow",tags = "站点信息首页展示")
@RestController
@RequestMapping("/siteShow")
public class EsSiteShowBaseController extends BaseController {

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsSettingsService settingManager;



    @ApiOperation(value = "站点信息首页展示", response = SiteSettingVO.class)
    @GetMapping(value = "/siteShow")
    @ResponseBody
    public ApiResponse getSiteSetting() {

        DubboResult<EsSettingsDO> result = settingManager.getByCfgGroup(SettingGroup.SITE.toString());
        if (result.isSuccess()) {
            SiteSettingVO esSettingsVO = JsonUtil.jsonToObject(result.getData().getCfgValue(), SiteSettingVO.class);
            return ApiPageResponse.success(esSettingsVO);
        } else {
            return ApiPageResponse.fail(ApiStatus.wrapperException(result));
        }

    }


}

