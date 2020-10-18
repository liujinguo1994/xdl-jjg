package com.xdl.jjg.web.controller;


import com.jjg.system.model.domain.EsGoodsRankingDO;
import com.jjg.system.model.dto.EsGoodsRankingDTO;
import com.jjg.system.model.form.EsGoodsRankingForm;
import com.jjg.system.model.form.EsGoodsRankingQueryForm;
import com.jjg.system.model.vo.EsGoodsRankingVO;
import com.xdl.jjg.constant.ApiStatus;
import com.xdl.jjg.constant.ErrorCode;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.response.web.ApiPageResponse;
import com.xdl.jjg.response.web.ApiResponse;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.util.StringUtil;
import com.xdl.jjg.web.service.IEsGoodsRankingService;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 热门榜单 前端控制器
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-05-07
 */
@Api(value = "/esGoodsRanking", tags = "热门榜单")
@RestController
@RequestMapping("/esGoodsRanking")
public class EsGoodsRankingController {

    @Autowired
    private IEsGoodsRankingService iesGoodsRankingService;

    @ApiOperation(value = "添加")
    @PostMapping(value = "/insertEsGoodsRanking")
    @ResponseBody
    public ApiResponse insertEsGoodsRanking(@RequestBody @ApiParam(name = "热门榜单form对象", value = "form") @Valid EsGoodsRankingForm form) {
        EsGoodsRankingDTO dto = new EsGoodsRankingDTO();
        BeanUtil.copyProperties(form, dto);
        String url = form.getGoodsUrl();
        String s = StringUtils.substringAfterLast(url, "/");
        if (StringUtils.isBlank(s) || !StringUtil.isNumber(s)) {
            throw new ArgumentException(ErrorCode.URL_ERROR.getErrorCode(), "商品链接异常");
        }
        dto.setGoodsId(Long.valueOf(s));
        DubboResult result = iesGoodsRankingService.insertGoodsRanking(dto);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "修改")
    @PutMapping(value = "/editEsGoodsRanking")
    @ResponseBody
    public ApiResponse editEsGoodsRanking(@RequestBody @ApiParam(name = "热门榜单form对象", value = "form") @Valid EsGoodsRankingForm form) {
        EsGoodsRankingDTO dto = new EsGoodsRankingDTO();
        BeanUtil.copyProperties(form, dto);
        String url = form.getGoodsUrl();
        String s = StringUtils.substringAfterLast(url, "/");
        if (StringUtils.isBlank(s) || !StringUtil.isNumber(s)) {
            throw new ArgumentException(ErrorCode.URL_ERROR.getErrorCode(), "商品链接异常");
        }
        dto.setGoodsId(Long.valueOf(s));
        DubboResult result = iesGoodsRankingService.updateGoodsRanking(dto);
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
    @DeleteMapping(value = "/deleteEsGoodsRanking/{id}")
    @ResponseBody
    public ApiResponse deleteEsGoodsRanking(@PathVariable Long id) {
        DubboResult result = iesGoodsRankingService.deleteGoodsRanking(id);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "分页查询列表", response = EsGoodsRankingVO.class)
    @GetMapping(value = "/getEsGoodsRanking")
    @ResponseBody
    public ApiResponse getEsGoodsRanking(EsGoodsRankingQueryForm form) {
        EsGoodsRankingDTO dto = new EsGoodsRankingDTO();
        BeanUtil.copyProperties(form, dto);
        DubboPageResult<EsGoodsRankingDO> result = iesGoodsRankingService.getGoodsRankingList(dto, form.getPageSize(), form.getPageNum());
        if (result.isSuccess()) {
            List<EsGoodsRankingDO> data = result.getData().getList();
            List<EsGoodsRankingVO> voList = BeanUtil.copyList(data, EsGoodsRankingVO.class);
            return ApiPageResponse.pageSuccess(result.getData().getTotal(), voList);
        } else {
            return ApiPageResponse.fail(ApiStatus.wrapperException(result));
        }
    }

}

