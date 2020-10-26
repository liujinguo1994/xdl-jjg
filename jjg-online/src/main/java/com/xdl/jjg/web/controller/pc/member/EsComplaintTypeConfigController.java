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
@Api(value = "/complaintTypeConfig",tags = "交易纠纷相关接口")
@RestController
@RequestMapping("/complaintTypeConfig")
public class EsComplaintTypeConfigController extends BaseController {

    @Reference(version = "${dubbo.application.version}", timeout = 5000, check = false)
    private IEsComplaintTypeConfigService complaintTypeConfigService;

//    @PostMapping
//    @ResponseBody
//    @ApiOperation(value = "新增信息", notes = "根据form表单数据提交")
//    @ApiImplicitParam(name = "complaintTypeConfigForm", value = "Form表单")
//    public ApiResponse addComplaintTypeConfig(@RequestBody @Valid EsComplaintTypeConfigForm complaintTypeConfigForm) {
//        EsComplaintTypeConfigDTO complaintTypeConfigDTO = new EsComplaintTypeConfigDTO();
//        BeanUtil.copyProperties(complaintTypeConfigForm, complaintTypeConfigDTO);
//        DubboResult result = complaintTypeConfigService.insertComplaintTypeConfig(complaintTypeConfigDTO);
//        if (result.isSuccess()) {
//            return ApiResponse.success();
//        } else {
//            return ApiResponse.fail(ApiStatus.wrapperException(result));
//        }
//    }

//    @PutMapping(value = "/{id}")
//    @ResponseBody
//    @ApiOperation(value = "编辑信息", notes = "根据form表单数据提交")
//    @ApiImplicitParam(name = "complaintTypeConfigForm", value = "Form表单")
//    public ApiResponse editComplaintTypeConfig(@RequestBody @Valid EsComplaintTypeConfigForm complaintTypeConfigForm, @PathVariable Long id) {
//        EsComplaintTypeConfigDTO complaintTypeConfigDTO = new EsComplaintTypeConfigDTO();
//        BeanUtil.copyProperties(complaintTypeConfigForm, complaintTypeConfigDTO);
//        DubboResult result = complaintTypeConfigService.updateComplaintTypeConfig(complaintTypeConfigDTO, id);
//        if (result.isSuccess()) {
//            return ApiResponse.success();
//        } else {
//            return ApiResponse.fail(ApiStatus.wrapperException(result));
//        }
//    }

//    @DeleteMapping(value = "/{id}")
//    @ResponseBody
//    @ApiOperation(value = "删除信息")
//    @ApiImplicitParam(name = "id", value = "主键id")
//    public ApiResponse removeComplaintTypeConfig(@PathVariable Long id) {
//        DubboResult result = complaintTypeConfigService.deleteComplaintTypeConfig(id);
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
//    public ApiResponse<EsComplaintTypeConfigVO> searchComplaintTypeConfig(@PathVariable Long id) {
//        DubboResult result = complaintTypeConfigService.getComplaintTypeConfig(id);
//        if (result.isSuccess()) {
//            EsComplaintTypeConfigVO complaintTypeConfigVO = new EsComplaintTypeConfigVO();
//            BeanUtil.copyProperties(result.getData(), complaintTypeConfigVO);
//            return ApiResponse.success(complaintTypeConfigVO);
//        } else {
//            return ApiResponse.fail(ApiStatus.wrapperException(result));
//        }
//    }

//    @GetMapping
//    @ResponseBody
//    @ApiOperation(value = "获取列表", notes = "根据form表单数据查询")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "complaintTypeConfigQueryForm", value = "查询Form表单"),
//            @ApiImplicitParam(name = "pageSize", value = "页数", dataType = "int", paramType = "query", required = true, example = "10"),
//            @ApiImplicitParam(name = "pageNum", value = "页码", dataType = "int", paramType = "query", required = true, example = "1")})
//    public ApiResponse<EsComplaintTypeConfigVO> searchComplaintTypeConfigList(@RequestBody @Valid EsComplaintTypeConfigQueryForm complaintTypeConfigQueryForm,
//                                                                              @NotEmpty(message = "页数不能为空") int pageSize,
//                                                                              @NotEmpty(message = "页码不能为空") int pageNum) {
//        EsComplaintTypeConfigDTO complaintTypeConfigDTO = new EsComplaintTypeConfigDTO();
//        BeanUtil.copyProperties(complaintTypeConfigQueryForm, complaintTypeConfigDTO);
//        DubboPageResult result = complaintTypeConfigService.getComplaintTypeConfigList(complaintTypeConfigDTO, pageSize, pageNum);
//        List<EsComplaintTypeConfigVO> complaintTypeConfigVOList = new ArrayList<>();
//        if (result.isSuccess()) {
//            List<EsComplaintTypeConfigDO> complaintTypeConfigDOList = result.getData().getList();
//            if (CollectionUtils.isNotEmpty(complaintTypeConfigDOList)) {
//                complaintTypeConfigVOList = complaintTypeConfigDOList.stream().map(complaintTypeConfigDO -> {
//                    EsComplaintTypeConfigVO complaintTypeConfigVO = new EsComplaintTypeConfigVO();
//                    BeanUtil.copyProperties(complaintTypeConfigDO, complaintTypeConfigVO);
//                    return complaintTypeConfigVO;
//                }).collect(Collectors.toList());
//            }
//            return ApiResponse.success(complaintTypeConfigVOList);
//        } else {
//            return ApiResponse.fail(ApiStatus.wrapperException(result));
//        }
//    }

//    @GetMapping("/getComplaintTypeConfigList")
//    @ResponseBody
//    @ApiOperation(value = "获取投诉类型列表", notes = "获取投诉类型列表")
//    public ApiResponse<EsComplaintTypeConfigVO> searchComplaintTypeConfigList() {
//        ShiroUser user = ShiroKit.getUser();
//        if (null == user) {
//            return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
//        }
//        DubboPageResult result = this.complaintTypeConfigService.getComplaintTypeConfigListInfo();
//        List<EsComplaintTypeConfigVO> complaintTypeConfigVOList = new ArrayList<>();
//        if (result.isSuccess()) {
//            List<EsComplaintTypeConfigDO> complaintTypeConfigDOList = result.getData().getList();
//            if (CollectionUtils.isNotEmpty(complaintTypeConfigDOList)) {
//                complaintTypeConfigVOList = complaintTypeConfigDOList.stream().map(complaintTypeConfigDO -> {
//                    EsComplaintTypeConfigVO complaintTypeConfigVO = new EsComplaintTypeConfigVO();
//                    BeanUtil.copyProperties(complaintTypeConfigDO, complaintTypeConfigVO);
//                    return complaintTypeConfigVO;
//                }).collect(Collectors.toList());
//            }
//            return ApiResponse.success(complaintTypeConfigVOList);
//        } else {
//            return ApiResponse.fail(ApiStatus.wrapperException(result));
//        }
//    }
}

