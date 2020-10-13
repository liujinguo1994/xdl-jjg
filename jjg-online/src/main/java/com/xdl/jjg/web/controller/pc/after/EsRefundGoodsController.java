package com.xdl.jjg.web.controller.pc.after;/*
package com.shopx.trade.web.controller;

import com.shopx.common.model.result.ApiResponse;
import com.shopx.common.model.result.DubboPageResult;
import com.shopx.common.model.result.DubboResult;
import com.shopx.common.web.BaseController;
import com.shopx.trade.api.model.domain.EsRefundGoodsDO;
import com.shopx.trade.api.model.domain.dto.EsRefundGoodsDTO;
import com.shopx.trade.api.model.domain.vo.EsRefundGoodsVO;
import com.shopx.trade.api.service.IEsRefundGoodsService;
import com.shopx.trade.web.constant.ApiStatus;
import com.shopx.trade.web.request.EsRefundGoodsForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

*/
/**
 * <p>
 * 退货商品 前端控制器
 * </p>
 *
 * @author LiuJG344009799@qq.com
 * @since 2019-05-29
 *//*

@Api(value = "/esRefundGoods",tags = "退货商品表接口")
@RestController
@RequestMapping("/esRefundGoods")
public class EsRefundGoodsController extends BaseController {

    @Reference(version = "${dubbo.application.version}",timeout = 5000)
    private IEsRefundGoodsService iesRefundGoodsService;


    */
/**
     * 插入退货商品信息
     * @param esRefundGoodsForm
     * @return
     *//*

    @PostMapping
    @ApiOperation(value = "插入退货商品信息",notes = "根据前台传入的Form")
    @ApiImplicitParam(name = "esRefundGoodsForm",value = "Form表单")
    @ResponseBody
    public ApiResponse insertRefundGoods(EsRefundGoodsForm esRefundGoodsForm){

        EsRefundGoodsDTO esRefundGoodsDTO = new EsRefundGoodsDTO();

        BeanUtils.copyProperties(esRefundGoodsForm,esRefundGoodsDTO);

        DubboResult result = iesRefundGoodsService.insertRefundGoods(esRefundGoodsDTO);

        if(result.isSuccess()){
            return ApiResponse.success(result.getData());
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }

    */
/**
     * 根据Id删除退货商品信息
     * @param id
     * @return
     *//*

    @DeleteMapping(value = "/{id}")
    @ApiOperation(value = "删除退货商品信息",notes = "根据Id删除退货商品信息")
    @ApiImplicitParam(name = "id", value = "主键id")
    @ResponseBody
    public ApiResponse deleteRefundGoods(@PathVariable Long id){

        DubboResult result = iesRefundGoodsService.deleteRefundGoods(id);

        if(result.isSuccess()){
            return ApiResponse.success(result.getData());
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }

    */
/**
     * 更新退货商品信息
     * @param esRefundGoodsForm
     * @return
     *//*

    @PutMapping
    @ApiOperation(value = "更新退货商品信息",notes = "根据Form更新退货商品信息")
    @ResponseBody
    @ApiImplicitParam(name = "esRefundGoodsForm", value = "Form表单")
    public ApiResponse updateRefundGoods(EsRefundGoodsForm esRefundGoodsForm){

        EsRefundGoodsDTO esRefundGoodsDTO = new EsRefundGoodsDTO();

        BeanUtils.copyProperties(esRefundGoodsForm,esRefundGoodsDTO);

        DubboResult result = iesRefundGoodsService.updateRefundGoods(esRefundGoodsDTO);

        if(result.isSuccess()){
            return ApiResponse.success(result.getData());
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }

    */
/**
     * 获取退货商品信息
     * @param id
     * @return
     *//*

    @GetMapping(value = "/{id}")
    @ApiOperation(value = "获取退货商品信息",notes = "通过id查询退货商品信息")
    @ResponseBody
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "主键id", required = true, dataType = "long", paramType = "query", example = "1")})
    public ApiResponse<EsRefundGoodsVO> getRefundGoods(@PathVariable Long id){

        DubboResult<EsRefundGoodsDO> result = iesRefundGoodsService.getRefundGoods(id);

        if (result.isSuccess()){
            return ApiResponse.success(result.getData());
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));

    }

    */
/**
     * 获取退货商品信息列表
     * @param esRefundGoodsForm
     * @param pageSize
     * @param pageNum
     * @return
     *//*

    @GetMapping
    @ResponseBody
    @ApiOperation(value = "获取退货商品信息列表",notes = "跟据form表单数据提交")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "esRefundGoodsForm", value = "查询Form表单"),
            @ApiImplicitParam(name = "pageSize", value = "页数", dataType = "int", paramType = "query", required = true, example = "10"),
            @ApiImplicitParam(name = "pageNum", value = "页码", dataType = "int", paramType = "query", required = true, example = "1")})
    public ApiResponse getOrderLogList(EsRefundGoodsForm esRefundGoodsForm, @ApiIgnore @NotEmpty(message = "页数不能为空") int pageSize,
                                       @ApiIgnore @NotEmpty(message = "页码不能为空") int pageNum){

        EsRefundGoodsDTO esRefundGoodsDTO = new EsRefundGoodsDTO();

        BeanUtils.copyProperties(esRefundGoodsForm,esRefundGoodsDTO);

        DubboPageResult<EsRefundGoodsDO> result = iesRefundGoodsService.getRefundGoodsList(esRefundGoodsDTO, pageSize, pageNum);

        if (result.isSuccess()){
            List<EsRefundGoodsDO> esRefundGoodsDOList = result.getData().getList();
            List<EsRefundGoodsVO> EsRefundGoodsVOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(esRefundGoodsDOList)){
                EsRefundGoodsVOList = esRefundGoodsDOList.stream().map(EsRefundGoods ->{
                    EsRefundGoodsVO esRefundGoodsVO = new EsRefundGoodsVO();
                    BeanUtils.copyProperties(EsRefundGoods,esRefundGoodsVO);
                    return esRefundGoodsVO;
                }).collect(Collectors.toList());
            }
            return ApiResponse.success(DubboPageResult.success(EsRefundGoodsVOList).getData());
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }

}

*/
