package com.xdl.jjg.web.controller.wap.promotion;

import com.shopx.common.model.result.ApiPageResponse;
import com.shopx.common.model.result.ApiResponse;
import com.shopx.common.model.result.DubboPageResult;
import com.shopx.common.model.result.DubboResult;
import com.shopx.common.util.BeanUtil;
import com.shopx.trade.api.model.domain.EsSeckillApplyDO;
import com.shopx.trade.api.model.domain.EsSeckillTimetableDO;
import com.shopx.trade.api.model.domain.dto.EsSeckillTimelineGoodsDTO;
import com.shopx.trade.api.model.domain.vo.EsSeckillApplyVO;
import com.shopx.trade.api.model.domain.vo.EsSeckillTimetableVO;
import com.shopx.trade.api.service.IEsSeckillApplyService;
import com.shopx.trade.web.request.EsSeckillTimelineGoodsForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Api(value = "/wap/promotion/seckill",tags = "移动端-秒杀相关接口")
@RestController
@RequestMapping("/wap/promotion/seckill")
public class EsWapSeckillController {
    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsSeckillApplyService esSeckillApplyService;

    @ApiOperation(value = "获取今日秒杀时刻表")
    @GetMapping(value = "/timetableToday")
    public ApiResponse<List<EsSeckillTimetableVO>> timetableToday() {
        DubboResult<List<EsSeckillTimetableDO>> seckillTimetableToday = esSeckillApplyService.getSeckillTimetableToday();
        if(seckillTimetableToday.isSuccess()){
            return ApiResponse.success(BeanUtil.copyList(seckillTimetableToday.getData(), EsSeckillTimetableVO.class));
        }
        return ApiResponse.fail(seckillTimetableToday.getCode(),seckillTimetableToday.getMsg());
    }

    @ApiOperation(value = "根据条件筛选出符合条件的秒杀商品",response = EsSeckillApplyVO.class)
    @GetMapping(value = "/seckillTimelineGoodsList")
    public ApiResponse seckillTimelineGoodsList(@Valid EsSeckillTimelineGoodsForm esSeckillTimelineGoodsForm){
        EsSeckillTimelineGoodsDTO esSeckillTimelineGoodsDTO = new EsSeckillTimelineGoodsDTO();
        BeanUtil.copyProperties(esSeckillTimelineGoodsForm,esSeckillTimelineGoodsDTO);
        DubboPageResult<EsSeckillApplyDO> esSeckillApplyDODubboPageResult = esSeckillApplyService.seckillTimelineGoodsList(esSeckillTimelineGoodsDTO, esSeckillTimelineGoodsForm.getPageNum(), esSeckillTimelineGoodsForm.getPageSize());
        if(esSeckillApplyDODubboPageResult.isSuccess()){
            return ApiPageResponse.pageSuccess(esSeckillApplyDODubboPageResult.getData().getTotal(),BeanUtil.copyList(esSeckillApplyDODubboPageResult.getData().getList(),EsSeckillApplyVO.class));
        }
        return ApiResponse.fail(esSeckillApplyDODubboPageResult.getCode(),esSeckillApplyDODubboPageResult.getMsg());
    }



}
