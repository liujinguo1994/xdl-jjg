package com.xdl.jjg.web.controller.pc.shop;


import com.jjg.system.model.domain.EsSettingsDO;
import com.jjg.system.model.enums.SettingGroup;
import com.jjg.trade.model.vo.SiteSettingVO;
import com.xdl.jjg.constant.ApiStatus;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.response.web.ApiPageResponse;
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

    @Autowired
    private SettingsService settingManager;



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

