package com.xdl.jjg.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jjg.member.model.dto.EsMemberCouponDTO;
import com.jjg.trade.model.domain.EsCouponDO;
import com.jjg.trade.model.domain.EsOrderDO;
import com.jjg.trade.model.domain.EsSellerCouponDO;
import com.jjg.trade.model.dto.EsCouponDTO;
import com.jjg.trade.model.dto.EsMemberTradeCouponDTO;
import com.jjg.trade.model.dto.EsOrderDTO;
import com.jjg.trade.model.dto.EsSellerCouponDTO;
import com.jjg.trade.model.enums.CouponTypeEnum;
import com.jjg.trade.model.vo.EsCouponVO;
import com.xdl.jjg.constant.TradeErrorCode;
import com.xdl.jjg.entity.EsCoupon;
import com.xdl.jjg.mapper.EsCouponMapper;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.web.service.IEsCouponService;
import com.xdl.jjg.web.service.IEsOrderService;
import com.xdl.jjg.web.service.feign.member.MemberCouponService;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.apache.dubbo.common.utils.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 优惠券 服务实现类
 * </p>
 *
 * @author LBW 981087977@qq.com
 * @since 2019-06-05 09:20:40
 */
@Service(version = "${dubbo.application.version}", interfaceClass = IEsCouponService.class, timeout = 50000)
public class EsCouponServiceImpl extends ServiceImpl<EsCouponMapper, EsCoupon> implements IEsCouponService {

    private static Logger logger = LoggerFactory.getLogger(EsCouponServiceImpl.class);

    @Autowired
    private EsCouponMapper couponMapper;

    @Autowired
    private IEsOrderService iEsOrderService;

    @Reference(version = "${dubbo.application.version}",timeout = 5000)
    private MemberCouponService iEsMemberCouponService;

