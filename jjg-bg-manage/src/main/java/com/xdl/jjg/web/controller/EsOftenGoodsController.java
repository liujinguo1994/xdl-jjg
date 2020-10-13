package com.xdl.jjg.web.controller;


import com.xdl.jjg.constant.ApiStatus;
import com.xdl.jjg.constant.ErrorCode;
import com.xdl.jjg.model.domain.EsOftenGoodsDO;
import com.xdl.jjg.model.dto.EsOftenGoodsDTO;
import com.xdl.jjg.model.form.EsOftenGoodsForm;
import com.xdl.jjg.model.form.EsOftenGoodsQueryForm;
import com.xdl.jjg.model.vo.EsOftenGoodsVO;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.response.web.ApiPageResponse;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.util.StringUtil;
import com.xdl.jjg.web.service.IEsOftenGoodsService;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 常买商品 前端控制器
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-05-06
 */
@Api(value = "/esOftenGoods", tags = "常买商品")
@RestController
@RequestMapping("/esOftenGoods")
public class EsOftenGoodsController {

    @Autowired
    private IEsOftenGoodsService iesOftenGoodsService;

    @ApiOperation(value = "添加")
    @PostMapping(value = "/insertEsOftenGoods")
    @ResponseBody
    public ApiResponse insertEsOftenGoods(@RequestBody @ApiParam(name = "常买商品form对象", value = "form") @Valid EsOftenGoodsForm form) {
        EsOftenGoodsDTO dto = new EsOftenGoodsDTO();
        BeanUtil.copyProperties(form, dto);
        String url = form.getGoodsUrl();
        String s = StringUtils.substringAfterLast(url, "/");
        if (StringUtils.isBlank(s) || !StringUtil.isNumber(s)) {
            throw new ArgumentException(ErrorCode.URL_ERROR.getErrorCode(), "商品链接异常");
        }
        dto.setGoodsId(Long.valueOf(s));
        DubboResult result = iesOftenGoodsService.insertOftenGoods(dto);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "修改")
    @PutMapping(value = "/editEsOftenGoods")
    @ResponseBody
    public ApiResponse editEsOftenGoods(@RequestBody @ApiParam(name = "常买商品form对象", value = "form") @Valid EsOftenGoodsForm form) {
        EsOftenGoodsDTO dto = new EsOftenGoodsDTO();
        BeanUtil.copyProperties(form, dto);
        String url = form.getGoodsUrl();
        String s = StringUtils.substringAfterLast(url, "/");
        if (StringUtils.isBlank(s) || !StringUtil.isNumber(s)) {
            throw new ArgumentException(ErrorCode.URL_ERROR.getErrorCode(), "商品链接异常");
        }
        dto.setGoodsId(Long.valueOf(s));
        DubboResult result = iesOftenGoodsService.updateOftenGoods(dto);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键id", required = true, dataType = "long", paramType = "path", example = "1")
    })
    @DeleteMapping(value = "/deleteEsOftenGoods/{id}")
    @ResponseBody
    public ApiResponse deleteEsOftenGoods(@PathVariable Long id) {
        DubboResult result = iesOftenGoodsService.deleteOftenGoods(id);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "分页查询列表", response = EsOftenGoodsVO.class)
    @GetMapping(value = "/getEsOftenGoods")
    @ResponseBody
    public ApiResponse getEsOftenGoods(EsOftenGoodsQueryForm form) {
        EsOftenGoodsDTO dto = new EsOftenGoodsDTO();
        BeanUtil.copyProperties(form, dto);
        DubboPageResult<EsOftenGoodsDO> result = iesOftenGoodsService.getOftenGoodsList(dto, form.getPageSize(), form.getPageNum());
        if (result.isSuccess()) {
            List<EsOftenGoodsDO> data = result.getData().getList();
            List<EsOftenGoodsVO> voList = BeanUtil.copyList(data, EsOftenGoodsVO.class);
            return ApiPageResponse.pageSuccess(result.getData().getTotal(), voList);
        } else {
            return ApiPageResponse.fail(ApiStatus.wrapperException(result));
        }
    }

}

