package com.xdl.jjg.web.controller.pc.member;


import com.jjg.member.model.domain.EsComplaintBuyerOrderItemsDO;
import com.jjg.member.model.domain.EsComplaintOrderDO;
import com.jjg.member.model.dto.EsComplaintDTO;
import com.jjg.member.model.dto.EsComrImglDTO;
import com.jjg.member.model.vo.EsComplaintBuyerOrderItemsVO;
import com.jjg.member.model.vo.EsComplaintOrderVO;
import com.jjg.member.model.vo.EsComplaintVO;
import com.jjg.trade.model.form.EsComplaintForm;
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
import com.xdl.jjg.web.service.feign.member.ComplaintService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    @Autowired
    private ComplaintService complaintService;

    @PostMapping
    @ApiOperation(value = "新增投诉信息", notes = "新增投诉信息")
    public ApiResponse addComplaint(@Valid EsComplaintForm complaintForm) {
        ShiroUser user = ShiroKit.getUser();
        if (null == user) {
            return ApiResponse.fail(TradeErrorCode.NOT_LOGIN.getErrorCode(), TradeErrorCode.NOT_LOGIN.getErrorMsg());
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
            return ApiResponse.fail(TradeErrorCode.NOT_LOGIN.getErrorCode(), TradeErrorCode.NOT_LOGIN.getErrorMsg());
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

