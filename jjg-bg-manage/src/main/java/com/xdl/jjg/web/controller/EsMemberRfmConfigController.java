package com.xdl.jjg.web.controller;


import com.xdl.jjg.constant.ApiStatus;
import com.xdl.jjg.model.form.EsRfmConfigForm;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.response.web.ApiResponse;
import com.xdl.jjg.util.BeanUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 前端控制器-会员RFM配置
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
@RestController
@RequestMapping("/esMemberRfmConfig")
@Api(value = "/esMemberRfmConfig", tags = "会员RFM配置")
public class EsMemberRfmConfigController {

    @Autowired
    private IEsMemberRfmConfigService memberRfmConfigService;


    @ApiOperation(value = "保存会员RFM配置")
    @PutMapping(value = "/saveRfmConfig")
    @ResponseBody
    public ApiResponse saveRfmConfig(@RequestBody @ApiParam(name = "RFM配置form对象", value = "form") EsRfmConfigForm form) {
        EsRfmConfigDTO dto = new EsRfmConfigDTO();
        BeanUtil.copyProperties(form, dto);
        DubboResult result = memberRfmConfigService.insertMemberRfmConfig(dto);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @GetMapping(value = "/getRfmConfig")
    @ResponseBody
    @ApiOperation(value = "查询会员RFM配置")
    public ApiResponse getRfmConfig() {
        DubboPageResult<EsMemberRfmConfigDO> result = memberRfmConfigService.getMemberRfmConfigListInfo();
        if (result.isSuccess()) {
            List<EsMemberRfmConfigDO> data = result.getData().getList();
            List<EsMemberRfmConfigVO> voList = BeanUtil.copyList(data, EsMemberRfmConfigVO.class);
            return ApiResponse.success(voList);
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }
}
