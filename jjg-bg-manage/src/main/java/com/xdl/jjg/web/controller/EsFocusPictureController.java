package com.xdl.jjg.web.controller;

import com.xdl.jjg.constant.ApiStatus;
import com.xdl.jjg.model.domain.EsFocusPictureDO;
import com.xdl.jjg.model.dto.EsFocusPictureDTO;
import com.xdl.jjg.model.form.EsFocusPictureForm;
import com.xdl.jjg.model.vo.EsFocusPictureVO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.response.web.ApiPageResponse;
import com.xdl.jjg.response.web.ApiResponse;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.web.service.IEsFocusPictureService;
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
 *  前端控制器-轮播图
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
@Api(value = "/esFocusPicture",tags = "轮播图")
@RestController
@RequestMapping("/esFocusPicture")
public class EsFocusPictureController {

    @Autowired
    private IEsFocusPictureService iEsFocusPictureService;


    @ApiOperation(value = "根据客户端类型查询轮播图列表",response = EsFocusPictureVO.class)
    @GetMapping(value = "/getList/{clientType}")
    @ApiImplicitParam(name = "clientType", value = "客户端类型 APP/WAP/PC", required = true, dataType = "String", paramType = "path")
    @ResponseBody
    public ApiResponse getList(@PathVariable String clientType) {
        DubboPageResult<EsFocusPictureDO> result = iEsFocusPictureService.getList(clientType);
        if (result.isSuccess()) {
            List<EsFocusPictureDO> data = result.getData().getList();
            List<EsFocusPictureVO> esFocusPictureVOList = BeanUtil.copyList(data, EsFocusPictureVO.class);
            return ApiPageResponse.pageSuccess(result.getData().getTotal(),esFocusPictureVOList);
        } else {
            return ApiPageResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "添加轮播图")
    @PostMapping(value = "/insertSupplier")
    @ResponseBody
    public ApiResponse insertSupplier(@Valid @RequestBody @ApiParam(name="轮播图form对象",value="form") EsFocusPictureForm form){
        EsFocusPictureDTO esFocusPictureDTO = new EsFocusPictureDTO();
        BeanUtil.copyProperties(form, esFocusPictureDTO);
        DubboResult<EsFocusPictureDO> result = iEsFocusPictureService.insertFocusPicture(esFocusPictureDTO);
        if (result.isSuccess()) {
            EsFocusPictureDO data = result.getData();
            EsFocusPictureVO vo = new EsFocusPictureVO();
            BeanUtil.copyProperties(data,vo);
            return ApiResponse.success(vo);
        }else{
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "修改轮播图")
    @PutMapping(value = "/updateFocusPicture")
    @ResponseBody
    public ApiResponse updateFocusPicture(@Valid @RequestBody @ApiParam(name="轮播图form对象",value="form") EsFocusPictureForm form){
        EsFocusPictureDTO esFocusPictureDTO = new EsFocusPictureDTO();
        BeanUtil.copyProperties(form, esFocusPictureDTO);
        DubboResult<EsFocusPictureDO> result = iEsFocusPictureService.updateFocusPicture(esFocusPictureDTO);
        if (result.isSuccess()) {
            EsFocusPictureDO data = result.getData();
            EsFocusPictureVO vo = new EsFocusPictureVO();
            BeanUtil.copyProperties(data,vo);
            return ApiResponse.success(vo);
        }else{
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @DeleteMapping(value = "/deleteFocusPicture/{id}")
    @ResponseBody
    @ApiOperation(value = "删除")
    @ApiImplicitParam(name = "id", value = "轮播图id", required = true, dataType = "long",example = "1", paramType = "path")
    public ApiResponse deleteFocusPicture(@PathVariable Long id){
        DubboResult result = iEsFocusPictureService.deleteFocusPicture(id);
        if(result.isSuccess()){
            return ApiResponse.success();
        }else{
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }
}
