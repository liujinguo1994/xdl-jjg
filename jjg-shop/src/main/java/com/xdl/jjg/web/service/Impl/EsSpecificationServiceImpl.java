package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xdl.jjg.constant.GoodsErrorCode;
import com.xdl.jjg.entity.EsSpecification;
import com.xdl.jjg.mapper.EsSpecificationMapper;
import com.xdl.jjg.model.domain.EsSpecificationDO;
import com.xdl.jjg.model.dto.EsSpecificationDTO;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.util.CollectionUtils;
import com.xdl.jjg.web.service.IEsSpecificationService;
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
 *  服务实现类
 * </p>
 *
 * @author wangaf 826988665@qq.com
 * @since 2019-06-03
 */
@Service
public class EsSpecificationServiceImpl extends ServiceImpl<EsSpecificationMapper, EsSpecification> implements IEsSpecificationService {

    private static Logger logger = LoggerFactory.getLogger(EsSpecificationServiceImpl.class);

    @Autowired
    private EsSpecificationMapper specificationMapper;

    /**
     * 插入数据
     *
     * @param specificationDTO DTO
     * @auther: wangaf 826988665@qq.com
     * @date: 2019-06-03
     * @return: com.shopx.common.model.result.DubboResult<EsSpecificationDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult<EsSpecificationDO> insertSpecification(EsSpecificationDTO specificationDTO) {
        try {
            EsSpecification specification = new EsSpecification();
            BeanUtil.copyProperties(specificationDTO, specification);
            this.specificationMapper.insert(specification);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("新增规格失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable ae) {
            logger.error("新增规格失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), GoodsErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新数据
     *
     * @param specificationDTO DTO
     * @auther: wangaf 826988665@qq.com
     * @date: 2019-06-03
     * @return: com.shopx.common.model.result.DubboResult<EsSpecificationDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult<EsSpecificationDO> updateSpecification(EsSpecificationDTO specificationDTO,Long id) {
        try {
            EsSpecification specification = new EsSpecification();
            BeanUtil.copyProperties(specificationDTO, specification);
            QueryWrapper<EsSpecification> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsSpecification::getId,id);
            this.specificationMapper.update(specification, queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("修改规格失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("修改规格失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), GoodsErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据id获取详情
     *
     * @param id 主键id
     * @auther: wangaf 826988665@qq.com
     * @date: 2019-06-03
     * @return: com.shopx.common.model.result.DubboResult<EsSpecificationDO>
     */
    @Override
    public DubboResult<EsSpecificationDO> getSpecification(Long id) {
        try {
            QueryWrapper<EsSpecification> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsSpecification::getId, id);
            EsSpecification specification = this.specificationMapper.selectOne(queryWrapper);
            EsSpecificationDO specificationDO = new EsSpecificationDO();
            if (specification == null) {
                throw new ArgumentException(GoodsErrorCode.DATA_NOT_EXIST.getErrorCode(), GoodsErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(specification, specificationDO);
            return DubboResult.success(specificationDO);
        } catch (ArgumentException ae){
            logger.error("获取规格失败", ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("获取规格失败", th);
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询列表
     *
     * @param specificationDTO DTO
     * @param pageSize     页数
     * @param pageNum      页码
     * @auther: wangaf 826988665@qq.com
     * @date: 2019-06-03
     * @return: com.shopx.common.model.result.DubboPageResult<EsSpecificationDO>
     */
    @Override
    public DubboPageResult<EsSpecificationDO> getSpecificationList(EsSpecificationDTO specificationDTO, int pageSize, int pageNum) {
        QueryWrapper<EsSpecification> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件

            Page<EsSpecification> page = new Page<>(pageNum, pageSize);
            IPage<EsSpecification> iPage = this.page(page, queryWrapper);
            List<EsSpecificationDO> specificationDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                specificationDOList = iPage.getRecords().stream().map(specification -> {
                    EsSpecificationDO specificationDO = new EsSpecificationDO();
                    BeanUtil.copyProperties(specification, specificationDO);
                    return specificationDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(iPage.getTotal(),specificationDOList);
        } catch (ArgumentException ae){
            logger.error("分页查询规格失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("分页查规格询失败", th);
            return DubboPageResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据主键删除数据
     *
     * @param ids 主键id
     * @auther: wangaf 826988665@qq.com
     * @date: 2019-06-03
     * @return: com.shopx.common.model.result.DubboResult<EsSpecificationDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult<EsSpecificationDO> deleteSpecification(Long[] ids) {
        try {
            QueryWrapper<EsSpecification> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().in(EsSpecification::getId,ids);
            this.remove(queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae){
             logger.error("删除规格失败", ae);
             TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
             return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("删除规格失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), GoodsErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
}
