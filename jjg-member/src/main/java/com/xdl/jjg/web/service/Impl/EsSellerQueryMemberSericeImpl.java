package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jjg.member.model.domain.EsMemberQueryActiveDO;
import com.jjg.member.model.dto.EsQueryMemberTypeDTO;
import com.jjg.member.model.enums.MemberActiveSortEnums;
import com.xdl.jjg.constant.MemberErrorCode;
import com.xdl.jjg.entity.EsMemberActiveConfig;
import com.xdl.jjg.mapper.EsMemberActiveConfigMapper;
import com.xdl.jjg.mapper.EsMemberShopMapper;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.web.service.IEsSellerQueryMemberSerice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 卖家端后台条件查询列表
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-04
 */
@Service
public class EsSellerQueryMemberSericeImpl implements IEsSellerQueryMemberSerice {
    private static Logger logger = LoggerFactory.getLogger(EsSellerQueryMemberSericeImpl.class);

    @Autowired
    private EsMemberShopMapper esMemberShopMapper;
    @Autowired
    private EsMemberActiveConfigMapper esMemberActiveConfigMapper;

    /**
     * 卖家端后台条件查询列表
     *
     * @param esQueryMemberTypeDTO 会员信息DTO
     * @param pageSize             行数
     * @param pageNum              页码
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsSellerQueryMemberDO>
     */
    public DubboResult<EsMemberQueryActiveDO> getMemberInfoBySeller(EsQueryMemberTypeDTO esQueryMemberTypeDTO, int pageSize, int pageNum) {

        Long shopId = esMemberShopMapper.getShopIdByMemberId(esQueryMemberTypeDTO.getMemberId());
        if (null == shopId) {
            throw new ArgumentException(MemberErrorCode.AUTHORITY.getErrorCode(), MemberErrorCode.AUTHORITY.getErrorMsg());
        }
        EsMemberQueryActiveDO esMemberQueryActiveDO = new EsMemberQueryActiveDO();
        try {
            if (null == esQueryMemberTypeDTO.getMemberType()) {
                QueryWrapper<EsMemberActiveConfig> queryWrapper = new QueryWrapper<>();
                queryWrapper.lambda().eq(EsMemberActiveConfig::getShopId, shopId).eq(EsMemberActiveConfig::getMemberType, MemberActiveSortEnums.ADD_MEMBER.getKey());
                EsMemberActiveConfig esMemberActiveConfigAdd = this.esMemberActiveConfigMapper.selectOne(queryWrapper);
                QueryWrapper<EsMemberActiveConfig> queryWrapperActive = new QueryWrapper<>();
                queryWrapperActive.lambda().eq(EsMemberActiveConfig::getShopId, shopId).eq(EsMemberActiveConfig::getMemberType, MemberActiveSortEnums.ACTIVE_MEMBER.getKey());
                EsMemberActiveConfig qActive = this.esMemberActiveConfigMapper.selectOne(queryWrapper);
                QueryWrapper<EsMemberActiveConfig> queryWrapperSleep = new QueryWrapper<>();
                queryWrapperSleep.lambda().eq(EsMemberActiveConfig::getShopId, shopId).eq(EsMemberActiveConfig::getMemberType, MemberActiveSortEnums.SLEEP_MEMBER.getKey());
                EsMemberActiveConfig sleep = this.esMemberActiveConfigMapper.selectOne(queryWrapper);
                esMemberQueryActiveDO.setAddDays(esMemberActiveConfigAdd.getDays());
                esMemberQueryActiveDO.setOrders(qActive.getOrders());
                esMemberQueryActiveDO.setNoVistor(qActive.getOrders());
                esMemberQueryActiveDO.setNoVistor(sleep.getVisitDays());
                esMemberQueryActiveDO.setNoOrders(sleep.getDays());
            }

        } catch (ArgumentException ae) {
            logger.error("查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("查询失败", th);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
        return DubboResult.success(esMemberQueryActiveDO);
    }



}
