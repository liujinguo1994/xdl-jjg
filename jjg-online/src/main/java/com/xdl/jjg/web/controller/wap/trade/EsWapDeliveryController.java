package com.xdl.jjg.web.controller.wap.trade;

import com.shopx.common.model.result.ApiResponse;
import com.shopx.common.model.result.DubboPageResult;
import com.shopx.common.util.BeanUtil;
import com.shopx.common.web.BaseController;
import com.shopx.trade.api.model.domain.vo.EsSelfTimeVO;
import com.shopx.trade.api.service.*;
import com.shopx.trade.web.constant.ApiStatus;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器-移动端-我的订单
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2020-02-19
 */
@RestController
@Api(value = "/wap/trade/delivery",tags = "移动端-自提配置")
@RequestMapping("/wap/trade/delivery")
public class EsWapDeliveryController extends BaseController {

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsSelfTimeService iEsSelfTimeService;

    @ApiOperation(value = "根据日期id获取自提时间点列表" ,response = EsSelfTimeVO.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dateId", value = "日期id", required = true, dataType = "long", paramType = "path") })
    @GetMapping(value = "/getSelfTime/{dateId}")
    @ResponseBody
    public ApiResponse<EsSelfTimeVO> getSelfTimeByDateId(@PathVariable Long dateId) {
        DubboPageResult dubboPageResult = iEsSelfTimeService.getSelfTimeListByDateid(dateId);
        if (dubboPageResult.isSuccess()) {
            List list = dubboPageResult.getData().getList();
            List<EsSelfTimeVO> esSelfTimeVOS = BeanUtil.copyList(list, EsSelfTimeVO.class);
            return ApiResponse.success(esSelfTimeVOS);
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(dubboPageResult));
        }
    }

}

