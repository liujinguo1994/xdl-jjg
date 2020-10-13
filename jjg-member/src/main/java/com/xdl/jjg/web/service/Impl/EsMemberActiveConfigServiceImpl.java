package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xdl.jjg.constant.MemberErrorCode;
import com.xdl.jjg.entity.EsMemberActiveConfig;
import com.xdl.jjg.entity.EsMemberShop;
import com.xdl.jjg.mapper.EsMemberActiveConfigMapper;
import com.xdl.jjg.mapper.EsMemberShopMapper;
import com.xdl.jjg.model.domain.EsMemberActiveConfigDO;
import com.xdl.jjg.model.domain.EsSellerMemberActiveConfigDO;
import com.xdl.jjg.model.domain.EsSellerMemberInFoDO;
import com.xdl.jjg.model.dto.EsActiveMemberDTO;
import com.xdl.jjg.model.dto.EsMemberActiveConfigDTO;
import com.xdl.jjg.model.dto.EsQueryConditionActiveInfoDTO;
import com.xdl.jjg.model.dto.EsQueryMemberActiveInfoDTO;
import com.xdl.jjg.model.enums.MemberActiveSortEnums;
import com.xdl.jjg.model.enums.MemberTypeEnums;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.web.service.IEsMemberActiveConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 会员活跃配置服务
 * </p>
 *
 * @author LINS 1220316142@qq.com
 * @since 2019-07-02 19:50:02
 */
@Service
public class EsMemberActiveConfigServiceImpl extends ServiceImpl<EsMemberActiveConfigMapper, EsMemberActiveConfig> implements IEsMemberActiveConfigService {

    private static Logger logger = LoggerFactory.getLogger(EsMemberActiveConfigServiceImpl.class);

    @Autowired
    private EsMemberActiveConfigMapper memberActiveConfigMapper;
    @Autowired
    private EsMemberShopMapper esMemberShopMapper;

