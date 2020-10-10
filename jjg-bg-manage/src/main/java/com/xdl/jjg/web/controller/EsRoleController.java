package com.xdl.jjg.web.controller;

import com.xdl.jjg.entity.EsRole;
import com.xdl.jjg.model.dto.EsRoleDTO;
import com.xdl.jjg.model.form.EsRoleForm;
import com.xdl.jjg.model.vo.EsRoleVO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.response.web.ApiPageResponse;
import com.xdl.jjg.response.web.RestResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.web.service.IEsRoleService;
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
@RestController
@RequestMapping("/esRole")
public class EsRoleController {

    @Autowired
    private IEsRoleService iesRoleService;

    @ApiOperation(value = "查询Role信息", notes = "通过Id查询Role信息")
    @GetMapping("/getRoleById")
    public Object getRoleById(String id)  {

        DubboResult<EsRole> roleResult =  iesRoleService.getRole(id);
        if(roleResult.isSuccess()){
            EsRole role = roleResult.getData();
            EsRoleVO roleVO = new EsRoleVO();
            BeanUtils.copyProperties(role,roleVO);
            return RestResult.ok(roleVO);
        }
        return RestResult.fail(roleResult.getCode(),roleResult.getMsg());
    }

    @ApiOperation(value = "插入Role信息", notes = "插入Role信息")
    @PostMapping("/insertRole")
    public Object insertRole(EsRoleForm roleForm)  {

        EsRoleDTO roleDTO = new EsRoleDTO();
        BeanUtil.copyProperties(roleForm,roleDTO);

        DubboResult roleResult =  iesRoleService.insertRole(roleDTO);
        if(roleResult.isSuccess()){
            return RestResult.ok();
        }
        return RestResult.fail(roleResult.getCode(),roleResult.getMsg());

    }

    @ApiOperation(value = "查询RoleList信息", notes = "查询RoleList信息")
    @GetMapping("/getRoleList")
    public Object getRoleList(Boolean delFlag,@NotEmpty int pageSize, @NotEmpty int pageNum)  {

        DubboPageResult roleResult =  iesRoleService.getRoleList(delFlag,pageSize,pageNum);

        List<EsRoleVO> roleVOS = BeanUtil.copyList(roleResult.getData().getList(),EsRoleVO.class);

        if(!roleResult.isSuccess()){
            return RestResult.fail(roleResult.getCode(),roleResult.getMsg());
        }
        return ApiPageResponse.pageSuccess(roleResult.getData().getTotal(),roleVOS);
    }

    @ApiOperation(value = "删除Role信息", notes = "删除RoleList信息")
    @DeleteMapping("/delRoleByIds")
    public Object delRoleByIds(String [] ids)  {

        DubboResult roleResult =  iesRoleService.deleteRole(Arrays.asList(ids));

        if(!roleResult.isSuccess()){
            return RestResult.fail(roleResult.getCode(),roleResult.getMsg());
        }
        return RestResult.ok();
    }

    @ApiOperation(value = "修改Role信息", notes = "修改RoleList信息")
    @PutMapping("/updateRole")
    public Object updateRole(EsRoleForm roleForm)  {

        EsRoleDTO roleDTO = new EsRoleDTO();
        BeanUtils.copyProperties(roleForm,roleDTO);
        DubboResult roleResult =  iesRoleService.updateRole(roleDTO);

        if(!roleResult.isSuccess()){
            return RestResult.fail(roleResult.getCode(),roleResult.getMsg());
        }
        return RestResult.ok();
    }

}