    /**
     * 插入优惠券数据
     *
     * @param couponDTO 优惠券DTO
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsCouponDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertCoupon(EsCouponDTO couponDTO) {
        try {
            EsCoupon coupon = new EsCoupon();
            BeanUtil.copyProperties(couponDTO, coupon);
            coupon.setIsProGoods(1);
            coupon.setIsDel(1);
            this.couponMapper.insert(coupon);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("优惠券新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable ae) {
            logger.error("优惠券新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult sellerInsertCoupon(EsSellerCouponDTO couponDTO) {
        try {
            EsCoupon coupon = new EsCoupon();
            BeanUtil.copyProperties(couponDTO, coupon);
            if(couponDTO.getIsProGoods()==null){
                couponDTO.setIsProGoods(false);
            }
            coupon.setIsDel(1);
            coupon.setIsProGoods(couponDTO.getIsProGoods() == true ? 1 : 2 );
            coupon.setIsReceive(couponDTO.getIsReceive() == null ? 1 : couponDTO.getIsReceive());
            this.couponMapper.insert(coupon);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("优惠券新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable ae) {
            logger.error("优惠券新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
    /**
     * 根据条件更新优惠券数据
     *
     * @param couponDTO 优惠券DTO
     * @param id                          主键id
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsCouponDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateCoupon(EsCouponDTO couponDTO, Long id) {
        try {
            EsCoupon coupon = this.couponMapper.selectById(id);
            if (coupon == null) {
                throw new ArgumentException(TradeErrorCode.DATA_NOT_EXIST.getErrorCode(), TradeErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(couponDTO, coupon);
            QueryWrapper<EsCoupon> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsCoupon::getId, id);
            this.couponMapper.update(coupon, queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("优惠券更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("优惠券更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult sellerUpdateCoupon(EsSellerCouponDTO couponDTO, Long id) {
        try {
            EsCoupon coupon = this.couponMapper.selectById(id);
            if (coupon == null) {
                throw new ArgumentException(TradeErrorCode.DATA_NOT_EXIST.getErrorCode(), TradeErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(couponDTO, coupon);
            QueryWrapper<EsCoupon> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsCoupon::getId, id);
            if(couponDTO.getIsProGoods()==null){
                couponDTO.setIsProGoods(false);
            }
            coupon.setIsProGoods(couponDTO.getIsProGoods() == true ? 1 : 2 );
            this.couponMapper.update(coupon, queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("优惠券更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("优惠券更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
    /**
     * 根据id获取优惠券详情
     *
     * @param id 主键id
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsCouponDO>
     */
    @Override
    public DubboResult<EsCouponDO> getCoupon(Long id) {
        try {
            EsCoupon coupon = this.couponMapper.selectById(id);
            if (coupon == null) {
                throw new ArgumentException(TradeErrorCode.COUPON_DATA_NOT_EXIST.getErrorCode(), TradeErrorCode.COUPON_DATA_NOT_EXIST.getErrorMsg());
            }
            EsCouponDO couponDO = new EsCouponDO();
            BeanUtil.copyProperties(coupon, couponDO);
            return DubboResult.success(couponDO);
        } catch (ArgumentException ae){
            logger.error("优惠券查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("优惠券查询失败", th);
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    @Override
    public DubboPageResult<EsCouponDO> getCoupons(EsCouponDTO esCouponDTO) {
        try {
            Integer isRegister = esCouponDTO.getIsRegister();

            List<EsCoupon> esCouponList = this.couponMapper.selectCoupons(isRegister, esCouponDTO.getIsDel());
            List<EsCouponDO> esCouponDOS = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(esCouponList)) {
                List<EsCouponDO> esCouponDOS1 = BeanUtil.copyList(esCouponList, EsCouponDO.class);
                return DubboPageResult.success(esCouponDOS1);
            }
            return DubboPageResult.success(esCouponDOS);
        } catch (ArgumentException ae){
            logger.error("优惠券查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("优惠券查询失败", th);
            return DubboPageResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    public DubboResult<EsSellerCouponDO> getSellerCoupon(Long id) {
        try {
            EsCoupon coupon = this.couponMapper.selectById(id);
            if (coupon == null) {
                throw new ArgumentException(TradeErrorCode.DATA_NOT_EXIST.getErrorCode(), TradeErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            EsSellerCouponDO couponDO = new EsSellerCouponDO();
            BeanUtil.copyProperties(coupon, couponDO);
            couponDO.setIsProGoods(coupon.getIsProGoods() == 1 ? true : false);
            if(!StringUtils.isBlank(coupon.getCouponType())){
                couponDO.setCouponTypeText(CouponTypeEnum.valueOf(coupon.getCouponType()).description());
            }
            return DubboResult.success(couponDO);
        } catch (ArgumentException ae){
            logger.error("优惠券查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("优惠券查询失败", th);
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }
    /**
     * 根据查询优惠券列表
     *
     * @param couponDTO 会员优惠券DTO
     * @param pageSize     页码
     * @param pageNum      页数
     * @auther: libw 981087977@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsCouponDO>
     */
    @Override
    public DubboPageResult<EsCouponDO> getCouponList(EsCouponDTO couponDTO, int pageSize, int pageNum) {
        QueryWrapper<EsCoupon> queryWrapper = new QueryWrapper<>();
        try {
            Page<EsCoupon> page = new Page<>(pageNum, pageSize);
            IPage<EsCoupon> iPage = this.page(page, queryWrapper);
            List<EsCouponDO> couponDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                couponDOList = iPage.getRecords().stream().map(coupon -> {
                    EsCouponDO couponDO = new EsCouponDO();
                    BeanUtil.copyProperties(coupon, couponDO);
                    long nowTime = System.currentTimeMillis();
                    //当前时间小于活动的开始时间 则为活动未开始
                    if(nowTime < coupon.getStartTime().longValue() ){
                        couponDO.setCouponStatus("活动未开始");
                        //大于活动的开始时间，小于活动的结束时间
                    }else if(coupon.getStartTime().longValue() < nowTime && nowTime < coupon.getEndTime() ){
                        couponDO.setCouponStatus("正在进行中");
                    }else{
                        couponDO.setCouponStatus("活动已失效");
                    }
                    if(!StringUtils.isBlank(coupon.getCouponType())){
                        couponDO.setCouponTypeText(CouponTypeEnum.valueOf(coupon.getCouponType()).description());
                    }
                    return couponDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(couponDOList);
        } catch (ArgumentException ae){
            logger.error("优惠券分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("优惠券分页查询失败", th);
            return DubboPageResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    @Override
    public DubboPageResult<EsSellerCouponDO> getSellerCouponList(EsCouponDTO couponDTO, int pageSize, int pageNum) {
        QueryWrapper<EsCoupon> queryWrapper = new QueryWrapper<>();
        try {
            queryWrapper.lambda().eq(EsCoupon::getShopId,couponDTO.getShopId()).eq(EsCoupon::getIsDel,1);
            if(!StringUtils.isBlank(couponDTO.getCouponType())){
                queryWrapper.lambda().eq(EsCoupon::getCouponType,couponDTO.getCouponType());
            }
            if(!StringUtils.isBlank(couponDTO.getCouponStatus())){
                long currentTimeMillis = System.currentTimeMillis();
                if ("0".equals(couponDTO.getCouponStatus())){
                    queryWrapper.lambda().gt(EsCoupon::getStartTime,currentTimeMillis);
                }else if ("1".equals(couponDTO.getCouponStatus())){
                    queryWrapper.lambda().lt(EsCoupon::getStartTime,currentTimeMillis).gt(EsCoupon::getEndTime,currentTimeMillis);
                }else if ("2".equals(couponDTO.getCouponStatus())){
                    queryWrapper.lambda().lt(EsCoupon::getEndTime,currentTimeMillis);
                }
            }
            if(!StringUtils.isBlank(couponDTO.getTitle())){
                queryWrapper.lambda().eq(EsCoupon::getTitle,couponDTO.getTitle());
            }
            if(couponDTO.getIsCoupons() !=null){
                queryWrapper.lambda().eq(EsCoupon::getIsCoupons,couponDTO.getIsCoupons());
            }
            Page<EsCoupon> page = new Page<>(pageNum, pageSize);
            IPage<EsCoupon> iPage = this.page(page, queryWrapper);
            List<EsSellerCouponDO> couponDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                couponDOList = iPage.getRecords().stream().map(coupon -> {
                    EsSellerCouponDO couponDO = new EsSellerCouponDO();
                    BeanUtil.copyProperties(coupon, couponDO);
                    long nowTime = System.currentTimeMillis();
                    //当前时间小于活动的开始时间 则为活动未开始
                    if(nowTime < coupon.getStartTime().longValue() ){
                        couponDO.setCouponStatus("活动未开始");
                        //大于活动的开始时间，小于活动的结束时间
                    }else if(coupon.getStartTime().longValue() < nowTime && nowTime < coupon.getEndTime() ){
                        couponDO.setCouponStatus("正在进行中");
                    }else{
                        couponDO.setCouponStatus("活动已失效");
                    }
                    if(!StringUtils.isBlank(coupon.getCouponType())){
                        couponDO.setCouponTypeText(CouponTypeEnum.valueOf(coupon.getCouponType()).description());
                    }
                    if(coupon.getIsDel() == 2){
                        couponDO.setCouponStatus("活动已失效");
                    }
                    couponDO.setIsProGoods(coupon.getIsProGoods() == 1 ? true : false);
                    return couponDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(couponDOList);
        } catch (ArgumentException ae){
            logger.error("优惠券分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("优惠券分页查询失败", th);
            return DubboPageResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据主键删除会员优惠券数据
     * 逻辑删除
     * @param ids 主键id
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsCouponDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteCoupon(Integer[] ids) {
        try {
            long now = System.currentTimeMillis();
            QueryWrapper<EsCoupon> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().in(EsCoupon::getId,ids);
            List<EsCoupon> esCouponList = this.list(queryWrapper);
            esCouponList.forEach(esCoupon -> {
                // 优惠券未失效状态下，验证当前是否有已领取未使用优惠券
                if(now < esCoupon.getEndTime()){
                    Integer receivedNum = esCoupon.getReceivedNum();
                    Integer usedNum = esCoupon.getUsedNum();
                    if (receivedNum > 0 && usedNum < receivedNum ){
                        throw new ArgumentException(TradeErrorCode.COUPON_DEL_ERROE.getErrorCode(),TradeErrorCode.COUPON_DEL_ERROE.getErrorMsg());
                    }
                }
                QueryWrapper<EsCoupon> queryWrapper2 = new QueryWrapper<>();
                queryWrapper2.lambda().eq(EsCoupon::getId,esCoupon.getId());
                esCoupon.setIsDel(2);
                this.update(esCoupon,queryWrapper2);
            });

            return DubboResult.success();
        } catch (ArgumentException ae){
             logger.error("会员优惠券删除失败", ae);
             TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
             return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("会员优惠券删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult downCoupon(Long id) {
        try {
            EsCoupon esCoupon = this.couponMapper.selectById(id);
            Integer receivedNum = esCoupon.getReceivedNum();
            Integer usedNum = esCoupon.getUsedNum();
            // 如果 优惠券已被领取，且 被使用的数量小于总共被领取的数量
            if (receivedNum > 0 && usedNum < receivedNum ){
                throw new ArgumentException(TradeErrorCode.COUPON_DEL_ERROE.getErrorCode(),TradeErrorCode.COUPON_DEL_ERROE.getErrorMsg());
            }
            esCoupon.setIsDel(2);
            this.couponMapper.updateById(esCoupon);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("会员优惠券下架失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("会员优惠券下架失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    public DubboPageResult<EsCouponDO> getNotReceivedCouponList(List<EsMemberTradeCouponDTO> esMemberCouponDTOList, int pageSize, int pageNum) {
        QueryWrapper<EsCoupon> queryWrapper = new QueryWrapper<>();
        try {
            List couponStr = new ArrayList<>();
            for (int i = 0; i < esMemberCouponDTOList.size(); i++) {
                if (i < esMemberCouponDTOList.size() - 1) {
                    couponStr.add(esMemberCouponDTOList.get(i).getCouponId());
                } else {
                    couponStr.add(esMemberCouponDTOList.get(i).getCouponId());
                }
            }

            queryWrapper.lambda().notIn(EsCoupon::getId,couponStr);
            queryWrapper.lambda().gt(EsCoupon::getEndTime,System.currentTimeMillis());
            Page<EsCoupon> page = new Page<>(pageNum, pageSize);
            IPage<EsCoupon> iPage = this.page(page, queryWrapper);
            List<EsCouponDO> couponDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                couponDOList = iPage.getRecords().stream().map(coupon -> {
                    EsCouponDO couponDO = new EsCouponDO();
                    BeanUtil.copyProperties(coupon, couponDO);
                    return couponDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(iPage.getTotal(),couponDOList);
        } catch (ArgumentException ae){
            logger.error("优惠券分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("优惠券分页查询失败", th);
            return DubboPageResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateUsedNum(Long id) {
        try {
            EsCoupon coupon = this.couponMapper.selectById(id);
            if (coupon == null) {
                throw new ArgumentException(TradeErrorCode.DATA_NOT_EXIST.getErrorCode(), TradeErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            coupon.setUsedNum(coupon.getUsedNum() + 1);
            this.couponMapper.updateById(coupon);
            return DubboResult.success();
        }  catch (ArgumentException ae){
            logger.error("优惠券更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("优惠券更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }


    /**
     * 会员模块 查询会员列表list
     * @auther: LIUJG
     * @date: 2019/08/12 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsCouponDO>
     */
    @Override
    public DubboResult getMemberCouponList() {
        QueryWrapper<EsCoupon> queryWrapper = new QueryWrapper<>();
        try {

            queryWrapper.lambda().eq(EsCoupon::getIsDel,1);
            List<EsCoupon> esCoupons = this.couponMapper.selectList(queryWrapper);
            List<EsCouponDO> couponDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(esCoupons)) {
                couponDOList = esCoupons.stream().map(coupon -> {
                    EsCouponDO couponDO = new EsCouponDO();
                    BeanUtil.copyProperties(coupon, couponDO);
                    return couponDO;
                }).collect(Collectors.toList());
            }
            return DubboResult.success(couponDOList);
        } catch (ArgumentException ae){
            logger.error("优惠券分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("优惠券分页查询失败", th);
            return DubboPageResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult addMemberCoupon(EsCouponVO esCouponVO, EsOrderDTO esOrderDTO) {
        try {
            // 赠送优惠券之前做 状态验证
            EsCoupon coupon = this.couponMapper.selectById(esCouponVO.getId());
            if (coupon == null) {
                throw new ArgumentException(TradeErrorCode.DATA_NOT_EXIST.getErrorCode(), TradeErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            DubboResult<EsOrderDO> esBuyerOrderInfo = this.iEsOrderService.getEsBuyerOrderInfo(esOrderDTO.getOrderSn());
            EsOrderDO data1 = esBuyerOrderInfo.getData();
            //  查询该会员是否领取该优惠券，验证最多可领取数量
            EsMemberCouponDTO MemberCouponDTO = new EsMemberCouponDTO();
            MemberCouponDTO.setMemberId(esOrderDTO.getMemberId());
            MemberCouponDTO.setCouponId(esCouponVO.getId());
            DubboResult<Integer> count = iEsMemberCouponService.getCountByMemberIdAndCouponId(MemberCouponDTO);

            // 会员优惠券 该券的已领取数量
            Integer data = count.getData();
            Integer limitNum = esCouponVO.getLimitNum();
            if (data < limitNum){
//                coupon.setReceivedNum(coupon.getReceivedNum() + 1);
//                this.couponMapper.updateById(coupon);

                EsMemberCouponDTO esMemberCouponDTO = new EsMemberCouponDTO();
                esMemberCouponDTO.setMemberId(esOrderDTO.getMemberId());
                esMemberCouponDTO.setCouponId(esCouponVO.getId());
                // 售后退回赠品信息通过 创建时间精准确定一条数据
                esMemberCouponDTO.setCreateTime(data1.getCreateTime());
                esMemberCouponDTO.setMemberName(esOrderDTO.getMemberName());
                esMemberCouponDTO.setTitle(esCouponVO.getTitle());
                esMemberCouponDTO.setCouponMoney(esCouponVO.getCouponMoney());
                esMemberCouponDTO.setCouponThresholdPrice(esCouponVO.getCouponThresholdPrice());
                esMemberCouponDTO.setStartTime(esCouponVO.getStartTime());
                esMemberCouponDTO.setEndTime(esCouponVO.getEndTime());
                esMemberCouponDTO.setState(1);
                esMemberCouponDTO.setShopId(esCouponVO.getShopId());
                esMemberCouponDTO.setShopName(esCouponVO.getSellerName());
                esMemberCouponDTO.setIsDel(esCouponVO.getIsDel());
                iEsMemberCouponService.insertMemberCoupon(esMemberCouponDTO);
            }
            return DubboResult.success();
        }  catch (ArgumentException ae){
            logger.error("优惠券更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),"优惠券信息不存在");
        } catch (Throwable th) {
            logger.error("优惠券更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult redMemberCoupon(EsCouponVO esCouponVO, EsOrderDTO esOrderDTO) {
        try {
            //
            EsCoupon coupon = this.couponMapper.selectById(esCouponVO.getId());
            if (coupon == null) {
                throw new ArgumentException(TradeErrorCode.DATA_NOT_EXIST.getErrorCode(), TradeErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            coupon.setReceivedNum(coupon.getReceivedNum() - 1);
            this.couponMapper.updateById(coupon);
            EsMemberCouponDTO esMemberCouponDTO = new EsMemberCouponDTO();
            esMemberCouponDTO.setMemberId(esOrderDTO.getMemberId());
            esMemberCouponDTO.setCouponId(esCouponVO.getId());
            esMemberCouponDTO.setCreateTime(esOrderDTO.getCreateTime());
            iEsMemberCouponService.deleteMemberCouponByCouponIdAndMemId(esMemberCouponDTO);

            return DubboResult.success();
        }  catch (ArgumentException ae){
            logger.error("优惠券更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),"优惠券信息不存在");
        } catch (Throwable th) {
            logger.error("优惠券更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateCouponByTask(EsCouponDTO esCouponDTO) {
        try {
            // 更新 优惠券状态
            Long id = esCouponDTO.getId();
            EsCoupon esCoupon = this.couponMapper.selectById(id);
            esCoupon.setIsDel(2);
            esCoupon.setIsReceive(2);
            this.couponMapper.updateById(esCoupon);

            // 更新 会员优惠券表信息
            EsMemberCouponDTO esMemberCouponDTO = new EsMemberCouponDTO();
            esMemberCouponDTO.setCouponId(id);
            esMemberCouponDTO.setShopId(esCoupon.getShopId());
            iEsMemberCouponService.updateStateByShopIdAndCouponId(esMemberCouponDTO);
            return DubboResult.success();
        }  catch (ArgumentException ae){
            logger.error("优惠券更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),"优惠券信息不存在");
        } catch (Throwable th) {
            logger.error("优惠券更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    public DubboPageResult<EsCouponDO> getEsCouponListAtHome(Integer pageNo, Integer pageSize) {
           try{
            QueryWrapper<EsCoupon> queryWrapper = new QueryWrapper<>();
            // 优惠券领取的条件,1，正常，2 根据主键进行降序，
            queryWrapper.lambda().eq(EsCoupon::getIsDel,1)
                    .eq(EsCoupon::getIsReceive,1)
                    .eq(EsCoupon::getIsRegister,2);
               long nowTime = System.currentTimeMillis();
               queryWrapper.lambda().lt(EsCoupon::getStartTime,nowTime);
               queryWrapper.lambda().gt(EsCoupon::getEndTime,nowTime);
               queryWrapper.last("AND limit_num > received_num");
            Page<EsCoupon> page = new Page<>(pageNo, pageSize);
            IPage<EsCoupon> iPage = this.page(page, queryWrapper);
            List<EsCouponDO> couponDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                couponDOList = iPage.getRecords().stream().map(coupon -> {
                    EsCouponDO couponDO = new EsCouponDO();
                    BeanUtil.copyProperties(coupon, couponDO);
                    return couponDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(iPage.getTotal(),couponDOList);
        } catch (ArgumentException ae){
            logger.error("优惠券分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("优惠券分页查询失败", th);
            return DubboPageResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    @Override
    public DubboPageResult<EsCouponDO> getEsCouponListByShopId(Long shopId) {
        try{
            QueryWrapper<EsCoupon> queryWrapper = new QueryWrapper<>();
            Long time = new Date().getTime();
            // 优惠券领取的条件,1，正常，2 根据主键进行降序，
            queryWrapper.lambda().eq(EsCoupon::getIsDel,1)
                    .le(EsCoupon::getStartTime,time).gt(EsCoupon::getEndTime,time)
                    .eq(EsCoupon::getIsReceive,1)
                    .eq(EsCoupon::getShopId,shopId).eq(EsCoupon::getIsCoupons,2)
                    .orderByDesc(EsCoupon::getId);
            List<EsCoupon> esCouponList = this.couponMapper.selectList(queryWrapper);
            List<EsCouponDO> couponDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(esCouponList)) {
                couponDOList = esCouponList.stream().map(coupon -> {
                    EsCouponDO couponDO = new EsCouponDO();
                    BeanUtil.copyProperties(coupon, couponDO);
                    return couponDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(couponDOList);
        } catch (Throwable th) {
            logger.error("根据shopID查询失败", th);
            return DubboPageResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    @Override
    public DubboPageResult<EsCouponDO> getEsCouponListByIds(List<Long> ids) {
        if (ids == null) {
            throw new ArgumentException(TradeErrorCode.PARAM_ERROR.getErrorCode(), TradeErrorCode.PARAM_ERROR.getErrorMsg());
        }
        try{
            QueryWrapper<EsCoupon> queryWrapper = new QueryWrapper<>();
            // 优惠券领取的条件,1，正常，2 根据主键进行降序，
            queryWrapper.lambda().in(EsCoupon::getId, ids)
                    .orderByDesc(EsCoupon::getId);
            List<EsCoupon> esCouponList = this.couponMapper.selectList(queryWrapper);
            List<EsCouponDO> couponDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(esCouponList)) {
                couponDOList = esCouponList.stream().map(coupon -> {
                    EsCouponDO couponDO = new EsCouponDO();
                    BeanUtil.copyProperties(coupon, couponDO);
                    return couponDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(couponDOList);
        } catch (Throwable th) {
            logger.error("根据shopID查询失败", th);
            return DubboPageResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    @Override
    public DubboPageResult<EsCouponDO> getWaitGetCouponList(Long userId) {
        try{
            QueryWrapper<EsCoupon> queryWrapper = new QueryWrapper<>();
            long now = System.currentTimeMillis();
            queryWrapper.lambda().eq(EsCoupon::getIsDel,1)
                    .eq(EsCoupon::getIsReceive,1)
                    .eq(EsCoupon::getIsCoupons,2)
                    .lt(EsCoupon::getStartTime,now)
                    .gt(EsCoupon::getEndTime,now)
                    .orderByDesc(EsCoupon::getId);
            List<EsCoupon> esCouponList = this.couponMapper.selectList(queryWrapper);
            List<EsCouponDO> couponDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(esCouponList)) {
                couponDOList = esCouponList.stream().map(coupon -> {
                    EsCouponDO couponDO = new EsCouponDO();
                    BeanUtil.copyProperties(coupon, couponDO);
                    return couponDO;
                }).collect(Collectors.toList());
            }
            //过滤掉已经领取且达到领取上线的优惠券
            if(!couponDOList.isEmpty()){
                couponDOList = couponDOList.stream().filter(coupon ->{
                    DubboResult<Integer> result = iEsMemberCouponService.getCouponCount(userId, coupon.getId());
                    Integer count = result.getData();
                    return count < coupon.getLimitNum();
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(couponDOList);
        } catch (ArgumentException ae){
            logger.error("查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("查询失败", th);
            return DubboPageResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }
}
