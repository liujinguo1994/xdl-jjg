package com.xdl.jjg.web.controller;

import com.jjg.shop.model.domain.EsGoodsDO;
import com.jjg.shop.model.dto.EsGoodsQueryDTO;
import com.jjg.shop.model.vo.EsGoodsVO;
import com.jjg.system.model.form.EsAuthEsGoodsForm;
import com.jjg.system.model.form.EsGoodsQueryForm;
import com.xdl.jjg.constant.ApiStatus;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.response.web.ApiPageResponse;
import com.xdl.jjg.response.web.ApiResponse;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.web.service.feign.shop.GoodsService;
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
 * 前端控制器-商品管理
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-06-24 15:46:00
 */
@Api(value = "/esGoods", tags = "商品管理")
@RestController
@RequestMapping("/esGoods")
public class EsGoodsController {

    @Autowired
    private GoodsService iesGoodsService;

    /**
     * 分页查询获取商品信息
     */
    @GetMapping(value = "/adminGetEsGoodsList")
    @ResponseBody
    @ApiOperation(value = "商品信息分页查询", response = EsGoodsVO.class)
    public ApiResponse adminGetEsGoodsList(EsGoodsQueryForm form) {
        EsGoodsQueryDTO esGoodsQueryDTO = new EsGoodsQueryDTO();
        BeanUtil.copyProperties(form, esGoodsQueryDTO);
        esGoodsQueryDTO.setIsAuth(1);
        DubboPageResult<EsGoodsDO> result = iesGoodsService.adminGetEsGoodsList(esGoodsQueryDTO, form.getPageSize(), form.getPageNum());
        if (result.isSuccess()) {
            List<EsGoodsDO> data = result.getData().getList();
            List<EsGoodsVO> esGoodsVOList = BeanUtil.copyList(data, EsGoodsVO.class);
            return ApiPageResponse.pageSuccess(result.getData().getTotal(), esGoodsVOList);
        } else {
            return ApiPageResponse.fail(ApiStatus.wrapperException(result));
        }
    }


    /**
     * 下架，批量下架商品
     */
    @PutMapping("/adminUnderEsGoods/{ids}")
    @ResponseBody
    @ApiOperation(value = "下架，批量下架商品")
    @ApiImplicitParam(name = "ids", value = "商品ID数组", dataType = "int", paramType = "path", required = true, example = "1", allowMultiple = true)
    public ApiResponse adminUnderEsGoods(@PathVariable Integer[] ids) {
        DubboResult result = iesGoodsService.adminUnderEsGoods(ids);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    /**
     * 分页查询待审核商品信息
     */
    @GetMapping(value = "/getWaitEsGoodsList")
    @ResponseBody
    @ApiOperation(value = "分页查询待审核商品信息", response = EsGoodsVO.class)
    public ApiResponse getWaitEsGoodsList(EsGoodsQueryForm form) {
        EsGoodsQueryDTO esGoodsQueryDTO = new EsGoodsQueryDTO();
        BeanUtil.copyProperties(form, esGoodsQueryDTO);
        esGoodsQueryDTO.setIsAuth(0);
        DubboPageResult<EsGoodsDO> result = iesGoodsService.adminGetEsGoodsList(esGoodsQueryDTO, form.getPageSize(), form.getPageNum());
        if (result.isSuccess()) {
            List<EsGoodsDO> data = result.getData().getList();
            List<EsGoodsVO> esGoodsVOList = BeanUtil.copyList(data, EsGoodsVO.class);
            return ApiPageResponse.pageSuccess(result.getData().getTotal(), esGoodsVOList);
        } else {
            return ApiPageResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    /**
     * 商品审核 支持批量审核
     */
    @PutMapping("/authEsGoods/{ids}")
    @ResponseBody
    @ApiOperation(value = "商品审核 支持批量审核")
    @ApiImplicitParam(name = "ids", value = "商品ID数组", required = true, dataType = "int", example = "1", paramType = "path", allowMultiple = true)
    public ApiResponse authEsGoods(@Valid @RequestBody @ApiParam(name = "商品审核form对象", value = "form") EsAuthEsGoodsForm form, @PathVariable Integer[] ids) {
        DubboResult result = iesGoodsService.authEsGoods(ids, form.getStatus(), form.getMessage());
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }
}

