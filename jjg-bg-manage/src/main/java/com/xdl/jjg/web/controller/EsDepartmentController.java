package com.xdl.jjg.web.controller;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.xdl.jjg.constant.ApiStatus;
import com.xdl.jjg.model.domain.EsAdminUserDO;
import com.xdl.jjg.model.domain.EsDepartmentDO;
import com.xdl.jjg.model.dto.EsDepartmentDTO;
import com.xdl.jjg.model.form.EsDepartmentForm;
import com.xdl.jjg.model.vo.EsDepartmentVO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.response.web.ApiResponse;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.web.service.IEsAdminUserService;
import com.xdl.jjg.web.service.IEsDepartmentService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 前端控制器-部门
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
@RestController
@RequestMapping("/esDepartment")
@Api(value = "/esDepartment", tags = "部门")
public class EsDepartmentController {

    @Autowired
    private IEsDepartmentService iEsDepartmentService;

    @Autowired
    private IEsAdminUserService iesAdminUserService;

    @ApiOperation(value = "添加部门")
    @PostMapping(value = "/insertEsDepartment")
    @ResponseBody
    public ApiResponse insertEsDepartment(@Valid @RequestBody @ApiParam(name = "部门form对象", value = "form") EsDepartmentForm form) {
        EsDepartmentDTO esDepartmentDTO = new EsDepartmentDTO();
        BeanUtil.copyProperties(form, esDepartmentDTO);
        DubboResult result = iEsDepartmentService.insertDepartment(esDepartmentDTO);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "修改部门")
    @PutMapping(value = "/updateEsDepartment/{id}")
    @ResponseBody
    public ApiResponse updateEsDepartment(@Valid @RequestBody @ApiParam(name = "部门form对象", value = "form") EsDepartmentForm form, @PathVariable Long id) {
        EsDepartmentDTO esDepartmentDTO = new EsDepartmentDTO();
        BeanUtil.copyProperties(form, esDepartmentDTO);
        esDepartmentDTO.setId(id);
        DubboResult result = iEsDepartmentService.updateDepartment(esDepartmentDTO);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "删除部门")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键id", required = true, dataType = "long", paramType = "path", example = "1")
    })
    @DeleteMapping(value = "/deleteEsDepartment/{id}")
    @ResponseBody
    public ApiResponse deleteEsDepartment(@PathVariable Long id) {
        //判断是否关联管理员
        DubboResult<List<EsAdminUserDO>> dubboResult = iesAdminUserService.getByDepartmentId(id);
        List<EsAdminUserDO> data = dubboResult.getData();
        if (data.size() > 0) {
            return ApiResponse.fail(10087, "该部门已关联管理员，不能删除");
        }
        DubboResult result = iEsDepartmentService.deleteDepartment(id);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "部门下拉框", response = EsDepartmentVO.class)
    @GetMapping(value = "/getEsDepartmentComboBox")
    @ResponseBody
    public ApiResponse getEsDepartmentComboBox() {
        DubboPageResult<EsDepartmentDO> result = iEsDepartmentService.getEsDepartmentComboBox();
        if (result.isSuccess()) {
            List<EsDepartmentDO> data = result.getData().getList();
            List<EsDepartmentVO> esDepartmentVOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(data)) {
                //属性复制
                esDepartmentVOList = data.stream().map(esDepartmentDO -> {
                    EsDepartmentVO esDepartmentVO = new EsDepartmentVO();
                    BeanUtil.copyProperties(esDepartmentDO, esDepartmentVO);
                    return esDepartmentVO;
                }).collect(Collectors.toList());
            }
            return ApiResponse.success(esDepartmentVOList);
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "查询某部门下的子部门列表", response = EsDepartmentVO.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "parentId", value = "父id，顶级为0", required = true, dataType = "long", paramType = "path", example = "1")})
    @GetMapping(value = "/{parentId}/children")
    @ResponseBody
    public ApiResponse getChildren(@PathVariable("parentId") Long parentId) {
        DubboResult<List<EsDepartmentDO>> result = iEsDepartmentService.getChildren(parentId);
        if (result.isSuccess()) {
            List<EsDepartmentDO> data = result.getData();
            List<EsDepartmentVO> esDepartmentVOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(data)) {
                //属性复制
                esDepartmentVOList = data.stream().map(esDepartmentDO -> {
                    EsDepartmentVO esDepartmentVO = new EsDepartmentVO();
                    BeanUtil.copyProperties(esDepartmentDO, esDepartmentVO);
                    return esDepartmentVO;
                }).collect(Collectors.toList());
            }
            return ApiResponse.success(esDepartmentVOList);
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }
}