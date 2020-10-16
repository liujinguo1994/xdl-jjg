package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jjg.member.model.domain.*;
import com.jjg.member.model.dto.EsMemberCouponDTO;
import com.jjg.member.model.dto.QueryAdminCouponDTO;
import com.jjg.member.model.dto.QuerySellerCouponDTO;
import com.jjg.trade.model.domain.EsCouponDO;
import com.jjg.trade.model.dto.EsCouponDTO;
import com.jjg.trade.model.dto.EsMemberTradeCouponDTO;
import com.xdl.jjg.constant.MemberConstant;
import com.xdl.jjg.constant.MemberErrorCode;
import com.xdl.jjg.entity.EsMemberCoupon;
import com.xdl.jjg.entity.EsShop;
import com.xdl.jjg.mapper.EsMemberCouponMapper;
import com.xdl.jjg.mapper.EsShopMapper;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.web.service.IEsMemberCouponService;
import com.xdl.jjg.web.service.IEsShopDetailService;
import com.xdl.jjg.web.service.feign.trade.CouponService;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 会员优惠券 服务实现类
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-04
 */
@Service
public class EsMemberCouponServiceImpl extends ServiceImpl<EsMemberCouponMapper, EsMemberCoupon> implements IEsMemberCouponService {

    private static Logger logger = LoggerFactory.getLogger(EsMemberCouponServiceImpl.class);

    @Autowired
    private EsMemberCouponMapper memberCouponMapper;
    @Autowired
    private CouponService iEsCouponService;
    @Autowired
    private IEsShopDetailService iEsShopDetailService;
    @Autowired
    private EsShopMapper esShopMapper;
    @Value("${zhuox.shop.name}")
    private String shopName;

