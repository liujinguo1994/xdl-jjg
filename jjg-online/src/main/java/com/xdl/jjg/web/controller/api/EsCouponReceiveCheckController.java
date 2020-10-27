package com.xdl.jjg.web.controller.api;


import com.jjg.member.model.dto.EsCouponReceiveCheckDTO;
import com.jjg.trade.model.form.EsCouponReceiveCheckForm;
import com.xdl.jjg.constant.ApiStatus;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.response.web.ApiResponse;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.web.controller.BaseController;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author LiuJG 344009799@qq.com
 * @since 2020-07-06
 */
@RestController
@RequestMapping("/api/esCouponReceiveCheck")
public class EsCouponReceiveCheckController extends BaseController {

    @Reference(version = "${dubbo.application.version}",timeout = 5000)
    private IEsCouponReceiveCheckService iesCouponReceiveCheckService;


    @ApiOperation(value = "插入用戶信息", notes = "依据前端传递表单值新增")
    @PostMapping("/insertCouponReceiveCheck")
    public ApiResponse insertCouponReceiveCheck(@Valid EsCouponReceiveCheckForm esCouponReceiveCheckForm) {

        EsCouponReceiveCheckDTO esCouponReceiveCheckDTO = new EsCouponReceiveCheckDTO();
        BeanUtil.copyProperties(esCouponReceiveCheckForm, esCouponReceiveCheckDTO);
        DubboResult result = iesCouponReceiveCheckService.insertCouponReceiveCheck(esCouponReceiveCheckDTO);
        if (result.isSuccess()) {
            return ApiResponse.success(result.getData());
        }else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

}

