package com.xdl.jjg.web.controller.wap.member;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.shopx.common.model.result.ApiPageResponse;
import com.shopx.common.model.result.DubboPageResult;
import com.shopx.common.model.result.DubboResult;
import com.shopx.common.util.BeanUtil;
import com.shopx.goods.api.model.domain.cache.EsGoodsCO;
import com.shopx.goods.api.service.IEsGoodsService;
import com.shopx.member.api.constant.MemberErrorCode;
import com.shopx.member.api.model.domain.EsMyFootprintDO;
import com.shopx.member.api.model.domain.dto.EsMyFootprintDTO;
import com.shopx.member.api.model.domain.vo.EsMyFootprintVO;
import com.shopx.member.api.model.domain.vo.wap.WapMyFootVO;
import com.shopx.member.api.service.IEsMemberAddressService;
import com.shopx.member.api.service.IEsMyFootprintService;
import com.shopx.trade.api.constant.TradeErrorCode;
import com.shopx.trade.web.constant.ApiStatus;
import com.shopx.trade.web.request.EsMyFootprintForm;
import com.shopx.trade.web.shiro.oath.ShiroKit;
import com.shopx.trade.web.shiro.oath.ShiroUser;
import io.swagger.annotations.*;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.*;
import java.util.stream.Collectors;


@Api(value = "/wap/member/footprint", tags = "移动端-我的足迹")
@RestController
@RequestMapping("/wap/member/footprint")
public class EsWapMyFootController {

    private static Logger logger = LoggerFactory.getLogger(EsWapMyFootController.class);

    @Reference(version = "${dubbo.application.version}", timeout = 5000, check = false)
    private IEsMyFootprintService myFootprintService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsMemberAddressService iesMemberAddressService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsGoodsService goodsService;


