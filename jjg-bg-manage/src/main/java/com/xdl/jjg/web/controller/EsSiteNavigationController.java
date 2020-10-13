package com.xdl.jjg.web.controller;


import com.xdl.jjg.constant.ApiStatus;
import com.xdl.jjg.model.domain.EsSiteNavigationDO;
import com.xdl.jjg.model.dto.EsSiteNavigationDTO;
import com.xdl.jjg.model.form.EsNavigationQueryForm;
import com.xdl.jjg.model.form.EsSiteNavigationForm;
import com.xdl.jjg.model.vo.EsSiteNavigationVO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.response.web.ApiPageResponse;
import com.xdl.jjg.response.web.ApiResponse;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.web.service.IEsSiteNavigationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 前端控制器-导航菜单
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-06-10
 */
@RestController
@RequestMapping("/esSiteNavigation")
@Api(value = "/esSiteNavigation", tags = "导航菜单")
public class EsSiteNavigationController {

    @Autowired
    private IEsSiteNavigationService iEsSiteNavigationService;

    @ApiOperation(value = "分页查询导航菜单", response = EsSiteNavigationVO.class)
    @GetMapping(value = "/getSiteNavigationList")
    @ResponseBody
    public ApiResponse getSiteNavigationList(EsNavigationQueryForm form) {
        EsSiteNavigationDTO esSiteNavigationDTO = new EsSiteNavigationDTO();
        BeanUtil.copyProperties(form, esSiteNavigationDTO);
        DubboPageResult<EsSiteNavigationDO> result = iEsSiteNavigationService.getSiteNavigationList(esSiteNavigationDTO, form.getPageSize(), form.getPageNum());
        if (result.isSuccess()) {
            List<EsSiteNavigationDO> data = result.getData().getList();
            List<EsSiteNavigationVO> esSiteNavigationVOList = BeanUtil.copyList(data, EsSiteNavigationVO.class);
            return ApiPageResponse.pageSuccess(result.getData().getTotal(), esSiteNavigationVOList);
        } else {
            return ApiPageResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "添加导航菜单")
    @PostMapping(value = "/insertSiteNavigation")
    @ResponseBody
    public ApiResponse insertSiteNavigation(@Valid @RequestBody @ApiParam(name = "导航菜单form对象", value = "form") EsSiteNavigationForm form) {
        EsSiteNavigationDTO esSiteNavigationDTO = new EsSiteNavigationDTO();
        BeanUtil.copyProperties(form, esSiteNavigationDTO);
        DubboResult result = iEsSiteNavigationService.insertSiteNavigation(esSiteNavigationDTO);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "修改导航菜单")
    @PutMapping(value = "/updateSiteNavigation")
    @ResponseBody
    public ApiResponse updateSiteNavigation(@Valid @RequestBody @ApiParam(name = "导航菜单form对象", value = "form") EsSiteNavigationForm form) {
        EsSiteNavigationDTO esSiteNavigationDTO = new EsSiteNavigationDTO();
        BeanUtil.copyProperties(form, esSiteNavigationDTO);
        DubboResult result = iEsSiteNavigationService.updateSiteNavigation(esSiteNavigationDTO);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @DeleteMapping(value = "/deleteSiteNavigation/{id}")
    @ResponseBody
    @ApiOperation(value = "删除")
    @ApiImplicitParam(name = "id", value = "导航菜单id", required = true, dataType = "long", example = "1", paramType = "path")
    public ApiResponse deleteSiteNavigation(@PathVariable Long id) {
        DubboResult result = iEsSiteNavigationService.deleteSiteNavigation(id);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }
}
