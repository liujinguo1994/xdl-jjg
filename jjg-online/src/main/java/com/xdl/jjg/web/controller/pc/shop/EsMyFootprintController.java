package com.xdl.jjg.web.controller.pc.shop;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.jjg.member.model.domain.EsMyFootprintDO;
import com.jjg.member.model.dto.EsMyFootprintDTO;
import com.jjg.member.model.vo.EsMyFootprintVO;
import com.jjg.shop.model.co.EsGoodsCO;
import com.jjg.trade.model.form.EsMyFootprintForm;
import com.jjg.trade.model.form.query.EsMyFootprintQueryForm;
import com.xdl.jjg.constant.ApiStatus;
import com.xdl.jjg.constant.MemberErrorCode;
import com.xdl.jjg.constant.TradeErrorCode;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.response.web.ApiResponse;
import com.xdl.jjg.roketmq.MQProducer;
import com.xdl.jjg.shiro.oath.ShiroKit;
import com.xdl.jjg.shiro.oath.ShiroUser;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.util.JsonUtil;
import com.xdl.jjg.web.controller.BaseController;
import com.xdl.jjg.web.service.feign.member.MyFootprintService;
import com.xdl.jjg.web.service.feign.shop.GoodsService;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;


/**
 * <p>
 *  会员活跃信息前端控制器
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2019-07-16 10:59:44
 */
@Api(value = "/myFootprint",tags = "我的足迹")
@RestController
@RequestMapping("/myFootprint")
public class EsMyFootprintController extends BaseController {

    @Autowired
    private MyFootprintService myFootprintService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private MQProducer mqProducer;

    @Value("${rocketmq.member.footprint.topic}")
    private String member_footprint_topic;

