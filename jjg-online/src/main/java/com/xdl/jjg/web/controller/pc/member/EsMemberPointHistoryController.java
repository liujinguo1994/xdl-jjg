package com.xdl.jjg.web.controller.pc.member;

import com.shopx.common.model.result.ApiPageResponse;
import com.shopx.common.model.result.ApiResponse;
import com.shopx.common.model.result.DubboPageResult;
import com.shopx.common.model.result.DubboResult;
import com.shopx.common.util.BeanUtil;
import com.shopx.common.web.BaseController;
import com.shopx.member.api.constant.MemberErrorCode;
import com.shopx.member.api.model.domain.EsMemberDO;
import com.shopx.member.api.model.domain.EsMemberPointHistoryDO;
import com.shopx.member.api.model.domain.vo.EsMemberPointHistoryBuyerVO;
import com.shopx.member.api.model.domain.vo.EsMemberPointHistoryVO;
import com.shopx.member.api.service.IEsMemberPointHistoryService;
import com.shopx.member.api.service.IEsMemberService;
import com.shopx.trade.web.constant.ApiStatus;
import com.shopx.trade.web.shiro.oath.ShiroKit;
import com.shopx.trade.web.shiro.oath.ShiroUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 会员积分明细 前端控制器
 * </p>
 *
 * @author HQL 236154186@qq.com
 * @since 2019-05-29
 */
@Api(value = "/esMemberPointHistory", tags = "我的积分明细")
@RestController
@RequestMapping("/esMemberPointHistory")
public class EsMemberPointHistoryController extends BaseController {

    @Reference(version = "${dubbo.application.version}",timeout = 5000,check = false)
    private IEsMemberPointHistoryService iesMemberPointHistoryService;

    @Reference(version = "${dubbo.application.version}",timeout = 5000,check = false)
    private IEsMemberService memberService;

    @ApiOperation(value = "积分详细列表", notes = "积分详细列表", response = EsMemberPointHistoryVO.class)
    @GetMapping("/selectList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页码", required = true, dataType = "int", paramType = "query", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "分页大小", required = true, dataType = "int", paramType = "query", defaultValue = "10"),
    })
    public ApiResponse selectList(Integer gradePointType, int pageSize, int pageNum) {
        ShiroUser user = ShiroKit.getUser();
        if(user == null){
            return ApiPageResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }
        Long userId = user.getId();

        //获取积分详细
        DubboPageResult result = iesMemberPointHistoryService.getMemberPointHistoryDetail(userId, null,
                10, 1);
        List<EsMemberPointHistoryVO> list = new ArrayList<>();
        if (result.isSuccess()) {
            if(CollectionUtils.isNotEmpty(result.getData().getList())){
                List<EsMemberPointHistoryDO> memberPointHistoryDOList = result.getData().getList();
                list = BeanUtil.copyList(memberPointHistoryDOList, EsMemberPointHistoryVO.class);
                list =  list.stream().map(e -> setDescription(e)).collect(Collectors.toList());
            }
            return ApiPageResponse.pageSuccess(result.getData().getTotal(), list);
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }

    @ApiOperation(value = "我的积分统计", notes = "我的积分统计", response = EsMemberPointHistoryBuyerVO.class)
    @GetMapping("/pointStatistics")
    public ApiResponse pointStatistics() {
        ShiroUser user = ShiroKit.getUser();
        if(user == null){
            return ApiPageResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }
        Long userId = user.getId();
        EsMemberPointHistoryBuyerVO memberPointHistoryBuyerVO = new EsMemberPointHistoryBuyerVO();
        //获取会员总积分
        DubboResult<EsMemberDO> memberResult = memberService.getMember(userId);
        if(memberResult.isSuccess() && memberResult.getData() != null){
            memberPointHistoryBuyerVO.setTotalNum(memberResult.getData().getConsumPoint());
        }
        //获取积分详细
        DubboPageResult result = iesMemberPointHistoryService.pointStatistics(userId);
        if (result.isSuccess()) {
            if(CollectionUtils.isNotEmpty(result.getData().getList())){
                List memberPointHistoryDOList = result.getData().getList();
                List<EsMemberPointHistoryVO> list = BeanUtil.copyList(memberPointHistoryDOList, EsMemberPointHistoryVO.class);
                Map<Integer,List<EsMemberPointHistoryVO>> map = list.stream().collect(Collectors.groupingBy(EsMemberPointHistoryVO::getReason));
                memberPointHistoryBuyerVO = sumTotal(map, memberPointHistoryBuyerVO);
            }
            return ApiResponse.success(memberPointHistoryBuyerVO);
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }

    private EsMemberPointHistoryVO setDescription(EsMemberPointHistoryVO memberPointHistoryVO){
        Integer reason = memberPointHistoryVO.getReason();
        switch (reason){
            case 1:
                memberPointHistoryVO.setDescription("购物赠送积分");
                break;
            case 2:
                memberPointHistoryVO.setDescription("评价赠送积分");
                break;
            default:
                memberPointHistoryVO.setDescription("其他赠送积分");
                break;
        }
        return memberPointHistoryVO;
    }

    private EsMemberPointHistoryBuyerVO sumTotal(Map<Integer,List<EsMemberPointHistoryVO>> map, EsMemberPointHistoryBuyerVO memberPointHistoryBuyerVO){
        List<EsMemberPointHistoryVO> list = new ArrayList<>();
        list = map.get(0);
        if(CollectionUtils.isNotEmpty(list)){
            memberPointHistoryBuyerVO.setOtherNum((long) list.size());
        }
        list = map.get(1);
        if(CollectionUtils.isNotEmpty(list)){
            memberPointHistoryBuyerVO.setShoppingNum((long) list.size());
        }
        list = map.get(2);
        if(CollectionUtils.isNotEmpty(list)){
            memberPointHistoryBuyerVO.setCommentNum((long) list.size());
        }
        return memberPointHistoryBuyerVO;
    }
//    @ApiOperation(value = "积分详细列表", notes = "积分详细列表")
//    @GetMapping("/select")
//    public ApiResponse add(@Valid EsMemberPointHistoryForm esMemberPointHistoryForm) {
//        EsMemberPointHistoryDTO esMemberPointHistoryDTO = new EsMemberPointHistoryDTO();
//        BeanUtil.copyProperties(esMemberPointHistoryForm, esMemberPointHistoryDTO);
//        DubboResult result = iesMemberPointHistoryService.insertMemberPointHistory(esMemberPointHistoryDTO);
//        if (result.isSuccess()) {
//            return ApiResponse.success(result.getData());
//        }
//        return ApiResponse.fail(ApiStatus.wrapperException(result));
//    }

}

