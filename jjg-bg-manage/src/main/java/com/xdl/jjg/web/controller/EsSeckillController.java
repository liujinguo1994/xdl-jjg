package com.xdl.jjg.web.controller;


import com.xdl.jjg.constant.ApiStatus;
import com.xdl.jjg.constant.ErrorCode;
import com.xdl.jjg.model.form.EsSeckillForm;
import com.xdl.jjg.model.form.EsSeckillQueryForm;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.response.web.ApiPageResponse;
import com.xdl.jjg.util.BeanUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  前端控制器-限时抢购活动
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-06-05 09:25:43
 */
@Api(value = "/esSeckill",tags = "限时抢购活动")
@RestController
@RequestMapping("/esSeckill")
public class EsSeckillController {

    @Autowired
    private IEsSeckillService seckillService;

    @ApiOperation(value = "分页查询限时抢购活动列表",response = EsSeckillVO.class)
    @GetMapping(value = "/getSeckillList")
    @ResponseBody
    public ApiResponse getSeckillList(EsSeckillQueryForm form) {
        EsSeckillDTO esSeckillDTO = new EsSeckillDTO();
        BeanUtil.copyProperties(form,esSeckillDTO);
        DubboPageResult<EsSeckillDO> result = seckillService.getSeckillList(esSeckillDTO, form.getPageSize(), form.getPageNum());
        if (result.isSuccess()) {
            List<EsSeckillDO> data = result.getData().getList();
            List<EsSeckillVO> esSeckillVOList = BeanUtil.copyList(data,  EsSeckillVO.class);
            return ApiPageResponse.pageSuccess(result.getData().getTotal(),esSeckillVOList);
        } else {
            return ApiPageResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "添加限时抢购活动")
    @PostMapping(value = "/insertSeckill")
    @ResponseBody
    public ApiResponse insertSeckill(@Valid @RequestBody @ApiParam(name="限时抢购活动form对象",value="form") EsSeckillForm form){
        //校验参数
        this.verifyParam(form);

        EsSeckillDTO esSeckillDTO = new EsSeckillDTO();
        BeanUtil.copyProperties(form, esSeckillDTO);
        //设置活动状态为:编辑中
        esSeckillDTO.setState(1);
        DubboResult result = seckillService.insertSeckill(esSeckillDTO);
        if (result.isSuccess()) {
            return ApiResponse.success();
        }else{
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "修改限时抢购活动")
    @PutMapping(value = "/updateSeckill")
    @ResponseBody
    public ApiResponse updateSeckill(@Valid @RequestBody @ApiParam(name="限时抢购活动form对象",value="form") EsSeckillForm form){
        //校验参数
        this.verifyParam(form);

        EsSeckillDTO esSeckillDTO = new EsSeckillDTO();
        BeanUtil.copyProperties(form, esSeckillDTO);
        //设置活动状态为:编辑中
        esSeckillDTO.setState(1);
        DubboResult result = seckillService.updateSeckill(esSeckillDTO);
        if (result.isSuccess()) {
            return ApiResponse.success();
        }else{
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "发布限时抢购活动")
    @PostMapping(value = "/release")
    @ResponseBody
    public ApiResponse release(@Valid @RequestBody @ApiParam(name="限时抢购活动form对象",value="form") EsSeckillForm form){
        //校验参数
        this.verifyParam(form);

        EsSeckillDTO esSeckillDTO = new EsSeckillDTO();
        BeanUtil.copyProperties(form, esSeckillDTO);
        //设置活动状态为:发布中
        esSeckillDTO.setState(2);
        if(form.getId() == null || form.getId() == 0){
            DubboResult result = seckillService.insertSeckill(esSeckillDTO);
            if (result.isSuccess()) {
                return ApiResponse.success();
            }else{
                return ApiResponse.fail(ApiStatus.wrapperException(result));
            }
        }else{
            DubboResult result = seckillService.updateSeckill(esSeckillDTO);
            if (result.isSuccess()) {
                return ApiResponse.success();
            }else{
                return ApiResponse.fail(ApiStatus.wrapperException(result));
            }
        }
    }

    @GetMapping(value =	"/getSeckill/{id}")
    @ApiOperation(value	= "根据活动id查询一个限时抢购活动",response = EsSeckillVO.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "活动id",required = true,dataType = "long",paramType = "path")
    })
    @ResponseBody
    public ApiResponse getSeckill(@PathVariable Long id) {
        DubboResult<EsSeckillDO> result = seckillService.getSeckill(id);
        if (result.isSuccess()) {
            EsSeckillDO data = result.getData();
            EsSeckillVO seckillVO = new EsSeckillVO();
            BeanUtil.copyProperties(data,seckillVO);
            return ApiResponse.success(seckillVO);
        }else{
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    /**
     * 验证参数
     */
    public void verifyParam(EsSeckillForm form){
        //参数验证
        long startDay = form.getStartDay();
        long currentTime = System.currentTimeMillis() / 1000;
        long applyTime = form.getApplyEndTime();

        //活动时间不能小于当前时间
        if(startDay < currentTime){
            throw new ArgumentException(ErrorCode.START_DAY_ERROR.getErrorCode(), "活动时间不能小于当前时间");
        }
        //报名截止时间不能小于当前时间
//        if(applyTime < currentTime){
//            throw new ArgumentException(ErrorCode.APPLY_TIME_SMALL.getErrorCode(), "报名截止时间不能小于当前时间");
//        }
        //报名截止时间不能大于活动时间
//        if(startDay < applyTime){
//            throw new ArgumentException(ErrorCode.APPLY_TIME_BIG.getErrorCode(), "报名截止时间不能大于活动时间");
//        }

        List<Integer> termList = new ArrayList<>();
        for(Integer time : form.getRangeList()){
            if(termList.contains(time)){
                throw new ArgumentException(ErrorCode.RANGE_LIST_EXIT.getErrorCode(), "抢购区间的值不能重复");
            }
            //抢购区间的值不在0到23范围内
            if(time<0||time>23){
                throw new ArgumentException(ErrorCode.RANGE_ERROR.getErrorCode(), "抢购区间必须在0点到23点的整点时刻");
            }
            termList.add(time);
        }
    }

    @ApiOperation(value = "删除")
    @ApiImplicitParam(name = "id", value = "活动id", required = true, dataType = "long",example = "1", paramType = "path")
    @DeleteMapping(value = "/deleteSeckill/{id}")
    @ResponseBody
    public ApiResponse deleteSeckill(@PathVariable Long id){
        DubboResult result = seckillService.deleteSeckill(id);
        if(result.isSuccess()){
            return ApiResponse.success();
        }else{
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "下架")
    @ApiImplicitParam(name = "id", value = "活动id", required = true, dataType = "long",example = "1", paramType = "path")
    @PutMapping(value = "/unloadSeckill/{id}")
    @ResponseBody
    public ApiResponse unloadSeckill(@PathVariable Long id){
        DubboResult result = seckillService.unloadSeckill(id);
        if(result.isSuccess()){
            return ApiResponse.success();
        }else{
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }


}

