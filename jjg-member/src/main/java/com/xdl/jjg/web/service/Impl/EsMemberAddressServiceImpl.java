package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shopx.common.exception.ArgumentException;
import com.shopx.common.model.result.DubboPageResult;
import com.shopx.common.model.result.DubboResult;
import com.shopx.common.util.BeanUtil;
import com.shopx.member.api.constant.MemberConstant;
import com.shopx.member.api.constant.MemberErrorCode;
import com.shopx.member.api.model.domain.EsMemberAddressDO;
import com.shopx.member.api.model.domain.dto.EsMemberAddressDTO;
import com.shopx.member.api.service.IEsMemberAddressService;
import com.xdl.jjg.entity.EsMember;
import com.xdl.jjg.entity.EsMemberAddress;
import  com.xdl.jjg.mapper.EsMemberAddressMapper;
import  com.xdl.jjg.mapper.EsMemberMapper;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.apache.dubbo.config.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 收货地址表 服务实现类
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-04
 */
@Service
public class EsMemberAddressServiceImpl extends ServiceImpl<EsMemberAddressMapper, EsMemberAddress> implements IEsMemberAddressService {

    private static Logger logger = LoggerFactory.getLogger(EsMemberAddressServiceImpl.class);

    @Autowired
    private EsMemberAddressMapper memberAddressMapper;
    @Autowired
    private EsMemberMapper esMemberMapper;