    @ApiOperation(value = "查询我的足迹列表-分页", notes = "查询我的足迹列表-分页",response = EsMyFootprintVO.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页码", required = true, dataType = "int", paramType = "query", example = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页显示数量", required = true, dataType = "int", paramType = "query", example = "1"),
    })
    @GetMapping("/myFoot")
    @ResponseBody
    public ApiResponse getMyFoot(@NotEmpty int pageNum, @NotEmpty int pageSize,@RequestParam(value = "recentDay",defaultValue = Integer.MAX_VALUE+"") Integer recentDay) {
        Long userId = ShiroKit.getUser().getId();
        if (null == userId) {
            return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }
        DubboPageResult<EsMyFootprintDO> result = myFootprintService.getMyFootprintPage(pageSize, pageNum,userId);
        if (result.isSuccess()) {

            Map<String, List<EsMyFootprintVO>> dataMap = new TreeMap();
            Map<String, List<EsMyFootprintVO>> MatchsListMap;
            List<EsMyFootprintDO> myFootPrintList = result.getData().getList();
            List<EsMyFootprintVO> esMyFootprintVOList;
            if (CollectionUtils.isNotEmpty(myFootPrintList)) {
                esMyFootprintVOList = BeanUtil.copyList(myFootPrintList,EsMyFootprintVO.class);
                MatchsListMap=esMyFootprintVOList.stream().collect(Collectors.groupingBy(EsMyFootprintVO::getViewTime, TreeMap::new,Collectors.toList()));
                dataMap = ((TreeMap) MatchsListMap).descendingMap();
            }

            Iterator<Map.Entry<String, List<EsMyFootprintVO>>> iterator = dataMap.entrySet().iterator();
            List<WapMyFootVO> footVOList=new ArrayList<>();
            while(iterator.hasNext()) {
                WapMyFootVO myFootVO=new WapMyFootVO();
                Map.Entry<String, List<EsMyFootprintVO>> entry = iterator.next();
                String time = entry.getKey();
                List<EsMyFootprintVO> list = entry.getValue();
                for (EsMyFootprintVO footVo: list) {
                    DubboResult<EsGoodsCO> resultG = goodsService.getEsGoods(footVo.getGoodsId());
                    if (resultG.isSuccess()){
                        EsGoodsCO goodsCO = resultG.getData();
                        footVo.setGoodName(goodsCO.getGoodsName());
                        footVo.setQuantity(goodsCO.getQuantity());
                        footVo.setMoney(goodsCO.getMoney());
                        footVo.setImg(goodsCO.getOriginal());
                    }
                }
                myFootVO.setTime(time);
                myFootVO.setFootprintVOList(list);
                footVOList.add(myFootVO);

            }
            return ApiPageResponse.pageSuccess(result.getData().getTotal(), footVOList);
        }else {
            return ApiPageResponse.fail(ApiStatus.wrapperException(result));
        }
    }


    @ApiOperation(value = "删除某天的浏览足迹", notes = "删除浏览足迹")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "viewTime", value = "浏览时间",dataType = "string",paramType = "path", example = "2020-03-27"),
    })
    @DeleteMapping(value = "/{viewTime}/deleteMyFoot")

    public ApiResponse deleteMyFoot(@PathVariable("viewTime") String viewTime) {
        ShiroUser user = ShiroKit.getUser();
        if (null == user) {
            return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }
        Long userId = user.getId();
        DubboResult result = myFootprintService.deleteMyFoot(userId,viewTime);
        if (result.isSuccess()) {
            return ApiResponse.success(result.getData());
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }

    @ApiOperation(value = "批量删除浏览记录-by主键", notes = "批量删除浏览记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "主键数组", required = true, dataType = "Integer", paramType = "path"),
    })
    @DeleteMapping("/deleteMyFoot/{ids}")
    @ResponseBody
    public ApiResponse deleteMyFoot(@PathVariable("ids") Integer [] ids) {
        DubboResult result = myFootprintService.deleteMyFoot(ids);
        if (result.isSuccess()) {
            return ApiResponse.success();
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }

    @ApiOperation(value = "获取近十条浏览记录", notes = "获取近十条浏览记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "shopId", value = "店铺ID", required = true, dataType = "Long", paramType = "path", example = "1234"),
    })
    @GetMapping("/getTopMyFoot/{shopId}")
    @ResponseBody
    public ApiResponse getTopMyFoot(@PathVariable("shopId") Long shopId) {
        ShiroUser user = ShiroKit.getUser();
        if (null == user) {
            return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }
        Long userId = user.getId();
        if(null == shopId){
            return ApiResponse.fail(MemberErrorCode.SHOP_EXIST.getErrorCode(), MemberErrorCode.SHOP_EXIST.getErrorMsg());
        }
        DubboResult result = myFootprintService.getTopMyFoot(userId,shopId);
        if (result.isSuccess()) {
            return ApiResponse.success(result.getData());
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }

    @ApiOperation(value = "添加足迹", notes = "添加足迹", response = EsMyFootprintVO.class)
    @PostMapping("/insertMyFootprint")
    @ResponseBody
    public ApiResponse insertMyFootprint(@RequestBody @ApiParam(name = "myFootprintForm",value = "足迹对象",required = true) @Valid EsMyFootprintForm myFootprintForm) {
        ShiroUser user = ShiroKit.getUser();
        if (null == user) {
            return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }
        Long userId = user.getId();
        if(null == myFootprintForm){
            return ApiResponse.fail(MemberErrorCode.PARAM_ERROR.getErrorCode(), MemberErrorCode.PARAM_ERROR.getErrorMsg());
        }
        if(myFootprintForm.getGoodsId() == null){
            return ApiResponse.fail(TradeErrorCode.GOOD_ID_NOT.getErrorCode(), TradeErrorCode.GOOD_ID_NOT.getErrorMsg());
        }
        if(myFootprintForm.getShopId() == null){
            return ApiResponse.fail(TradeErrorCode.SHOP_ID_NOT.getErrorCode(), TradeErrorCode.SHOP_ID_NOT.getErrorMsg());
        }
        EsMyFootprintDTO myFootprintDTO = new EsMyFootprintDTO();
        BeanUtil.copyProperties(myFootprintForm, myFootprintDTO);
        myFootprintDTO.setMemberId(userId);
        DubboResult<EsMyFootprintDO> result = myFootprintService.insertMyFootprint(myFootprintDTO);
        if (result.isSuccess()) {
            return ApiResponse.success(result);
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }


    @ApiOperation(value = "清空我的足迹", notes = "清空我的足迹")
    @DeleteMapping("/clear")
    @ResponseBody
    public ApiResponse deleteMyFoot() {
        DubboResult result = myFootprintService.clearMyFoot(ShiroKit.getUser().getId());
        if (result.isSuccess()) {
            return ApiResponse.success();
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }
}