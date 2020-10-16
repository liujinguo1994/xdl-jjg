package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jjg.member.model.domain.*;
import com.jjg.member.model.dto.*;
import com.jjg.member.model.enums.ConsumeEnumType;
import com.jjg.member.model.vo.*;
import com.jjg.trade.model.domain.EsCouponDO;
import com.jjg.trade.model.dto.EsCouponDTO;
import com.xdl.jjg.constant.MemberConstant;
import com.xdl.jjg.constant.MemberErrorCode;
import com.xdl.jjg.entity.*;
import com.xdl.jjg.mapper.*;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.shiro.oath.ShiroKit;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.util.CollectionUtils;
import com.xdl.jjg.util.MathUtil;
import com.xdl.jjg.web.service.*;
import com.xdl.jjg.web.service.feign.trade.CouponService;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-04
 */
@Service
public class EsMemberServiceImpl extends ServiceImpl<EsMemberMapper, EsMember> implements IEsMemberService {

    private static Logger logger = LoggerFactory.getLogger(EsMemberServiceImpl.class);

    //加密长度
    @Value("${zhuo.member.encryption.length}")
    private int ENLENGTH;
    //token过期时间(30天)
    @Value("${zhuox.shiro.expire}")
    private static int EXPIRE;
    @Autowired
    private EsMemberMapper memberMapper;
    @Autowired
    private EsMemberTokenMapper memberTokenMapper;
    @Autowired
    private EsMemberLevelConfigMapper esMemberLevelConfigMapper;
    @Autowired
    private EsCompanyMapper esCompanyMapper;

    @Autowired
    private IEsMemberDepositService iEsMemberDepositService;
    @Autowired
    private EsMemberDepositMapper esMemberDepositMapper;

    @Autowired
    private IEsGrowthWeightConfigService iEsGrowthWeightConfigService;

    @Autowired
    private IEsClerkService iEsClerkService;

    @Autowired
    private IEsMemberPointHistoryService memberPointHistoryService;

    @Autowired
    private IEsMemberCouponService memberCouponService;

    @Autowired
    private IEsMemberCollectionGoodsService memberCollectionGoodsService;

    @Autowired
    private IEsMemberCollectionShopService memberCollectionShopService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private EsCompanyMapper companyMapper;

    @Autowired
    private CouponService iesCouponService;
    @Autowired
    private IEsMemberCouponService iesMemberCouponService;
    @Autowired
    private IEsMemberService iesMemberService;
    @Autowired
    private IEsCouponReceiveCheckService iesCouponReceiveCheckService;

