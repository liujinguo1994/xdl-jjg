package com.xdl.jjg.web.controller;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.xdl.jjg.constant.ApiStatus;
import com.xdl.jjg.model.domain.EsMenuDO;
import com.xdl.jjg.model.dto.EsMenuDTO;
import com.xdl.jjg.model.form.EsMenuForm;
import com.xdl.jjg.model.vo.EsMenuVO;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.response.web.ApiResponse;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.web.service.IEsMenuService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 前端控制器-菜单管理
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
@RestController
@RequestMapping("/esMenu")
@Api(value = "/esMenu", tags = "菜单管理")
public class EsMenuController {

    @Autowired
    private IEsMenuService iesMenuService;

    @ApiOperation(value = "添加菜单")
    @PostMapping(value = "/insertEsMenu")
    @ResponseBody
    public ApiResponse insertEsMenu(@Valid @RequestBody @ApiParam(name = "菜单form对象", value = "form") EsMenuForm form) {
        EsMenuDTO esMenuDTO = new EsMenuDTO();
        BeanUtil.copyProperties(form, esMenuDTO);
        DubboResult result = iesMenuService.insertEsMenu(esMenuDTO);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }


    @ApiOperation(value = "修改菜单")
    @PutMapping(value = "/updateEsMenu/{id}")
    @ResponseBody
    public ApiResponse updateEsMenu(@Valid @RequestBody @ApiParam(name = "菜单form对象", value = "form") EsMenuForm form, @PathVariable Long id) {
        EsMenuDTO esMenuDTO = new EsMenuDTO();
        BeanUtil.copyProperties(form, esMenuDTO);
        esMenuDTO.setId(id);
        DubboResult result = iesMenuService.updateEsMenu(esMenuDTO);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "删除菜单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "要删除的菜单主键", required = true, dataType = "long", paramType = "path", example = "1")
    })
    @DeleteMapping(value = "/deleteEsMenu/{id}")
    @ResponseBody
    public ApiResponse deleteEsMenu(@PathVariable Long id) {
        DubboResult result = iesMenuService.deleteEsMenu(id);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "根据父id查询所有菜单", response = EsMenuVO.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "parentId", value = "父菜单主键", required = true, dataType = "long", paramType = "query", example = "1")
    })
    @GetMapping(value = "/getEsMenuTree")
    @ResponseBody
    public ApiResponse getEsMenuTree(@RequestParam("parentId") Long parentId) {
        DubboResult<List<EsMenuDO>> result = iesMenuService.getEsMenuTree(parentId);
        if (result.isSuccess()) {
            List<EsMenuDO> data = result.getData();
            List<EsMenuVO> esMenuVOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(data)) {
                //属性复制
                esMenuVOList = data.stream().map(esMenu -> {
                    EsMenuVO esMenuVO = new EsMenuVO();
                    BeanUtil.copyProperties(esMenu, esMenuVO);
                    return esMenuVO;
                }).collect(Collectors.toList());
            }
            return ApiResponse.success(esMenuVOList);
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }
}

