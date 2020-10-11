package com.xdl.jjg.web.controller;

import com.xdl.jjg.constant.ApiStatus;
import com.xdl.jjg.model.form.EsShopThemesForm;
import com.xdl.jjg.model.form.EsShopThemesQueryForm;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.response.web.ApiPageResponse;
import com.xdl.jjg.util.BeanUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 *  前端控制器-店铺模板管理
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-06-10
 */
@RestController
@RequestMapping("/esShopThemes")
@Api(value="/esShopThemes", tags="店铺模板管理")
public class EsShopThemesController {

    @Autowired
    private IEsShopThemesService iEsShopThemesService;

    @ApiOperation(value = "分页查询店铺模板列表",response = EsShopThemesVO.class)
    @GetMapping(value = "/getShopThemesList")
    @ResponseBody
    public ApiResponse getShopThemesList(EsShopThemesQueryForm form) {
        EsShopThemesDTO esShopThemesDTO = new EsShopThemesDTO();
        BeanUtil.copyProperties(form,esShopThemesDTO);
        DubboPageResult<EsShopThemesDO> result = iEsShopThemesService.getShopThemesList(esShopThemesDTO, form.getPageSize(), form.getPageNum());
        if (result.isSuccess()) {
            List<EsShopThemesDO> data = result.getData().getList();
            List<EsShopThemesVO> esShopThemesVOList = BeanUtil.copyList(data, EsShopThemesVO.class);
            return ApiPageResponse.pageSuccess(result.getData().getTotal(),esShopThemesVOList);
        } else {
            return ApiPageResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "添加店铺模板")
    @PostMapping(value = "/insertEsShopThemes")
    @ResponseBody
    public ApiResponse insertEsShopThemes(@Valid @RequestBody @ApiParam(name="店铺模板form对象",value="form") EsShopThemesForm form){
        EsShopThemesDTO esShopThemesDTO=new EsShopThemesDTO();
        BeanUtil.copyProperties(form, esShopThemesDTO);
        DubboResult result = iEsShopThemesService.insertShopThemes(esShopThemesDTO);
        if (result.isSuccess()) {
            return ApiResponse.success();
        }else{
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "修改店铺模板")
    @PutMapping(value = "/updateEsShopThemes/{id}")
    @ResponseBody
    public ApiResponse updateEsShopThemes(@Valid @RequestBody @ApiParam(name="店铺模板form对象",value="form") EsShopThemesForm form, @PathVariable Long id){
        EsShopThemesDTO esShopThemesDTO=new EsShopThemesDTO();
        BeanUtil.copyProperties(form, esShopThemesDTO);
        DubboResult result = iEsShopThemesService.updateShopThemes(esShopThemesDTO,id);
        if (result.isSuccess()) {
            return ApiResponse.success();
        }else{
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "删除店铺模板")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "要删除的店铺模板id", required = true, dataType = "long", paramType = "path",example = "1")
    })
    @DeleteMapping(value = "/deleteEsShopThemes/{id}")
    @ResponseBody
    public ApiResponse deleteEsShopThemes(@PathVariable Long id) {
        DubboResult result = iEsShopThemesService.deleteShopThemes(id);
        if (result.isSuccess()) {
            return ApiResponse.success();
        }else{
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

}
