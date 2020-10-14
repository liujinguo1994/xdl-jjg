package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jjg.member.model.domain.EsCompanyDeliveryDO;
import com.jjg.member.model.dto.EsCompanyDeliveryDTO;
import com.xdl.jjg.constant.MemberErrorCode;
import com.xdl.jjg.entity.EsCompanyDelivery;
import com.xdl.jjg.mapper.EsCompanyDeliveryMapper;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.util.CollectionUtils;
import com.xdl.jjg.web.service.IEsCompanyDeliveryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 签约公司门店自提表 服务实现类
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-04
 */
@Service
public class EsCompanyDeliveryServiceImpl extends ServiceImpl<EsCompanyDeliveryMapper, EsCompanyDelivery> implements IEsCompanyDeliveryService {

    private static Logger logger = LoggerFactory.getLogger(EsCompanyDeliveryServiceImpl.class);

    @Autowired
    private EsCompanyDeliveryMapper companyDeliveryMapper;

    /**
     * 插入签约公司门店自提表数据
     *
     * @param companyDeliveryDTO 签约公司门店自提表DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsCompanyDeliveryDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertCompanyDelivery(EsCompanyDeliveryDTO companyDeliveryDTO) {
        try {
            if (companyDeliveryDTO == null) {
                throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), MemberErrorCode.PARAM_ERROR.getErrorMsg());
            }

            EsCompanyDelivery companyDelivery = new EsCompanyDelivery();
            BeanUtil.copyProperties(companyDeliveryDTO, companyDelivery);
            this.companyDeliveryMapper.insert(companyDelivery);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("签约公司门店自提表新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable ae) {
            logger.error("签约公司门店自提表新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新签约公司门店自提表数据
     *
     * @param companyDeliveryDTO 签约公司门店自提表DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsCompanyDeliveryDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateCompanyDelivery(EsCompanyDeliveryDTO companyDeliveryDTO) {
        try {
            if (companyDeliveryDTO == null) {
                throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), MemberErrorCode.PARAM_ERROR.getErrorMsg());
            }
            EsCompanyDelivery companyDelivery = new EsCompanyDelivery();
            BeanUtil.copyProperties(companyDeliveryDTO, companyDelivery);
            QueryWrapper<EsCompanyDelivery> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsCompanyDelivery::getId, companyDeliveryDTO.getId());
            this.companyDeliveryMapper.update(companyDelivery, queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("签约公司门店自提表更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("签约公司门店自提表更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据id获取签约公司门店自提表详情
     *
     * @param id 主键id
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsCompanyDeliveryDO>
     */
    @Override
    public DubboResult<EsCompanyDeliveryDO> getCompanyDelivery(Long id) {
        try {
            QueryWrapper<EsCompanyDelivery> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsCompanyDelivery::getId, id);
            EsCompanyDelivery companyDelivery = this.companyDeliveryMapper.selectOne(queryWrapper);
            EsCompanyDeliveryDO companyDeliveryDO = new EsCompanyDeliveryDO();
            if (companyDelivery == null) {
                throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(companyDelivery, companyDeliveryDO);
            return DubboResult.success(companyDeliveryDO);
        } catch (ArgumentException ae){
            logger.error("签约公司门店自提表查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("签约公司门店自提表查询失败", th);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询签约公司门店自提表列表
     *
     * @param companyDeliveryDTO 签约公司门店自提表DTO
     * @param pageSize     页码
     * @param pageNum      页数
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsCompanyDeliveryDO>
     */
    @Override
    public DubboPageResult<EsCompanyDeliveryDO> getCompanyDeliveryList(EsCompanyDeliveryDTO companyDeliveryDTO, int pageSize, int pageNum) {
        QueryWrapper<EsCompanyDelivery> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件

            Page<EsCompanyDelivery> page = new Page<>(pageNum, pageSize);
            IPage<EsCompanyDelivery> iPage = this.page(page, queryWrapper);
            List<EsCompanyDeliveryDO> companyDeliveryDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                companyDeliveryDOList = iPage.getRecords().stream().map(companyDelivery -> {
                    EsCompanyDeliveryDO companyDeliveryDO = new EsCompanyDeliveryDO();
                    BeanUtil.copyProperties(companyDelivery, companyDeliveryDO);
                    return companyDeliveryDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(companyDeliveryDOList);
        } catch (ArgumentException ae){
            logger.error("签约公司门店自提表分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("签约公司门店自提表分页查询失败", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据主键删除签约公司门店自提表数据
     *
     * @param id 主键id
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsCompanyDeliveryDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteCompanyDelivery(Long id) {
        try {
            if (id == 0) {
                throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), String.format("参数传入错误ID不能为空[%s]", id));
            }
            QueryWrapper<EsCompanyDelivery> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.lambda().eq(EsCompanyDelivery::getId, id);
            this.companyDeliveryMapper.delete(deleteWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae){
             logger.error("签约公司门店自提表删除失败", ae);
             TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
             return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("签约公司门店自提表删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
}