    /**
     * 插入会员优惠券数据
     *
     * @param memberCouponDTO 会员优惠券DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsMemberCouponDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertMemberCoupon(EsMemberCouponDTO memberCouponDTO) {
        try {
            if (memberCouponDTO == null || null == memberCouponDTO.getCouponId()) {
                throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), MemberErrorCode.PARAM_ERROR.getErrorMsg());
            }
            DubboResult<EsCouponDO> memberCouponDO = iEsCouponService.getCoupon(memberCouponDTO.getCouponId());
            EsCouponDO couponDO = new EsCouponDO();
            if(memberCouponDO.isSuccess()){
                couponDO = memberCouponDO.getData();
            }
            if (null == couponDO) {
                throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), "数据不存在错误");
            }
            Integer limit = memberCouponMapper.getMemberCouponLimit(memberCouponDTO);
            if (null != limit && null != couponDO.getLimitNum() && couponDO.getLimitNum() <= limit) {
                throw new ArgumentException(MemberErrorCode.COUPON__LIMIT.getErrorCode(), "领取店铺优惠券数量到达限制");
            }

            // 更新优惠券领取数量
            EsCouponDTO esCouponDTO = new EsCouponDTO();
            BeanUtil.copyProperties(couponDO,esCouponDTO);
            esCouponDTO.setReceivedNum(couponDO.getReceivedNum()+1);
            iEsCouponService.updateCoupon(esCouponDTO,couponDO.getId());
            EsMemberCoupon memberCoupon = new EsMemberCoupon();
            BeanUtil.copyProperties(memberCouponDTO, memberCoupon);
            memberCoupon.setCouponType(memberCouponDTO.getType());
            if (null != memberCouponDTO.getCouponMoney()) {
                memberCoupon.setCouponMoney(memberCouponDTO.getCouponMoney());
            }
            if (null != memberCouponDTO.getCouponThresholdPrice()) {
                memberCoupon.setCouponThresholdPrice(memberCouponDTO.getCouponThresholdPrice());
            }

            //填充店铺名称
            if(memberCouponDTO.getShopId() != null){
                EsShop shop = esShopMapper.selectById(memberCouponDTO.getShopId());
                if(shop != null){
                    memberCoupon.setShopName(shop.getShopName());
                }
            }

            memberCoupon.setState(MemberConstant.noSend);
//            memberCoupon.setIsDel(0);
            if( null == memberCouponDTO.getCreateTime()){
                memberCoupon.setCreateTime(System.currentTimeMillis());
            }else {
                this.memberCouponMapper.insert(memberCoupon);
                return DubboResult.success();
            }
            this.memberCouponMapper.insert(memberCoupon);

            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("会员优惠券新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Exception ae) {
            logger.error("会员优惠券新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新会员优惠券数据
     *
     * @param memberCouponDTO 会员优惠券DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsMemberCouponDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateMemberCoupon(EsMemberCouponDTO memberCouponDTO) {
        try {
            if (memberCouponDTO == null) {
                throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), MemberErrorCode.PARAM_ERROR.getErrorMsg());
            }
            EsMemberCoupon memberCoupon = new EsMemberCoupon();
            BeanUtil.copyProperties(memberCouponDTO, memberCoupon);
            QueryWrapper<EsMemberCoupon> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsMemberCoupon::getId, memberCouponDTO.getId());
            this.memberCouponMapper.update(memberCoupon, queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("会员优惠券更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Exception th) {
            logger.error("会员优惠券更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新会员优惠券数据
     *
     * @param memberCouponDTO 会员优惠券DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsMemberCouponDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateMemberCouponByCouponIdAndMemId(EsMemberCouponDTO memberCouponDTO) {
        try {
            if (memberCouponDTO == null) {
                throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), MemberErrorCode.PARAM_ERROR.getErrorMsg());
            }
            EsMemberCoupon memberCoupon = new EsMemberCoupon();
            BeanUtil.copyProperties(memberCouponDTO, memberCoupon);
            QueryWrapper<EsMemberCoupon> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsMemberCoupon::getCouponId, memberCouponDTO.getCouponId()).eq(EsMemberCoupon::getState, MemberConstant.userCoupon).eq(EsMemberCoupon::getMemberId, memberCouponDTO.getMemberId());
            List<EsMemberCoupon> esMemberCouponList = this.memberCouponMapper.selectList(queryWrapper);
            if (CollectionUtils.isEmpty(esMemberCouponList) || esMemberCouponList.size() == 0) {
                throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            QueryWrapper<EsMemberCoupon> queryWrapperUpdate = new QueryWrapper<>();
            queryWrapperUpdate.lambda().eq(EsMemberCoupon::getId, esMemberCouponList.get(0).getId());
            EsMemberCoupon memberCouponInfo = new EsMemberCoupon();
            memberCouponInfo.setState(MemberConstant.send);
            if (null != memberCouponDTO.getOrderSn()) {
                memberCouponInfo.setOrderSn(memberCouponDTO.getOrderSn());
            }
            memberCouponInfo.setUpdateTime(System.currentTimeMillis());
            this.memberCouponMapper.update(memberCouponInfo, queryWrapperUpdate);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("会员优惠券更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Exception th) {
            logger.error("会员优惠券更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新会员优惠券数据
     *
     * @param memberCouponDTO 会员优惠券DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:10 mvn clean package -DskipTest -U
     * @return: com.shopx.common.model.result.DubboResult<EsMemberCouponDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteMemberCouponByCouponIdAndMemId(EsMemberCouponDTO memberCouponDTO) {
        try {
            if (memberCouponDTO == null) {
                throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), MemberErrorCode.PARAM_ERROR.getErrorMsg());
            }
            this.memberCouponMapper.deleteCoupontByMemberIdAndCouponId(memberCouponDTO.getMemberId(), memberCouponDTO.getCouponId(),memberCouponDTO.getCreateTime());
            return DubboResult.success();
        } catch (Exception th) {
            logger.error("会员优惠券删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据id获取会员优惠券详情
     *
     * @param id 主键id
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsMemberCouponDO>
     */
    @Override
    public DubboResult<EsMemberCouponDO> getMemberCoupon(Long id) {
        try {
            QueryWrapper<EsMemberCoupon> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsMemberCoupon::getId, id);
            EsMemberCoupon memberCoupon = this.memberCouponMapper.selectOne(queryWrapper);
            EsMemberCouponDO memberCouponDO = new EsMemberCouponDO();
            if (memberCoupon == null) {
                throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(memberCoupon, memberCouponDO);
            return DubboResult.success(memberCouponDO);
        } catch (ArgumentException ae) {
            logger.error("会员优惠券查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Exception th) {
            logger.error("会员优惠券查询失败", th);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询会员优惠券列表
     *
     * @param memberCouponDTO 会员优惠券DTO
     * @param pageSize        页码
     * @param pageNum         页数
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsMemberCouponDO>
     */
    @Override
    public DubboPageResult<EsMemberCouponDO> getMemberCouponList(EsMemberCouponDTO memberCouponDTO, int pageSize, int pageNum) {
        try {
            QueryWrapper<EsMemberCoupon> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsMemberCoupon::getMemberId, memberCouponDTO.getMemberId())
                    .eq(EsMemberCoupon::getState,memberCouponDTO.getState())
                    .lt(EsMemberCoupon::getStartTime,System.currentTimeMillis())
                    .gt(EsMemberCoupon::getEndTime,System.currentTimeMillis());
            IPage<EsMemberCoupon> couponIPage = memberCouponMapper.selectPage(new Page(pageNum,pageSize), queryWrapper);
            List<EsMemberCouponDO> esMemberCouponDOS = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(couponIPage.getRecords())) {
                esMemberCouponDOS = couponIPage.getRecords().stream().map(eMembereCoupon -> {
                    EsMemberCouponDO esMemberCouponDO = new EsMemberCouponDO();
                    BeanUtil.copyProperties(eMembereCoupon, esMemberCouponDO);
                    return esMemberCouponDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(couponIPage.getTotal(), esMemberCouponDOS);
        } catch (ArgumentException ae) {
            logger.error("会员优惠券分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Exception th) {
            logger.error("会员优惠券分页查询失败", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 后台根据查询会员优惠券列表
     *
     * @param queryAdminCouponDTO 会员优惠券DTO
     * @param pageSize            页码
     * @param pageNum             页数
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsMemberCouponDO>
     */
    @Override
    public DubboPageResult<EsAdminMemberCouponDO> getAdminMemberCouponList(QueryAdminCouponDTO queryAdminCouponDTO, int pageSize, int pageNum) {
        try {
            if(queryAdminCouponDTO == null){
                throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), MemberErrorCode.PARAM_ERROR.getErrorMsg());
            }
            QueryAdminCouponDTO queryAdminCouponDTO1 = new QueryAdminCouponDTO();
            BeanUtil.copyProperties(queryAdminCouponDTO, queryAdminCouponDTO1);
            queryAdminCouponDTO1.setCurrentTime(System.currentTimeMillis());
            Page<EsMemberCoupon> page = new Page<>(pageNum, pageSize);
            IPage<EsAdminMemberCouponDO> esMemberCouponDOList = this.memberCouponMapper.getAdminMemberCouponListGroupNum(page, queryAdminCouponDTO1);
            return DubboPageResult.success(esMemberCouponDOList.getTotal(), esMemberCouponDOList.getRecords());
        } catch (ArgumentException ae) {
            logger.error("会员优惠券分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Exception th) {
            logger.error("会员优惠券分页查询失败", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    @Override
    public DubboResult<EsAdminMemberCouponDO> getBuyerMemberCouponList(Long memberId) {
        try {
            QueryWrapper<EsMemberCoupon> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsMemberCoupon::getMemberId, memberId);
            List<EsMemberCoupon> list = this.memberCouponMapper.selectList(queryWrapper);
            if (CollectionUtils.isEmpty(list)) {
                return DubboPageResult.success(Arrays.asList());
            }
            List<EsMemberCouponDO> listDO = BeanUtil.copyList(list, EsMemberCouponDO.class);
            Map<Integer, List<EsMemberCouponDO>> map = listDO.stream().collect(Collectors.groupingBy(EsMemberCouponDO::getState));
            Map<String , List<EsMemberCouponDO>> resultMap = assembleData(map);
            return DubboPageResult.success(resultMap);
        } catch (ArgumentException ae) {
            logger.error("会员优惠券分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Exception th) {
            logger.error("会员优惠券分页查询失败", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    @Override
    public DubboResult<EsAdminMemberCouponDO> getBuyerMemberCouponNumList(Long memberId) {
        try {
            QueryWrapper<EsMemberCoupon> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsMemberCoupon::getMemberId, memberId);
            List<EsMemberCoupon> list = this.memberCouponMapper.selectList(queryWrapper);
            if (CollectionUtils.isEmpty(list)) {
                return DubboPageResult.success(Arrays.asList());
            }
            List<EsMemberCouponDO> listDO = BeanUtil.copyList(list, EsMemberCouponDO.class);
            Map<Integer, List<EsMemberCouponDO>> map = listDO.stream().collect(Collectors.groupingBy(EsMemberCouponDO::getState));
            Map<String , Integer> resultMap = assembleNum(map);
            return DubboPageResult.success(resultMap);
        } catch (ArgumentException ae) {
            logger.error("会员优惠券分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Exception th) {
            logger.error("会员优惠券分页查询失败", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    private Map<String, List<EsMemberCouponDO>> assembleData(Map<Integer, List<EsMemberCouponDO>> mapInt){
        Map<String, List<EsMemberCouponDO>> map = new HashMap<>();
        List<EsMemberCouponDO> list = null;
        list = mapInt.get(1);
        if(list == null){
            map.put("noUseNum", Arrays.asList());
        }else{
            map.put("noUseNum", list);
        }

        list = mapInt.get(2);
        if(list == null){
            map.put("hasMadeNum", Arrays.asList());
        }else{
            map.put("hasMadeNum", list);
        }

        list = mapInt.get(3);
        if(list == null){
            map.put("expireCouponNum", Arrays.asList());
        }else{
            map.put("expireCouponNum", list);
        }
        return map;
    }

    private Map<String, Integer> assembleNum(Map<Integer, List<EsMemberCouponDO>> mapInt){
        Map<String, Integer> map = new HashMap<>();
        List<EsMemberCouponDO> list = null;
        list = mapInt.get(1);
        if(list == null){
            map.put("noUseNum", 0);
        }else{
            map.put("noUseNum", list.size());
        }

        list = mapInt.get(2);
        if(list == null){
            map.put("hasMadeNum", 0);
        }else{
            map.put("hasMadeNum", list.size());
        }

        list = mapInt.get(3);
        if(list == null){
            map.put("expireCouponNum", 0);
        }else{
            map.put("expireCouponNum", list.size());
        }
        return map;
    }

    /**
     * 根据查询会员优惠券列表
     *
     * @param querySellerCouponDTO 会员优惠券DTO
     * @param pageSize             页码
     * @param pageNum              页数
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsMemberCouponDO>
     */
    @Override
    public DubboPageResult<EsSellerMemberCouponDO> getSellerMemberCouponList(QuerySellerCouponDTO querySellerCouponDTO, int pageSize, int pageNum) {
        try {
            Page<EsMemberCoupon> page = new Page<>(pageNum, pageSize);
            IPage<EsSellerMemberCouponDO> esMemberCouponDOList = memberCouponMapper.getSellerMember(page, querySellerCouponDTO);
            return DubboPageResult.success(esMemberCouponDOList.getTotal(), esMemberCouponDOList.getRecords());
        } catch (Exception th) {
            logger.error("会员优惠券分页查询失败", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 依据会员id查询已经领取的优惠券
     *
     * @param memberId
     * @return
     */
    public Integer getListByMemberId(Long memberId, List<EsCouponDO> memberCouponList, Integer countCoupon) {
        if (countCoupon == 0) {
            return 0;
        }
        QueryWrapper<EsMemberCoupon> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(EsMemberCoupon::getMemberId, memberId);
        List<EsMemberCoupon> esMemberCouponList = this.memberCouponMapper.selectList(queryWrapper);
        if (CollectionUtils.isNotEmpty(esMemberCouponList)) {
            for (EsMemberCoupon esCouponDO : esMemberCouponList) {
                Boolean judge = memberCouponList.stream().anyMatch(esCouponDO1 -> {
                    Boolean t = esCouponDO1.getId().equals(esCouponDO.getCouponId());
                    return t;
                });
                if (countCoupon > 0 && judge) {
                    --countCoupon;
                }

            }
        }
        return countCoupon;
    }

    /**
     * 根据查询会员优惠券列表
     *
     * @param esMemberCouponDTO 会员优惠券DTO
     * @param pageSize          页码
     * @param pageNum           页数
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsMemberCouponDO>
     */
    public DubboPageResult<EsCouponManageMementDO> getCouponManageMentList(EsMemberCouponDTO esMemberCouponDTO, int pageSize, int pageNum) {
        try {
            EsMemberCouponDTO esMemberCouponDTO1 = new EsMemberCouponDTO();
            BeanUtil.copyProperties(esMemberCouponDTO, esMemberCouponDTO1);
            esMemberCouponDTO1.setTimesNow(System.currentTimeMillis());
            Page<EsCouponManageMementDO> page = new Page<>(pageNum, pageSize);
            IPage<EsCouponManageMementDO> result = this.memberCouponMapper.getAdminCouponList(page, esMemberCouponDTO1);
            return DubboPageResult.success(result.getTotal(), result.getRecords());
        } catch (ArgumentException ae) {
            logger.error("会员优惠券查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Exception th) {
            logger.error("会员优惠券查询失败", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }

    }

    /**
     * 根据查询会员优惠券列表
     *
     * @param memberId 会员id
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsMemberCouponDO>
     */
    public DubboPageResult<EsMemberCouponDO> getMemberCouponList(Long memberId) {
        try {
            // 查询条件
            List<EsMemberCouponCountDO> esMemberCouponCountDOList = memberCouponMapper.getMemberCouponNum(memberId);
            for (int i = 0; i < esMemberCouponCountDOList.size(); i++) {
                DubboResult<EsCouponDO> result = iEsCouponService.getCoupon(esMemberCouponCountDOList.get(i).getCouponId());
                if(result.isSuccess() &&  null == result.getData()){
                    continue;
                }
                if (result.isSuccess()) {
                    if (null != result.getData().getLimitNum()) {
                        if (esMemberCouponCountDOList.get(i).getNum() == result.getData().getLimitNum()) {
                            esMemberCouponCountDOList.remove(i);
                        }
                    }
                }
            }
            List<EsMemberCouponDO> lists = new ArrayList<>();
            esMemberCouponCountDOList.stream().forEach(esMemberCouponCountDO -> {
                EsMemberCouponDO esMemberCouponDO = new EsMemberCouponDO();
                esMemberCouponDO.setCouponId(esMemberCouponCountDO.getCouponId());
                lists.add(esMemberCouponDO);
            });
            return DubboPageResult.success(lists);
        }catch (Exception th) {
            logger.error("会员优惠券查询失败", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据主键删除会员优惠券数据
     *
     * @param id 主键id
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsMemberCouponDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteMemberCoupon(Long id) {
        try {
            if (id == 0) {
                throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), String.format("参数传入错误ID不能为空[%s]", id));
            }
            QueryWrapper<EsMemberCoupon> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.lambda().eq(EsMemberCoupon::getId, id);
            this.memberCouponMapper.delete(deleteWrapper);
            return DubboResult.success();
        } catch (Exception th) {
            logger.error("会员优惠券删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据查询会员优惠券列表
     *
     * @param memberId        会员id
     * @param pageSize        页码
     * @param pageNum         页数
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsMemberCouponDO>
     */
    @Override
    public DubboPageResult<EsTradeCouponDO> getMemberCouponRecommendList(Long memberId, int pageSize, int pageNum) {
        try {
            DubboPageResult<EsMemberCouponDO> result = this.getMemberCouponList(memberId);
            List<EsTradeCouponDO> esTradeCouponDOS = new ArrayList<>();
            List<EsTradeCouponDO> esTradeCouponDOList = new ArrayList<>();
            DubboPageResult<EsCouponDO> resultTrade;
            if (result.isSuccess() &&  CollectionUtils.isNotEmpty(result.getData().getList())) {
                List<EsMemberCouponDO> lists = result.getData().getList();
                List<EsMemberTradeCouponDTO> esMemberTradeCouponDTOList = BeanUtil.copyList(lists, EsMemberTradeCouponDTO.class);
                //调用交易模块接口
//                resultTrade = iEsCouponService.getNotReceivedCouponList(esMemberTradeCouponDTOList, pageSize, pageNum);
                resultTrade = null;
                if (!resultTrade.isSuccess()) {
                    logger.error("交易模块会员优惠券分页查询失败");
                    return DubboPageResult.fail(MemberErrorCode.TRADE_COUPON_ERROR.getErrorCode(), MemberErrorCode.TRADE_COUPON_ERROR.getErrorMsg());
                }
                if (CollectionUtils.isEmpty(resultTrade.getData().getList())) {
                    return DubboPageResult.success(esTradeCouponDOS);
                }
                if (CollectionUtils.isNotEmpty(resultTrade.getData().getList())) {
                    esTradeCouponDOList = resultTrade.getData().getList().stream().map(esCouponDOTrade -> {
                        EsTradeCouponDO esTradeCouponDO = new EsTradeCouponDO();
                        DubboResult<EsShopDetailDO> shopDetails = iEsShopDetailService.getByShopId(esCouponDOTrade.getShopId());
                        BeanUtil.copyProperties(esCouponDOTrade, esTradeCouponDO);
                        if (shopDetails.isSuccess()) {
                            if (null != shopDetails.getData() && null != shopDetails.getData().getShopLogo()) {
                                String shopLogo = shopDetails.getData().getShopLogo();
                                esTradeCouponDO.setShopLogo(shopLogo);
                            }
                        }
                        return esTradeCouponDO;
                    }).collect(Collectors.toList());
                }
            }
            return DubboPageResult.success(result.getData().getTotal(), esTradeCouponDOList);
        } catch (ArgumentException ae) {
            logger.error("会员优惠券分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Exception th) {
            logger.error("会员优惠券分页查询失败", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 查询优惠券数码
     *
     * @param memberId 页数
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<Integer>
     */
    @Override
    public DubboResult<Integer> getCouponNum(Long memberId) {
        try {
            Long times = System.currentTimeMillis();
            Integer num = this.memberCouponMapper.getCouponNum(memberId, times);
            return DubboResult.success(num);
        }catch (Exception th) {
            logger.error("会员优惠券分页查询失败", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    @Override
    public DubboResult<Integer> getCouponId(Long memberId, Integer isCheck) {
        try {
            Integer couponId = this.memberCouponMapper.getCouponId(memberId, isCheck);
            return DubboResult.success(couponId);
        }catch (Exception th) {
            logger.error("会员优惠券ID查询失败", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }

    }

    @Override
    public DubboResult<Integer> getCouponNum(Long memberId, Long couponId) {
        try {
            Long times = System.currentTimeMillis();
            Integer num = this.memberCouponMapper.getCouponNumById(memberId,couponId, times);
            return DubboResult.success(num);
        }catch (Exception th) {
            logger.error("会员优惠券分页查询失败", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 依据优惠券id和会员id查询优惠券数量
     * mvn clean package -DskipTest -U
     * @param esMemberCouponDTO DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<Integer>
     */
    @Override
    public DubboResult<Integer> getCountByMemberIdAndCouponId(EsMemberCouponDTO esMemberCouponDTO) {
        QueryWrapper<EsMemberCoupon> queryWrapper = new QueryWrapper<>();
        try {
            queryWrapper.lambda().eq(EsMemberCoupon::getCouponId, esMemberCouponDTO.getCouponId()).eq(EsMemberCoupon::getMemberId, esMemberCouponDTO.getMemberId()).eq(EsMemberCoupon::getState, MemberConstant.userCoupon);
            Integer num = this.memberCouponMapper.selectCount(queryWrapper);
            return DubboResult.success(num);
        }catch (Exception th) {
            logger.error("会员优惠券统计失败", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }

    }

    /**
     * 根据优惠券id和店铺id修改优惠券状态
     *
     * @param memberCouponDTO DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<Integer>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateStateByShopIdAndCouponId(EsMemberCouponDTO memberCouponDTO) {
        QueryWrapper<EsMemberCoupon> queryWrapper = new QueryWrapper<>();
        try {
            queryWrapper.lambda().eq(EsMemberCoupon::getCouponId, memberCouponDTO.getCouponId()).eq(EsMemberCoupon::getShopId, memberCouponDTO.getShopId()).eq(EsMemberCoupon::getState, MemberConstant.userCoupon);
            EsMemberCoupon esMemberCoupon = new EsMemberCoupon();
            esMemberCoupon.setState(MemberConstant.loseUse);
            this.memberCouponMapper.update(esMemberCoupon, queryWrapper);
            return DubboResult.success();
        }catch (Exception th) {
            logger.error("会员优惠券修改失败", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    @Override
    public DubboPageResult<EsMemberCouponDO> getByMemberIdAndCouponIdList(EsMemberCouponDTO memberCouponDTO) {
        QueryWrapper<EsMemberCoupon> queryWrapper = new QueryWrapper<>();
        try {
            queryWrapper.lambda().eq(EsMemberCoupon::getCouponId, memberCouponDTO.getCouponId()).eq(EsMemberCoupon::getMemberId, memberCouponDTO.getMemberId()).eq(EsMemberCoupon::getState, MemberConstant.userCoupon);
            List<EsMemberCoupon> esMemberCoupons = this.memberCouponMapper.selectList(queryWrapper);
            List<EsMemberCouponDO> esMemberCouponDOS1 = BeanUtil.copyList(esMemberCoupons, EsMemberCouponDO.class);
            return DubboPageResult.success(esMemberCouponDOS1);
        }catch (Throwable th) {
            logger.error("会员优惠券查询失败", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    @Override
    public DubboResult getMemberWhetherCouponIds(Long memberId, List<Long> couponIdList) {
        try{
            List<Long> result = memberCouponMapper.getMemberWhetherCouponIds(memberId,couponIdList);
            return DubboResult.success(result);
        } catch (Exception th) {
            logger.error("优惠券id列表查询失败", th);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    public DubboResult getMemberCouponInOrder(Long memberId,Double money, List<Long> shopIdList) {
        try{
            QueryWrapper<EsShop> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsShop::getShopName, shopName);
            EsShop shop = esShopMapper.selectOne(queryWrapper);
            if(shop != null && !shopIdList.contains(shop.getId())){
                shopIdList.add(shop.getId());
            }

            List<Object> list = new ArrayList<>();
            Map<String, List<EsMemberCouponDO>> collect = new HashMap();
            if(null == memberId || shopIdList == null){
                throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), MemberErrorCode.PARAM_ERROR.getErrorMsg());
            }

            if(shopIdList.size() == 0){
                return DubboResult.success(list);
            }
            long curTime = new Date().getTime();
            List<EsMemberCouponDO> resultList = memberCouponMapper.getMemberCouponInOrder(memberId, money, shopIdList, curTime);
            resultList.remove(null);
            if (CollectionUtils.isNotEmpty(resultList)) {
                resultList = resultList.stream()
                        .map(esMemberCouponDO -> {
                            if(esMemberCouponDO.getEndTime() - curTime > 0 && (esMemberCouponDO.getEndTime() - curTime)/(24*60*60*1000) <= 3){
                                esMemberCouponDO.setJudgmentToExpire(true);
                            }
                            return esMemberCouponDO;
                        }).collect(Collectors.toList());
            }
            collect = resultList.stream().collect(Collectors.groupingBy(EsMemberCouponDO::getShopName));
            for(Map.Entry<String, List<EsMemberCouponDO>> entry : collect.entrySet()){
                Map<String, Object> map = new HashMap<>();
                map.put("shopName", entry.getKey());
                if(CollectionUtils.isNotEmpty(entry.getValue())){
                    map.put("shopId", entry.getValue().get(0).getShopId());
                    map.put("usableNum", entry.getValue().size());
                }
                map.put("data", entry.getValue());
                list.add(map);
            }
//            list.add(getCurrencyCouponList(memberId, curTime));
            return DubboResult.success(list);
         } catch (ArgumentException ae) {
            logger.error("会员优惠券分页查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
         }catch (Exception th) {
            logger.error("获取会员优惠券订单失败", th);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }

    }

    @Override
    public DubboResult getMemberCouponInOrder(Long memberId, Map<Long, Double> shopIdPrice) {
        try{
            long curTime = new Date().getTime();
            List<EsMemberCouponDO> resultList = new ArrayList<>();
            for (Map.Entry<Long, Double> e : shopIdPrice.entrySet()) {
                Long shopId = e.getKey();
                Double price = e.getValue();
                List<EsMemberCouponDO> memberCouponInOrder = memberCouponMapper.getMemberCouponInOrder(memberId, price, Arrays.asList(shopId), curTime);
                resultList.addAll(memberCouponInOrder);
            }
            resultList.remove(null);
            if (CollectionUtils.isNotEmpty(resultList)) {
                resultList = resultList.stream()
                        .map(esMemberCouponDO -> {
                            if(esMemberCouponDO.getEndTime() - curTime > 0 && (esMemberCouponDO.getEndTime() - curTime)/(24*60*60*1000) <= 3){
                                esMemberCouponDO.setJudgmentToExpire(true);
                            }
                            return esMemberCouponDO;
                        }).collect(Collectors.toList());
            }
            Map<String, List<EsMemberCouponDO>> collect = new HashMap();
            List<Object> list = new ArrayList<>();
            collect = resultList.stream().collect(Collectors.groupingBy(EsMemberCouponDO::getShopName));
            for(Map.Entry<String, List<EsMemberCouponDO>> entry : collect.entrySet()){
                Map<String, Object> map = new HashMap<>();
                map.put("shopName", entry.getKey());
                if(CollectionUtils.isNotEmpty(entry.getValue())){
                    map.put("shopId", entry.getValue().get(0).getShopId());
                    map.put("usableNum", entry.getValue().size());
                }
                map.put("data", entry.getValue());
                list.add(map);
            }
//            list.add(getCurrencyCouponList(memberId, curTime));
            return DubboResult.success(list);
        } catch (ArgumentException ae) {
            logger.error("会员优惠券分页查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        }catch (Exception th) {
            logger.error("获取会员优惠券订单失败", th);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateMemberCouponIsCheck(EsMemberCouponDTO memberCouponDTO) {
        try {
            //  首先把所有的优惠券设置为未选中
            QueryWrapper<EsMemberCoupon> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(EsMemberCoupon::getMemberId,memberCouponDTO.getMemberId())
                    .eq(EsMemberCoupon::getShopId,memberCouponDTO.getShopId());
            EsMemberCoupon esMemberCoupon =new EsMemberCoupon();
            esMemberCoupon.setIsCheck(2);
            logger.info("会员优惠券ID"+memberCouponDTO.getId());
            this.memberCouponMapper.update(esMemberCoupon,wrapper);
            // 重新设置新的优惠券ID的选中状态
            EsMemberCoupon esMemberCoupon2 = this.memberCouponMapper.selectById(memberCouponDTO.getId());
            esMemberCoupon2.setIsCheck(1);
            this.memberCouponMapper.updateById(esMemberCoupon2);
            return DubboResult.success();
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            logger.error("优惠券选中状态失败", e);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateMemberCouponIsNotCheck(EsMemberCouponDTO memberCouponDTO) {
        try {
            //  首先把所有的优惠券设置为未选中
            QueryWrapper<EsMemberCoupon> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(EsMemberCoupon::getMemberId,memberCouponDTO.getMemberId())
                    .eq(EsMemberCoupon::getShopId,memberCouponDTO.getShopId());
            EsMemberCoupon esMemberCoupon =new EsMemberCoupon();
            esMemberCoupon.setIsCheck(2);
            logger.info("会员优惠券ID"+memberCouponDTO.getId());
            this.memberCouponMapper.update(esMemberCoupon,wrapper);
            return DubboResult.success();
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            logger.error("优惠券设置未选中状态失败", e);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    public DubboResult<EsMemberCouponDO> getMemberCouponByOrder(String orderSn) {
        try {
            QueryWrapper<EsMemberCoupon> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsMemberCoupon::getOrderSn,orderSn);
            EsMemberCoupon coupon = this.memberCouponMapper.selectOne(queryWrapper);
            if(coupon!=null){
                EsMemberCouponDO esMemberCouponDO = new EsMemberCouponDO();
                BeanUtil.copyProperties(coupon,esMemberCouponDO);
                return DubboResult.success(

                );
            }
            return DubboResult.success();
        }catch (ArgumentException ae) {
            logger.error("会员优惠券分页查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        }catch (Exception th) {
            logger.error("获取会员优惠券订单失败", th);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    private Map<String, Object> getCurrencyCouponList(Long memberId, Long curTime){
        Map<String, Object> map = new HashMap<>();
        QueryWrapper<EsMemberCoupon> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(EsMemberCoupon::getMemberId, memberId)
                .le(EsMemberCoupon::getStartTime, curTime)
                .ge(EsMemberCoupon::getEndTime, curTime);
        List<EsMemberCoupon> list = memberCouponMapper.selectList(queryWrapper);
        List<Long> ids = list.stream().map(EsMemberCoupon::getCouponId).collect(Collectors.toList());
        DubboPageResult<EsCouponDO> couponDOResult = iEsCouponService.getEsCouponListByIds(ids);

        List<Long> currencyListIds = new ArrayList<>();
        if(couponDOResult.isSuccess()){
            List<EsCouponDO> couponDOList = couponDOResult.getData().getList();
            if(CollectionUtils.isNotEmpty(couponDOList)){
                currencyListIds = couponDOList.stream().filter(e -> "CURRENCY".equals(e.getCouponType())).map(EsCouponDO::getId).collect(Collectors.toList());
            }
        }

        List<EsMemberCoupon> currencyList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(currencyListIds)){
            List<Long> finalCurrencyListIds = currencyListIds;
            currencyList = list.stream().filter(e -> finalCurrencyListIds.contains(e.getCouponId())).collect(Collectors.toList());
        }
        map.put("currency", currencyList);
        return map;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateIsCheckByMemberIdAndShopId(Long memberId,List<Long> shopIdList) {
        try {
            memberCouponMapper.updateIsCheckByMemberId(memberId,shopIdList);
            return DubboResult.success();
        }catch (Exception th) {
            logger.error("会员优惠券修改失败", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    @Override
    public DubboResult<Integer> getCouponCount(Long memberId, Long couponId) {
        try {
            QueryWrapper<EsMemberCoupon> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsMemberCoupon::getMemberId, memberId).eq(EsMemberCoupon::getCouponId,couponId);
            List<EsMemberCoupon> list = memberCouponMapper.selectList(queryWrapper);
            Integer count = 0;
            if (CollectionUtils.isNotEmpty(list)){
                count = list.size();
            }
            return DubboResult.success(count);
        }catch (ArgumentException ae) {
            logger.error("查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        }catch (Exception th) {
            logger.error("查询失败", th);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    public DubboResult<EsMemberCouponStatDO> getCouponStat(Long memberId) {
        try {
        QueryWrapper<EsMemberCoupon> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.lambda().eq(EsMemberCoupon::getMemberId, memberId).eq(EsMemberCoupon::getState, 1);
        Integer count1 = memberCouponMapper.selectCount(queryWrapper1);
        QueryWrapper<EsMemberCoupon> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.lambda().eq(EsMemberCoupon::getMemberId, memberId).eq(EsMemberCoupon::getState, 2);
        Integer count2 = memberCouponMapper.selectCount(queryWrapper2);
        QueryWrapper<EsMemberCoupon> queryWrapper3 = new QueryWrapper<>();
        queryWrapper3.lambda().eq(EsMemberCoupon::getMemberId, memberId).eq(EsMemberCoupon::getState, 3);
        Integer count3 = memberCouponMapper.selectCount(queryWrapper3);
        EsMemberCouponStatDO memberCouponStatDO = new EsMemberCouponStatDO();
        memberCouponStatDO.setEnabledCount(count1);
        memberCouponStatDO.setUsedCount(count2);
        memberCouponStatDO.setFailureCount(count3);
        return DubboResult.success(memberCouponStatDO);
        }catch (ArgumentException ae) {
            logger.error("查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        }catch (Exception th) {
            logger.error("查询失败", th);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    public DubboResult<List<EsMemberCouponDO>> getByMemberId(Long memberId) {
        List<EsMemberCoupon> esMemberCoupons = this.baseMapper.selectList(Wrappers.<EsMemberCoupon>lambdaQuery().eq(EsMemberCoupon::getMemberId, memberId).eq(EsMemberCoupon::getIsDel,"1"));
        List<EsMemberCouponDO> esMemberCouponDOS = BeanUtil.copyList(esMemberCoupons, EsMemberCouponDO.class);
        return DubboResult.success(esMemberCouponDOS);
    }
}
