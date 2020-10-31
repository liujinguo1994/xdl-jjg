package com.xdl.jjg.web.controller.pc.goods;


import com.jjg.member.model.dto.EsSearchKeyWordDTO;
import com.jjg.member.model.vo.EsSearchKeyWordVO;
import com.xdl.jjg.constant.ApiStatus;
import com.xdl.jjg.constant.TradeErrorCode;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.response.web.ApiResponse;
import com.xdl.jjg.shiro.oath.ShiroKit;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.web.controller.BaseController;
import com.xdl.jjg.web.service.feign.member.SearchKeyWordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  商城搜索栏 搜索历史接口
 * </p>
 *
 * @author LiuJG 344009799@qq.com
 * @since 2019-10-26
 */
@RestController
@RequestMapping("/esSearchKeyWord")
@Api(value = "/esSearchKeyWord",tags = "商城搜索栏 搜索历史接口")
public class EsSearchKeyWordController extends BaseController {

    @Reference(version = "${dubbo.application.version}",timeout = 5000)
    private SearchKeyWordService iesSearchKeyWordService;

    @ApiOperation(value = "搜索栏 搜索历史列表",response = EsSearchKeyWordVO.class)
    @GetMapping(value = "/getSearchKeyWordList")
    @ResponseBody
    public ApiResponse getSearchKeyWordList(){
        // 登录的情况
        if(ShiroKit.getUser() == null){
            return ApiResponse.fail(TradeErrorCode.NOT_LOGIN.getErrorCode(), TradeErrorCode.NOT_LOGIN.getErrorMsg());
        }
        Long memberId  = ShiroKit.getUser().getId();
        DubboPageResult searchKeyWordList = iesSearchKeyWordService.getSearchKeyWordList(memberId);
        if (searchKeyWordList.isSuccess()){
            List list = searchKeyWordList.getData().getList();

            List<EsSearchKeyWordVO> esSearchKeyWordVOS = BeanUtil.copyList(list, EsSearchKeyWordVO.class);
            return ApiResponse.success(esSearchKeyWordVOS);
        }else {
            return ApiResponse.fail(ApiStatus.wrapperException(searchKeyWordList));
        }
    }


    @ApiOperation(value = "新增搜索历史信息", notes = "根据登录后的会员ID 和 输入框内容")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "搜索词",name = "Key_word",dataType = "string", paramType = "query")}
    )
    @ResponseBody
    @PostMapping(value = "/addSearchKeyWord")
    public ApiResponse addSearchKeyWord(String Key_word) {
        // 登录的情况
        if(ShiroKit.getUser() == null){
            return ApiResponse.fail(TradeErrorCode.NOT_LOGIN.getErrorCode(), TradeErrorCode.NOT_LOGIN.getErrorMsg());
        }
        if (Key_word == null || "undefined".equals(Key_word)){
            return ApiResponse.success();
        }
        Long memberId  = ShiroKit.getUser().getId();
        EsSearchKeyWordDTO esSearchKeyWordDTO = new EsSearchKeyWordDTO();
        esSearchKeyWordDTO.setMemberId(memberId);
        esSearchKeyWordDTO.setSearchKeyword(Key_word);
        DubboResult dubboResult = this.iesSearchKeyWordService.insertSearchKeyWord(esSearchKeyWordDTO);
        if (dubboResult.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(dubboResult));
        }
    }


    /**
     * 根据Id 删除单个搜索历史
     * @param id
     * @return
     */
    @DeleteMapping(value = "/deleteSearchKeyWord/{id}")
    @ApiOperation(value = "删除单个搜索历史",notes = "根据Id删除单个搜索历史")
    @ApiImplicitParam(name = "id", value = "主键id")
    @ResponseBody
    public ApiResponse deleteSearchKeyWord(@PathVariable Long id){

        DubboResult dubboResult = this.iesSearchKeyWordService.deleteSearchKeyWord(id);

        if(dubboResult.isSuccess()){
            return ApiResponse.success(dubboResult.getData());
        }
        return ApiResponse.fail(ApiStatus.wrapperException(dubboResult));
    }

    /**
     * 根据memberId 批量删除搜索历史
     * @param
     * @return
     */
    @DeleteMapping(value = "/deleteSearchKeyWordBatch")
    @ApiOperation(value = "批量删除搜索历史",notes = "根据会员Id批量删除搜索历史")
    @ResponseBody
    public ApiResponse deleteSearchKeyWordBatch(){
        // 登录的情况
        if(ShiroKit.getUser() == null){
            return ApiResponse.fail(TradeErrorCode.NOT_LOGIN.getErrorCode(), TradeErrorCode.NOT_LOGIN.getErrorMsg());
        }
        Long memberId  = ShiroKit.getUser().getId();
        DubboResult dubboResult = this.iesSearchKeyWordService.deleteSearchKeyWordBatch(memberId);

        if(dubboResult.isSuccess()){
            return ApiResponse.success(dubboResult.getData());
        }
        return ApiResponse.fail(ApiStatus.wrapperException(dubboResult));
    }

}