    /**
     * 插入数据
     *
     * @param esMemberActiveDTOs DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsMemberActiveConfigDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertMemberActiveConfig(List<EsActiveMemberDTO> esMemberActiveDTOs, Long shopId) {
        if (CollectionUtils.isEmpty(esMemberActiveDTOs)) {
            throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), MemberErrorCode.PARAM_ERROR.getErrorMsg());
        }
        try {
            if (null == shopId) {
                return DubboPageResult.fail(MemberErrorCode.AUTHORITY.getErrorCode(), MemberErrorCode.AUTHORITY.getErrorMsg());
            }
            memberActiveConfigMapper.deleteMemberActiveConfig(shopId);
            esMemberActiveDTOs.stream().forEach(esActiveMemberDTO -> {
                EsMemberActiveConfig memberAddConfig = new EsMemberActiveConfig();
                BeanUtil.copyProperties(esActiveMemberDTO, memberAddConfig);
                memberAddConfig.setShopId(shopId);
                this.memberActiveConfigMapper.insert(memberAddConfig);
            });
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable ae) {
            logger.error("新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新数据
     *
     * @param memberActiveConfigDTO DTO
     * @param id                    主键id
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsMemberActiveConfigDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateMemberActiveConfig(EsMemberActiveConfigDTO memberActiveConfigDTO, Long id) {
        try {

            EsMemberActiveConfig memberActiveConfig = this.memberActiveConfigMapper.selectById(id);
            if (memberActiveConfig == null) {
                throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(memberActiveConfigDTO, memberActiveConfig);
            QueryWrapper<EsMemberActiveConfig> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsMemberActiveConfig::getId, id);
            this.memberActiveConfigMapper.update(memberActiveConfig, queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据memberType获取详情
     *
     * @param memberType
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsMemberActiveConfigDO>
     */
    @Override
    public DubboResult<EsMemberActiveConfigDO> getMemberActiveConfig(Integer memberType, Long userId) {
        QueryWrapper<EsMemberShop> queryWrapper = new QueryWrapper<>();
        QueryWrapper<EsMemberActiveConfig> queryWrapperByMemberType = new QueryWrapper<>();
        try {
            queryWrapper.lambda().eq(EsMemberShop::getMemberId, userId);
            EsMemberShop esMemberShop = this.esMemberShopMapper.selectOne(queryWrapper);
            if (null == esMemberShop || null == esMemberShop.getShopId()) {
                throw new ArgumentException(MemberErrorCode.AUTHORITY.getErrorCode(), MemberErrorCode.AUTHORITY.getErrorMsg());
            }
            EsMemberActiveConfigDO memberActiveConfigDO = new EsMemberActiveConfigDO();
            queryWrapperByMemberType.lambda().eq(EsMemberActiveConfig::getMemberType, memberType).eq(EsMemberActiveConfig::getShopId, esMemberShop.getShopId());
            EsMemberActiveConfig esMemberActiveConfig = memberActiveConfigMapper.selectOne(queryWrapperByMemberType);
            if( null == esMemberActiveConfig){
                throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(esMemberActiveConfig, memberActiveConfigDO);
            return DubboResult.success(memberActiveConfigDO);
        } catch (ArgumentException ae) {
            logger.error("查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("查询失败", th);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询列表
     *
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsMemberActiveConfigDO>
     */
    @Override
    public DubboPageResult<EsSellerMemberActiveConfigDO> getMemberActiveConfigList(Long shopId) {
        try {
            List<EsSellerMemberActiveConfigDO> memberActiveConfigDOList = memberActiveConfigMapper.getListInfo(shopId);
            return DubboPageResult.success(memberActiveConfigDOList);
        } catch (ArgumentException ae) {
            logger.error("分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("分页查询失败", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据主键删除数据
     *
     * @param id 主键id
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsMemberActiveConfigDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteMemberActiveConfig(Long id) {
        try {
            this.memberActiveConfigMapper.deleteById(id);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("删除失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 查询卖家会员列表
     *
     * @param esQueryConditionActive
     * @param pageSize
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: package com.shopx.member.dao.domain.DubboResult<EsMemberCollectionShopDO>
     */
    @Override
    public DubboPageResult<EsSellerMemberInFoDO> getMemberInfoByUserId(int pageNum, int pageSize, EsQueryConditionActiveInfoDTO esQueryConditionActive) {
        try {
            if (null == esQueryConditionActive || null == esQueryConditionActive.getShopId()) {
                return DubboPageResult.fail(MemberErrorCode.AUTHORITY.getErrorCode(), MemberErrorCode.AUTHORITY.getErrorMsg());
            }
            QueryWrapper<EsMemberActiveConfig> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsMemberActiveConfig::getShopId, esQueryConditionActive.getShopId());
            List<EsMemberActiveConfig> list = memberActiveConfigMapper.selectList(queryWrapper);
            EsQueryMemberActiveInfoDTO esQueryMemberActiveInfoDTO = new EsQueryMemberActiveInfoDTO();
            if(CollectionUtils.isEmpty(list)){
                esQueryMemberActiveInfoDTO.setShopId(esQueryConditionActive.getShopId());
                Page<EsQueryMemberActiveInfoDTO> page = new Page<>(pageNum, pageSize);
                IPage<EsSellerMemberInFoDO> result = memberActiveConfigMapper.getCommenMemberInfoByUserId(page, esQueryMemberActiveInfoDTO);
                return DubboPageResult.success(result.getTotal(), result.getRecords());
            }
            Date d = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd ");
            list.stream().forEach(esActiveMemberDTO -> {
                if (esActiveMemberDTO.getMemberType() == 0) {
                    esQueryMemberActiveInfoDTO.setAddDays(esActiveMemberDTO.getDays());
                    esQueryMemberActiveInfoDTO.setAddOrders(esActiveMemberDTO.getOrders());
                    esQueryMemberActiveInfoDTO.setAddTime(format.format(DateUtils.addDays(d,-esActiveMemberDTO.getDays())));
                }
                if (esActiveMemberDTO.getMemberType() == 1) {
                    esQueryMemberActiveInfoDTO.setActiveDays(esActiveMemberDTO.getDays());
                    esQueryMemberActiveInfoDTO.setActiveOrders(esActiveMemberDTO.getOrders());
                    esQueryMemberActiveInfoDTO.setActiveTime(format.format(DateUtils.addDays(d,-esActiveMemberDTO.getDays())));
                }
                if (esActiveMemberDTO.getMemberType() == 2) {
                    esQueryMemberActiveInfoDTO.setNoOrders(esActiveMemberDTO.getDays());
                    esQueryMemberActiveInfoDTO.setOutLandDays(esActiveMemberDTO.getVisitDays());
                    esQueryMemberActiveInfoDTO.setNoOrderTime(format.format(DateUtils.addDays(d,-esActiveMemberDTO.getDays())));
                    esQueryMemberActiveInfoDTO.setOutLandTime(format.format(DateUtils.addDays(d,-esActiveMemberDTO.getVisitDays())));
                }
            });
            esQueryMemberActiveInfoDTO.setShopId(esQueryConditionActive.getShopId());
            if(null != esQueryConditionActive.getCoent()){
                esQueryMemberActiveInfoDTO.setCoent(esQueryConditionActive.getCoent());
            }
            if(null != esQueryConditionActive.getMemberType() && esQueryConditionActive.getMemberType() != MemberActiveSortEnums.WHOLE_MEMBER.getKey()){
                esQueryMemberActiveInfoDTO.setMemberType(MemberTypeEnums.getName(esQueryConditionActive.getMemberType()));
            }
            if(null != esQueryConditionActive.getBeginTime() && esQueryConditionActive.getBeginTime() !=0){
                esQueryMemberActiveInfoDTO.setBeginTime(esQueryConditionActive.getBeginTime());
            }
            if(null != esQueryConditionActive.getEndTime() && esQueryConditionActive.getEndTime() !=0){
                esQueryMemberActiveInfoDTO.setEndTime(esQueryConditionActive.getEndTime());
            }
            Page<EsQueryMemberActiveInfoDTO> page = new Page<>(pageNum, pageSize);
            IPage<EsSellerMemberInFoDO> result = memberActiveConfigMapper.getMemberInfoByUserId(page, esQueryMemberActiveInfoDTO);
            return DubboPageResult.success(result.getTotal(), result.getRecords());
        } catch (ArgumentException ae) {
            logger.error("查詢店鋪列表失敗", ae);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        } catch (Throwable th) {
            logger.error("查詢店鋪列表失敗", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }
}