    /**
     * 插入收货地址表数据
     *
     * @param memberAddressDTO 收货地址表DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsMemberAddressDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult<EsMemberAddressDO> insertMemberAddress(EsMemberAddressDTO memberAddressDTO) {
        try {
            if (memberAddressDTO == null) {
                throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), MemberErrorCode.PARAM_ERROR.getErrorMsg());
            }
            if (null != memberAddressDTO.getDefAddress() && memberAddressDTO.getDefAddress() == MemberConstant.IsDefault) {
                QueryWrapper<EsMemberAddress> queryWrapper = new QueryWrapper<>();
                queryWrapper.lambda().eq(EsMemberAddress::getMemberId, memberAddressDTO.getMemberId());
                EsMemberAddress esMemberAddress = new EsMemberAddress();
                esMemberAddress.setDefAddress(MemberConstant.IsDel);
                this.memberAddressMapper.update(esMemberAddress, queryWrapper);
            }
            EsMemberAddress memberAddress = new EsMemberAddress();
            BeanUtil.copyProperties(memberAddressDTO, memberAddress);
            this.memberAddressMapper.insert(memberAddress);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("收货地址表新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable ae) {
            logger.error("收货地址表新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新收货地址表数据
     *
     * @param memberAddressDTO 收货地址表DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsMemberAddressDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult<EsMemberAddressDO> updateMemberAddress(EsMemberAddressDTO memberAddressDTO) {
        try {
            if (memberAddressDTO == null) {
                throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), MemberErrorCode.PARAM_ERROR.getErrorMsg());
            }

            if (null != memberAddressDTO.getDefAddress() && memberAddressDTO.getDefAddress() == MemberConstant.IsDefault) {
                QueryWrapper<EsMemberAddress> queryWrapper = new QueryWrapper<>();
                queryWrapper.lambda().eq(EsMemberAddress::getMemberId, memberAddressDTO.getMemberId());
                EsMemberAddress esMemberAddress = new EsMemberAddress();
                esMemberAddress.setDefAddress(MemberConstant.IsDel);
                this.memberAddressMapper.update(esMemberAddress, queryWrapper);
            }
            EsMemberAddress memberAddress = new EsMemberAddress();
            BeanUtil.copyProperties(memberAddressDTO, memberAddress);
            QueryWrapper<EsMemberAddress> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsMemberAddress::getId, memberAddressDTO.getId());
            this.memberAddressMapper.update(memberAddress, queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("收货地址表更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("收货地址表更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据id获取收货地址表详情
     *
     * @param id 主键id
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsMemberAddressDO>
     */
    @Override
    public DubboResult<EsMemberAddressDO> getMemberAddress(Long id) {
        try {
            QueryWrapper<EsMemberAddress> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsMemberAddress::getId, id);
            EsMemberAddress memberAddress = this.memberAddressMapper.selectOne(queryWrapper);
            EsMemberAddressDO memberAddressDO = new EsMemberAddressDO();
            if (null == memberAddress) {
                throw new ArgumentException(MemberErrorCode.SET_ADDRESS.getErrorCode(), MemberErrorCode.SET_ADDRESS.getErrorMsg());
            }
            BeanUtil.copyProperties(memberAddress, memberAddressDO);
            return DubboResult.success(memberAddressDO);
        } catch (ArgumentException ae) {
            logger.error("收货地址表查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("收货地址表查询失败", th);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询收货地址表列表
     *
     * @param memberAddressDTO 收货地址表DTO
     * @param pageSize         页码
     * @param pageNum          页数
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsMemberAddressDO>
     */
    @Override
    public DubboPageResult<EsMemberAddressDO> getMemberAddressList(EsMemberAddressDTO memberAddressDTO, int pageSize, int pageNum) {
        QueryWrapper<EsMemberAddress> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件
            queryWrapper.lambda().eq(EsMemberAddress::getMemberId, memberAddressDTO.getMemberId());
            Page<EsMemberAddress> page = new Page<>(pageNum, pageSize);
            IPage<EsMemberAddress> iPage = this.page(page, queryWrapper);
            List<EsMemberAddressDO> memberAddressDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                memberAddressDOList = iPage.getRecords().stream().map(memberAddress -> {
                    EsMemberAddressDO memberAddressDO = new EsMemberAddressDO();
                    BeanUtil.copyProperties(memberAddress, memberAddressDO);
                    return memberAddressDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(iPage.getTotal(), memberAddressDOList);
        } catch (ArgumentException ae) {
            logger.error("收货地址表分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("收货地址表分页查询失败", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询收货地址表列表
     *
     * @param memberAddressDTO 收货地址表DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsMemberAddressDO>
     */
    @Override
    public DubboPageResult<EsMemberAddressDO> getMemberAddressLists(EsMemberAddressDTO memberAddressDTO) {
        QueryWrapper<EsMemberAddress> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件
            queryWrapper.lambda().eq(EsMemberAddress::getMemberId, memberAddressDTO.getMemberId());
            List<EsMemberAddress> esMemberAddressList = this.memberAddressMapper.selectList(queryWrapper);
            List<EsMemberAddressDO> memberAddressDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(esMemberAddressList)) {
                memberAddressDOList = esMemberAddressList.stream().map(memberAddress -> {
                    EsMemberAddressDO memberAddressDO = new EsMemberAddressDO();
                    BeanUtil.copyProperties(memberAddress, memberAddressDO);
                    return memberAddressDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(memberAddressDOList);
        } catch (ArgumentException ae) {
            logger.error("收货地址表查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("收货地址表查询失败", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据会员id查询列表
     *
     * @param memberId 收货地址表DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsMemberAddressDO>
     */
    @Override
    public DubboPageResult<EsMemberAddressDO> getMemberAddressListByMemberId(Long memberId) {
        try {
            // 查询条件
            List<EsMemberAddressDO> memberAddressDOList = memberAddressMapper.getMemberAddressListByMemberId(memberId);
            return DubboPageResult.success(memberAddressDOList);
        } catch (ArgumentException ae) {
            logger.error("收货地址查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("收货地址表查询失败", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }


    /**
     * 根据主键删除收货地址表数据
     *
     * @param id 主键id
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsMemberAddressDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteMemberAddress(Long id) {
        try {
            if (id == 0) {
                throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), String.format("参数传入错误ID不能为空[%s]", id));
            }
            QueryWrapper<EsMemberAddress> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.lambda().eq(EsMemberAddress::getId, id);
            this.memberAddressMapper.delete(deleteWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("收货地址表删除失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("收货地址表删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 查询默认收货地址
     *
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsMemberAddressDO>
     */
    @Override
    public DubboResult<EsMemberAddressDO> getDefaultMemberAddress(Long memberId) {
        try {
            QueryWrapper<EsMember> queryMemberWrapper = new QueryWrapper<>();
            queryMemberWrapper.lambda().eq(EsMember::getId, memberId).eq(EsMember::getState, MemberConstant.IsCommon);
            EsMember esMember = this.esMemberMapper.selectOne(queryMemberWrapper);
            if (null == esMember) {
                throw new ArgumentException(MemberErrorCode.ITEM_NOT_FOUND.getErrorCode(), MemberErrorCode.ITEM_NOT_FOUND.getErrorMsg());
            }
            QueryWrapper<EsMemberAddress> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsMemberAddress::getDefAddress, MemberConstant.IsDefault).eq(EsMemberAddress::getMemberId, memberId);
            EsMemberAddress memberAddress = this.memberAddressMapper.selectOne(queryWrapper);
            EsMemberAddressDO memberAddressDO = new EsMemberAddressDO();
            if (null == memberAddress) {
                return DubboResult.success(null);
            }
            BeanUtil.copyProperties(memberAddress, memberAddressDO);
            return DubboResult.success(memberAddressDO);
        } catch (ArgumentException ae) {
            logger.error("默认收货地址表查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("默认收货地址表查询失败", th);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    @Override
    public DubboResult<EsMemberAddressDO> setDefaultMemberAddress(Long id, Long memberId) {
        try {
            QueryWrapper<EsMemberAddress> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsMemberAddress::getDefAddress, 0).eq(EsMemberAddress::getMemberId, memberId);
            EsMemberAddress memberAddress = memberAddressMapper.selectOne(queryWrapper);
            if (null != memberAddress && memberAddress.getId().equals(id)) {
                return DubboResult.success();
            }

            EsMemberAddress memberAddressNew = new EsMemberAddress();
            memberAddressNew.setId(id);
            memberAddressNew.setDefAddress(0);
            memberAddressMapper.updateById(memberAddressNew);
            if(null != memberAddress){
                memberAddress.setDefAddress(1);
                memberAddressMapper.updateById(memberAddress);
            }
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("设置默认收货地址失败", ae);
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("设置默认收货地址失败", th);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }


    /**
     * 查询默认收货地址,没有设置默认,读取第一个
     *
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsMemberAddressDO>
     */
    @Override
    public DubboResult<EsMemberAddressDO> getWapDefaultMemberAddress(Long memberId) {
        try {
            QueryWrapper<EsMemberAddress> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsMemberAddress::getDefAddress, MemberConstant.IsDefault).eq(EsMemberAddress::getMemberId, memberId);
            EsMemberAddress memberAddress = this.memberAddressMapper.selectOne(queryWrapper);

            QueryWrapper<EsMemberAddress> query=new QueryWrapper<>();
            query.lambda().eq(EsMemberAddress::getMemberId,memberId);
            List<EsMemberAddress> addList = this.memberAddressMapper.selectList(query);

            EsMemberAddressDO memberAddressDO = new EsMemberAddressDO();
            if (null == memberAddress && addList.size()==0) {
                return DubboResult.success(null);
            }
            if (null== memberAddress && addList.size()>0){
                BeanUtil.copyProperties(addList.get(0), memberAddressDO);
                return DubboResult.success(memberAddressDO);
            }
            BeanUtil.copyProperties(memberAddress, memberAddressDO);
            return DubboResult.success(memberAddressDO);
        } catch (ArgumentException ae) {
            logger.error("默认收货地址表查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("默认收货地址表查询失败", th);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

}
