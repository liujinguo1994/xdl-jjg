package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xdl.jjg.constant.GoodsErrorCode;
import com.xdl.jjg.entity.EsAdminTags;
import com.xdl.jjg.mapper.EsAdminTagGoodsMapper;
import com.xdl.jjg.mapper.EsAdminTagsMapper;
import com.xdl.jjg.mapper.EsGoodsMapper;
import com.xdl.jjg.model.domain.EsAdminTagsDO;
import com.xdl.jjg.model.dto.EsAdminTagsDTO;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.response.service.ErrorCode;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.util.CollectionUtils;
import com.xdl.jjg.web.service.IEsAdminTagsService;
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
 * @author WAF 826988665@qq.com
 * @since 2019-07-27 14:57:56
 */
@Service
public class EsAdminTagsServiceImpl extends ServiceImpl<EsAdminTagsMapper, EsAdminTags> implements IEsAdminTagsService {

    private static Logger logger = LoggerFactory.getLogger(EsAdminTagsServiceImpl.class);

    @Autowired
    private EsAdminTagsMapper adminTagsMapper;
    @Autowired
    private EsGoodsMapper esGoodsMapper;
    @Autowired
    private EsAdminTagGoodsMapper adminTagGoodsMapper;

    /**
     * 插入数据
     *
     * @param adminTagsDTO DTO
     * @auther: WAF 826988665@qq.com
     * @date: 2019-07-27 14:57:56
     * @return: com.shopx.common.model.result.DubboResult<EsAdminTagsDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertAdminTags(EsAdminTagsDTO adminTagsDTO) {
        try {
            EsAdminTags adminTags = new EsAdminTags();
            BeanUtil.copyProperties(adminTagsDTO, adminTags);
            this.adminTagsMapper.insert(adminTags);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable ae) {
            logger.error("新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), GoodsErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新数据
     *
     * @param adminTagsDTO DTO
     * @param id                          主键id
     * @auther: WAF 826988665@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsAdminTagsDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateAdminTags(EsAdminTagsDTO adminTagsDTO, Long id) {
        try {
            EsAdminTags adminTags = this.adminTagsMapper.selectById(id);
            if (adminTags == null) {
                throw new ArgumentException(GoodsErrorCode.DATA_NOT_EXIST.getErrorCode(), GoodsErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(adminTagsDTO, adminTags);
            QueryWrapper<EsAdminTags> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsAdminTags::getId, id);
            this.adminTagsMapper.update(adminTags, queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), GoodsErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据id获取详情
     *
     * @param id 主键id
     * @auther: WAF 826988665@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsAdminTagsDO>
     */
    @Override
    public DubboResult<EsAdminTagsDO> getAdminTags(Long id) {
        try {
            EsAdminTags adminTags = this.adminTagsMapper.selectById(id);
            if (adminTags == null) {
                throw new ArgumentException(GoodsErrorCode.DATA_NOT_EXIST.getErrorCode(), GoodsErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            EsAdminTagsDO adminTagsDO = new EsAdminTagsDO();
            BeanUtil.copyProperties(adminTags, adminTagsDO);
            return DubboResult.success(adminTagsDO);
        } catch (ArgumentException ae){
            logger.error("查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("查询失败", th);
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询列表
     *
     * @auther: WAF 826988665@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsAdminTagsDO>
     */
    @Override
    public DubboPageResult<EsAdminTagsDO> getAdminTagsList(EsAdminTagsDTO esAdminTagsDTO,int pageSize,int pageNum) {
        try {
            // 查询条件
            List<EsAdminTags> esAdminTagsList = this.list();
            List<EsAdminTagsDO> adminTagsDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(esAdminTagsList)) {
                adminTagsDOList = esAdminTagsList.stream().map(adminTags -> {
                    EsAdminTagsDO adminTagsDO = new EsAdminTagsDO();
                    BeanUtil.copyProperties(adminTags, adminTagsDO);
                    return adminTagsDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(adminTagsDOList);
        } catch (ArgumentException ae){
            logger.error("查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("查询失败", th);
            return DubboPageResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }
    public DubboPageResult<EsAdminTagsDO> getAdminTagsList() {
        try {
            // 查询条件
            List<EsAdminTags> esAdminTagsList = this.list();
            List<EsAdminTagsDO> adminTagsDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(esAdminTagsList)) {
                adminTagsDOList = esAdminTagsList.stream().map(adminTags -> {
                    EsAdminTagsDO adminTagsDO = new EsAdminTagsDO();
                    BeanUtil.copyProperties(adminTags, adminTagsDO);
                    return adminTagsDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(adminTagsDOList);
        } catch (ArgumentException ae){
            logger.error("查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("查询失败", th);
            return DubboPageResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }
    /**
     * 根据主键删除数据
     *
     * @param id 主键id
     * @auther: WAF 826988665@qq.com
     * @date: 2019/05/31 16:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsAdminTagsDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteAdminTags(Long id) {
        try {
            this.adminTagsMapper.deleteById(id);
            return DubboResult.success();
        } catch (ArgumentException ae){
             logger.error("删除失败", ae);
             TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
             return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), GoodsErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
}
