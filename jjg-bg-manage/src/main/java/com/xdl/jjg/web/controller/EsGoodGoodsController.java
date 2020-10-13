package com.xdl.jjg.web.controller;

import com.xdl.jjg.constant.ApiStatus;
import com.xdl.jjg.model.domain.EsGoodGoodsDO;
import com.xdl.jjg.model.dto.EsGoodGoodsDTO;
import com.xdl.jjg.model.form.EsGoodGoodsForm;
import com.xdl.jjg.model.form.EsGoodGoodsQueryForm;
import com.xdl.jjg.model.form.EsSoldOutGoodsForm;
import com.xdl.jjg.model.vo.EsGoodGoodsVO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.response.web.ApiPageResponse;
import com.xdl.jjg.response.web.ApiResponse;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.web.service.IEsGoodGoodsService;
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
 * 前端控制器-品质好货
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
@Api(value = "/esGoodGoods", tags = "品质好货")
@RestController
@RequestMapping("/esGoodGoods")
public class EsGoodGoodsController {

    @Autowired
    private IEsGoodGoodsService goodGoodsService;

    @ApiOperation(value = "品质好货分页查询", response = EsGoodGoodsVO.class)
    @GetMapping(value = "/getGoodGoodsList")
    @ResponseBody
    public ApiResponse getGoodGoodsList(EsGoodGoodsQueryForm form) {
        EsGoodGoodsDTO dto = new EsGoodGoodsDTO();
        BeanUtil.copyProperties(form, dto);
        DubboPageResult<EsGoodGoodsDO> result = goodGoodsService.getGoodGoodsList(dto, form.getPageSize(), form.getPageNum());
        if (result.isSuccess()) {
            List<EsGoodGoodsDO> data = result.getData().getList();
            List<EsGoodGoodsVO> voList = BeanUtil.copyList(data, EsGoodGoodsVO.class);
            return ApiPageResponse.pageSuccess(result.getData().getTotal(), voList);
        } else {
            return ApiPageResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "添加品质好货")
    @PostMapping(value = "/insertGoodGoods")
    @ResponseBody
    public ApiResponse insertGoodGoods(@Valid @RequestBody @ApiParam(name = "品质好货form对象", value = "form") EsGoodGoodsForm form) {
        EsGoodGoodsDTO dto = new EsGoodGoodsDTO();
        BeanUtil.copyProperties(form, dto);
        DubboResult result = goodGoodsService.insertGoodGoods(dto);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "修改品质好货")
    @PutMapping(value = "/updateGoodGoods")
    @ResponseBody
    public ApiResponse updateGoodGoods(@Valid @RequestBody @ApiParam(name = "品质好货form对象", value = "form") EsGoodGoodsForm form) {
        EsGoodGoodsDTO dto = new EsGoodGoodsDTO();
        BeanUtil.copyProperties(form, dto);
        DubboResult result = goodGoodsService.updateGoodGoods(dto);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @DeleteMapping(value = "/batchDel/{ids}")
    @ApiOperation(value = "删除或批量删除")
    @ApiImplicitParam(name = "ids", value = "id数组", required = true, dataType = "int", example = "1", paramType = "path", allowMultiple = true)
    @ResponseBody
    public ApiResponse batchDel(@PathVariable Integer[] ids) {
        DubboResult result = goodGoodsService.batchDel(ids);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @DeleteMapping(value = "/batchRelease/{ids}")
    @ApiOperation(value = "发布或批量发布")
    @ApiImplicitParam(name = "ids", value = "id数组", required = true, dataType = "int", example = "1", paramType = "path", allowMultiple = true)
    @ResponseBody
    public ApiResponse batchRelease(@PathVariable Integer[] ids) {
        DubboResult result = goodGoodsService.batchRelease(ids);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "下架")
    @PutMapping(value = "/underGoodGoods")
    @ResponseBody
    public ApiResponse underGoodGoods(@Valid @RequestBody @ApiParam(name = "品质好货form对象", value = "form") EsSoldOutGoodsForm form) {
        EsGoodGoodsDTO dto = new EsGoodGoodsDTO();
        BeanUtil.copyProperties(form, dto);
        DubboResult result = goodGoodsService.underGoodGoods(dto);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }
}
