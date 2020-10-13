package com.xdl.jjg.web.controller;

import com.xdl.jjg.constant.ApiStatus;
import com.xdl.jjg.constant.ErrorCode;
import com.xdl.jjg.model.domain.EsFindGoodsDO;
import com.xdl.jjg.model.domain.EsFindGoodsGalleryDTO;
import com.xdl.jjg.model.dto.EsFindGoodsDTO;
import com.xdl.jjg.model.form.EsFindGoodsForm;
import com.xdl.jjg.model.form.EsFindGoodsQueryForm;
import com.xdl.jjg.model.vo.EsFindGoodsGalleryVO;
import com.xdl.jjg.model.vo.EsFindGoodsVO;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.response.web.ApiPageResponse;
import com.xdl.jjg.response.web.ApiResponse;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.util.CollectionUtils;
import com.xdl.jjg.util.StringUtil;
import com.xdl.jjg.web.service.IEsFindGoodsService;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 发现好货 前端控制器
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-05-07
 */
@Api(value = "/esFindGoods", tags = "发现好货")
@RestController
@RequestMapping("/esFindGoods")
public class EsFindGoodsController {

    @Autowired
    private IEsFindGoodsService iesFindGoodsService;

    @ApiOperation(value = "添加")
    @PostMapping(value = "/insertEsFindGoods")
    @ResponseBody
    public ApiResponse insertEsFindGoods(@RequestBody @ApiParam(name = "发现好货form对象", value = "form") @Valid EsFindGoodsForm form) {
        EsFindGoodsDTO dto = new EsFindGoodsDTO();
        BeanUtil.copyProperties(form, dto);
        String url = form.getGoodsUrl();
        String s = StringUtils.substringAfterLast(url, "/");
        if (StringUtils.isBlank(s) || !StringUtil.isNumber(s)) {
            throw new ArgumentException(ErrorCode.URL_ERROR.getErrorCode(), "商品链接异常");
        }
        dto.setGoodsId(Long.valueOf(s));
        if (CollectionUtils.isNotEmpty(form.getGalleryList())) {
            List<EsFindGoodsGalleryDTO> dtoList = BeanUtil.copyList(form.getGalleryList(), EsFindGoodsGalleryDTO.class);
            dto.setGalleryList(dtoList);
        }
        DubboResult result = iesFindGoodsService.insertFindGoods(dto);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "修改")
    @PutMapping(value = "/editEsFindGoods")
    @ResponseBody
    public ApiResponse editEsFindGoods(@RequestBody @ApiParam(name = "发现好货form对象", value = "form") @Valid EsFindGoodsForm form) {
        EsFindGoodsDTO dto = new EsFindGoodsDTO();
        BeanUtil.copyProperties(form, dto);
        String url = form.getGoodsUrl();
        String s = StringUtils.substringAfterLast(url, "/");
        if (StringUtils.isBlank(s) || !StringUtil.isNumber(s)) {
            throw new ArgumentException(ErrorCode.URL_ERROR.getErrorCode(), "商品链接异常");
        }
        dto.setGoodsId(Long.valueOf(s));
        if (CollectionUtils.isNotEmpty(form.getGalleryList())) {
            List<EsFindGoodsGalleryDTO> dtoList = BeanUtil.copyList(form.getGalleryList(), EsFindGoodsGalleryDTO.class);
            dto.setGalleryList(dtoList);
        }
        DubboResult result = iesFindGoodsService.updateFindGoods(dto);
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
    @DeleteMapping(value = "/deleteEsFindGoods/{id}")
    @ResponseBody
    public ApiResponse deleteEsFindGoods(@PathVariable Long id) {
        DubboResult result = iesFindGoodsService.deleteFindGoods(id);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "分页查询列表", response = EsFindGoodsVO.class)
    @GetMapping(value = "/getEsFindGoods")
    @ResponseBody
    public ApiResponse getEsFindGoods(EsFindGoodsQueryForm form) {
        EsFindGoodsDTO dto = new EsFindGoodsDTO();
        BeanUtil.copyProperties(form, dto);
        DubboPageResult<EsFindGoodsDO> result = iesFindGoodsService.getFindGoodsList(dto, form.getPageSize(), form.getPageNum());
        if (result.isSuccess()) {
            List<EsFindGoodsDO> doList = result.getData().getList();
            List<EsFindGoodsVO> voList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(doList)) {
                doList.stream().forEach(esFindGoodsDO -> {
                    EsFindGoodsVO vo = new EsFindGoodsVO();
                    BeanUtil.copyProperties(esFindGoodsDO, vo);
                    if (CollectionUtils.isNotEmpty(esFindGoodsDO.getGalleryList())) {
                        List<EsFindGoodsGalleryVO> galleryVOS = BeanUtil.copyList(esFindGoodsDO.getGalleryList(), EsFindGoodsGalleryVO.class);
                        vo.setGalleryList(galleryVOS);
                    }
                    voList.add(vo);
                });
            }
            return ApiPageResponse.pageSuccess(result.getData().getTotal(), voList);
        } else {
            return ApiPageResponse.fail(ApiStatus.wrapperException(result));
        }
    }
}

