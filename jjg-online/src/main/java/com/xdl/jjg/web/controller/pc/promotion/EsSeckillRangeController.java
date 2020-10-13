package com.xdl.jjg.web.controller.pc.promotion;

import com.shopx.common.model.result.ApiResponse;
import com.shopx.common.model.result.DubboPageResult;
import com.shopx.common.web.BaseController;
import com.shopx.trade.api.model.domain.vo.TimeLineVO;
import com.shopx.trade.api.service.IEsSeckillRangeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author LiuJG 344009799@qq.com
 * @since 2019-06-05 09:25:43
 */
@Api(value = "/seckillRange",tags = "秒杀时刻模块接口")
@RestController
@RequestMapping("/seckillRange")
public class EsSeckillRangeController extends BaseController {

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsSeckillRangeService seckillRangeService;

    @ApiOperation(value = "读取秒杀时刻")
    @ResponseBody
    @GetMapping(value = "/timeLine")
    public ApiResponse<TimeLineVO> readTimeLine() throws ParseException {
        DubboPageResult dubboPageResult = seckillRangeService.readTimeList();
        if(dubboPageResult.isSuccess()){
            List<TimeLineVO> list = dubboPageResult.getData().getList();
            return ApiResponse.success(list);
        }else {
            return ApiResponse.fail(dubboPageResult.getCode(),dubboPageResult.getMsg());
        }

    }
}

