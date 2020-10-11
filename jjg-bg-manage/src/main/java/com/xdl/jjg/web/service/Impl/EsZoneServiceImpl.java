package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xdl.jjg.constant.ErrorCode;
import com.xdl.jjg.entity.EsZone;
import com.xdl.jjg.mapper.EsZoneMapper;
import com.xdl.jjg.model.domain.EsZoneDO;
import com.xdl.jjg.model.dto.EsZoneDTO;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.util.CollectionUtils;
import com.xdl.jjg.web.service.IEsZoneService;
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
 * 专区管理 服务实现类
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-05-12
 */
@Service
public class EsZoneServiceImpl extends ServiceImpl<EsZoneMapper, EsZone> implements IEsZoneService {

    private static Logger logger = LoggerFactory.getLogger(EsZoneServiceImpl.class);

    @Autowired
    private EsZoneMapper zoneMapper;

    /**
     * 插入专区管理数据
     *
     * @param zoneDTO 专区管理DTO
     * @auther: rm 2817512105@qq.com
     * @date: 2020-05-12
     * @return: com.shopx.common.model.result.DubboResult<EsZoneDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertZone(EsZoneDTO zoneDTO) {
        try {
            if (zoneDTO == null) {
                throw new ArgumentException(ErrorCode.PARAM_ERROR.getErrorCode(), ErrorCode.PARAM_ERROR.getErrorMsg());
            }

            EsZone zone = new EsZone();
            BeanUtil.copyProperties(zoneDTO, zone);
            this.zoneMapper.insert(zone);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("专区管理新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable ae) {
            logger.error("专区管理新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新专区管理数据
     *
     * @param zoneDTO 专区管理DTO
     * @auther: rm 2817512105@qq.com
     * @date: 2020-05-12
     * @return: com.shopx.common.model.result.DubboResult<EsZoneDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateZone(EsZoneDTO zoneDTO) {
        try {
            if (zoneDTO == null) {
                throw new ArgumentException(ErrorCode.PARAM_ERROR.getErrorCode(), ErrorCode.PARAM_ERROR.getErrorMsg());
            }
            EsZone zone = new EsZone();
            BeanUtil.copyProperties(zoneDTO, zone);
            QueryWrapper<EsZone> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsZone::getId, zoneDTO.getId());
            this.zoneMapper.update(zone, queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("专区管理更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("专区管理更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据id获取专区管理详情
     *
     * @param id 主键id
     * @auther: rm 2817512105@qq.com
     * @date: 2020-05-12
     * @return: com.shopx.common.model.result.DubboResult<EsZoneDO>
     */
    @Override
    public DubboResult<EsZoneDO> getZone(Long id) {
        try {
            QueryWrapper<EsZone> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsZone::getId, id);
            EsZone zone = this.zoneMapper.selectOne(queryWrapper);
            EsZoneDO zoneDO = new EsZoneDO();
            if (zone == null) {
                throw new ArgumentException(ErrorCode.DATA_NOT_EXIST.getErrorCode(), ErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(zone, zoneDO);
            return DubboResult.success(zoneDO);
        } catch (ArgumentException ae){
            logger.error("专区管理查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("专区管理查询失败", th);
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询专区管理列表
     *
     * @param zoneDTO 专区管理DTO
     * @param pageSize     页码
     * @param pageNum      页数
     * @auther: rm 2817512105@qq.com
     * @date: 2020-05-12
     * @return: com.shopx.common.model.result.DubboPageResult<EsZoneDO>
     */
    @Override
    public DubboPageResult<EsZoneDO> getZoneList(EsZoneDTO zoneDTO, int pageSize, int pageNum) {
        QueryWrapper<EsZone> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件
            queryWrapper.lambda().orderByAsc(EsZone::getId);
            Page<EsZone> page = new Page<>(pageNum, pageSize);
            IPage<EsZone> iPage = this.page(page, queryWrapper);
            List<EsZoneDO> zoneDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                zoneDOList = iPage.getRecords().stream().map(zone -> {
                    EsZoneDO zoneDO = new EsZoneDO();
                    BeanUtil.copyProperties(zone, zoneDO);
                    return zoneDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(iPage.getTotal(),zoneDOList);
        } catch (ArgumentException ae){
            logger.error("专区管理分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("专区管理分页查询失败", th);
            return DubboPageResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据主键删除专区管理数据
     *
     * @param id 主键id
     * @auther: rm 2817512105@qq.com
     * @date: 2020-05-12
     * @return: com.shopx.common.model.result.DubboResult<EsZoneDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteZone(Long id) {
        try {
            if (id == 0) {
                throw new ArgumentException(ErrorCode.PARAM_ERROR.getErrorCode(), String.format("参数传入错误ID不能为空[%s]", id));
            }
            QueryWrapper<EsZone> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.lambda().eq(EsZone::getId, id);
            this.zoneMapper.delete(deleteWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae){
             logger.error("专区管理删除失败", ae);
             TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
             return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("专区管理删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    public DubboResult<EsZoneDO> getByName(String zoneName) {
        try {
            QueryWrapper<EsZone> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsZone::getZoneName, zoneName);
            EsZone zone = this.zoneMapper.selectOne(queryWrapper);
            EsZoneDO zoneDO = new EsZoneDO();
            if (zone != null) {
                BeanUtil.copyProperties(zone, zoneDO);
            }
            return DubboResult.success(zoneDO);
        } catch (ArgumentException ae){
            logger.error("专区管理查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("专区管理查询失败", th);
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }
}
