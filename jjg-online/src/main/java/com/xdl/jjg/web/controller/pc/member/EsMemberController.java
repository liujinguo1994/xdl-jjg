package com.xdl.jjg.web.controller.pc.member;

import com.jjg.member.model.domain.EsMemberDO;
import com.jjg.member.model.dto.EsMemberDTO;
import com.jjg.member.model.vo.EsCompanyVO;
import com.jjg.member.model.vo.EsMemberVO;
import com.jjg.trade.model.form.EsBindMobileForm;
import com.jjg.trade.model.form.EsEditPersonInfoForm;
import com.jjg.trade.model.form.EsPassWordForm;
import com.jjg.trade.model.form.MemberCheckForm;
import com.jjg.trade.model.vo.EsCheckPasswordVO;
import com.xdl.jjg.constant.ApiStatus;
import com.xdl.jjg.constant.TradeErrorCode;
import com.xdl.jjg.oss.upload.OSSUploader;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.response.web.ApiResponse;
import com.xdl.jjg.shiro.oath.ShiroKit;
import com.xdl.jjg.shiro.oath.ShiroUser;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.web.controller.BaseController;
import com.xdl.jjg.web.service.IEsOrderService;
import com.xdl.jjg.web.service.feign.member.CompanyService;
import com.xdl.jjg.web.service.feign.member.MemberLevelConfigService;
import com.xdl.jjg.web.service.feign.member.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.JedisCluster;

import javax.validation.Valid;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author HQL 236154186@qq.com
 * @since 2019-05-29
 */
@Api(value = "/registerMember", tags = "会员注册模块")
@RestController
@RequestMapping("/registerMember")
public class EsMemberController extends BaseController {

    @Autowired
    private MemberService iesMemberService;
    @Autowired
    private CompanyService iEsCompanyService;
    @Reference(version = "${dubbo.application.version}", timeout = 5000,check = false)
    private IEsOrderService iEsOrderService;
    @Autowired
    private MemberLevelConfigService esMemberLevelConfigService;

    @Autowired
    private OSSUploader uploader;

    @Autowired
    private JedisCluster jedisCluster;

    //加密长度
    @Value("${zhuo.member.encryption.length}")
    private int ENLENGTH;

    /*  @ApiOperation(value = "发送手机验证验证码")
      @ApiImplicitParams({
              @ApiImplicitParam(name = "telephone", value = "手机号", required = true, dataType = "String", paramType = "query")
      })
  */