    @ApiOperation(value = "查询我的足迹列表", notes = "查询我的足迹列表", response = EsMyFootprintVO.class)
    @GetMapping("/getMyFootList")
    @ResponseBody
    public ApiResponse getMyFootList(@Valid EsMyFootprintQueryForm myFootprintQueryForm) {
        Long userId = ShiroKit.getUser().getId();
        if (null == userId) {
            return ApiResponse.fail(TradeErrorCode.NOT_LOGIN.getErrorCode(), TradeErrorCode.NOT_LOGIN.getErrorMsg());
        }
        myFootprintQueryForm.setMemberId(userId);
        if(myFootprintQueryForm == null){
            return ApiResponse.fail(TradeErrorCode.PARAM_ERROR.getErrorCode(), TradeErrorCode.PARAM_ERROR.getErrorMsg());
        }
        EsMyFootprintDTO myFootprintDTO = new EsMyFootprintDTO();
        BeanUtil.copyProperties(myFootprintQueryForm, myFootprintDTO);
        DubboResult<EsMyFootprintDO> result = myFootprintService.getMyFootprintList(myFootprintDTO);
        if (result.isSuccess()) {
            List<EsMyFootprintDO> list = (List<EsMyFootprintDO>) result.getData();
            Map<String, List<EsMyFootprintVO>>  dataMap = new TreeMap();
            List<EsMyFootprintVO> esMyFootprintVOList;
            Map<String, List<EsMyFootprintVO>> MatchsListMap;
            if (CollectionUtils.isNotEmpty(list)) {
                esMyFootprintVOList = BeanUtil.copyList(list,EsMyFootprintVO.class);
                for(EsMyFootprintVO myFootprintVO : esMyFootprintVOList){
                    DubboResult<EsGoodsCO> goodsCODubboResult = goodsService.getEsGoods(myFootprintVO.getGoodsId());
                    if(!goodsCODubboResult.isSuccess() || goodsCODubboResult.getData() == null){
                        continue;
                    }
                    myFootprintVO.setGoodName(goodsCODubboResult.getData().getGoodsName());
                    myFootprintVO.setGoodPrice(goodsCODubboResult.getData().getMktmoney());
                    if(StringUtils.isBlank(myFootprintVO.getImg())){
                        myFootprintVO.setImg(goodsCODubboResult.getData().getOriginal());
                    }

                }

                MatchsListMap=esMyFootprintVOList.stream().collect(Collectors.groupingBy(EsMyFootprintVO::getViewTime, TreeMap::new,Collectors.toList()));
                dataMap = ((TreeMap) MatchsListMap).descendingMap();
            }
            List<Map<String, Object>> resultList = new ArrayList<>();
            if(dataMap.size() > 0){
                for(Map.Entry<String, List<EsMyFootprintVO>> set : dataMap.entrySet()){
                    Map<String, Object> map = new HashMap<>();
                    map.put("date", set.getKey());
                    map.put("list", set.getValue());
                    resultList.add(map);
                }
            }
            return ApiResponse.success(resultList);
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }

//    @ApiOperation(value = "查询我的足迹列表-分页", notes = "查询我的足迹列表-分页")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "pageNum", value = "页码", required = true, dataType = "int", paramType = "query", example = "1"),
//            @ApiImplicitParam(name = "pageSize", value = "每页显示数量", required = true, dataType = "int", paramType = "query", example = "1"),
//    })
//    @GetMapping("/myFoot")
//    @ResponseBody
//    public ApiResponse getMyFoot(@NotEmpty int pageNum, @NotEmpty int pageSize) {
//        ShiroUser user = ShiroKit.getUser();
//        if (null == user) {
//            return ApiResponse.fail(TradeErrorCode.NOT_LOGIN.getErrorCode(), TradeErrorCode.NOT_LOGIN.getErrorMsg());
//        }
//        Long userId = user.getId();
//        DubboPageResult<EsMyFootprintDO> result = myFootprintService.getMyFootprintList(pageSize, pageNum,userId);
//        if (result.isSuccess()) {
//            Map<String, List<EsMyFootprintVO>>  dataMap = new TreeMap();
//            Map<String, List<EsMyFootprintVO>> MatchsListMap;
//            List<EsMyFootprintDO> myFootPrintList = result.getData().getList();
//            List<EsMyFootprintVO> esMyFootprintVOList;
//            if (CollectionUtils.isNotEmpty(myFootPrintList)) {
//                esMyFootprintVOList = BeanUtil.copyList(myFootPrintList, EsMyFootprintVO.class);
//                MatchsListMap=esMyFootprintVOList.stream().collect(Collectors.groupingBy(EsMyFootprintVO::getViewTime, TreeMap::new,Collectors.toList()));
//                dataMap = ((TreeMap) MatchsListMap).descendingMap();
//            }
//            return ApiResponse.success(DubboPageResult.success(dataMap));
//        }
//        return ApiResponse.fail(ApiStatus.wrapperException(result));
//    }

    @ApiOperation(value = "删除某天的浏览足迹", notes = "删除浏览足迹")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "viewTime", value = "浏览时间",dataType = "string",paramType = "path", example = "2020-03-27"),
    })
    @DeleteMapping(value = "/{viewTime}/deleteMyFoot")

    public ApiResponse deleteMyFoot(@PathVariable("viewTime") String viewTime) {
        ShiroUser user = ShiroKit.getUser();
        if (null == user) {
            return ApiResponse.fail(TradeErrorCode.NOT_LOGIN.getErrorCode(), TradeErrorCode.NOT_LOGIN.getErrorMsg());
        }
        Long userId = user.getId();
        DubboResult result = myFootprintService.deleteMyFoot(userId,viewTime);
        if (result.isSuccess()) {
            return ApiResponse.success(result.getData());
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }

    @ApiOperation(value = "删除浏览记录-by主键", notes = "删除浏览记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键", required = true, dataType = "Long", paramType = "path", example = "12"),
    })
    @DeleteMapping("/deleteMyFoot/{id}")
    @ResponseBody
    public ApiResponse deleteMyFoot(@PathVariable("id") Long id) {
        ShiroUser user = ShiroKit.getUser();
        if (null == user) {
            return ApiResponse.fail(TradeErrorCode.NOT_LOGIN.getErrorCode(), TradeErrorCode.NOT_LOGIN.getErrorMsg());
        }
        DubboResult result = myFootprintService.deleteMyFootById(id);
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
            return ApiResponse.fail(TradeErrorCode.NOT_LOGIN.getErrorCode(), TradeErrorCode.NOT_LOGIN.getErrorMsg());
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
            return ApiResponse.fail(TradeErrorCode.NOT_LOGIN.getErrorCode(), TradeErrorCode.NOT_LOGIN.getErrorMsg());
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
        try {
            // 会员插入足迹发送消息，解决查看商品耗时问题
            mqProducer.send(member_footprint_topic, JsonUtil.objectToJson(myFootprintDTO));
            logger.info("会员插入足迹发送消息成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ApiResponse.success();

    }
}

