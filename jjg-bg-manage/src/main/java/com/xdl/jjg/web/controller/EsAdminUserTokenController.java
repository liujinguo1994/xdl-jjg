package com.xdl.jjg.web.controller;

import com.xdl.jjg.entity.EsAdminUserToken;
import com.xdl.jjg.model.dto.EsAdminUserTokenDTO;
import com.xdl.jjg.model.form.EsAdminUserTokenForm;
import com.xdl.jjg.model.vo.EsAdminUserTokenVO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.response.web.ApiPageResponse;
import com.xdl.jjg.response.web.RestResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.web.service.IEsAdminUserTokenService;
import io.swagger.annotations.ApiOperation;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author LiuJG 344009799@qq.com
 * @since 2020-10-09
 */
@RestController
@RequestMapping("/esAdminUserToken")
public class EsAdminUserTokenController {

    @Autowired
    private IEsAdminUserTokenService iesAdminUserTokenService;

    @ApiOperation(value = "查询AdminUserToken信息", notes = "通过Id查询AdminUserToken信息")
    @GetMapping("/getAdminUserTokenById")
    public Object getAdminUserTokenById(String id) {

        DubboResult<EsAdminUserToken> adminUserTokenResult = iesAdminUserTokenService.getAdminUserToken(id);
        if (adminUserTokenResult.isSuccess()) {
            EsAdminUserToken adminUserToken = adminUserTokenResult.getData();
            EsAdminUserTokenVO adminUserTokenVO = new EsAdminUserTokenVO();
            BeanUtils.copyProperties(adminUserToken, adminUserTokenVO);
            return RestResult.ok(adminUserTokenVO);
        }
        return RestResult.fail(adminUserTokenResult.getCode(), adminUserTokenResult.getMsg());
    }

    @ApiOperation(value = "插入AdminUserToken信息", notes = "插入AdminUserToken信息")
    @PostMapping("/insertAdminUserToken")
    public Object insertAdminUserToken(EsAdminUserTokenForm adminUserTokenForm) {

        EsAdminUserTokenDTO adminUserTokenDTO = new EsAdminUserTokenDTO();
        BeanUtil.copyProperties(adminUserTokenForm, adminUserTokenDTO);

        DubboResult adminUserTokenResult = iesAdminUserTokenService.insertAdminUserToken(adminUserTokenDTO);
        if (adminUserTokenResult.isSuccess()) {
            return RestResult.ok();
        }
        return RestResult.fail(adminUserTokenResult.getCode(), adminUserTokenResult.getMsg());

    }

    @ApiOperation(value = "查询AdminUserTokenList信息", notes = "查询AdminUserTokenList信息")
    @GetMapping("/getAdminUserTokenList")
    public Object getAdminUserTokenList(Boolean delFlag, @NotEmpty int pageSize, @NotEmpty int pageNum) {

        DubboPageResult adminUserTokenResult = iesAdminUserTokenService.getAdminUserTokenList(delFlag, pageSize, pageNum);

        List<EsAdminUserTokenVO> adminUserTokenVOS = BeanUtil.copyList(adminUserTokenResult.getData().getList(), EsAdminUserTokenVO.class);

        if (!adminUserTokenResult.isSuccess()) {
            return RestResult.fail(adminUserTokenResult.getCode(), adminUserTokenResult.getMsg());
        }
        return ApiPageResponse.pageSuccess(adminUserTokenResult.getData().getTotal(), adminUserTokenVOS);
    }

    @ApiOperation(value = "删除AdminUserToken信息", notes = "删除AdminUserTokenList信息")
    @DeleteMapping("/delAdminUserTokenByIds")
    public Object delAdminUserTokenByIds(String[] ids) {

        DubboResult adminUserTokenResult = iesAdminUserTokenService.deleteAdminUserToken(Arrays.asList(ids));

        if (!adminUserTokenResult.isSuccess()) {
            return RestResult.fail(adminUserTokenResult.getCode(), adminUserTokenResult.getMsg());
        }
        return RestResult.ok();
    }

    @ApiOperation(value = "修改AdminUserToken信息", notes = "修改AdminUserTokenList信息")
    @PutMapping("/updateAdminUserToken")
    public Object updateAdminUserToken(EsAdminUserTokenForm adminUserTokenForm) {

        EsAdminUserTokenDTO adminUserTokenDTO = new EsAdminUserTokenDTO();
        BeanUtils.copyProperties(adminUserTokenForm, adminUserTokenDTO);
        DubboResult adminUserTokenResult = iesAdminUserTokenService.updateAdminUserToken(adminUserTokenDTO);

        if (!adminUserTokenResult.isSuccess()) {
            return RestResult.fail(adminUserTokenResult.getCode(), adminUserTokenResult.getMsg());
        }
        return RestResult.ok();
    }

}

