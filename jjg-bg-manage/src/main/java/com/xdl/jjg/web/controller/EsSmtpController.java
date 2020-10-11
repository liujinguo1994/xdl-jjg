package com.xdl.jjg.web.controller;

import com.xdl.jjg.constant.ApiStatus;
import com.xdl.jjg.model.domain.EsSmtpDO;
import com.xdl.jjg.model.dto.EsSmtpDTO;
import com.xdl.jjg.model.form.EsQueryPageForm;
import com.xdl.jjg.model.form.EsSmtpForm;
import com.xdl.jjg.model.vo.EsSmtpVO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.response.web.ApiPageResponse;
import com.xdl.jjg.response.web.ApiResponse;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.web.service.IEsSendEmailService;
import com.xdl.jjg.web.service.IEsSmtpService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器-邮件设置管理
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
@RestController
@RequestMapping("/esSmtp")
@Api(value="/esSmtp", tags="邮件设置管理")
public class EsSmtpController {

    @Autowired
    private IEsSmtpService iEsSmtpService;

    @Autowired
    private IEsSendEmailService sendEmailService;

    @ApiOperation(value = "邮件设置管理分页查询",response = EsSmtpVO.class)
    @GetMapping(value = "/getSmtpList")
    @ResponseBody
    public ApiResponse getSmtpList(EsQueryPageForm form){
        EsSmtpDTO esSmtpDTO = new EsSmtpDTO();
        DubboPageResult<EsSmtpDO> result = iEsSmtpService.getSmtpList(esSmtpDTO, form.getPageSize(), form.getPageNum());
        if(result.isSuccess()){
            List<EsSmtpVO> list = BeanUtil.copyList(result.getData().getList(),EsSmtpVO.class);
            return ApiPageResponse.pageSuccess(result.getData().getTotal(),list);
        }else{
            return ApiPageResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "添加邮件设置")
    @PostMapping(value = "/insertSmtp")
    @ResponseBody
    public ApiResponse insertSmtp(@RequestBody @ApiParam(name="邮件设置form对象",value="form") EsSmtpForm form){
        EsSmtpDTO esSmtpDTO = new EsSmtpDTO();
        BeanUtil.copyProperties(form,esSmtpDTO);
        DubboResult result = iEsSmtpService.insertSmtp(esSmtpDTO);
        if (result.isSuccess()) {
            return ApiResponse.success();
        }else{
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "修改邮件设置")
    @PutMapping(value = "/updateSmtp")
    @ResponseBody
    public ApiResponse updateSmtp(@RequestBody @ApiParam(name="邮件设置form对象",value="form") EsSmtpForm form){
        EsSmtpDTO esSmtpDTO = new EsSmtpDTO();
        BeanUtil.copyProperties(form,esSmtpDTO);
        DubboResult result = iEsSmtpService.updateSmtp(esSmtpDTO);
        if (result.isSuccess()) {
            return ApiResponse.success();
        }else{
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @DeleteMapping(value = "/deleteSmtp/{id}")
    @ResponseBody
    @ApiOperation(value = "删除")
    @ApiImplicitParam(name = "id", value = "主键id", required = true, dataType = "long",example = "1", paramType = "path")
    public ApiResponse deleteSmtp(@PathVariable Long id){
        DubboResult result = iEsSmtpService.deleteSmtp(id);
        if(result.isSuccess()){
            return ApiResponse.success();
        }else{
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    /*@ApiOperation(value = "查询当前stmp方案")
    @GetMapping(value = "/getCurrentSmtp")
    @ResponseBody
    public ApiResponse getCurrentSmtp(){
        DubboResult<EsSmtpDO> result = iEsSmtpService.getCurrentSmtp();
        if(result.isSuccess()){
            EsSmtpVO vo = new EsSmtpVO();
            BeanUtil.copyProperties(result.getData(),vo);
            return ApiResponse.success(vo);
        }else{
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }*/

   /* @ApiOperation(value = "邮件发送测试")
    @GetMapping(value = "/smtpSendTest")
    @ResponseBody
    public ApiResponse smtpSendTest() {
        EsSendEmailDTO esSendEmailDTO = new EsSendEmailDTO();
        esSendEmailDTO.setContent("你好，小明同学");
        esSendEmailDTO.setTitle("测试邮件");
        esSendEmailDTO.setEmail("ruanming1105@163.com");
        DubboResult result = sendEmailService.sendEmail(esSendEmailDTO);
        if(result.isSuccess()){
            return ApiResponse.success();
        }else{
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }*/


}
