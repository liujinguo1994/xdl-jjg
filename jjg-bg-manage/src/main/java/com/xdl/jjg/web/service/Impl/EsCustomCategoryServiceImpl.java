package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jjg.system.model.domain.EsCustomCategoryDO;
import com.jjg.system.model.domain.EsOftenGoodsDO;
import com.jjg.system.model.domain.EsZoneDO;
import com.jjg.system.model.dto.EsCustomCategoryDTO;
import com.jjg.system.model.vo.EsOftenGoodsVO;
import com.xdl.jjg.constant.ErrorCode;
import com.xdl.jjg.entity.EsCustomCategory;
import com.xdl.jjg.mapper.EsCustomCategoryMapper;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.util.CollectionUtils;
import com.xdl.jjg.web.service.IEsCustomCategoryService;
import com.xdl.jjg.web.service.IEsFindGoodsService;
import com.xdl.jjg.web.service.IEsOftenGoodsService;
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
 * 自定义分类 服务实现类
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-05-06
 */
@Service
public class EsCustomCategoryServiceImpl extends ServiceImpl<EsCustomCategoryMapper, EsCustomCategory> implements IEsCustomCategoryService {

    private static Logger logger = LoggerFactory.getLogger(EsCustomCategoryServiceImpl.class);

    @Autowired
    private EsCustomCategoryMapper customCategoryMapper;
    @Autowired
    private IEsZoneService zoneService;
    @Autowired
    private IEsFindGoodsService findGoodsService;
    @Autowired
    private IEsOftenGoodsService oftenGoodsService;

    /**
     * 插入自定义分类数据
     *
     * @param customCategoryDTO 自定义分类DTO
     * @auther: rm 2817512105@qq.com
     * @date: 2020-05-06
     * @return: com.shopx.common.model.result.DubboResult<EsCustomCategoryDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertCustomCategory(EsCustomCategoryDTO customCategoryDTO) {
        try {
            if (customCategoryDTO == null) {
                throw new ArgumentException(ErrorCode.PARAM_ERROR.getErrorCode(), ErrorCode.PARAM_ERROR.getErrorMsg());
            }

            EsCustomCategory customCategory = new EsCustomCategory();
            BeanUtil.copyProperties(customCategoryDTO, customCategory);
            this.customCategoryMapper.insert(customCategory);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("自定义分类新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable ae) {
            logger.error("自定义分类新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新自定义分类数据
     *
     * @param customCategoryDTO 自定义分类DTO
     * @auther: rm 2817512105@qq.com
     * @date: 2020-05-06
     * @return: com.shopx.common.model.result.DubboResult<EsCustomCategoryDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateCustomCategory(EsCustomCategoryDTO customCategoryDTO) {
        try {
            if (customCategoryDTO == null) {
                throw new ArgumentException(ErrorCode.PARAM_ERROR.getErrorCode(), ErrorCode.PARAM_ERROR.getErrorMsg());
            }
            EsCustomCategory customCategory = new EsCustomCategory();
            BeanUtil.copyProperties(customCategoryDTO, customCategory);
            QueryWrapper<EsCustomCategory> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsCustomCategory::getId, customCategoryDTO.getId());
            this.customCategoryMapper.update(customCategory, queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("自定义分类更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("自定义分类更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据id获取自定义分类详情
     *
     * @param id 主键id
     * @auther: rm 2817512105@qq.com
     * @date: 2020-05-06
     * @return: com.shopx.common.model.result.DubboResult<EsCustomCategoryDO>
     */
    @Override
    public DubboResult<EsCustomCategoryDO> getCustomCategory(Long id) {
        try {
            QueryWrapper<EsCustomCategory> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsCustomCategory::getId, id);
            EsCustomCategory customCategory = this.customCategoryMapper.selectOne(queryWrapper);
            EsCustomCategoryDO customCategoryDO = new EsCustomCategoryDO();
            if (customCategory == null) {
                throw new ArgumentException(ErrorCode.DATA_NOT_EXIST.getErrorCode(), ErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(customCategory, customCategoryDO);
            return DubboResult.success(customCategoryDO);
        } catch (ArgumentException ae) {
            logger.error("自定义分类查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("自定义分类查询失败", th);
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询自定义分类列表
     *
     * @param customCategoryDTO 自定义分类DTO
     * @param pageSize          页码
     * @param pageNum           页数
     * @auther: rm 2817512105@qq.com
     * @date: 2020-05-06
     * @return: com.shopx.common.model.result.DubboPageResult<EsCustomCategoryDO>
     */
    @Override
    public DubboPageResult<EsCustomCategoryDO> getCustomCategoryList(EsCustomCategoryDTO customCategoryDTO, int pageSize, int pageNum) {
        QueryWrapper<EsCustomCategory> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件
            queryWrapper.lambda().eq(customCategoryDTO.getZoneId() != null, EsCustomCategory::getZoneId, customCategoryDTO.getZoneId());
            Page<EsCustomCategory> page = new Page<>(pageNum, pageSize);
            IPage<EsCustomCategory> iPage = this.page(page, queryWrapper);
            List<EsCustomCategoryDO> customCategoryDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                customCategoryDOList = iPage.getRecords().stream().map(customCategory -> {
                    EsCustomCategoryDO customCategoryDO = new EsCustomCategoryDO();
                    BeanUtil.copyProperties(customCategory, customCategoryDO);
                    DubboResult<EsZoneDO> result = zoneService.getZone(customCategoryDO.getZoneId());
                    if (result.isSuccess() && result.getData() != null) {
                        customCategoryDO.setZoneName(result.getData().getZoneName());
                    }
                    //常买清单
                    if (null != customCategoryDTO.getZoneId() && customCategoryDTO.getZoneId() == 2) {
                        DubboPageResult<EsOftenGoodsDO> findGoods = this.oftenGoodsService.getByCustomCategoryId(customCategory.getId());
                        if (findGoods.isSuccess() && findGoods.getData().getList().size() > 0) {
                            List<EsOftenGoodsVO> esFindGoodsVOS = BeanUtil.copyList(findGoods.getData().getList(), EsOftenGoodsVO.class);
                            customCategoryDO.setOftenGoodsVOS(esFindGoodsVOS);
                        }
                    }
                    return customCategoryDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(iPage.getTotal(), customCategoryDOList);
        } catch (ArgumentException ae) {
            logger.error("自定义分类分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("自定义分类分页查询失败", th);
            return DubboPageResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据主键删除自定义分类数据
     *
     * @param id 主键id
     * @auther: rm 2817512105@qq.com
     * @date: 2020-05-06
     * @return: com.shopx.common.model.result.DubboResult<EsCustomCategoryDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteCustomCategory(Long id) {
        try {
            if (id == 0) {
                throw new ArgumentException(ErrorCode.PARAM_ERROR.getErrorCode(), String.format("参数传入错误ID不能为空[%s]", id));
            }
            QueryWrapper<EsCustomCategory> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.lambda().eq(EsCustomCategory::getId, id);
            this.customCategoryMapper.delete(deleteWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("自定义分类删除失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("自定义分类删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
}
