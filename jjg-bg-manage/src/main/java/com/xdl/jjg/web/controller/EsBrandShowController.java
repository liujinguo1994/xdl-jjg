package com.xdl.jjg.web.controller;

import com.xdl.jjg.constant.ApiStatus;
import com.xdl.jjg.model.domain.EsBrandShowDO;
import com.xdl.jjg.model.dto.EsBrandShowDTO;
import com.xdl.jjg.model.form.EsBrandShowForm;
import com.xdl.jjg.model.form.EsQueryPageForm;
import com.xdl.jjg.model.vo.EsBrandShowVO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.response.web.ApiPageResponse;
import com.xdl.jjg.response.web.ApiResponse;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.web.service.IEsBrandShowService;
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
 * 前端控制器-品牌展示管理
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-06-20 15:30:00
 */
@Api(value = "/esBrandShow", tags = "品牌展示管理")
@RestController
@RequestMapping("/esBrandShow")
public class EsBrandShowController {

    @Autowired
    private IEsBrandShowService iEsBrandShowService;

    @ApiOperation(value = "新增品牌展示")
    @ResponseBody
    @PostMapping(value = "/insertBrandShow")
    public ApiResponse insertBrandShow(@Valid @RequestBody @ApiParam(name = "品牌展示form对象", value = "form") EsBrandShowForm form) {
        EsBrandShowDTO esBrandShowDTO = new EsBrandShowDTO();
        BeanUtil.copyProperties(form, esBrandShowDTO);
        DubboResult result = iEsBrandShowService.insertBrandShow(esBrandShowDTO);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @PutMapping(value = "/updateBrandShow/{id}")
    @ResponseBody
    @ApiOperation(value = "修改品牌展示信息")
    public ApiResponse updateBrandShow(@Valid @RequestBody @ApiParam(name = "品牌展示form对象", value = "form") EsBrandShowForm form, @PathVariable Long id) {
        EsBrandShowDTO esBrandShowDTO = new EsBrandShowDTO();
        BeanUtil.copyProperties(form, esBrandShowDTO);
        DubboResult result = iEsBrandShowService.updateBrandShow(esBrandShowDTO, id);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @DeleteMapping(value = "/deleteBrandShow/{id}")
    @ResponseBody
    @ApiOperation(value = "删除")
    @ApiImplicitParam(name = "id", value = "品牌id", required = true, dataType = "long", example = "1", paramType = "path")
    public ApiResponse deleteBrandShow(@PathVariable Long id) {
        DubboResult result = iEsBrandShowService.deleteBrandShow(id);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "分页获取品牌展示列表", response = EsBrandShowVO.class)
    @GetMapping(value = "/getBrandShowList")
    @ResponseBody
    public ApiResponse getBrandShowList(EsQueryPageForm form) {
        EsBrandShowDTO esBrandShowDTO = new EsBrandShowDTO();
        DubboPageResult<EsBrandShowDO> result = iEsBrandShowService.getBrandShowList(esBrandShowDTO, form.getPageSize(), form.getPageNum());
        if (result.isSuccess()) {
            List<EsBrandShowDO> data = result.getData().getList();
            List<EsBrandShowVO> esBrandShowVOList = BeanUtil.copyList(data, EsBrandShowVO.class);
            return ApiPageResponse.pageSuccess(result.getData().getTotal(), esBrandShowVOList);
        } else {
            return ApiPageResponse.fail(ApiStatus.wrapperException(result));
        }
    }


}
