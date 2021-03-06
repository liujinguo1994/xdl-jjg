package com.xdl.jjg.web.controller;

import com.jjg.member.model.domain.EsShopAndDetailDO;
import com.jjg.member.model.domain.EsShopDO;
import com.jjg.member.model.dto.EsShopAndDetailDTO;
import com.jjg.member.model.dto.ShopQueryParam;
import com.jjg.member.model.vo.EsShopAndDetailVO;
import com.jjg.member.model.vo.EsShopVO;
import com.jjg.system.model.form.EsShopQueryForm;
import com.jjg.system.model.form.EsUpdateShopInfoForm;
import com.xdl.jjg.constant.ApiStatus;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.response.web.ApiPageResponse;
import com.xdl.jjg.response.web.ApiResponse;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.web.service.feign.member.ShopService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 前端控制器-店铺管理
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-06-10
 */
@RestController
@RequestMapping("/esShop")
@Api(value = "/esShop", tags = "店铺管理")
public class EsShopController {

    @Autowired
    private ShopService iEsShopService;

    @ApiOperation(value = "分页查询店铺列表", response = EsShopVO.class)
    @GetMapping(value = "/getShopThemesList")
    @ResponseBody
    public ApiResponse getShopThemesList(EsShopQueryForm form) {
        ShopQueryParam shopQueryParam = new ShopQueryParam();
        BeanUtil.copyProperties(form, shopQueryParam);
        DubboPageResult<EsShopDO> result = iEsShopService.getShopList(shopQueryParam, form.getPageSize(), form.getPageNum());
        if (result.isSuccess()) {
            List<EsShopDO> data = result.getData().getList();
            List<EsShopVO> esShopVOList = BeanUtil.copyList(data, EsShopVO.class);
            return ApiPageResponse.pageSuccess(result.getData().getTotal(), esShopVOList);
        } else {
            return ApiPageResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "获取店铺详细", response = EsShopAndDetailVO.class)
    @GetMapping("/getShopDetails/{id}")
    @ApiImplicitParam(name = "id", value = "店铺id", required = true, dataType = "long", paramType = "path", example = "1")
    @ResponseBody
    public ApiResponse getShopDetails(@PathVariable Long id) {
        DubboResult<EsShopAndDetailDO> result = iEsShopService.getShopDetail(id);
        if (result.isSuccess()) {
            EsShopAndDetailDO data = result.getData();
            EsShopAndDetailVO esShopAndDetailVO = new EsShopAndDetailVO();
            BeanUtil.copyProperties(data, esShopAndDetailVO);
            return ApiResponse.success(esShopAndDetailVO);
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "店铺关闭")
    @PutMapping(value = "/underShop/{id}")
    @ApiImplicitParam(name = "id", value = "店铺id", required = true, dataType = "long", paramType = "path", example = "1")
    @ResponseBody
    public ApiResponse underShop(@PathVariable Long id) {
        DubboResult result = iEsShopService.underShop(id);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "店铺开启")
    @PutMapping(value = "/useShop/{id}")
    @ApiImplicitParam(name = "id", value = "店铺id", required = true, dataType = "long", paramType = "path", example = "1")
    @ResponseBody
    public ApiResponse useShop(@PathVariable Long id) {
        DubboResult result = iEsShopService.useShop(id);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "修改店铺信息")
    @PutMapping(value = "/updateShopInfo")
    @ResponseBody
    public ApiResponse updateShopInfo(@RequestBody @ApiParam(name = "修改店铺信息form对象", value = "form") EsUpdateShopInfoForm form) {
        EsShopAndDetailDTO esShopAndDetailDTO = new EsShopAndDetailDTO();
        BeanUtil.copyProperties(form, esShopAndDetailDTO);
        esShopAndDetailDTO.setShopId(form.getId());
        DubboResult result = iEsShopService.updateShop(esShopAndDetailDTO);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }
}
