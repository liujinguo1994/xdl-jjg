package com.xdl.jjg.web.controller;


import com.jjg.member.model.domain.EsGrowthValueStrategyDO;
import com.jjg.member.model.dto.EsGrowthStrategyDTO;
import com.jjg.member.model.vo.EsGrowthValueStrategyVO;
import com.jjg.system.model.form.EsGrowthValueStrategyForm;
import com.xdl.jjg.constant.ApiStatus;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.response.web.ApiResponse;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.web.service.feign.member.GrowthValueStrategyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 前端控制器-评论及收藏成长值设置
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
@RestController
@RequestMapping("/esGrowthValueStrategy")
@Api(value = "/esGrowthValueStrategy", tags = "评论及收藏成长值设置")
public class EsGrowthValueStrategyController {

    @Autowired
    private GrowthValueStrategyService growthValueStrategyService;

    @ApiOperation(value = "保存评论及收藏成长值设置")
    @PutMapping(value = "/saveGrowthValueStrategy")
    @ResponseBody
    public ApiResponse saveGrowthValueStrategy(@RequestBody @ApiParam(name = "评论及收藏成长值设置form对象", value = "form") EsGrowthValueStrategyForm form) {
        EsGrowthStrategyDTO dto = new EsGrowthStrategyDTO();
        BeanUtil.copyProperties(form, dto);
        DubboResult result = growthValueStrategyService.insertGrowthValueStrategy(dto);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @GetMapping(value = "/getGrowthValueStrategy")
    @ResponseBody
    @ApiOperation(value = "获取评论及收藏成长值设置", response = EsGrowthValueStrategyVO.class)
    public ApiResponse getGrowthValueStrategy() {
        DubboPageResult<EsGrowthValueStrategyDO> result = growthValueStrategyService.getGrowthValueStrategy();
        if (result.isSuccess()) {
            List<EsGrowthValueStrategyDO> data = result.getData().getList();
            List<EsGrowthValueStrategyVO> voList = BeanUtil.copyList(data, EsGrowthValueStrategyVO.class);
            return ApiResponse.success(voList);
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }
}