    /**
     * 插入会员表数据
     *
     * @param memberDTO 会员表DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsMemberDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult<EsMemberDO> insertMember(EsMemberDTO memberDTO) {
        try {
            if (memberDTO == null) {
                throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), MemberErrorCode.PARAM_ERROR.getErrorMsg());
            }
            EsMemberDO esMemberDO = new EsMemberDO();
            EsMember member = new EsMember();
            BeanUtil.copyProperties(memberDTO, member);
            member.setState(MemberConstant.IsCommon);
            member.setLastLogin(System.currentTimeMillis());
            member.setCreateTime(System.currentTimeMillis());
            if(member.getNickname()==null ||StringUtils.isBlank( member.getNickname())){
                //如果昵称为空 系统随机生成昵称
                member.setNickname(getStringRandom(16));
            }
            if (null != memberDTO.getMemberBalance()) {
                member.setMemberBalance(memberDTO.getMemberBalance());
            }
            this.memberMapper.insert(member);

            BeanUtil.copyProperties(member, esMemberDO);
            // TODO 注册完成后发送消息给会员提示注册成功
            // 验证用户是否满足注册赠券的条件
            DubboResult<Boolean> result = iesCouponReceiveCheckService.getCouponReceiveCheckByMobile(member.getMobile());
            logger.info("验证用户是否存在手机号码[{}]",result.getData());
            if (result.getData()){
                EsCouponDTO esCouponDTO = new EsCouponDTO();
                esCouponDTO.setIsRegister(1);
                esCouponDTO.setIsDel(1);
                DubboPageResult<EsCouponDO> coupons = iesCouponService.getCoupons(esCouponDTO);
                if (coupons.isSuccess()){
                    //设置优惠券信息
                    List<EsCouponDO> list = coupons.getData().getList();
                    list.forEach(esCouponDO -> {
                        DubboResult<EsCouponDO> resultEsCouponDO = iesCouponService.getCoupon(esCouponDO.getId());
                        EsCouponDO couponDO = resultEsCouponDO.getData();
                        // 注册成功 无条件赠送注册赠券
                        EsMemberCouponDTO memberCouponDTO = new EsMemberCouponDTO();
                        Long memberId = esMemberDO.getId();
                        memberCouponDTO.setCouponId(couponDO.getId());
                        memberCouponDTO.setCouponMoney(couponDO.getCouponMoney());
                        memberCouponDTO.setCouponThresholdPrice(couponDO.getCouponThresholdPrice());
                        memberCouponDTO.setEndTime(couponDO.getEndTime());
                        memberCouponDTO.setStartTime(couponDO.getStartTime());
                        memberCouponDTO.setIsDel(couponDO.getIsDel());
                        memberCouponDTO.setShopId(couponDO.getShopId());
                        memberCouponDTO.setState(1);
                        memberCouponDTO.setTitle(couponDO.getTitle());
                        memberCouponDTO.setType(couponDO.getCouponType());
                        memberCouponDTO.setMemberId(memberId);
                        memberCouponDTO.setMemberName(esMemberDO.getName());
                        logger.info("系统自动发放注册赠券");
                        //插入会员注册优惠券
                        iesMemberCouponService.insertMemberCoupon(memberCouponDTO);
                    });
                }
            }
            return DubboResult.success(esMemberDO);
        } catch (ArgumentException ae) {
            logger.error("会员表新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable ae) {
            logger.error("会员表新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 买家端添加会员
     *
     * @param esSellerMemberAdminDTO 会员表DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsMemberDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertSellerMember(EsSellerMemberAdminDTO esSellerMemberAdminDTO) {
        try {
            if (esSellerMemberAdminDTO == null) {
                throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), MemberErrorCode.PARAM_ERROR.getErrorMsg());
            }
            EsMember member = new EsMember();
            if (null != esSellerMemberAdminDTO.getName()) {
                DubboResult<EsMemberDO> resultName = this.getMemberInfoByName(esSellerMemberAdminDTO.getName());
                if (resultName.isSuccess() && null != resultName.getData()) {
                    throw new ArgumentException(MemberErrorCode.MEMBER_ACCOUNT_IS_EXIST.getErrorCode(), MemberErrorCode.MEMBER_ACCOUNT_IS_EXIST.getErrorMsg());
                }
                member.setName(esSellerMemberAdminDTO.getName());
            }
            if (null != esSellerMemberAdminDTO.getEmail()) {
                member.setEmail(esSellerMemberAdminDTO.getEmail());
            }
            if (null != esSellerMemberAdminDTO.getMobile()) {
                DubboResult<EsMemberDO> resultName = this.getMemberInfoByMobile(esSellerMemberAdminDTO.getMobile());
                if (resultName.isSuccess() && null != resultName.getData()) {
                    throw new ArgumentException(MemberErrorCode.EXIST_MOBILE.getErrorCode(), MemberErrorCode.EXIST_MOBILE.getErrorMsg());
                }
                member.setMobile(esSellerMemberAdminDTO.getMobile());
            }
            if (null != esSellerMemberAdminDTO.getPassword() && null != esSellerMemberAdminDTO.getName()) {
                String salt = ShiroKit.getRandomSalt(ENLENGTH);
                String passWord = ShiroKit.md5(esSellerMemberAdminDTO.getPassword(), esSellerMemberAdminDTO.getName() + salt);
                member.setPassword(passWord);
                member.setSalt(salt);
            }
            if(!StringUtils.isBlank(esSellerMemberAdminDTO.getRegisterIp())){
                member.setRegisterIp(esSellerMemberAdminDTO.getRegisterIp());
            }
            member.setState(MemberConstant.IsCommon);
            member.setLastLogin(System.currentTimeMillis());
            member.setCreateTime(System.currentTimeMillis());
            this.memberMapper.insert(member);
            //往店员表写数据
            EsClerkDTO esClerkDTO = new EsClerkDTO();
            esClerkDTO.setMemberId(member.getId());
            esClerkDTO.setClerkName(esSellerMemberAdminDTO.getName());
            esClerkDTO.setIsAdmin(esSellerMemberAdminDTO.getIsAdmin());
            esClerkDTO.setRoleId(esSellerMemberAdminDTO.getRoleId());
            esClerkDTO.setShopId(esSellerMemberAdminDTO.getShopId());
            DubboResult resultClerk = iEsClerkService.insertClerk(esClerkDTO);
            if (!resultClerk.isSuccess()) {
                throw new ArgumentException(resultClerk.getCode(), resultClerk.getMsg());
            }
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("用户表新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable ae) {
            logger.error("用户表新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }


    /**
     * 买家端添加会员
     *
     * @param esSellerMemberAdminDTO 会员表DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsMemberDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateSellerMember(EsSellerMemberAdminDTO esSellerMemberAdminDTO) {
        QueryWrapper<EsMember> queryWrapper = new QueryWrapper<>();
        try {
            queryWrapper.lambda().eq(EsMember::getId,esSellerMemberAdminDTO.getMemberId());
            if (esSellerMemberAdminDTO == null) {
                throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), MemberErrorCode.PARAM_ERROR.getErrorMsg());
            }
            EsMember member = new EsMember();
            //判断用户名是否重复
            if (!StringUtils.isBlank(esSellerMemberAdminDTO.getName())) {
                DubboResult<EsMemberDO> resultName = this.getMemberInfoByName(esSellerMemberAdminDTO.getName());
                if (resultName.isSuccess() && null != resultName.getData()) {
                    throw new ArgumentException(MemberErrorCode.MEMBER_ACCOUNT_IS_EXIST.getErrorCode(), MemberErrorCode.MEMBER_ACCOUNT_IS_EXIST.getErrorMsg());
                }
                member.setName(esSellerMemberAdminDTO.getName());
            }
            if(!StringUtils.isBlank(esSellerMemberAdminDTO.getEmail())){
                member.setEmail(esSellerMemberAdminDTO.getEmail());
            }

            if ( !StringUtils.isBlank(esSellerMemberAdminDTO.getMobile())) {
                DubboResult<EsMemberDO> resultName = this.getMemberInfoByMobile(esSellerMemberAdminDTO.getMobile());
                if (resultName.isSuccess() && null != resultName.getData()) {
                    throw new ArgumentException(MemberErrorCode.EXIST_MOBILE.getErrorCode(), MemberErrorCode.EXIST_MOBILE.getErrorMsg());
                }
                member.setMobile(esSellerMemberAdminDTO.getMobile());
            }
            //修改密码
            if ( !StringUtils.isBlank(esSellerMemberAdminDTO.getPassword()) &&  !StringUtils.isBlank(esSellerMemberAdminDTO.getName())) {
                String salt = ShiroKit.getRandomSalt(ENLENGTH);
                String passWord = ShiroKit.md5(esSellerMemberAdminDTO.getPassword(), esSellerMemberAdminDTO.getName() + salt);
                member.setPassword(passWord);
                member.setSalt(salt);
            }
            member.setState(MemberConstant.IsCommon);
            member.setLastLogin(System.currentTimeMillis());
            member.setCreateTime(System.currentTimeMillis());
            this.memberMapper.update(member,queryWrapper);
            //修改店员表数据
            EsClerkDTO esClerkDTO = new EsClerkDTO();
            esClerkDTO.setMemberId(esSellerMemberAdminDTO.getMemberId());
            if(!StringUtils.isBlank(esSellerMemberAdminDTO.getName())){
                esClerkDTO.setClerkName(esSellerMemberAdminDTO.getName());
            }
            esClerkDTO.setIsAdmin(esSellerMemberAdminDTO.getIsAdmin());
            esClerkDTO.setRoleId(esSellerMemberAdminDTO.getRoleId());
            esClerkDTO.setShopId(esSellerMemberAdminDTO.getShopId());
            DubboResult resultClerk = iEsClerkService.updateSellerClerk(esClerkDTO);
            if (!resultClerk.isSuccess()) {
                throw new ArgumentException(resultClerk.getCode(), resultClerk.getMsg());
            }
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("用户修改失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable ae) {
            logger.error("用户修改增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 查询端添加会员
     *
     * @param memberId 会员表DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsMemberDO>
     */
    @Override
    public DubboResult<EsSellerMemberAdminDO> getSellerMember(Long memberId, Long shopId) {
        try {
             EsSellerMemberAdminDO esSellerMemberAdminDO = memberMapper.getSellerAdmin(memberId, shopId);
            if (esSellerMemberAdminDO == null) {
                throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            return DubboResult.success(esSellerMemberAdminDO);
        } catch (ArgumentException ae) {
            logger.error("会员表查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("会员表查询失败", th);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }

    }


    /**
     * 后台插入会员表数据
     *
     * @param memberDTO 会员表DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsMemberDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult<EsMemberDO> insertAdminMember(EsMemberDTO memberDTO) {
        QueryWrapper<EsMember> queryWrapperName = new QueryWrapper<>();
        QueryWrapper<EsMember> queryWrapperMobile = new QueryWrapper<>();
        QueryWrapper<EsMember> queryWrapperEmail = new QueryWrapper<>();
        String passWord = null;
        String salt = null;
        try {
            if (memberDTO == null) {
                throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), MemberErrorCode.PARAM_ERROR.getErrorMsg());
            }
            if (null != memberDTO && null != memberDTO.getName()) {
                queryWrapperName.lambda().eq(EsMember::getState, MemberConstant.IsDefault).eq(EsMember::getName, memberDTO.getName());
            }
            if (null != memberDTO && null != memberDTO.getEmail()) {
                queryWrapperEmail.lambda().eq(EsMember::getState, MemberConstant.IsDefault).eq(EsMember::getEmail, memberDTO.getEmail());
            }
            if (null != memberDTO && null != memberDTO.getMobile()) {
                queryWrapperMobile.lambda().eq(EsMember::getState, MemberConstant.IsDefault).eq(EsMember::getMobile, memberDTO.getMobile());
            }
            List<EsMember> esMemberListName = this.memberMapper.selectList(queryWrapperName);
            if (CollectionUtils.isNotEmpty(esMemberListName)) {
                throw new ArgumentException(MemberErrorCode.EXIST_NAME.getErrorCode(), MemberErrorCode.EXIST_NAME.getErrorMsg());
            }
            List<EsMember> esMemberListMobile = this.memberMapper.selectList(queryWrapperMobile);
            if (CollectionUtils.isNotEmpty(esMemberListMobile)) {
                throw new ArgumentException(MemberErrorCode.EXIST_MOBILE.getErrorCode(), MemberErrorCode.EXIST_MOBILE.getErrorMsg());
            }
            List<EsMember> esMemberListEmail = this.memberMapper.selectList(queryWrapperEmail);
            if (CollectionUtils.isNotEmpty(esMemberListEmail)) {
                throw new ArgumentException(MemberErrorCode.EXIST_EMAIL.getErrorCode(), MemberErrorCode.EXIST_EMAIL.getErrorMsg());
            }
            if (null != memberDTO && null != memberDTO.getPassword()) {
                salt = ShiroKit.getRandomSalt(ENLENGTH);
                passWord = ShiroKit.md5(memberDTO.getPassword(), memberDTO.getName() + salt);
            }
            EsMemberDO esMemberDO = new EsMemberDO();
            EsMember member = new EsMember();
            BeanUtil.copyProperties(memberDTO, member);
            if (null != passWord) {
                member.setPassword(passWord);
            }
            if (null != salt) {
                member.setSalt(salt);
            }
            member.setCreateTime(System.currentTimeMillis());
            member.setState(MemberConstant.IsCommon);
            this.memberMapper.insert(member);
            BeanUtil.copyProperties(member, esMemberDO);
            // TODO 注册完成后发送消息给会员提示注册成功
            return DubboResult.success(esMemberDO);
        } catch (ArgumentException ae) {
            logger.error("会员表新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable ae) {
            logger.error("会员表新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 修改会员个人信息
     *
     * @param memberDTO 会员表DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsMemberDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateMemberByInfo(EsMemberDTO memberDTO) {
        QueryWrapper<EsMember> queryWrapper = new QueryWrapper<>();
        QueryWrapper<EsMember> queryWrapperName = new QueryWrapper<>();
        QueryWrapper<EsMember> queryWrapperMobile = new QueryWrapper<>();
        QueryWrapper<EsMember> queryWrapperEmail = new QueryWrapper<>();
        String passWord;
        String salt;
        try {

            if (null != memberDTO && null != memberDTO.getName()) {
                queryWrapperName.lambda().eq(EsMember::getState, MemberConstant.IsDefault).eq(EsMember::getName, memberDTO.getName());
            }
            if (null != memberDTO && null != memberDTO.getEmail()) {
                queryWrapperEmail.lambda().eq(EsMember::getState, MemberConstant.IsDefault).eq(EsMember::getEmail, memberDTO.getEmail());
            }
            if (null != memberDTO && null != memberDTO.getMobile()) {
                queryWrapperMobile.lambda().eq(EsMember::getState, MemberConstant.IsDefault).eq(EsMember::getMobile, memberDTO.getMobile());
            }
            List<EsMember> esMemberListName = this.memberMapper.selectList(queryWrapperName);
            if (CollectionUtils.isNotEmpty(esMemberListName)) {
                throw new ArgumentException(MemberErrorCode.EXIST_NAME.getErrorCode(), MemberErrorCode.EXIST_NAME.getErrorMsg());
            }
            List<EsMember> esMemberListMobile = this.memberMapper.selectList(queryWrapperMobile);
            if (CollectionUtils.isNotEmpty(esMemberListMobile)) {
                throw new ArgumentException(MemberErrorCode.EXIST_MOBILE.getErrorCode(), MemberErrorCode.EXIST_MOBILE.getErrorMsg());
            }
            List<EsMember> esMemberListEmail = this.memberMapper.selectList(queryWrapperEmail);
            if (CollectionUtils.isNotEmpty(esMemberListEmail)) {
                throw new ArgumentException(MemberErrorCode.EXIST_EMAIL.getErrorCode(), MemberErrorCode.EXIST_EMAIL.getErrorMsg());
            }
            queryWrapper.lambda().eq(EsMember::getId, memberDTO.getId());
            EsMember es = new EsMember();
            BeanUtil.copyProperties(memberDTO, es);
            if (null != memberDTO && null != memberDTO.getPassword()) {
                salt = ShiroKit.getRandomSalt(ENLENGTH);
                passWord = ShiroKit.md5(memberDTO.getPassword(), memberDTO.getName() + salt);
                es.setSalt(salt);
                es.setPassword(passWord);
            }
            this.memberMapper.update(es, queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("会员表更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("会员表更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 修改会员最后登陆时间
     *
     * @param memberDTO 会员表DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsMemberDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateMemberLastLoginTime(EsMemberDTO memberDTO) {
        QueryWrapper<EsMember> queryWrapper = new QueryWrapper<>();
        try {
            queryWrapper.lambda().eq(EsMember::getId, memberDTO.getId());
            EsMember esMember = new EsMember();
            BeanUtil.copyProperties(memberDTO, esMember);
            esMember.setLastLogin(System.currentTimeMillis());
            this.memberMapper.update(esMember, queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("会员表更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("会员表更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }


    /**
     * 修改会员个人信息
     *
     * @param memberDTO 会员表DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsMemberDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateMember(EsMemberDTO memberDTO) {
        try {
            if (memberDTO == null) {
                throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), MemberErrorCode.PARAM_ERROR.getErrorMsg());
            }
            EsMember member = new EsMember();
            BeanUtil.copyProperties(memberDTO, member);
            if (null != memberDTO.getMemberBalance()) {
                member.setMemberBalance(memberDTO.getMemberBalance());
            }
            QueryWrapper<EsMember> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsMember::getId, memberDTO.getId()).eq(EsMember::getState, MemberConstant.IsCommon);
            EsMember esMember = this.memberMapper.selectOne(queryWrapper);
            if (null == esMember) {
                throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), "会员不存在");
            }
            this.memberMapper.update(member, queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("会员表更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("会员表更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 修改密码
     *
     * @param memberDTO 会员表DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsMemberDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateMemberPass(EsMemberDTO memberDTO) {
        try {
            QueryWrapper<EsMember> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsMember::getId, memberDTO.getId());
            EsMember esMember = new EsMember();
            esMember.setPassword(memberDTO.getPassword());
            esMember.setSalt(memberDTO.getSalt());
            this.memberMapper.update(esMember, queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("会员表更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("会员表更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 修改信息
     *
     * @param memberDTO 会员表DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsMemberDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateMemberInfo(EsMemberDTO memberDTO) {
        try {
            QueryWrapper<EsMember> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsMember::getId, memberDTO.getId());
            EsMember esMember = new EsMember();
            BeanUtil.copyProperties(memberDTO, esMember);
            //修改密码
            if ( !StringUtils.isBlank(memberDTO.getPassword())) {
                String salt = ShiroKit.getRandomSalt(ENLENGTH);
                String passWord = ShiroKit.md5(memberDTO.getPassword(), memberDTO.getName() + salt);
                esMember.setPassword(passWord);
                esMember.setSalt(salt);
            }
            this.memberMapper.update(esMember, queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("会员表更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("会员表更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 修改会员余额
     *
     * @param memberBalanceDTO 会员表DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsMemberDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateMemberBalance(EsMemberBalanceDTO memberBalanceDTO) {
        QueryWrapper<EsMember> queryWrapper = new QueryWrapper<>();
        try {
            if (memberBalanceDTO == null) {
                throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), MemberErrorCode.PARAM_ERROR.getErrorMsg());
            }
            queryWrapper.lambda().eq(EsMember::getId, memberBalanceDTO.getMemberId()).eq(EsMember::getState, MemberConstant.IsDefault);
            EsMember esMember = this.memberMapper.selectOne(queryWrapper);
            if (null == esMember) {
                throw new ArgumentException(MemberErrorCode.ITEM_NOT_FOUND.getErrorCode(), MemberErrorCode.ITEM_NOT_FOUND.getErrorMsg());
            }
            if (memberBalanceDTO.getType().getDescription().equals(ConsumeEnumType.CONSUME.getDescription()) && (esMember.getMemberBalance() < (memberBalanceDTO.getMoney()))) {
                throw new ArgumentException(MemberErrorCode.MEMBER_LOW_OF_BALANCE.getErrorCode(), MemberErrorCode.MEMBER_LOW_OF_BALANCE.getErrorMsg());
            }
            //修改会员余额
            Double balance1;
            if (ConsumeEnumType.REFUND.value().equals(memberBalanceDTO.getType().value()) ||
                    ConsumeEnumType.RECHARGE.value().equals(memberBalanceDTO.getType().value())) {
                balance1 = MathUtil.add(esMember.getMemberBalance(), memberBalanceDTO.getMoney());
            } else {
                balance1 = MathUtil.subtract(esMember.getMemberBalance(), memberBalanceDTO.getMoney());
            }
            EsMember esMember1 = new EsMember();
            esMember1.setMemberBalance(balance1);
            QueryWrapper<EsMember> queryWrapperBalance = new QueryWrapper<>();
            queryWrapperBalance.lambda().eq(EsMember::getId, memberBalanceDTO.getMemberId());
            this.memberMapper.update(esMember1, queryWrapperBalance);
            //添加会员余额明细
            memberBalanceDTO.setMemberBalance(balance1);
            iEsMemberDepositService.insertMemberDepositBalance(memberBalanceDTO);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("会员表更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("会员表更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据id获取会员表详情
     *
     * @param id 主键id
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsMemberDO>
     */
    @Override
    public DubboResult<EsMemberDO> getMember(Long id) {
        try {
            EsMemberDO memberDO = memberMapper.selectById(id);
            if (null == memberDO) {
                throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            return DubboResult.success(memberDO);
        } catch (ArgumentException ae) {
            logger.error("会员表查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("会员表查询失败", th);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据id获取会员表详情
     *
     * @param id 主键id
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsMemberDO>
     */
    @Override
    public DubboResult<EsMemberDO> getMemberById(Long id) {
        QueryWrapper<EsMember> queryWrapper = new QueryWrapper<>();
        try {
            queryWrapper.lambda().eq(EsMember::getId, id);
            EsMember member = memberMapper.selectOne(queryWrapper);
            if (null == member) {
                throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            EsMemberDO memberDO = new EsMemberDO();
            BeanUtil.copyProperties(member, memberDO);
            return DubboResult.success(memberDO);
        } catch (ArgumentException ae) {
            logger.error("会员表查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("会员表查询失败", th);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据id获取会员表详情
     *
     * @param id 主键id
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsMemberDO>
     */
    @Override
    public DubboResult<EsMemberDO> getAdminMember(Long id) {
        QueryWrapper<EsMember> queryWrapper = new QueryWrapper<>();
        try {
            queryWrapper.lambda().eq(EsMember::getId, id);
            EsMember member = this.memberMapper.selectOne(queryWrapper);
            EsMemberDO esMemberDO = new EsMemberDO();
            if (null == member) {
                throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(member, esMemberDO);

            QueryWrapper<EsMemberToken> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(EsMemberToken::getMemberId, id);
            EsMemberToken memberToken = memberTokenMapper.selectOne(wrapper);
            if(memberToken != null){
                esMemberDO.setToken(memberToken.getToken());
            }
            return DubboResult.success(esMemberDO);
        } catch (ArgumentException ae) {
            logger.error("会员表查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("会员表查询失败", th);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    @Override
    public DubboResult<EsMemberDO> getMemberByPassWord(String password, Long userId) {
        try {
            QueryWrapper<EsMember> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsMember::getId, userId).eq(EsMember::getPassword, password).eq(EsMember::getState, MemberConstant.IsCommon);
            EsMember member = this.memberMapper.selectOne(queryWrapper);
            EsMemberDO memberDO = new EsMemberDO();
            if (null == member) {
                throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(member, memberDO);
            return DubboResult.success(memberDO);
        } catch (ArgumentException ae) {
            logger.error("会员表查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("会员表查询失败", th);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 后台-根据查询会员表列表
     *
     * @param memberDTO 会员表DTO
     * @param pageSize  页码
     * @param pageNum   页数
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsMemberDO>
     */
    @Override
    public DubboPageResult< EsMemberAdminDO> getMemberList(EsMemberDTO memberDTO, int pageSize, int pageNum) {
        QueryWrapper<EsMember> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件
            Page<EsMember> page = new Page<>(pageNum, pageSize);
            if(memberDTO.getCreateTimeStart() != null){
                queryWrapper.lambda().ge(EsMember::getCreateTime, memberDTO.getCreateTimeStart());
            }
            if(memberDTO.getCreateTimeEnd() != null){
                queryWrapper.lambda().lt(EsMember::getCreateTime, memberDTO.getCreateTimeEnd());
            }
            if(org.apache.commons.lang3.StringUtils.isNotBlank(memberDTO.getKeyword())){
                queryWrapper.lambda().eq(EsMember::getName, memberDTO.getKeyword())
                        .or()
                        .eq(EsMember::getMobile, memberDTO.getKeyword());
            }

            IPage<EsMember> iPage = this.page(page, queryWrapper);
            List<EsMemberAdminDO> memberDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                memberDOList = iPage.getRecords().stream().map(member -> {
                     EsMemberAdminDO memberAdminDO = new  EsMemberAdminDO();
                    BeanUtil.copyProperties(member, memberAdminDO);
                    return memberAdminDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(iPage.getTotal(), memberDOList);
        } catch (ArgumentException ae) {
            logger.error("会员表分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("会员表分页查询失败", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询会员表列表
     *
     * @param esQueryAdminMemberDTO 会员表DTO
     * @param pageSize              页码
     * @param pageNum               页数
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsMemberDO>
     */
    @Override
    public DubboPageResult<EsAdminMemberDO> getAdminMemberList(EsQueryAdminMemberDTO esQueryAdminMemberDTO, int pageSize, int pageNum) {
        if (null != esQueryAdminMemberDTO) {
            if (null != esQueryAdminMemberDTO.getMemberLevelId()) {
                EsLevelValueDO esMemberLevelConfig = this.getGradeByName(esQueryAdminMemberDTO.getMemberLevelId());
                if (null != esMemberLevelConfig && null != esMemberLevelConfig.getMin()) {
                    esQueryAdminMemberDTO.setMin(esMemberLevelConfig.getMin());
                }
                if (null != esMemberLevelConfig.getMax() && null != esMemberLevelConfig.getMax()) {
                    esQueryAdminMemberDTO.setMax(esMemberLevelConfig.getMax());
                }
            }
        }

        try {
            // 查询条件
            Page<EsMember> page = new Page<>(pageNum, pageSize);
            IPage<EsAdminMemberDO> esAdminMemberDOIPage = this.memberMapper.selectAdminMemberList(page, esQueryAdminMemberDTO);
            List<EsAdminMemberDO> memberDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(esAdminMemberDOIPage.getRecords())) {
                esAdminMemberDOIPage.getRecords().stream().forEach(esDO -> {
                    EsAdminMemberDO esAdminMemberDO = new EsAdminMemberDO();
                    BeanUtil.copyProperties(esDO, esAdminMemberDO);
                    EsMemberLevelConfig esMemberLevelConfig = null;
                    if (null != esDO.getGrade()) {
                        esMemberLevelConfig = this.getEsMemberlever(esDO.getGrade());
                    }
                    if (null != esMemberLevelConfig) {
                        esAdminMemberDO.setMemberLevel(esMemberLevelConfig.getLevel());
                    }
                    if (null != esDO.getCompany()) {
                        EsCompany esCompany = this.getEsCompany(esDO.getCompany());
                        if (null != esCompany && null != esCompany.getCompanyName()) {
                            esAdminMemberDO.setCompany(esCompany.getCompanyName());
                        } else {
                            esAdminMemberDO.setCompany("");
                        }
                    }
                    memberDOList.add(esAdminMemberDO);
                });

            }
            return DubboPageResult.success(esAdminMemberDOIPage.getTotal(), memberDOList);
        } catch (ArgumentException ae) {
            logger.error("会员表分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("会员表分页查询失败", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 查询会员等级
     *
     * @param grade
     * @return
     */
    public EsMemberLevelConfig getEsMemberlever(Integer grade) {
        EsMemberLevelConfig esMemberLevelConfig = new EsMemberLevelConfig();
        QueryWrapper<EsMemberLevelConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("under_line");
        List<EsMemberLevelConfig> list = esMemberLevelConfigMapper.selectList(queryWrapper);
        if (CollectionUtils.isNotEmpty(list)) {
            for (EsMemberLevelConfig es : list) {
                if (null != es.getUnderLine()) {
                    int num = grade.compareTo(es.getUnderLine());
                    if (num >= 0) {
                        return es;
                    }

                }
            }
        }
        return esMemberLevelConfig;
    }

    /**
     * 查询签约公司信息
     *
     * @param code
     * @return
     */
    public EsCompany getEsCompany(String code) {
        QueryWrapper<EsCompany> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(EsCompany::getCompanyCode, code).eq(EsCompany::getIsDel, MemberConstant.IsDefault).eq(EsCompany::getState, MemberConstant.IsDefault);
        EsCompany esCompany = esCompanyMapper.selectOne(queryWrapper);
        return esCompany;
    }

    /**
     * 依据等级名称查询等级下线
     *
     * @param memberLevelId
     * @return
     */
    public EsLevelValueDO getGradeByName(Long memberLevelId) {
        QueryWrapper<EsMemberLevelConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("under_line");
        EsLevelValueDO esLevelValueDO = new EsLevelValueDO();
        List<EsMemberLevelConfig> list = this.esMemberLevelConfigMapper.selectList(queryWrapper);
        for (int i = 0; i < list.size(); i++) {
            int num = memberLevelId.compareTo(list.get(i).getId());
            if (num != 0) {
                continue;
            }
            esLevelValueDO.setMin(list.get(i).getUnderLine());
            if (i != list.size() - 1) {
                esLevelValueDO.setMax(list.get(i + 1).getUnderLine());
            }
            return esLevelValueDO;
        }
        return esLevelValueDO;
    }

    /**
     * 依据公司名称查询公司code
     *
     * @param name
     * @return
     */
    public EsCompany getCodeByName(String name) {
        QueryWrapper<EsCompany> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(EsCompany::getCompanyName, name).eq(EsCompany::getIsDel, MemberConstant.IsDefault).eq(EsCompany::getState, MemberConstant.IsCommon);
        EsCompany esCompany = this.esCompanyMapper.selectOne(queryWrapper);
        return esCompany;
    }

    /**
     * 根据会员列表
     *
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsMemberDO>
     */
    @Override
    public DubboPageResult<EsMemberDO> getMemberListNoPage() {
        QueryWrapper<EsMember> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件
            queryWrapper.lambda().eq(EsMember::getState, MemberConstant.IsCommon);
            List<EsMemberDO> memberDOList = new ArrayList<>();
            List<EsMember> esMemberList = this.memberMapper.selectList(queryWrapper);
            if (CollectionUtils.isNotEmpty(esMemberList)) {
                memberDOList = esMemberList.stream().map(member -> {
                    EsMemberDO memberDO = new EsMemberDO();
                    BeanUtil.copyProperties(member, memberDO);
                    return memberDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(memberDOList);
        } catch (ArgumentException ae) {
            logger.error("会员表查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("会员表查询失败", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }


    /**
     * 根据主键删除会员表数据
     *
     * @param id 主键id
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsMemberDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteMember(Long id) {
        try {
            if (id == 0) {
                throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), String.format("参数传入错误ID不能为空[%s]", id));
            }
            QueryWrapper<EsMember> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.lambda().eq(EsMember::getId, id);
            this.memberMapper.delete(deleteWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("会员表删除失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("会员表删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据查询会员是否已注册
     *
     * @param memberDTO 会员表DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsMemberDO>
     */
    @Override
    public DubboResult<EsMemberDO> getMemberExistList(EsMemberDTO memberDTO) {
        QueryWrapper<EsMember> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件
            if (null != memberDTO.getName()) {
                queryWrapper.lambda().eq(EsMember::getName, memberDTO.getName());
            }
            if (null != memberDTO.getMobile()) {
                queryWrapper.lambda().eq(EsMember::getMobile, memberDTO.getMobile());
            }
            EsMemberDO esMemberDO = new EsMemberDO();
            queryWrapper.lambda().eq(EsMember::getState, 0);
            EsMember esMember = this.memberMapper.selectOne(queryWrapper);
            if (null == esMember) {
                throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(esMember, esMemberDO);
            return DubboPageResult.success(esMemberDO);
        } catch (ArgumentException ae) {
            logger.error("会员表查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("会员表查询失败", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 依据会员手机号或者姓名查询会员信息
     *
     * @param nameOrMobile
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsMemberDO>
     */
    public DubboResult<EsMemberDO> getMemberInfoByNameOrMobile(String nameOrMobile) {
        try {
            // 查询条件
            EsMemberDO esMemberDO = memberMapper.selectByNameOrMobile(nameOrMobile);
            return DubboPageResult.success(esMemberDO);
        } catch (ArgumentException ae) {
            logger.error("会员表查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("会员表查询失败", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }

    }

    /**
     * 依据会员手机号查询会员信息
     *
     * @param mobile
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsMemberDO>
     */
    public DubboResult<EsMemberDO> getMemberInfoByMobile(String mobile) {
        try {
            // 查询条件
            EsMemberDO esMemberDO = memberMapper.selectByMobile(mobile);
            return DubboPageResult.success(esMemberDO);
        } catch (ArgumentException ae) {
            logger.error("会员表查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("会员表查询失败", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }

    }

    /**
     * 依据会员姓名查询会员信息
     *
     * @param name
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsMemberDO>
     */
    public DubboResult<EsMemberDO> getMemberInfoByName(String name) {
        try {
            // 查询条件
            EsMemberDO esMemberDO = memberMapper.selectByName(name);
            return DubboPageResult.success(esMemberDO);
        } catch (ArgumentException ae) {
            logger.error("会员表查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("会员表查询失败", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }

    }

    /**
     * 依据会员id判断会员是否存在
     *
     * @param id Long
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsMemberDO>
     */
    public DubboResult<EsMemberDO> getMemberByIdInfo(Long id) {
        QueryWrapper<EsMember> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件
            queryWrapper.lambda().eq(EsMember::getId, id).eq(EsMember::getState, MemberConstant.IsDefault);
            EsMember esMember = this.memberMapper.selectOne(queryWrapper);
            EsMemberDO esMemberDO = new EsMemberDO();
            if (null == esMember) {
                throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(esMember, esMemberDO);
            return DubboPageResult.success(esMemberDO);
        } catch (ArgumentException ae) {
            logger.error("会员表查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("会员表查询失败", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }

    }

    /**
     * 更改绑定手机号
     *
     * @param memberDTO 会员表DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsMemberDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateByMobile(EsMemberDTO memberDTO) {
        try {
            if (memberDTO == null) {
                throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), MemberErrorCode.PARAM_ERROR.getErrorMsg());
            }
            EsMember member = new EsMember();
            BeanUtil.copyProperties(memberDTO, member);
            Long mobile = this.memberMapper.selectMobileById(member);
            if (null == mobile) {
                throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), "手机号不存在");
            }
            this.memberMapper.updateByMobile(member);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("会员表更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("会员表更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 查询所有用户姓名
     *
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsMemberDO>
     */
    @Override
    public DubboPageResult<String> getAllNameExistList() {
        try {
            List<String> result = memberMapper.getNameExistListInfo();
            return DubboPageResult.success(result);
        } catch (ArgumentException ae) {
            logger.error("会员表查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("会员表查询失败", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }

    }

    /**
     * 查询所有手機號碼
     *
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsMemberDO>
     */
    @Override
    public DubboPageResult<String> getAllMobileExistList() {
        try {
            List<String> result = memberMapper.getMobileExistList();
            return DubboPageResult.success(result);
        } catch (ArgumentException ae) {
            logger.error("会员表查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("会员表查询失败", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * @param memberDTOList
     * @return
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * 批量插入会员
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult batchInsertMember(List<EsAdminDepositDTO> memberDTOList) {
        try {
            List<EsMember> memberList = new ArrayList<>();
            for (EsAdminDepositDTO esAdminDepositDTO : memberDTOList) {
                EsMember esMember = new EsMember();
                EsMemberCopyDTO esMemberCopyDTO = new EsMemberCopyDTO();
                BeanUtil.copyProperties(esAdminDepositDTO, esMemberCopyDTO);
                BeanUtil.copyProperties(esMemberCopyDTO, esMember);
                if (null != esMember.getMemberBalance()) {
                    esMember.setMemberBalance(esAdminDepositDTO.getMoney());
                }
                if (null != esMember.getPassword()) {
                    String salt = ShiroKit.getRandomSalt(ENLENGTH);
                    String passWord = ShiroKit.md5(esAdminDepositDTO.getPassword(), esAdminDepositDTO.getName() + salt);
                    esMember.setPassword(passWord);
                    esMember.setSalt(salt);
                }
                esMember.setCreateTime(System.currentTimeMillis());
                memberList.add(esMember);
            }
            this.saveBatch(memberList);
            return DubboPageResult.success();
        } catch (ArgumentException ae) {
            logger.error("会员表批量插入失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("会员表批量插入失败", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }


    /**
     * @return
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * 修改后台余额和明细
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult batchUpdateMemberDeposit(List<EsAdminDepositDTO> esAdminDepositDTOS) {
        try {
            List<EsMemberDepositDTO> esMemberDepositDTOList = new ArrayList<>();
            List<EsMember> esMemberList = new ArrayList<>();
            for (EsAdminDepositDTO esMemberDTO : esAdminDepositDTOS) {
                EsMember esMember = getEsMemberInfo(esMemberDTO.getId());
                if (null == esMember) {
                    continue;
                }
                //余额明细表
                EsMemberDepositDTO esMemberDepositDTO = new EsMemberDepositDTO();
                if (null != esMemberDTO.getMoney()) {
                    esMemberDepositDTO.setMoney(esMemberDTO.getMoney());
                }
                esMemberDepositDTO.setMemberId(esMember.getId());
                //修改会员余额表
                EsMember esMemberModify = new EsMember();
                EsMemberCopyDTO esMemberCopyDTO = new EsMemberCopyDTO();
                BeanUtil.copyProperties(esMemberDTO, esMemberCopyDTO);
                BeanUtil.copyProperties(esMemberCopyDTO, esMemberModify);
                if (null != esMemberDTO.getMemberBalance()) {
                    Double balance;
                    balance = MathUtil.subtract(esMemberDTO.getMemberBalance(), esMember.getMemberBalance());
                    esMemberModify.setMemberBalance(esMemberDTO.getMemberBalance());
                    //余额明细
                    esMemberDepositDTO.setMemberBalance(esMemberDTO.getMemberBalance());
                    esMemberDepositDTO.setMoney(balance);
                }
                esMemberModify.setCreateTime(System.currentTimeMillis());
                esMemberList.add(esMemberModify);
                esMemberDepositDTOList.add(esMemberDepositDTO);
            }
            if (CollectionUtils.isNotEmpty(esMemberList) && CollectionUtils.isNotEmpty(esMemberDepositDTOList)) {
                this.updateBatchById(esMemberList, esMemberList.size());
                DubboResult result = iEsMemberDepositService.insertMemberBatchDeposit(esMemberDepositDTOList);
                if (!result.isSuccess()) {
                    throw new ArgumentException(result.getCode(), result.getMsg());
                }
            }
            return DubboPageResult.success();
        } catch (ArgumentException ae) {
            logger.error("会员批量修改失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("会员批量修改失败", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }

    }

    /**
     * 查询会员信息
     *
     * @param id
     * @return
     */
    EsMember getEsMemberInfo(Long id) {
        QueryWrapper<EsMember> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(EsMember::getId, id);
        return this.memberMapper.selectOne(queryWrapper);
    }


    /**
     * @returnmvn clean package -DskipTest -U
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * 修改会员成长值  judge为 true 时候，增加，false时候减少
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateGrowthValue(Long userId, Integer growthValue, Boolean judge, Integer type) {
        try {
            Integer resultValue = growthValue;
            QueryWrapper<EsMember> queryWrapperInfo = new QueryWrapper<>();
            queryWrapperInfo.lambda().eq(EsMember::getId, userId);
            EsMember esMember = this.memberMapper.selectOne(queryWrapperInfo);
            EsMember esMemberValue = new EsMember();
            if (null != esMember.getGrade() && esMember.getGrade() != 0) {
                if (judge) {
                    esMemberValue.setGrade(esMember.getGrade() + resultValue);
                } else {
                    if ((esMember.getGrade() - resultValue) > 0) {
                        esMemberValue.setGrade(esMember.getGrade() - resultValue);
                    } else {
                        esMemberValue.setGrade(0);
                    }
                }
            } else {
                if (judge) {
                    esMemberValue.setGrade(resultValue);
                } else {
                    esMemberValue.setGrade(0);
                }
            }
            QueryWrapper<EsMember> queryWrapperUpdate = new QueryWrapper<>();
            queryWrapperUpdate.lambda().eq(EsMember::getId, userId);
            this.memberMapper.update(esMemberValue, queryWrapperUpdate);
            return DubboPageResult.success();
        } catch (ArgumentException ae) {
            logger.error("修改会员成长值失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("修改会员成长值失败", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    @Override
    public DubboResult< EsMyMeansDO> meansCensus(Long memberId) {
        QueryWrapper<EsMember> queryWrapper = new QueryWrapper<>();
        try {
            queryWrapper.lambda().eq(EsMember::getId, memberId);
            EsMember member = this.memberMapper.selectOne(queryWrapper);
            if (null == member) {
                throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }

             EsMyMeansDO myMeansDO = new  EsMyMeansDO();
            myMeansDO.setBalanceAccount(member.getMemberBalance());
            myMeansDO.setPointNum(member.getConsumPoint());

            DubboResult<Integer> couponResult = memberCouponService.getCouponNum(memberId);
            if(couponResult.isSuccess() && couponResult.getData() != null){
                myMeansDO.setCouponNum(couponResult.getData());
            }
            DubboResult<Integer> countGoodResult = memberCollectionGoodsService.getCountGoodsNum(memberId);
            if(countGoodResult.isSuccess() && countGoodResult.getData() != null){
                myMeansDO.setCollectionGoodNum(countGoodResult.getData());
            }
            DubboResult<Integer> countShopResult = memberCollectionShopService.getCountShopNum(memberId);
            if(countShopResult.isSuccess() && countShopResult.getData() != null){
                myMeansDO.setCollectionShopNum(countShopResult.getData());
            }
            return DubboResult.success(myMeansDO);
        } catch (ArgumentException ae) {
            logger.error("会员表查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("会员表查询失败", th);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateMemberState(Long memberId, Integer state) {
        if(memberId == null || state == null){
            throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), MemberErrorCode.PARAM_ERROR.getErrorMsg());
        }
        try {
            QueryWrapper<EsMember> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsMember::getId, memberId);
            EsMember esMember = this.memberMapper.selectOne(queryWrapper);
            if (null == esMember) {
                throw new ArgumentException(MemberErrorCode.MEMBER_NOT_EXIST.getErrorCode(), MemberErrorCode.MEMBER_NOT_EXIST.getErrorMsg());
            }
            EsMember member = new EsMember();
            member.setState(state);
            member.setId(memberId);
            memberMapper.updateById(member);

            if(state == 1){
                redisTemplate.delete("zhuox-dubbo-shiro-cache-authenticationCache:" + memberId);
            }
            return DubboPageResult.success();
        } catch (ArgumentException ae) {
            logger.error("修改会员状态失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("修改会员状态失败", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }


    public  String getStringRandom(int length) {
            String val = "";
            Random random = new Random();
          for(int i = 0; i < length; i++) {
               String charOrNum = random.nextInt(3) % 3 == 0 ? "char" : "num";
                if( "char".equalsIgnoreCase(charOrNum) ) {
                   int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                   val += (char)(random.nextInt(26) + temp);
                } else if( "num".equalsIgnoreCase(charOrNum) ) {
                    val += String.valueOf(random.nextInt(10));
                }
          }
          return val;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult<EsImportBalanceVO> importBalance(byte[] base64) {
        try {
        Workbook workbook = null;
        Integer totalNum=0;//总个数
        Integer successNum=0;//成功数
        Integer failNum=0;//失败数
        //兼容excel 03之前和excel 07版本
        try {
            workbook = new XSSFWorkbook(new ByteArrayInputStream(base64));
        } catch (Exception ex) {
            try {
                workbook = new HSSFWorkbook(new ByteArrayInputStream(base64));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        List<EsImportBalanceFailDataVO> list = new ArrayList<>();

        // 获取每一个工作薄
        for (int numSheet = 0; numSheet < workbook.getNumberOfSheets(); numSheet++) {
            Sheet xssfSheet = workbook.getSheetAt(numSheet);
            if (xssfSheet == null) {
                continue;
            }

            for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
                Row xssfRow = xssfSheet.getRow(rowNum);
                totalNum ++;
                if (xssfRow != null) {
                    EsMemberDO member;
                    String mobile;
                    EsImportBalanceFailDataVO failData = new EsImportBalanceFailDataVO();
                    if (xssfRow.getCell(0) != null) {
                        DecimalFormat df = new DecimalFormat("0");
                        mobile = df.format(xssfRow.getCell(0).getNumericCellValue());
                        member = memberMapper.selectByMobile(mobile);
                    }else{
                        throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), "手机号不能有空");
                    }
                    if (xssfRow.getCell(1) != null) {
                        //后台更改余额记录
                        String bal = String.valueOf(xssfRow.getCell(1));
                        if (member == null){
                            failNum++;
                            failData.setMobile(mobile);
                            failData.setUpdateBal(bal);
                            list.add(failData);
                            continue;
                        }
                        BigDecimal balance = new BigDecimal(bal);
                        BigDecimal originalBalance=new BigDecimal(member.getMemberBalance());
                        if (BigDecimal.ZERO.compareTo(originalBalance)==0){
                            member.setMemberBalance(balance.doubleValue());
                        }else{
                            member.setMemberBalance(originalBalance.add(balance).doubleValue());
                        }
                        //更改余额
                        memberMapper.updateBalanceByMobile(member);

                        //插入余额记录
                        EsMemberDeposit memberDeposit = new EsMemberDeposit();
                        memberDeposit.setMoney(balance.doubleValue());
                        memberDeposit.setMemberId(member.getId());
                        memberDeposit.setType(ConsumeEnumType.RECHARGE.value());
                        memberDeposit.setMemberBalance(member.getMemberBalance());
                        esMemberDepositMapper.insert(memberDeposit);

                        successNum++;
                    }else{
                        throw new ArgumentException(MemberErrorCode.BALANCE_IS_NULL.getErrorCode(), "余额不能为空");
                    }
                }
            }
        }
        EsImportBalanceVO importBalanceVO=new EsImportBalanceVO();
        importBalanceVO.setTotalNum(totalNum);
        importBalanceVO.setSuccessNum(successNum);
        importBalanceVO.setFailNum(failNum);
        importBalanceVO.setFailDataVOList(list);
        return DubboResult.success(importBalanceVO);
    } catch (ArgumentException ae) {
        logger.error("批量导入失败", ae);
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
        return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
    } catch (Throwable be) {
        logger.error("批量导入失败", be);
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
         return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult<EsImportMemberVO> importMember(byte[] base64) {
        try {
            Workbook workbook = null;
            Integer totalNum=0;//总个数
            Integer successNum=0;//成功数
            Integer failNum=0;//失败数
            //兼容excel 03之前和excel 07版本
            try {
                workbook = new XSSFWorkbook(new ByteArrayInputStream(base64));
            } catch (Exception ex) {
                try {
                    workbook = new HSSFWorkbook(new ByteArrayInputStream(base64));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            List<EsImportMemberFailDataVO> list = new ArrayList<>();

            // 获取每一个工作薄
            for (int numSheet = 0; numSheet < workbook.getNumberOfSheets(); numSheet++) {
                Sheet xssfSheet = workbook.getSheetAt(numSheet);
                if (xssfSheet == null) {
                    continue;
                }

                for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
                    Row xssfRow = xssfSheet.getRow(rowNum);
                    totalNum ++;
                    if (xssfRow != null) {
                        String mobile;
                        String name;
                        String companyCode;
                        //手机号
                        if (xssfRow.getCell(0) != null) {
                            DecimalFormat df = new DecimalFormat("0");
                            mobile = df.format(xssfRow.getCell(0).getNumericCellValue());
                        }else{
                            throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), "手机号不能有空");
                        }
                        //用户名
                        if (xssfRow.getCell(1) != null) {
                            name = String.valueOf(xssfRow.getCell(1));
                        }else{
                            throw new ArgumentException(MemberErrorCode.NAME_IS_NULL.getErrorCode(), "用户名不能为空");
                        }
                        //企业标识符
                        if (xssfRow.getCell(2) != null) {
                            companyCode = String.valueOf(xssfRow.getCell(2));
                        }else{
                            throw new ArgumentException(MemberErrorCode.COMPANY_CODE_IS_NULL.getErrorCode(), "企业标识符不能为空");
                        }

                        EsMemberDO memberDO1 = memberMapper.selectByMobile(mobile);
                        EsMemberDO memberDO2 = memberMapper.selectByName(name);
                        QueryWrapper<EsCompany> queryWrapper = new QueryWrapper<>();
                        queryWrapper.lambda().eq(EsCompany::getCompanyCode, companyCode);
                        EsCompany company = companyMapper.selectOne(queryWrapper);
                        if (memberDO1 != null || memberDO2 != null || company ==null){
                            failNum++;
                            EsImportMemberFailDataVO failData = new EsImportMemberFailDataVO();
                            failData.setMobile(mobile);
                            failData.setName(name);
                            failData.setCompanyCode(companyCode);
                            list.add(failData);
                            continue;
                        }else {
                            //插入会员数据
                            EsMember member = new EsMember();
                            member.setMobile(mobile);
                            member.setName(name);
                            member.setCompanyCode(companyCode);
                            String salt = ShiroKit.getRandomSalt(ENLENGTH);
                            String passWord = ShiroKit.md5("e10adc3949ba59abbe56e057f20f883e", name + salt);
                            member.setPassword(passWord);
                            member.setSalt(salt);
                            member.setState(0);
                            member.setCreateTime(System.currentTimeMillis());
                            memberMapper.insert(member);
                            successNum++;
                        }
                    }
                }
            }
            EsImportMemberVO importMemberVO=new EsImportMemberVO();
            importMemberVO.setTotalNum(totalNum);
            importMemberVO.setSuccessNum(successNum);
            importMemberVO.setFailNum(failNum);
            importMemberVO.setFailDataVOList(list);
            return DubboResult.success(importMemberVO);
        } catch (ArgumentException ae) {
            logger.error("批量导入失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable be) {
            logger.error("批量导入失败", be);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    @Override
    public DubboPageResult<EsExportMemberVO> exportMember(EsQueryAdminMemberDTO dto) {
        try{
            if (null != dto && dto.getMemberLevelId() != null) {
                EsLevelValueDO esMemberLevelConfig = this.getGradeByName(dto.getMemberLevelId());
                if (null != esMemberLevelConfig && null != esMemberLevelConfig.getMin()) {
                    dto.setMin(esMemberLevelConfig.getMin());
                }
                if (null != esMemberLevelConfig.getMax() && null != esMemberLevelConfig.getMax()) {
                    dto.setMax(esMemberLevelConfig.getMax());
                }
            }
            List<EsExportMemberVO> list = memberMapper.selectExportMemberList(dto);
            if (CollectionUtils.isNotEmpty(list)){
                list.stream().forEach(esExportMemberVO -> {
                    if (esExportMemberVO.getSex() != null){
                        if (esExportMemberVO.getSex() == 1){
                            esExportMemberVO.setSexText("男");
                        }else if (esExportMemberVO.getSex() == 0){
                            esExportMemberVO.setSexText("女");
                        }else {
                            esExportMemberVO.setSexText("");
                        }
                    }
                    if (null != esExportMemberVO.getGrade()) {
                        EsMemberLevelConfig levelConfig = this.getEsMemberlever(esExportMemberVO.getGrade());
                        if (levelConfig != null){
                            esExportMemberVO.setMemberLevel(levelConfig.getLevel());
                        }else {
                            esExportMemberVO.setMemberLevel("");
                        }
                    }
                    if (null != esExportMemberVO.getCompanyCode()) {
                        EsCompany esCompany = this.getEsCompany(esExportMemberVO.getCompanyCode());
                        if (null != esCompany) {
                            esExportMemberVO.setCompany(esCompany.getCompanyName());
                        } else {
                            esExportMemberVO.setCompany("");
                        }
                    }
                });
            }
            return DubboPageResult.success(list);
        } catch (ArgumentException ae) {
            logger.error("查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("查询失败", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult quitLogin(String skey) {
        try {
            QueryWrapper<EsMember> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsMember::getSkey,skey);
            EsMember esMember = memberMapper.selectOne(queryWrapper);
            if (null == esMember) {
                throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), "会员不存在");
            }
            esMember.setOpenid(null);
            esMember.setSkey(null);
            memberMapper.updateById(esMember);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("会员表更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("会员表更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    public DubboResult<EsMemberDO> getMemberBySkey(String skey) {
        try {
            QueryWrapper<EsMember> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsMember::getSkey,skey);
            EsMember esMember = memberMapper.selectOne(queryWrapper);
            EsMemberDO memberDO = null;
            if (esMember != null){
                memberDO  = new EsMemberDO();
                BeanUtil.copyProperties(esMember,memberDO);
            }
            return DubboResult.success(memberDO);
        } catch (ArgumentException ae) {
            logger.error("会员查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("会员查询失败", th);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
}
