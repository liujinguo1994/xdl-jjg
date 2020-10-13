package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xdl.jjg.constant.ErrorCode;
import com.xdl.jjg.entity.EsSiteNavigation;
import com.xdl.jjg.mapper.EsSiteNavigationMapper;
import com.xdl.jjg.model.domain.EsSiteNavigationDO;
import com.xdl.jjg.model.dto.EsSiteNavigationDTO;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.util.CollectionUtils;
import com.xdl.jjg.util.StringUtil;
import com.xdl.jjg.web.service.IEsSiteNavigationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import redis.clients.jedis.JedisCluster;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-06-04
 */
@Service
public class EsSiteNavigationServiceImpl extends ServiceImpl<EsSiteNavigationMapper, EsSiteNavigation> implements IEsSiteNavigationService {

    private static Logger logger = LoggerFactory.getLogger(EsSiteNavigationServiceImpl.class);

    @Autowired
    private EsSiteNavigationMapper siteNavigationMapper;

    @Autowired
    private JedisCluster jedisCluster;

    /**
     * 插入数据
     *
     * @param siteNavigationDTO DTO
     * @auther: rm 2817512105@qq.com
     * @date: 2019-06-04
     * @return: com.shopx.common.model.result.DubboResult<EsSiteNavigationDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertSiteNavigation(EsSiteNavigationDTO siteNavigationDTO) {
        try {
            //移动端图片地址必填
            if ("MOBILE".equals(siteNavigationDTO.getClientType())) {
                if (StringUtil.isEmpty(siteNavigationDTO.getImage())) {
                    throw new ArgumentException(ErrorCode.IMAGE_IS_NULL.getErrorCode(), "移动端导航，图片必传");
                }
            }
            //导航名称长度不能超过6
            if (siteNavigationDTO.getNavigationName().length() > 6) {
                throw new ArgumentException(ErrorCode.NAME_IS_LONG.getErrorCode(), "导航栏菜单名称已经超出最大限制");
            }
            EsSiteNavigation siteNavigation = new EsSiteNavigation();
            BeanUtil.copyProperties(siteNavigationDTO, siteNavigation);
            this.siteNavigationMapper.insert(siteNavigation);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable ae) {
            logger.error("新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新数据
     *
     * @param siteNavigationDTO DTO
     * @auther: rm 2817512105@qq.com
     * @date: 2019-06-04
     * @return: com.shopx.common.model.result.DubboResult<EsSiteNavigationDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateSiteNavigation(EsSiteNavigationDTO siteNavigationDTO) {
        try {
            EsSiteNavigation esSiteNavigation = siteNavigationMapper.selectById(siteNavigationDTO.getId());
            if (esSiteNavigation == null) {
                throw new ArgumentException(ErrorCode.SITE_NAVIGATION_NOT_EXIT.getErrorCode(), "导航栏不存在，请正确操作");
            }
            //移动端图片地址必填
            if ("MOBILE".equals(siteNavigationDTO.getClientType())) {
                if (StringUtil.isEmpty(siteNavigationDTO.getImage())) {
                    throw new ArgumentException(ErrorCode.IMAGE_IS_NULL.getErrorCode(), "移动端导航，图片必传");
                }
            }
            //导航名称长度不能超过6
            if (siteNavigationDTO.getNavigationName().length() > 6) {
                throw new ArgumentException(ErrorCode.NAME_IS_LONG.getErrorCode(), "导航栏菜单名称已经超出最大限制");
            }

            EsSiteNavigation siteNavigation = new EsSiteNavigation();
            BeanUtil.copyProperties(siteNavigationDTO, siteNavigation);
            QueryWrapper<EsSiteNavigation> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsSiteNavigation::getId, siteNavigationDTO.getId());
            this.siteNavigationMapper.update(siteNavigation, queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
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
     * @auther: rm 2817512105@qq.com
     * @date: 2019-06-04
     * @return: com.shopx.common.model.result.DubboResult<EsSiteNavigationDO>
     */
    @Override
    public DubboResult<EsSiteNavigationDO> getSiteNavigation(Long id) {
        try {
            QueryWrapper<EsSiteNavigation> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsSiteNavigation::getId, id);
            EsSiteNavigation siteNavigation = this.siteNavigationMapper.selectOne(queryWrapper);
            EsSiteNavigationDO siteNavigationDO = new EsSiteNavigationDO();
            if (siteNavigation == null) {
                throw new ArgumentException(ErrorCode.DATA_NOT_EXIST.getErrorCode(), ErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(siteNavigation, siteNavigationDO);
            return DubboResult.success(siteNavigationDO);
        } catch (ArgumentException ae) {
            logger.error("查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("查询失败", th);
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询列表
     *
     * @param siteNavigationDTO DTO
     * @param pageSize          页码
     * @param pageNum           页数
     * @auther: rm 2817512105@qq.com
     * @date: 2019-06-04
     * @return: com.shopx.common.model.result.DubboPageResult<EsSiteNavigationDO>
     */
    @Override
    public DubboPageResult<EsSiteNavigationDO> getSiteNavigationList(EsSiteNavigationDTO siteNavigationDTO, int pageSize, int pageNum) {
        QueryWrapper<EsSiteNavigation> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件
            queryWrapper.lambda().eq(EsSiteNavigation::getClientType, siteNavigationDTO.getClientType()).orderByDesc(EsSiteNavigation::getSort);
            Page<EsSiteNavigation> page = new Page<>(pageNum, pageSize);
            IPage<EsSiteNavigation> iPage = this.page(page, queryWrapper);
            List<EsSiteNavigationDO> siteNavigationDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                siteNavigationDOList = iPage.getRecords().stream().map(siteNavigation -> {
                    EsSiteNavigationDO siteNavigationDO = new EsSiteNavigationDO();
                    BeanUtil.copyProperties(siteNavigation, siteNavigationDO);
                    return siteNavigationDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(iPage.getTotal(), siteNavigationDOList);
        } catch (ArgumentException ae) {
            logger.error("分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("分页查询失败", th);
            return DubboPageResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据主键删除数据
     *
     * @param id 主键id
     * @auther: rm 2817512105@qq.com
     * @date: 2019-06-04
     * @return: com.shopx.common.model.result.DubboResult<EsSiteNavigationDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteSiteNavigation(Long id) {
        try {
            if (id == 0) {
                throw new ArgumentException(ErrorCode.PARAM_ERROR.getErrorCode(), String.format("参数传入错误ID不能为空[%s]", id));
            }
            EsSiteNavigation esSiteNavigation = siteNavigationMapper.selectById(id);
            if (esSiteNavigation == null) {
                throw new ArgumentException(ErrorCode.SITE_NAVIGATION_NOT_EXIT.getErrorCode(), "导航栏不存在，请正确操作");
            }
            QueryWrapper<EsSiteNavigation> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.lambda().eq(EsSiteNavigation::getId, id);
            this.siteNavigationMapper.delete(deleteWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("删除失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    //根据客户端类型查询导航菜单
    @Override
    public DubboPageResult<EsSiteNavigationDO> getByClientType(String clientType) {
        try {
            QueryWrapper<EsSiteNavigation> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsSiteNavigation::getClientType, clientType).orderByAsc(EsSiteNavigation::getSort);
            List<EsSiteNavigation> data = siteNavigationMapper.selectList(queryWrapper);
            List<EsSiteNavigationDO> doList = BeanUtil.copyList(data, EsSiteNavigationDO.class);
            return DubboPageResult.success(doList);
        } catch (ArgumentException ae) {
            logger.error("根据客户端类型查询导航菜单失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("根据客户端类型查询导航菜单失败", th);
            return DubboPageResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }
}
