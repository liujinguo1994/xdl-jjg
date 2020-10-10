package com.xdl.jjg.web.controller;

import com.xdl.jjg.entity.EsAdminUser;
import com.xdl.jjg.model.dto.EsAdminUserDTO;
import com.xdl.jjg.model.form.EsAdminUserForm;
import com.xdl.jjg.model.vo.EsAdminUserVO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.response.web.ApiPageResponse;
import com.xdl.jjg.response.web.RestResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.web.service.IEsAdminUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author LiuJG 344009799@qq.com
 * @since 2020-10-09
 */
@Api(value = "/admin")
@RestController
@RequestMapping("/esAdminUser")
public class EsAdminUserController {

    @Autowired
    private IEsAdminUserService iesAdminUserService;

    @ApiOperation(value = "查询AdminUser信息", notes = "通过Id查询AdminUser信息")
    @GetMapping("/getAdminUserById")
    public Object getAdminUserById(String id)  {

        DubboResult<EsAdminUser> adminUserResult =  iesAdminUserService.getAdminUser(id);
        if(adminUserResult.isSuccess()){
            EsAdminUser adminUser = adminUserResult.getData();
            EsAdminUserVO adminUserVO = new EsAdminUserVO();
            BeanUtils.copyProperties(adminUser,adminUserVO);
            return RestResult.ok(adminUserVO);
        }
        return RestResult.fail(adminUserResult.getCode(),adminUserResult.getMsg());
    }

    @ApiOperation(value = "插入AdminUser信息", notes = "插入AdminUser信息")
    @PostMapping("/insertAdminUser")
    public Object insertAdminUser(EsAdminUserForm adminUserForm)  {

        EsAdminUserDTO adminUserDTO = new EsAdminUserDTO();
        BeanUtil.copyProperties(adminUserForm,adminUserDTO);

        DubboResult adminUserResult =  iesAdminUserService.insertAdminUser(adminUserDTO);
        if(adminUserResult.isSuccess()){
            return RestResult.ok();
        }
        return RestResult.fail(adminUserResult.getCode(),adminUserResult.getMsg());

    }

    @ApiOperation(value = "查询AdminUserList信息", notes = "查询AdminUserList信息")
    @GetMapping("/getAdminUserList")
    public Object getAdminUserList(Boolean delFlag,@NotEmpty int pageSize, @NotEmpty int pageNum)  {

        DubboPageResult adminUserResult =  iesAdminUserService.getAdminUserList(delFlag,pageSize,pageNum);

        List<EsAdminUserVO> adminUserVOS = BeanUtil.copyList(adminUserResult.getData().getList(),EsAdminUserVO.class);

        if(!adminUserResult.isSuccess()){
            return RestResult.fail(adminUserResult.getCode(),adminUserResult.getMsg());
        }
        return ApiPageResponse.pageSuccess(adminUserResult.getData().getTotal(),adminUserVOS);
    }

    @ApiOperation(value = "删除AdminUser信息", notes = "删除AdminUserList信息")
    @DeleteMapping("/delAdminUserByIds")
    public Object delAdminUserByIds(String [] ids)  {

        DubboResult adminUserResult =  iesAdminUserService.deleteAdminUser(Arrays.asList(ids));

        if(!adminUserResult.isSuccess()){
            return RestResult.fail(adminUserResult.getCode(),adminUserResult.getMsg());
        }
        return RestResult.ok();
    }

    @ApiOperation(value = "修改AdminUser信息", notes = "修改AdminUserList信息")
    @PutMapping("/updateAdminUser")
    public Object updateAdminUser(EsAdminUserForm adminUserForm)  {

        EsAdminUserDTO adminUserDTO = new EsAdminUserDTO();
        BeanUtils.copyProperties(adminUserForm,adminUserDTO);
        DubboResult adminUserResult =  iesAdminUserService.updateAdminUser(adminUserDTO);

        if(!adminUserResult.isSuccess()){
            return RestResult.fail(adminUserResult.getCode(),adminUserResult.getMsg());
        }
        return RestResult.ok();
    }

}

