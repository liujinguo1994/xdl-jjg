package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xdl.jjg.entity.EsBrandShow;
import com.xdl.jjg.mapper.EsBrandShowMapper;
import com.xdl.jjg.model.domain.EsBrandShowDO;
import com.xdl.jjg.model.dto.EsBrandShowDTO;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.web.service.IEsBrandShowService;
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
 * @author WANGAF 826988665@qq.com
 * @since 2019-07-12 13:23:46
 */
@Service
public class EsBrandShowServiceImpl extends ServiceImpl<EsBrandShowMapper, EsBrandShow> implements IEsBrandShowService {

    private static Logger logger = LoggerFactory.getLogger(EsBrandShowServiceImpl.class);

    @Autowired
    private EsBrandShowMapper brandShowMapper;

    /**
     * 插入数据
     *
     * @param brandShowDTO DTO
     * @auther: WAF 826988665@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsBrandShowDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertBrandShow(EsBrandShowDTO brandShowDTO) {
        try {
            EsBrandShow brandShow = new EsBrandShow();
            BeanUtil.copyProperties(brandShowDTO, brandShow);
            this.brandShowMapper.insert(brandShow);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable ae) {
            logger.error("新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新数据
     *
     * @param brandShowDTO DTO
     * @auther: WAF 826988665@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsBrandShowDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateBrandShow(EsBrandShowDTO brandShowDTO, Long id) {
        try {
            EsBrandShow brandShow = new EsBrandShow();
            BeanUtil.copyProperties(brandShowDTO, brandShow);
            QueryWrapper<EsBrandShow> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsBrandShow::getId, id);
            this.brandShowMapper.update(brandShow, queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据id获取详情
     *
     * @param id 主键id
     * @auther: WAF 826988665@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsBrandShowDO>
     */
    @Override
    public DubboResult<EsBrandShowDO> getBrandShow(Long id) {
        try {
            QueryWrapper<EsBrandShow> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsBrandShow::getId, id);
            EsBrandShow brandShow = this.brandShowMapper.selectOne(queryWrapper);
            EsBrandShowDO brandShowDO = new EsBrandShowDO();
            if (brandShow == null) {
                throw new ArgumentException(ErrorCode.DATA_NOT_EXIST.getErrorCode(), ErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(brandShow, brandShowDO);
            return DubboResult.success(brandShowDO);
        } catch (ArgumentException ae){
            logger.error("查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("查询失败", th);
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询列表
     *
     * @param brandShowDTO DTO
     * @param pageSize     页码
     * @param pageNum      页数
     * @auther: WAF 826988665@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsBrandShowDO>
     */
    @Override
    public DubboPageResult<EsBrandShowDO> getBrandShowList(EsBrandShowDTO brandShowDTO, int pageSize, int pageNum) {
        QueryWrapper<EsBrandShow> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件

            Page<EsBrandShow> page = new Page<>(pageNum, pageSize);
            IPage<EsBrandShow> iPage = this.page(page, queryWrapper);
            List<EsBrandShowDO> brandShowDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                brandShowDOList = iPage.getRecords().stream().map(brandShow -> {
                    EsBrandShowDO brandShowDO = new EsBrandShowDO();
                    BeanUtil.copyProperties(brandShow, brandShowDO);
                    return brandShowDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(iPage.getTotal(),brandShowDOList);
        } catch (ArgumentException ae){
            logger.error("分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("分页查询失败", th);
            return DubboPageResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据主键删除数据
     *
     * @param id 主键id
     * @auther: WAF 826988665@qq.com
     * @date: 2019/05/31 16:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsBrandShowDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteBrandShow(Long id) {
        try {
            QueryWrapper<EsBrandShow> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.lambda().eq(EsBrandShow::getId, id);
            this.brandShowMapper.delete(deleteWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae){
             logger.error("删除失败", ae);
             TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
             return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
}
