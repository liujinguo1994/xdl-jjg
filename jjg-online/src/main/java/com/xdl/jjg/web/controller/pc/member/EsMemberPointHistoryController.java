package com.xdl.jjg.web.controller.pc.member;

import com.jjg.member.model.domain.EsMemberDO;
import com.jjg.member.model.domain.EsMemberPointHistoryDO;
import com.jjg.member.model.vo.EsMemberPointHistoryBuyerVO;
import com.jjg.member.model.vo.EsMemberPointHistoryVO;
import com.xdl.jjg.constant.ApiStatus;
import com.xdl.jjg.constant.TradeErrorCode;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.response.web.ApiPageResponse;
import com.xdl.jjg.response.web.ApiResponse;
import com.xdl.jjg.shiro.oath.ShiroKit;
import com.xdl.jjg.shiro.oath.ShiroUser;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.web.controller.BaseController;
import com.xdl.jjg.web.service.feign.member.MemberPointHistoryService;
import com.xdl.jjg.web.service.feign.member.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private MemberPointHistoryService iesMemberPointHistoryService;

    @Autowired
    private MemberService memberService;

    @ApiOperation(value = "积分详细列表", notes = "积分详细列表", response = EsMemberPointHistoryVO.class)
    @GetMapping("/selectList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页码", required = true, dataType = "int", paramType = "query", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "分页大小", required = true, dataType = "int", paramType = "query", defaultValue = "10"),
    })
    public ApiResponse selectList(Integer gradePointType, int pageSize, int pageNum) {
        ShiroUser user = ShiroKit.getUser();
        if(user == null){
            return ApiPageResponse.fail(TradeErrorCode.NOT_LOGIN.getErrorCode(), TradeErrorCode.NOT_LOGIN.getErrorMsg());
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
            return ApiPageResponse.fail(TradeErrorCode.NOT_LOGIN.getErrorCode(), TradeErrorCode.NOT_LOGIN.getErrorMsg());
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

