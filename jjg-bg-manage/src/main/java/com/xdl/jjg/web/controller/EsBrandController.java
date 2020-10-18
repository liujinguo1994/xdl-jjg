package com.xdl.jjg.web.controller;


import com.jjg.shop.model.domain.EsBrandDO;
import com.jjg.shop.model.dto.EsBrandDTO;
import com.jjg.shop.model.vo.EsBrandVO;
import com.jjg.system.model.form.EsBrandForm;
import com.jjg.system.model.form.EsBrandQueryForm;
import com.xdl.jjg.constant.ApiStatus;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.response.web.ApiPageResponse;
import com.xdl.jjg.response.web.ApiResponse;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.web.service.feign.shop.BrandService;
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
 * 前端控制器-品牌管理
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-06-20 15:30:00
 */
@Api(value = "/esBrand", tags = "品牌管理")
@RestController
@RequestMapping("/esBrand")
public class EsBrandController {

    @Autowired
    private BrandService iesBrandService;

    /**
     * 新增品牌
     *
     * @param form
     * @return
     */

    @ApiOperation(value = "新增品牌信息")
    @ResponseBody
    @PostMapping(value = "/insertEsBrand")
    public ApiResponse insertEsBrand(@Valid @RequestBody @ApiParam(name = "品牌form对象", value = "form") EsBrandForm form) {
        EsBrandDTO esBrandDTO = new EsBrandDTO();
        BeanUtil.copyProperties(form, esBrandDTO);
        DubboResult<EsBrandDO> result = iesBrandService.insertBrand(esBrandDTO);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    /**
     * 修改品牌
     *
     * @param form
     * @return
     */
    @PutMapping(value = "/updateEsBrand/{id}")
    @ResponseBody
    @ApiOperation(value = "修改品牌信息")
    public ApiResponse updateEsBrand(@Valid @RequestBody @ApiParam(name = "品牌form对象", value = "form") EsBrandForm form, @PathVariable Long id) {
        EsBrandDTO esBrandDTO = new EsBrandDTO();
        BeanUtil.copyProperties(form, esBrandDTO);
        DubboResult<EsBrandDO> result = iesBrandService.updateBrand(esBrandDTO, id);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    /**
     * 分页获取品牌信息
     */
    @ApiOperation(value = "分页获取品牌信息", response = EsBrandVO.class)
    @GetMapping(value = "/getEsBrandList")
    @ResponseBody
    public ApiResponse getEsBrandList(EsBrandQueryForm form) {
        EsBrandDTO esBrandDTO = new EsBrandDTO();
        BeanUtil.copyProperties(form, esBrandDTO);
        DubboPageResult<EsBrandDO> result = iesBrandService.getBrandList(esBrandDTO, form.getPageSize(), form.getPageNum());
        if (result.isSuccess()) {
            List<EsBrandDO> data = result.getData().getList();
            List<EsBrandVO> esBrandVOList = BeanUtil.copyList(data, EsBrandVO.class);
            return ApiPageResponse.pageSuccess(result.getData().getTotal(), esBrandVOList);
        } else {
            return ApiPageResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    /**
     * 批量删除品牌
     *
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/batchDel/{ids}")
    @ResponseBody
    @ApiOperation(value = "批量删除品牌")
    @ApiImplicitParam(name = "ids", value = "品牌id数组", required = true, dataType = "int", example = "1", paramType = "path", allowMultiple = true)
    public ApiResponse batchDel(@PathVariable Integer[] ids) {
        DubboResult result = iesBrandService.deleteBrand(ids);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }
}

