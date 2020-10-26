package com.xdl.jjg.web.controller.pc.member;

import com.xdl.jjg.web.controller.BaseController;
import io.swagger.annotations.Api;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author LBW 981087977@qq.com
 * @since 2019-12-11 09:28:26
 */
@Api(value = "/complaintReasonConfig",tags = "投诉原因接口")
@RestController
@RequestMapping("/complaintReasonConfig")
public class EsComplaintReasonConfigController extends BaseController {

    @Reference(version = "${dubbo.application.version}", timeout = 5000, check = false)
    private IEsComplaintReasonConfigService complaintReasonConfigService;

//    @PostMapping
//    @ResponseBody
//    @ApiOperation(value = "新增信息", notes = "根据form表单数据提交")
//    @ApiImplicitParam(name = "complaintReasonConfigForm", value = "Form表单")
//    public ApiResponse addComplaintReasonConfig(@RequestBody @Valid EsComplaintReasonConfigForm complaintReasonConfigForm) {
//        EsComplaintReasonConfigDTO complaintReasonConfigDTO = new EsComplaintReasonConfigDTO();
//        BeanUtil.copyProperties(complaintReasonConfigForm, complaintReasonConfigDTO);
//        DubboResult result = complaintReasonConfigService.insertComplaintReasonConfig(complaintReasonConfigDTO);
//        if (result.isSuccess()) {
//            return ApiResponse.success();
//        } else {
//            return ApiResponse.fail(ApiStatus.wrapperException(result));
//        }
//    }
//
//    @PutMapping(value = "/{id}")
//    @ResponseBody
//    @ApiOperation(value = "编辑信息", notes = "根据form表单数据提交")
//    @ApiImplicitParam(name = "complaintReasonConfigForm", value = "Form表单")
//    public ApiResponse editComplaintReasonConfig(@RequestBody @Valid EsComplaintReasonConfigForm complaintReasonConfigForm, @PathVariable Long id) {
//        EsComplaintReasonConfigDTO complaintReasonConfigDTO = new EsComplaintReasonConfigDTO();
//        BeanUtil.copyProperties(complaintReasonConfigForm, complaintReasonConfigDTO);
//        DubboResult result = complaintReasonConfigService.updateComplaintReasonConfig(complaintReasonConfigDTO, id);
//        if (result.isSuccess()) {
//            return ApiResponse.success();
//        } else {
//            return ApiResponse.fail(ApiStatus.wrapperException(result));
//        }
//    }
//
//    @DeleteMapping(value = "/{id}")
//    @ResponseBody
//    @ApiOperation(value = "删除信息")
//    @ApiImplicitParam(name = "id", value = "主键id")
//    public ApiResponse removeComplaintReasonConfig(@PathVariable Long id) {
//        DubboResult result = complaintReasonConfigService.deleteComplaintReasonConfig(id);
//        if (result.isSuccess()) {
//            return ApiResponse.success();
//        } else {
//            return ApiResponse.fail(ApiStatus.wrapperException(result));
//        }
//    }

//    @GetMapping(value = "/{id}")
//    @ResponseBody
//    @ApiOperation(value = "获取信息", notes = "根据ID获取信息")
//    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "主键id", required = true, dataType = "long", paramType = "query", example = "1")})
//    public ApiResponse<EsComplaintReasonConfigVO> searchComplaintReasonConfig(@PathVariable Long id) {
//        DubboResult result = complaintReasonConfigService.getComplaintReasonConfig(id);
//        if (result.isSuccess()) {
//            EsComplaintReasonConfigVO complaintReasonConfigVO = new EsComplaintReasonConfigVO();
//            BeanUtil.copyProperties(result.getData(), complaintReasonConfigVO);
//            return ApiResponse.success(complaintReasonConfigVO);
//        } else {
//            return ApiResponse.fail(ApiStatus.wrapperException(result));
//        }
//    }

//    @GetMapping("/getComplaintReasonConfigList")
//    @ResponseBody
//    @ApiOperation(value = "获取列表", notes = "根据form表单数据查询")
//    public ApiResponse<EsComplaintReasonConfigVO> searchComplaintReasonConfigList() {
//        DubboPageResult result = complaintReasonConfigService.getComplaintReasonConfigList();
//        List<EsComplaintReasonConfigVO> complaintReasonConfigVOList = new ArrayList<>();
//        if (result.isSuccess()) {
//            List<EsComplaintReasonConfigDO> complaintReasonConfigDOList = result.getData().getList();
//            if (CollectionUtils.isNotEmpty(complaintReasonConfigDOList)) {
//                complaintReasonConfigVOList = complaintReasonConfigDOList.stream().map(complaintReasonConfigDO -> {
//                    EsComplaintReasonConfigVO complaintReasonConfigVO = new EsComplaintReasonConfigVO();
//                    BeanUtil.copyProperties(complaintReasonConfigDO, complaintReasonConfigVO);
//                    return complaintReasonConfigVO;
//                }).collect(Collectors.toList());
//            }
//            return ApiResponse.success(complaintReasonConfigVOList);
//        } else {
//            return ApiResponse.fail(ApiStatus.wrapperException(result));
//        }
//    }
}

