package com.xdl.jjg.web.controller.pc.promotion;


import com.jjg.trade.model.vo.TimeLineVO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.web.ApiResponse;
import com.xdl.jjg.web.controller.BaseController;
import com.xdl.jjg.web.service.IEsSeckillRangeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
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