    @ApiOperation(value = "查询手机号或者用户名是否被注册", notes = "根据url获取")
    @GetMapping(value = "/queryRepeatMobile/{nameOrMobile}")
    @ResponseBody
    @ApiImplicitParam(name = "nameOrMobile", value = "姓名或手机号", dataType = "string", paramType = "path", required = true, example = "1")
    public ApiResponse selectRepeatMobile(@PathVariable("nameOrMobile") String nameOrMobile) {
        DubboResult result = iesMemberService.getMemberInfoByNameOrMobile(nameOrMobile);
        if (result.isSuccess()) {
            EsMemberVO esMemberMobileVO = new EsMemberVO();
            if (null != result.getData()) {
                BeanUtil.copyProperties(result.getData(), esMemberMobileVO);
            }
            return ApiResponse.success(esMemberMobileVO);
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }

    @ApiOperation(value = "判断公司标识符是否合法")
    @GetMapping(value = "/queryCompanyByNotes/{notes}")
    @ResponseBody
    @ApiImplicitParam(name = "notes", value = "公司标识符", dataType = "string", paramType = "path", required = true, example = "1")
    public ApiResponse selectCompanyBynotes(@PathVariable("notes") String notes) {
        DubboResult result = iEsCompanyService.getCompanyByCode(notes);
        if (null == result) {
            EsCompanyVO esMemberMobileVO = new EsCompanyVO();
            return ApiResponse.success(esMemberMobileVO);
        }
        if (result.isSuccess()) {
            EsCompanyVO esMemberMobileVO = new EsCompanyVO();
            if (null != result.getData()) {
                BeanUtil.copyProperties(result.getData(), esMemberMobileVO);
            }
            return ApiResponse.success(esMemberMobileVO);
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }

    @ApiOperation(value = "获取旧密码", notes = "依据前端传递获取旧的密码")
    @ApiImplicitParam(name = "oldPassword", value = "密码", required = true, dataType = "String", paramType = "path")
    @GetMapping("/{oldPassword}")
    @ResponseBody
    public ApiResponse getOldPassword( @PathVariable("oldPassword") String oldPassword) {
        ShiroUser user = ShiroKit.getUser();
        if (user == null) {
            return ApiResponse.fail(TradeErrorCode.NOT_LOGIN.getErrorCode(), TradeErrorCode.NOT_LOGIN.getErrorMsg());
        }
        Long userId = user.getId();
        DubboResult<EsMemberDO> result = iesMemberService.getMemberByPassWord(oldPassword, userId);//TODO 密码需要前端加密后传过来，还是我后端加密有待验证
        if (result.isSuccess()) {
            EsMemberVO esMemberVO = new EsMemberVO();
            BeanUtil.copyProperties(result.getData(), esMemberVO);
            return ApiResponse.success(esMemberVO);
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }

    @ApiOperation(value = "编辑个人信息", notes = "编辑个人信息")
    @PutMapping
    @ResponseBody
    public ApiResponse editPersonInfo(@RequestBody @ApiParam(name = "personInfoForm",value = "用户对象",required = true) @Valid EsEditPersonInfoForm personInfoForm) {
        ShiroUser user = ShiroKit.getUser();
        if (null == user) {
            return ApiResponse.fail(TradeErrorCode.NOT_LOGIN.getErrorCode(), TradeErrorCode.NOT_LOGIN.getErrorMsg());
        }
        Long userId = user.getId();
        EsMemberDTO esMemberDTO = new EsMemberDTO();
        BeanUtil.copyProperties(personInfoForm, esMemberDTO);
        esMemberDTO.setId(userId);
        DubboResult result = iesMemberService.updateMemberInfo(esMemberDTO);
        if (result.isSuccess()) {
            return ApiResponse.success();
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }
    @ApiOperation(value = "绑定手机号", notes = "绑定手机号")
    @PutMapping("/bind")
    @ResponseBody
    public ApiResponse bindMobile(@RequestBody @ApiParam(name = "personInfoForm",value = "用户对象",required = true) @Valid EsBindMobileForm personInfoForm) {
        //获取当前用户验证码
        String mobileCode = jedisCluster.get("mobile" + "_new_" + personInfoForm.getMobile());
        //比对
        if (StringUtils.isBlank(mobileCode) || !personInfoForm.getCode().equalsIgnoreCase(mobileCode)) {
            return ApiResponse.fail(TradeErrorCode.PARAM_ERROR.getErrorCode(),TradeErrorCode.PARAM_ERROR.getErrorMsg());
        }
        ShiroUser user = ShiroKit.getUser();
        if (null == user) {
            return ApiResponse.fail(TradeErrorCode.NOT_LOGIN.getErrorCode(), TradeErrorCode.NOT_LOGIN.getErrorMsg());
        }
        Long userId = user.getId();
        EsMemberDTO esMemberDTO = new EsMemberDTO();
        BeanUtil.copyProperties(personInfoForm, esMemberDTO);
        esMemberDTO.setId(userId);
        DubboResult result = iesMemberService.updateMemberInfo(esMemberDTO);
        if (result.isSuccess()) {
            return ApiResponse.success();
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }


    @ApiOperation(value = "找回密码", notes = "找回密码")
    @PutMapping("/newPassword")
    @ResponseBody
    public ApiResponse updatePassword(@RequestBody @ApiParam(name = "passWordForm",value = "密码对象",required = true) @Valid EsPassWordForm passWordForm) {
        DubboResult<EsMemberDO> result = iesMemberService.getMemberInfoByNameOrMobile(passWordForm.getMobile());
        if (!result.isSuccess() || result.getData() == null) {
            return ApiResponse.fail(TradeErrorCode.PARAM_ERROR.getErrorCode(), TradeErrorCode.PARAM_ERROR.getErrorMsg());
        }
        EsMemberDO memberDO = result.getData();
        String salt = ShiroKit.getRandomSalt(ENLENGTH);
        EsMemberDTO esMemberDTO = new EsMemberDTO();
        esMemberDTO.setId(memberDO.getId());
        //新密码
        String passWordNew = ShiroKit.md5(passWordForm.getNewPassword(), memberDO.getName() + salt);
        esMemberDTO.setPassword(passWordNew);
        esMemberDTO.setSalt(salt);

        String newPassWord = ShiroKit.md5(passWordForm.getNewPassword(), memberDO.getName() + memberDO.getSalt());
        if(StringUtils.equals(memberDO.getPassword(), newPassWord)){
            return ApiResponse.fail(TradeErrorCode.PASSWORD_SAME.getErrorCode(), TradeErrorCode.PASSWORD_SAME.getErrorMsg());
        }

        //修改密码
        DubboResult memberResult = iesMemberService.updateMemberPass(esMemberDTO);
        if (memberResult.isSuccess()) {
            return ApiResponse.success(result.getData());
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }

    @ApiOperation(value = "验证手机号与图片验证码", notes = "验证手机号与图片验证码", response = EsCheckPasswordVO.class)
    @PutMapping("/checkPassword")
    @ResponseBody
    public ApiResponse checkPassword(@RequestBody MemberCheckForm form) {
        String validateCode = jedisCluster.get("findPassWordCode" + "_" + form.getUuid());
        //比对
        if (StringUtils.isBlank(form.getCaptcha()) || StringUtils.isBlank(validateCode) || !validateCode.equalsIgnoreCase(form.getCaptcha())) {
            return ApiResponse.fail(TradeErrorCode.PARAM_ERROR.getErrorCode(), TradeErrorCode.PARAM_ERROR.getErrorMsg());
        }
        DubboResult<EsMemberDO> result = iesMemberService.getMemberInfoByNameOrMobile(form.getNameOrMobile());
        if (result.isSuccess()) {
            if (result.getData() == null) {
                return ApiResponse.fail(TradeErrorCode.PARAM_ERROR.getErrorCode(), TradeErrorCode.PARAM_ERROR.getErrorMsg());
            }
//            jedisCluster.del("findPassWordCode" + "_" + form.getUuid());
            EsCheckPasswordVO checkPasswordVO = new EsCheckPasswordVO();
            checkPasswordVO.setMobile(result.getData().getMobile());
            checkPasswordVO.setUuid(form.getUuid());
            return ApiResponse.success(checkPasswordVO);
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }
}

