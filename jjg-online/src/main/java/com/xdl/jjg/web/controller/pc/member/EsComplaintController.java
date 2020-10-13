package com.xdl.jjg.web.controller.pc.member;

import com.shopx.common.model.result.ApiPageResponse;
import com.shopx.common.model.result.DubboPageResult;
import com.shopx.common.model.result.DubboResult;
import com.shopx.common.util.BeanUtil;
import com.shopx.common.web.BaseController;
import com.shopx.member.api.constant.MemberErrorCode;
import com.shopx.member.api.model.domain.EsComplaintBuyerOrderItemsDO;
import com.shopx.member.api.model.domain.EsComplaintOrderDO;
import com.shopx.member.api.model.domain.dto.EsComplaintDTO;
import com.shopx.member.api.model.domain.dto.EsComrImglDTO;
import com.shopx.member.api.model.domain.vo.EsComplaintBuyerOrderItemsVO;
import com.shopx.member.api.model.domain.vo.EsComplaintOrderVO;
import com.shopx.member.api.model.domain.vo.EsComplaintVO;
import com.shopx.member.api.service.IEsComplaintService;
import com.shopx.trade.web.constant.ApiStatus;
import com.shopx.trade.web.request.EsComplaintForm;
import com.shopx.trade.web.shiro.oath.ShiroKit;
import com.shopx.trade.web.shiro.oath.ShiroUser;
import io.swagger.annotations.*;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * <p>
 * 投诉信息表--es_complaint 前端控制器
 * </p>
 *
 * @author LBW 981087977@qq.com
 * @since 2019-12-11 09:28:26
 */
@Api(value = "/complaint",tags = "投诉信息模块接口")
@RestController
@RequestMapping("/complaint")
public class EsComplaintController extends BaseController {

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsComplaintService complaintService;

    @PostMapping
    @ApiOperation(value = "新增投诉信息", notes = "新增投诉信息")
    public ApiResponse addComplaint(@Valid EsComplaintForm complaintForm) {
        ShiroUser user = ShiroKit.getUser();
        if (null == user) {
            return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }
        Long userId = user.getId();
        EsComplaintDTO complaintDTO = new EsComplaintDTO();
        BeanUtil.copyProperties(complaintForm, complaintDTO);
        complaintDTO.setMemberId(userId);
        String imgStr = complaintForm.getImgStr();
        complaintDTO.setComrImglDTOList(openImg(imgStr));
        DubboResult result = complaintService.insertComplaintBuyer(complaintDTO);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    private List<EsComrImglDTO> openImg(String imgStr){
        String[] array = imgStr.split(",");
        List<EsComrImglDTO> list = new ArrayList<>();
        for(int i = 0; i < array.length; i++){
            EsComrImglDTO comrImglDTO = new EsComrImglDTO();
            comrImglDTO.setOriginal(array[i]);
            comrImglDTO.setSort(i);
            comrImglDTO.setType(0);
            list.add(comrImglDTO);
        }
        return list;
    }

    @DeleteMapping(value = "/updateComplaint/{id}")
    @ResponseBody
    @ApiOperation(value = "撤销投诉信息")
    @ApiImplicitParam(name = "id",value ="主键Id" ,dataType = "long",paramType = "path",required = true,example = "1")
    public ApiResponse updateComplaint(@PathVariable Long id) {
        EsComplaintDTO complaintDTO = new EsComplaintDTO();
        complaintDTO.setId(id);
        complaintDTO.setIsDel(1);
        DubboResult result = complaintService.updateComplaint(complaintDTO);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @GetMapping(value = "/getComplaint/{id}")
    @ResponseBody
    @ApiOperation(value = "根据主键ID获取投诉信息", notes = "根据主键ID获取投诉信息", response = EsComplaintVO.class)
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "投诉信息表--es_complaint主键id", required = true, dataType = "long", paramType = "query", example = "1")})
    public ApiResponse<EsComplaintVO> getComplaint(@PathVariable Long id) {
        DubboResult result = complaintService.getComplaint(id);
        if (result.isSuccess()) {
            EsComplaintVO complaintVO = new EsComplaintVO();
            BeanUtil.copyProperties(result.getData(), complaintVO);
            return ApiResponse.success(complaintVO);
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @GetMapping("/getComplaintListBuyerPage")
    @ResponseBody
    @ApiOperation(value = "获取投诉信息列表", notes = "获取投诉信息列表", response = EsComplaintOrderVO.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageSize", value = "页数", dataType = "int", paramType = "query", required = true, example = "10"),
            @ApiImplicitParam(name = "pageNum", value = "页码", dataType = "int", paramType = "query", required = true, example = "1")})
    public ApiResponse<EsComplaintVO> getComplaintListBuyerPage(@NotEmpty(message = "页数不能为空") int pageSize,
                                                          @NotEmpty(message = "页码不能为空") int pageNum) {
        ShiroUser user = ShiroKit.getUser();
        if (null == user) {
            return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }
        Long userId = user.getId();
        DubboPageResult result = complaintService.getComplaintListBuyerPage(userId, pageSize, pageNum);
        List<EsComplaintOrderDO> complaintDOList = result.getData().getList();
        if (!result.isSuccess()) {
            return ApiPageResponse.fail(ApiStatus.wrapperException(result));
        }
        if (CollectionUtils.isNotEmpty(complaintDOList)) {
            List<EsComplaintOrderVO> complaintVOList = complaintDOList.stream().map(complaintDO -> {
                EsComplaintOrderVO complaintOrderVO = new EsComplaintOrderVO();
                BeanUtil.copyProperties(complaintDO, complaintOrderVO);
                List<EsComplaintBuyerOrderItemsDO> complaintBuyerOrderItemsDOList = complaintDO.getEsBuyerOrderItemsDO();
                if(CollectionUtils.isNotEmpty(complaintBuyerOrderItemsDOList)){
                    List<EsComplaintBuyerOrderItemsVO> complaintBuyerOrderItemsVOList = BeanUtil.copyList(complaintBuyerOrderItemsDOList, EsComplaintBuyerOrderItemsVO.class);
                    complaintOrderVO.setEsBuyerOrderItemsVO(complaintBuyerOrderItemsVOList);
                }
                return complaintOrderVO;
            }).collect(Collectors.toList());
            return ApiPageResponse.pageSuccess(result.getData().getTotal(), complaintVOList);
        }
        return ApiPageResponse.success(result);
    }
}

