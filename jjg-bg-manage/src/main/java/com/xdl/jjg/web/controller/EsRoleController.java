package com.xdl.jjg.web.controller;


import com.jjg.system.model.domain.EsAdminUserDO;
import com.jjg.system.model.domain.EsRoleDO;
import com.jjg.system.model.dto.EsRoleDTO;
import com.jjg.system.model.form.EsRoleForm;
import com.jjg.system.model.form.EsRoleQueryForm;
import com.jjg.system.model.vo.EsRoleVO;
import com.jjg.system.model.vo.Menus;
import com.xdl.jjg.constant.ApiStatus;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.response.web.ApiPageResponse;
import com.xdl.jjg.response.web.ApiResponse;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.util.JsonUtil;
import com.xdl.jjg.web.service.IEsAdminUserService;
import com.xdl.jjg.web.service.IEsRoleService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


/**
 * <p>
 * 前端控制器-角色管理
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
@RestController
@RequestMapping("/esRole")
@Api(value = "/esRole", tags = "角色管理")
public class EsRoleController {

    @Autowired
    private IEsRoleService iesRoleService;

    @Autowired
    private IEsAdminUserService iesAdminUserService;

    @ApiOperation(value = "添加角色")
    @PostMapping(value = "/insertEsRole")
    @ResponseBody
    public ApiResponse insertEsRole(@Valid @RequestBody @ApiParam(name = "角色form对象", value = "form") EsRoleForm form) {
        EsRoleDTO esRoleDTO = new EsRoleDTO();
        BeanUtil.copyProperties(form, esRoleDTO);
        DubboResult result = iesRoleService.insertEsRole(esRoleDTO);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "根据id查询角色", response = EsRoleVO.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "角色id", required = true, dataType = "long", paramType = "path", example = "1")
    })
    @GetMapping(value = "/getEsRole/{id}")
    @ResponseBody
    public ApiResponse getEsRole(@PathVariable Long id) {
        DubboResult<EsRoleDO> result = iesRoleService.getEsRole(id);
        if (result.isSuccess()) {
            EsRoleDO esRoleDO = result.getData();
            EsRoleVO esRoleVO = new EsRoleVO();
            BeanUtil.copyProperties(esRoleDO, esRoleVO);
            List<Menus> esMenuList = JsonUtil.jsonToList(esRoleVO.getAuthIds(), Menus.class);
            esRoleVO.setMenus(esMenuList);
            return ApiResponse.success(esRoleVO);
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "修改角色")
    @PutMapping(value = "/updateEsRole/{id}")
    @ResponseBody
    public ApiResponse updateEsRole(@Valid @RequestBody @ApiParam(name = "角色form对象", value = "form") EsRoleForm form, @PathVariable Long id) {
        EsRoleDTO esRoleDTO = new EsRoleDTO();
        BeanUtil.copyProperties(form, esRoleDTO);
        esRoleDTO.setId(id);
        DubboResult result = iesRoleService.updateEsRole(esRoleDTO);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "删除角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "要删除的角色id", required = true, dataType = "long", paramType = "path", example = "1")
    })
    @DeleteMapping(value = "/deleteEsRole/{id}")
    @ResponseBody
    public ApiResponse deleteEsRole(@PathVariable Long id) {
        //判断是否关联管理员
        DubboPageResult<EsAdminUserDO> pageResult = iesAdminUserService.getByRoleId(id);
        List<EsAdminUserDO> list = pageResult.getData().getList();
        if (list.size() > 0) {
            return ApiResponse.fail(10086, "该角色已关联管理员，不能删除");
        }
        DubboResult result = iesRoleService.deleteEsRole(id);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @GetMapping(value = "/{id}/checked")
    @ApiOperation(value = "根据角色id查询所拥有的菜单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "要查询的角色表主键", required = true, dataType = "long", paramType = "path", example = "1")
    })
    @ResponseBody
    public ApiResponse getCheckedMenu(@PathVariable Long id) {
        DubboResult<List<String>> result = iesRoleService.getRoleMenu(id);
        if (result.isSuccess()) {
            List<String> data = result.getData();
            return ApiResponse.success(data);
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "分页查询角色列表", response = EsRoleVO.class)
    @GetMapping(value = "/getEsRoleList")
    @ResponseBody
    public ApiResponse getEsRoleList(EsRoleQueryForm form) {
        EsRoleDTO esRoleDTO = new EsRoleDTO();
        BeanUtil.copyProperties(form, esRoleDTO);
        DubboPageResult<EsRoleDO> result = iesRoleService.getEsRoleList(esRoleDTO, form.getPageSize(), form.getPageNum());
        if (result.isSuccess()) {
            List<EsRoleDO> data = result.getData().getList();
            List<EsRoleVO> esRoleVOList = BeanUtil.copyList(data, EsRoleVO.class);
            return ApiPageResponse.pageSuccess(result.getData().getTotal(), esRoleVOList);
        } else {
            return ApiPageResponse.fail(ApiStatus.wrapperException(result));
        }
    }

}

