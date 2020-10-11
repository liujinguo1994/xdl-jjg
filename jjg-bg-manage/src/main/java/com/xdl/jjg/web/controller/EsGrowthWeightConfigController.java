package com.xdl.jjg.web.controller;

import com.shopx.common.model.result.ApiResponse;
import com.shopx.common.model.result.DubboPageResult;
import com.shopx.common.model.result.DubboResult;
import com.shopx.common.util.BeanUtil;
import com.shopx.member.api.model.domain.EsGrowthWeightConfigDO;
import com.shopx.member.api.model.domain.dto.EsGrowthWeightConfigListDTO;
import com.shopx.member.api.model.domain.vo.EsGrowthWeightConfigVO;
import com.shopx.member.api.service.IEsGrowthWeightConfigService;
import com.shopx.system.web.constant.ApiStatus;
import com.shopx.system.web.request.EsGrowthWeightConfigForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器-成长值权重设置
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-06-24 15:46:00
 */
@Api(value = "/esGrowthWeightConfig",tags = "成长值权重设置")
@RestController
@RequestMapping("/esGrowthWeightConfig")
public class EsGrowthWeightConfigController {

    @Autowired
    private IEsGrowthWeightConfigService growthWeightConfigService;

    @ApiOperation(value = "保存成长值权重设置")
    @PutMapping(value = "/saveGrowthWeightConfig")
    @ResponseBody
    public ApiResponse saveGrowthWeightConfig(@RequestBody @ApiParam(name="成长值权重设置form对象",value="form") EsGrowthWeightConfigForm form){
        EsGrowthWeightConfigListDTO dto = new EsGrowthWeightConfigListDTO();
        BeanUtil.copyProperties(form,dto);
        DubboResult result = growthWeightConfigService.insertGrowthWeightConfig(dto);
        if (result.isSuccess()) {
            return ApiResponse.success();
        }else{
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @GetMapping(value = "/getGrowthWeightConfig")
    @ResponseBody
    @ApiOperation(value = "获取成长值权重设置",response = EsGrowthWeightConfigVO.class)
    public ApiResponse getGrowthWeightConfig(){
        DubboPageResult<EsGrowthWeightConfigDO> result = growthWeightConfigService.getGrowthWeightConfigList();
        if (result.isSuccess()) {
            List<EsGrowthWeightConfigDO> data = result.getData().getList();
            List<EsGrowthWeightConfigVO> voList = BeanUtil.copyList(data, EsGrowthWeightConfigVO.class);
            return ApiResponse.success(voList);
        }else{
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }
}
