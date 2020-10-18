package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jjg.system.model.domain.EsAdvertisingDO;
import com.jjg.system.model.dto.EsAdvertisingDTO;
import com.xdl.jjg.constant.ErrorCode;
import com.xdl.jjg.entity.EsAdvertising;
import com.xdl.jjg.mapper.EsAdvertisingMapper;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.util.CollectionUtils;
import com.xdl.jjg.web.service.IEsAdvertisingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 广告位 服务实现类
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-05-12
 */
@Service
public class EsAdvertisingServiceImpl extends ServiceImpl<EsAdvertisingMapper, EsAdvertising> implements IEsAdvertisingService {

    private static Logger logger = LoggerFactory.getLogger(EsAdvertisingServiceImpl.class);

    @Autowired
    private EsAdvertisingMapper advertisingMapper;

    /**
     * 插入广告位数据
     *
     * @param advertisingDTO 广告位DTO
     * @auther: rm 2817512105@qq.com
     * @date: 2020-05-12
     * @return: com.shopx.common.model.result.DubboResult<EsAdvertisingDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertAdvertising(EsAdvertisingDTO advertisingDTO) {
        try {
            if (advertisingDTO == null) {
                throw new ArgumentException(ErrorCode.PARAM_ERROR.getErrorCode(), ErrorCode.PARAM_ERROR.getErrorMsg());
            }
            //根据位置查询数据库是否存在
            QueryWrapper<EsAdvertising> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsAdvertising::getLocation, advertisingDTO.getLocation());
            EsAdvertising esAdvertising = advertisingMapper.selectOne(queryWrapper);
            if (esAdvertising != null) {
                throw new ArgumentException(ErrorCode.ADVERTISING_EXIT.getErrorCode(), "该位置广告已存在");
            }
            EsAdvertising advertising = new EsAdvertising();
            BeanUtil.copyProperties(advertisingDTO, advertising);
            this.advertisingMapper.insert(advertising);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("广告位新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable ae) {
            logger.error("广告位新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新广告位数据
     *
     * @param advertisingDTO 广告位DTO
     * @auther: rm 2817512105@qq.com
     * @date: 2020-05-12
     * @return: com.shopx.common.model.result.DubboResult<EsAdvertisingDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateAdvertising(EsAdvertisingDTO advertisingDTO) {
        try {
            if (advertisingDTO == null) {
                throw new ArgumentException(ErrorCode.PARAM_ERROR.getErrorCode(), ErrorCode.PARAM_ERROR.getErrorMsg());
            }
            //根据位置查询数据库是否存在
            QueryWrapper<EsAdvertising> query = new QueryWrapper<>();
            query.lambda().eq(EsAdvertising::getLocation, advertisingDTO.getLocation());
            EsAdvertising esAdvertising = advertisingMapper.selectOne(query);
            if (esAdvertising != null && !Objects.equals(esAdvertising.getId(), advertisingDTO.getId())) {
                throw new ArgumentException(ErrorCode.ADVERTISING_EXIT.getErrorCode(), "该位置广告已存在");
            }

            EsAdvertising advertising = new EsAdvertising();
            BeanUtil.copyProperties(advertisingDTO, advertising);
            QueryWrapper<EsAdvertising> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsAdvertising::getId, advertisingDTO.getId());
            this.advertisingMapper.update(advertising, queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("广告位更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("广告位更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据id获取广告位详情
     *
     * @param id 主键id
     * @auther: rm 2817512105@qq.com
     * @date: 2020-05-12
     * @return: com.shopx.common.model.result.DubboResult<EsAdvertisingDO>
     */
    @Override
    public DubboResult<EsAdvertisingDO> getAdvertising(Long id) {
        try {
            QueryWrapper<EsAdvertising> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsAdvertising::getId, id);
            EsAdvertising advertising = this.advertisingMapper.selectOne(queryWrapper);
            EsAdvertisingDO advertisingDO = new EsAdvertisingDO();
            if (advertising == null) {
                throw new ArgumentException(ErrorCode.DATA_NOT_EXIST.getErrorCode(), ErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(advertising, advertisingDO);
            return DubboResult.success(advertisingDO);
        } catch (ArgumentException ae) {
            logger.error("广告位查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("广告位查询失败", th);
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询广告位列表
     *
     * @param advertisingDTO 广告位DTO
     * @param pageSize       页码
     * @param pageNum        页数
     * @auther: rm 2817512105@qq.com
     * @date: 2020-05-12
     * @return: com.shopx.common.model.result.DubboPageResult<EsAdvertisingDO>
     */
    @Override
    public DubboPageResult<EsAdvertisingDO> getAdvertisingList(EsAdvertisingDTO advertisingDTO, int pageSize, int pageNum) {
        QueryWrapper<EsAdvertising> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件

            Page<EsAdvertising> page = new Page<>(pageNum, pageSize);
            IPage<EsAdvertising> iPage = this.page(page, queryWrapper);
            List<EsAdvertisingDO> advertisingDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                advertisingDOList = iPage.getRecords().stream().map(advertising -> {
                    EsAdvertisingDO advertisingDO = new EsAdvertisingDO();
                    BeanUtil.copyProperties(advertising, advertisingDO);
                    return advertisingDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(iPage.getTotal(), advertisingDOList);
        } catch (ArgumentException ae) {
            logger.error("广告位分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("广告位分页查询失败", th);
            return DubboPageResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据主键删除广告位数据
     *
     * @param id 主键id
     * @auther: rm 2817512105@qq.com
     * @date: 2020-05-12
     * @return: com.shopx.common.model.result.DubboResult<EsAdvertisingDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteAdvertising(Long id) {
        try {
            if (id == 0) {
                throw new ArgumentException(ErrorCode.PARAM_ERROR.getErrorCode(), String.format("参数传入错误ID不能为空[%s]", id));
            }
            QueryWrapper<EsAdvertising> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.lambda().eq(EsAdvertising::getId, id);
            this.advertisingMapper.delete(deleteWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("广告位删除失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("广告位删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    public DubboResult<EsAdvertisingDO> getListByLocation(String location) {
        try {
            QueryWrapper<EsAdvertising> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsAdvertising::getLocation, location);
            EsAdvertising advertising = this.advertisingMapper.selectOne(queryWrapper);
            EsAdvertisingDO advertisingDO = new EsAdvertisingDO();
            if (advertising != null) {
                BeanUtil.copyProperties(advertising, advertisingDO);
            }
            return DubboResult.success(advertisingDO);
        } catch (ArgumentException ae) {
            logger.error("广告位查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("广告位查询失败", th);
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }
}
