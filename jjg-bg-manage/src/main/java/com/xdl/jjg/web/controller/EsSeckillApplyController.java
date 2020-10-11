package com.xdl.jjg.web.controller;

import com.xdl.jjg.constant.ApiStatus;
import com.xdl.jjg.model.form.EsSeckillApplyForm;
import com.xdl.jjg.model.form.EsSeckillApplyQueryForm;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.response.web.ApiPageResponse;
import com.xdl.jjg.response.web.ApiResponse;
import com.xdl.jjg.util.BeanUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 *  前端控制器-限时抢购商品
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-06-05 09:25:43
 */
@Api(value = "/esSeckillApply",tags = "限时抢购商品")
@RestController
@RequestMapping("/esSeckillApply")
public class EsSeckillApplyController {

    @Reference(version = "${dubbo.application.version}")
    private IEsSeckillApplyService seckillApplyService;

    @ApiOperation(value = "分页查询限时抢购商品列表",response = EsSeckillApplyVO.class)
    @GetMapping(value = "/getSeckillApplyList")
    @ResponseBody
    public ApiResponse getSeckillApplyList(@Valid EsSeckillApplyQueryForm form) {
        EsSeckillApplyDTO esSeckillApplyDTO = new EsSeckillApplyDTO();
        BeanUtil.copyProperties(form,esSeckillApplyDTO);
        DubboPageResult<EsSeckillApplyDO> result = seckillApplyService.getSeckillApplyList(esSeckillApplyDTO, form.getPageSize(), form.getPageNum());
        if (result.isSuccess()) {
            List<EsSeckillApplyDO> data = result.getData().getList();
            List<EsSeckillApplyVO> list = BeanUtil.copyList(data, EsSeckillApplyVO.class);
            return ApiPageResponse.pageSuccess(result.getData().getTotal(),list);
        } else {
            return ApiPageResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "审核限时抢购商品")
    @PutMapping(value = "/reviewGoods")
    @ResponseBody
    public ApiResponse reviewGoods(@Valid @RequestBody @ApiParam(name="审核限时抢购商品form对象",value="form") EsSeckillApplyForm form){
        EsSeckillApplyDTO dto = new EsSeckillApplyDTO();
        BeanUtil.copyProperties(form,dto);
        if (form.getStatus() == 0){//通过
            dto.setState(1);
        }else if (form.getStatus() ==1){//不通过
            dto.setState(2);
        }
       DubboResult result = seckillApplyService.updateSeckillApply(dto);
        if (result.isSuccess()) {
            return ApiResponse.success();
        }else{
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }
}

