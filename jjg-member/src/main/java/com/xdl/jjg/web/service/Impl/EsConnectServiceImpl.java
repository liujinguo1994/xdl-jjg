package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xdl.jjg.constant.MemberErrorCode;
import com.xdl.jjg.entity.EsConnect;
import com.xdl.jjg.mapper.EsConnectMapper;
import com.xdl.jjg.model.domain.EsConnectDO;
import com.xdl.jjg.model.domain.EsMemberDO;
import com.xdl.jjg.model.dto.EsConnectDTO;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.web.service.IEsConnectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 信任登录 服务实现类
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2019-07-18
 */
@Service
public class EsConnectServiceImpl extends ServiceImpl<EsConnectMapper, EsConnect> implements IEsConnectService {

    private static Logger logger = LoggerFactory.getLogger(EsConnectServiceImpl.class);

    @Autowired
    private EsConnectMapper connectMapper;


    /**
     * 插入信任登录数据
     *
     * @param connectDTO 信任登录DTO
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/18 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsConnectDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertConnect(EsConnectDTO connectDTO) {
        try {
            if (connectDTO == null) {
                throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), MemberErrorCode.PARAM_ERROR.getErrorMsg());
            }

            EsConnect connect = new EsConnect();
            BeanUtil.copyProperties(connectDTO, connect);
            this.connectMapper.insert(connect);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("信任登录新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable ae) {
            logger.error("信任登录新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新信任登录数据
     *
     * @param connectDTO 信任登录DTO
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/18 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsConnectDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateConnect(EsConnectDTO connectDTO) {
        try {
            if (connectDTO == null) {
                throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), MemberErrorCode.PARAM_ERROR.getErrorMsg());
            }
            EsConnect connect = new EsConnect();
            BeanUtil.copyProperties(connectDTO, connect);
            QueryWrapper<EsConnect> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsConnect::getId, connectDTO.getId());
            this.connectMapper.update(connect, queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("信任登录更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("信任登录更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据id获取信任登录详情
     *
     * @param id 主键id
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/18 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsConnectDO>
     */
    @Override
    public DubboResult<EsConnectDO> getConnect(Long id) {
        try {
            QueryWrapper<EsConnect> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsConnect::getId, id);
            EsConnect connect = this.connectMapper.selectOne(queryWrapper);
            EsConnectDO connectDO = new EsConnectDO();
            if (connect == null) {
                throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(connect, connectDO);
            return DubboResult.success(connectDO);
        } catch (ArgumentException ae) {
            logger.error("信任登录查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("信任登录查询失败", th);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询信任登录列表
     *
     * @param connectDTO 信任登录DTO
     * @param pageSize   页码
     * @param pageNum    页数
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/18 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsConnectDO>
     */
    @Override
    public DubboPageResult<EsConnectDO> getConnectList(EsConnectDTO connectDTO, int pageSize, int pageNum) {
        QueryWrapper<EsConnect> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件

            Page<EsConnect> page = new Page<>(pageNum, pageSize);
            IPage<EsConnect> iPage = this.page(page, queryWrapper);
            List<EsConnectDO> connectDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                connectDOList = iPage.getRecords().stream().map(connect -> {
                    EsConnectDO connectDO = new EsConnectDO();
                    BeanUtil.copyProperties(connect, connectDO);
                    return connectDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(connectDOList);
        } catch (ArgumentException ae) {
            logger.error("信任登录分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("信任登录分页查询失败", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据主键删除信任登录数据
     *
     * @param id 主键id
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/18 16:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsConnectDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteConnect(Long id) {
        try {
            if (id == 0) {
                throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), String.format("参数传入错误ID不能为空[%s]", id));
            }
            QueryWrapper<EsConnect> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.lambda().eq(EsConnect::getId, id);
            this.connectMapper.delete(deleteWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("信任登录删除失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("信任登录删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }


    /**
     * 解除用户绑定
     *
     * @param type 绑定类型
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/17 16:20:44
     * @return: com.shopx.common.model.result.DubboResult<EsConnectDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult unbind(String type,Long memberId) {
        try {
            QueryWrapper<EsConnect> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsConnect::getMemberId, memberId).eq(EsConnect::getUnionType, type);
            EsConnect esConnectBack = this.connectMapper.selectOne(queryWrapper);
            if (null == esConnectBack) {
                throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), "未绑定");
            }
//            QueryWrapper<EsConnect> queryWrapperUnbind = new QueryWrapper<>();
//            queryWrapperUnbind.lambda().eq(EsConnect::getMemberId, memberId).eq(EsConnect::getUnionType, type);
            EsConnectDTO esConnectDTO = new EsConnectDTO();
            esConnectDTO.setMemberId(memberId);
            esConnectDTO.setUnionType(type);
            esConnectDTO.setUnboundTime(System.currentTimeMillis());
            this.connectMapper.unbindWechaAndQQ(esConnectDTO);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("解绑失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("解绑失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }


    }

    @Override
    public DubboResult wechatAuthCallBack() {
        return null;
    }

    @Override
    public Map bindLogin(String uuid) {
        return null;
    }

    @Override
    public DubboResult wechatOut() {
        return null;
    }

    @Override
    public DubboResult initiate(String type, String port, String member) {
        return null;
    }

    @Override
    public DubboResult<EsMemberDO> callBack(String type, String port, String member, String uuid) {
        return null;
    }


}
