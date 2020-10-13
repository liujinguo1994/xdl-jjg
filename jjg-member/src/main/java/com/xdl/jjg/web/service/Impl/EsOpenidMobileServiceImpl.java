package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shopx.common.exception.ArgumentException;
import com.shopx.common.model.result.DubboPageResult;
import com.shopx.common.model.result.DubboResult;
import com.shopx.common.util.BeanUtil;
import com.shopx.member.api.model.domain.EsOpenidMobileDO;
import com.shopx.member.api.model.domain.dto.EsOpenidMobileDTO;
import com.shopx.member.api.service.IEsOpenidMobileService;
import com.xdl.jjg.entity.EsOpenidMobile;
import  com.xdl.jjg.mapper.EsOpenidMobileMapper;
import com.shopx.system.api.constant.ErrorCode;
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
 * 微信关联手机号 服务实现类
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-05-09
 */
@Service
public class EsOpenidMobileServiceImpl extends ServiceImpl<EsOpenidMobileMapper, EsOpenidMobile> implements IEsOpenidMobileService {

    private static Logger logger = LoggerFactory.getLogger(EsOpenidMobileServiceImpl.class);

    @Autowired
    private EsOpenidMobileMapper openidMobileMapper;

    /**
     * 插入微信关联手机号数据
     *
     * @param openidMobileDTO 微信关联手机号DTO
     * @auther: rm 2817512105@qq.com
     * @date: 2020-05-09
     * @return: com.shopx.common.model.result.DubboResult<EsOpenidMobileDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertOpenidMobile(EsOpenidMobileDTO openidMobileDTO) {
        try {
            if (openidMobileDTO == null) {
                throw new ArgumentException(ErrorCode.PARAM_ERROR.getErrorCode(), ErrorCode.PARAM_ERROR.getErrorMsg());
            }
            //先判断该手机号是否已经关联此微信
            QueryWrapper<EsOpenidMobile> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsOpenidMobile::getOpenid,openidMobileDTO.getOpenid()).eq(EsOpenidMobile::getMobile,openidMobileDTO.getMobile());
            EsOpenidMobile esOpenidMobile = this.openidMobileMapper.selectOne(queryWrapper);
            if (esOpenidMobile == null){
                EsOpenidMobile openidMobile = new EsOpenidMobile();
                BeanUtil.copyProperties(openidMobileDTO, openidMobile);
                this.openidMobileMapper.insert(openidMobile);
            }
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("微信关联手机号新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable ae) {
            logger.error("微信关联手机号新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新微信关联手机号数据
     *
     * @param openidMobileDTO 微信关联手机号DTO
     * @auther: rm 2817512105@qq.com
     * @date: 2020-05-09
     * @return: com.shopx.common.model.result.DubboResult<EsOpenidMobileDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateOpenidMobile(EsOpenidMobileDTO openidMobileDTO) {
        try {
            if (openidMobileDTO == null) {
                throw new ArgumentException(ErrorCode.PARAM_ERROR.getErrorCode(), ErrorCode.PARAM_ERROR.getErrorMsg());
            }
            EsOpenidMobile openidMobile = new EsOpenidMobile();
            BeanUtil.copyProperties(openidMobileDTO, openidMobile);
            QueryWrapper<EsOpenidMobile> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsOpenidMobile::getId, openidMobileDTO.getId());
            this.openidMobileMapper.update(openidMobile, queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("微信关联手机号更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("微信关联手机号更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据id获取微信关联手机号详情
     *
     * @param id 主键id
     * @auther: rm 2817512105@qq.com
     * @date: 2020-05-09
     * @return: com.shopx.common.model.result.DubboResult<EsOpenidMobileDO>
     */
    @Override
    public DubboResult<EsOpenidMobileDO> getOpenidMobile(Long id) {
        try {
            QueryWrapper<EsOpenidMobile> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsOpenidMobile::getId, id);
            EsOpenidMobile openidMobile = this.openidMobileMapper.selectOne(queryWrapper);
            EsOpenidMobileDO openidMobileDO = new EsOpenidMobileDO();
            if (openidMobile == null) {
                throw new ArgumentException(ErrorCode.DATA_NOT_EXIST.getErrorCode(), ErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(openidMobile, openidMobileDO);
            return DubboResult.success(openidMobileDO);
        } catch (ArgumentException ae){
            logger.error("微信关联手机号查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("微信关联手机号查询失败", th);
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询微信关联手机号列表
     *
     * @param openidMobileDTO 微信关联手机号DTO
     * @param pageSize     页码
     * @param pageNum      页数
     * @auther: rm 2817512105@qq.com
     * @date: 2020-05-09
     * @return: com.shopx.common.model.result.DubboPageResult<EsOpenidMobileDO>
     */
    @Override
    public DubboPageResult<EsOpenidMobileDO> getOpenidMobileList(EsOpenidMobileDTO openidMobileDTO, int pageSize, int pageNum) {
        QueryWrapper<EsOpenidMobile> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件

            Page<EsOpenidMobile> page = new Page<>(pageNum, pageSize);
            IPage<EsOpenidMobile> iPage = this.page(page, queryWrapper);
            List<EsOpenidMobileDO> openidMobileDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                openidMobileDOList = iPage.getRecords().stream().map(openidMobile -> {
                    EsOpenidMobileDO openidMobileDO = new EsOpenidMobileDO();
                    BeanUtil.copyProperties(openidMobile, openidMobileDO);
                    return openidMobileDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(openidMobileDOList);
        } catch (ArgumentException ae){
            logger.error("微信关联手机号分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("微信关联手机号分页查询失败", th);
            return DubboPageResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据主键删除微信关联手机号数据
     *
     * @param id 主键id
     * @auther: rm 2817512105@qq.com
     * @date: 2020-05-09
     * @return: com.shopx.common.model.result.DubboResult<EsOpenidMobileDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteOpenidMobile(Long id) {
        try {
            if (id == 0) {
                throw new ArgumentException(ErrorCode.PARAM_ERROR.getErrorCode(), String.format("参数传入错误ID不能为空[%s]", id));
            }
            QueryWrapper<EsOpenidMobile> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.lambda().eq(EsOpenidMobile::getId, id);
            this.openidMobileMapper.delete(deleteWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae){
             logger.error("微信关联手机号删除失败", ae);
             TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
             return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("微信关联手机号删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    public DubboPageResult<EsOpenidMobileDO> getListByOpenid(String openid) {
        QueryWrapper<EsOpenidMobile> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件
            queryWrapper.lambda().eq(EsOpenidMobile::getOpenid,openid);
            List<EsOpenidMobile> openidMobileList = openidMobileMapper.selectList(queryWrapper);
            List<EsOpenidMobileDO> openidMobileDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(openidMobileList)) {
                openidMobileDOList = openidMobileList.stream().map(openidMobile -> {
                    EsOpenidMobileDO openidMobileDO = new EsOpenidMobileDO();
                    BeanUtil.copyProperties(openidMobile, openidMobileDO);
                    return openidMobileDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(openidMobileDOList);
        } catch (ArgumentException ae){
            logger.error("微信关联手机号分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("微信关联手机号分页查询失败", th);
            return DubboPageResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }
}
