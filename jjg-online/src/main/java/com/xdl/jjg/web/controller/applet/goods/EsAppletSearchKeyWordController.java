package com.xdl.jjg.web.controller.applet.goods;

import com.shopx.common.model.result.ApiResponse;
import com.shopx.common.model.result.DubboPageResult;
import com.shopx.common.model.result.DubboResult;
import com.shopx.common.util.BeanUtil;
import com.shopx.member.api.constant.MemberErrorCode;
import com.shopx.member.api.model.domain.EsMemberDO;
import com.shopx.member.api.model.domain.dto.EsSearchKeyWordDTO;
import com.shopx.member.api.model.domain.vo.EsSearchKeyWordVO;
import com.shopx.member.api.service.IEsMemberService;
import com.shopx.member.api.service.IEsSearchKeyWordService;
import com.shopx.trade.web.constant.ApiStatus;
import com.shopx.trade.web.request.AppletSearchKeyWordForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 *  小程序-搜索历史接口
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-06-23 09:28:26
 */
@RestController
@RequestMapping("/applet/goods/esSearchKeyWord")
@Api(value = "/applet/goods/esSearchKeyWord",tags = "小程序-搜索历史接口")
public class EsAppletSearchKeyWordController {

    @Reference(version = "${dubbo.application.version}",timeout = 5000)
    private IEsSearchKeyWordService iesSearchKeyWordService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsMemberService memberService;

    @ApiOperation(value = "搜索栏 搜索历史列表",response = EsSearchKeyWordVO.class)
    @GetMapping(value = "/getSearchKeyWordList/{skey}")
    @ApiImplicitParam(name = "skey", value = "登录态标识", required =true, dataType = "String" ,paramType="path")
    @ResponseBody
    public ApiResponse getSearchKeyWordList(@PathVariable String skey){
        DubboResult<EsMemberDO> dubboResult = memberService.getMemberBySkey(skey);
        if (!dubboResult.isSuccess() || dubboResult.getData() == null) {
            return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }
        EsMemberDO memberDO = dubboResult.getData();
        DubboPageResult searchKeyWordList = iesSearchKeyWordService.getSearchKeyWordList(memberDO.getId());
        if (searchKeyWordList.isSuccess()){
            List list = searchKeyWordList.getData().getList();
            List<EsSearchKeyWordVO> esSearchKeyWordVOS = BeanUtil.copyList(list, EsSearchKeyWordVO.class);
            return ApiResponse.success(esSearchKeyWordVOS);
        }else {
            return ApiResponse.fail(ApiStatus.wrapperException(searchKeyWordList));
        }
    }


    @ApiOperation(value = "新增搜索历史信息", notes = "根据登录后的会员ID 和 输入框内容")
    @ResponseBody
    @PostMapping(value = "/addSearchKeyWord")
    public ApiResponse addSearchKeyWord(@RequestBody @Valid AppletSearchKeyWordForm form) {
        DubboResult<EsMemberDO> dubboResult = memberService.getMemberBySkey(form.getSkey());
        if (!dubboResult.isSuccess() || dubboResult.getData() == null) {
            return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }
        EsMemberDO memberDO = dubboResult.getData();
        if (form.getSearchKeyword() == null || "undefined".equals(form.getSearchKeyword())){
            return ApiResponse.success();
        }
        EsSearchKeyWordDTO esSearchKeyWordDTO = new EsSearchKeyWordDTO();
        esSearchKeyWordDTO.setMemberId(memberDO.getId());
        esSearchKeyWordDTO.setSearchKeyword(form.getSearchKeyword());
        DubboResult result = this.iesSearchKeyWordService.insertSearchKeyWord(esSearchKeyWordDTO);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @DeleteMapping(value = "/deleteSearchKeyWord/{id}")
    @ApiOperation(value = "删除单个搜索历史")
    @ApiImplicitParam(name = "id", value = "主键id", required =true, dataType = "long" ,paramType="path")
    @ResponseBody
    public ApiResponse deleteSearchKeyWord(@PathVariable Long id){
        DubboResult dubboResult = this.iesSearchKeyWordService.deleteSearchKeyWord(id);
        if(dubboResult.isSuccess()){
            return ApiResponse.success(dubboResult.getData());
        }else {
            return ApiResponse.fail(ApiStatus.wrapperException(dubboResult));
        }
    }


    @DeleteMapping(value = "/deleteSearchKeyWordBatch/{skey}")
    @ApiOperation(value = "批量删除搜索历史")
    @ApiImplicitParam(name = "skey", value = "登录态标识", required =true, dataType = "String" ,paramType="path")
    @ResponseBody
    public ApiResponse deleteSearchKeyWordBatch(@PathVariable String skey){
        DubboResult<EsMemberDO> dubboResult = memberService.getMemberBySkey(skey);
        if (!dubboResult.isSuccess() || dubboResult.getData() == null) {
            return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }
        EsMemberDO memberDO = dubboResult.getData();
        DubboResult result = this.iesSearchKeyWordService.deleteSearchKeyWordBatch(memberDO.getId());
        if(result.isSuccess()){
            return ApiResponse.success(result.getData());
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }

}

