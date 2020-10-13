package com.xdl.jjg.web.controller;


import com.xdl.jjg.constant.ApiStatus;
import com.xdl.jjg.model.domain.EsGoodsArchDO;
import com.xdl.jjg.model.domain.EsGoodsSkuDO;
import com.xdl.jjg.model.dto.EsGoodsArchDTO;
import com.xdl.jjg.model.dto.EsGoodsSkuDTO;
import com.xdl.jjg.model.dto.EsGoodsSkuQueryDTO;
import com.xdl.jjg.model.dto.EsSpecValuesDTO;
import com.xdl.jjg.model.form.EsGoodsArchDetailQueryForm;
import com.xdl.jjg.model.form.EsGoodsArchForm;
import com.xdl.jjg.model.form.EsGoodsArchQueryForm;
import com.xdl.jjg.model.vo.EsGoodsArchVO;
import com.xdl.jjg.model.vo.EsGoodsSkuVO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.response.web.ApiPageResponse;
import com.xdl.jjg.response.web.ApiResponse;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.web.service.IEsGoodsArchService;
import com.xdl.jjg.web.service.IEsGoodsSkuService;
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
 * 前端控制器-商品档案
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
@Api(value = "/esGoodsArch", tags = "商品档案")
@RestController
@RequestMapping("/esGoodsArch")
public class EsGoodsArchController {

    @Autowired
    private IEsGoodsArchService iesGoodsArchService;

    @Autowired
    private IEsGoodsSkuService iEsGoodsSkuService;

    @ApiOperation(value = "商品档案分页查询", response = EsGoodsArchVO.class)
    @GetMapping(value = "/getGoodsArchList")
    @ResponseBody
    public ApiResponse getGoodsArchList(EsGoodsArchQueryForm form) {
        EsGoodsArchDTO archDTO = new EsGoodsArchDTO();
        BeanUtil.copyProperties(form, archDTO);
        DubboPageResult<EsGoodsArchDO> result = iesGoodsArchService.getGoodsArchList(archDTO, form.getPageSize(), form.getPageNum());
        if (result.isSuccess()) {
            List<EsGoodsArchDO> data = result.getData().getList();
            List<EsGoodsArchVO> esGoodsArchVOList = BeanUtil.copyList(data, EsGoodsArchVO.class);
            return ApiPageResponse.pageSuccess(result.getData().getTotal(), esGoodsArchVOList);
        } else {
            return ApiPageResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @DeleteMapping(value = "/batchDel/{ids}")
    @ResponseBody
    @ApiOperation(value = "删除或批量删除商品档案")
    @ApiImplicitParam(name = "ids", value = "档案id数组", required = true, dataType = "int", example = "1", paramType = "path", allowMultiple = true)
    public ApiResponse batchDel(@PathVariable Integer[] ids) {
        DubboResult result = iesGoodsArchService.deleteGoodsArch(ids);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "添加商品档案")
    @PostMapping(value = "/insertGoodsArch")
    @ResponseBody
    public ApiResponse insertGoodsArch(@Valid @RequestBody @ApiParam(name = "商品档案form对象", value = "form") EsGoodsArchForm form) {
        EsGoodsArchDTO esGoodsArchDTO = new EsGoodsArchDTO();
        BeanUtil.copyProperties(form, esGoodsArchDTO);
        List<EsGoodsSkuDTO> skuDTOList = BeanUtil.copyList(form.getSkuList(), EsGoodsSkuDTO.class);
        skuDTOList.forEach(skuDTO -> {
            List<EsSpecValuesDTO> specValuesDTOList = BeanUtil.copyList(skuDTO.getSpecList(), EsSpecValuesDTO.class);
            skuDTO.setSpecList(specValuesDTOList);
        });
        esGoodsArchDTO.setSkuList(skuDTOList);
        DubboResult result = iesGoodsArchService.adminInsertGoodsArch(esGoodsArchDTO);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "修改商品档案")
    @PutMapping(value = "/updateGoodsArch/{id}")
    @ResponseBody
    public ApiResponse updateGoodsArch(@Valid @RequestBody @ApiParam(name = "商品档案form对象", value = "form") EsGoodsArchForm form, @PathVariable Long id) {
        EsGoodsArchDTO esGoodsArchDTO = new EsGoodsArchDTO();
        BeanUtil.copyProperties(form, esGoodsArchDTO);
        List<EsGoodsSkuDTO> skuDTOList = BeanUtil.copyList(form.getSkuList(), EsGoodsSkuDTO.class);
        skuDTOList.forEach(skuDTO -> {
            List<EsSpecValuesDTO> specValuesDTOList = BeanUtil.copyList(skuDTO.getSpecList(), EsSpecValuesDTO.class);
            skuDTO.setSpecList(specValuesDTOList);
        });
        esGoodsArchDTO.setSkuList(skuDTOList);
        DubboResult result = iesGoodsArchService.adminUpdateGoodsArch(esGoodsArchDTO, id);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }


   /* @ApiOperation(value = "禁用或解禁")
    @PutMapping(value = "/updateStatus/{ids}/{state}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "商品档案id数组", required = true, dataType = "int", paramType = "path",example = "1",allowMultiple = true),
            @ApiImplicitParam(name = "state", value = "有效状态(0:解禁,1:禁用)", required = true, dataType = "long", paramType = "path",example = "1")
    })
    @ResponseBody
    public ApiResponse updateStatus(@PathVariable Integer[] ids,@PathVariable Long state) {
        DubboResult result=iesGoodsArchService.updateGoodsArch(ids,state);
        if (result.isSuccess()) {
            return ApiResponse.success();
        }else{
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }*/

    @ApiOperation(value = "查看详情(分页查询)", response = EsGoodsSkuVO.class)
    @GetMapping(value = "/getGoodsArchDetailList")
    @ResponseBody
    public ApiResponse getGoodsArchDetailList(@Valid EsGoodsArchDetailQueryForm form) {
        EsGoodsSkuQueryDTO esGoodsSkuQueryDTO = new EsGoodsSkuQueryDTO();
        esGoodsSkuQueryDTO.setGoodsId(form.getId());
        esGoodsSkuQueryDTO.setKeyword(form.getSkuSn());
        DubboPageResult<EsGoodsSkuDO> result = iEsGoodsSkuService.getGoodsSkuList(esGoodsSkuQueryDTO, form.getPageSize(), form.getPageNum());
        if (result.isSuccess()) {
            List<EsGoodsSkuDO> data = result.getData().getList();
            List<EsGoodsSkuVO> esGoodsArchVOList = BeanUtil.copyList(data, EsGoodsSkuVO.class);
            return ApiPageResponse.pageSuccess(result.getData().getTotal(), esGoodsArchVOList);
        } else {
            return ApiPageResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "根据id查询商品档案信息", response = EsGoodsArchVO.class)
    @GetMapping(value = "/getGoodsArch/{id}")
    @ApiImplicitParam(name = "id", value = "档案id", required = true, dataType = "long", example = "1", paramType = "path")
    @ResponseBody
    public ApiResponse getGoodsArch(@PathVariable Long id) {
        DubboResult<EsGoodsArchDO> result = iesGoodsArchService.getGoodsArch(id);
        if (result.isSuccess()) {
            EsGoodsArchDO data = result.getData();
            EsGoodsArchVO esGoodsArchVO = new EsGoodsArchVO();
            BeanUtil.copyProperties(data, esGoodsArchVO);
            return ApiResponse.success(esGoodsArchVO);
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

}

