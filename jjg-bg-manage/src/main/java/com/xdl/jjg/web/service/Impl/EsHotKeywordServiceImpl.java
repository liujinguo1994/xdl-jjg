package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xdl.jjg.constant.ErrorCode;
import com.xdl.jjg.entity.EsHotKeyword;
import com.xdl.jjg.mapper.EsHotKeywordMapper;
import com.xdl.jjg.model.domain.EsHotKeywordDO;
import com.xdl.jjg.model.dto.EsHotKeywordDTO;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.web.service.IEsHotKeywordService;
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
 * 服务实现类
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-06-04
 */
@Service
public class EsHotKeywordServiceImpl extends ServiceImpl<EsHotKeywordMapper, EsHotKeyword> implements IEsHotKeywordService {

    private static Logger logger = LoggerFactory.getLogger(EsHotKeywordServiceImpl.class);

    @Autowired
    private EsHotKeywordMapper hotKeywordMapper;

    /**
     * 插入数据
     *
     * @param hotKeywordDTO DTO
     * @auther: rm 2817512105@qq.com
     * @date: 2019-06-04
     * @return: com.shopx.common.model.result.DubboResult<EsHotKeywordDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertHotKeyword(EsHotKeywordDTO hotKeywordDTO) {
        try {
            if (hotKeywordDTO == null) {
                throw new ArgumentException(ErrorCode.PARAM_ERROR.getErrorCode(), ErrorCode.PARAM_ERROR.getErrorMsg());
            }

            EsHotKeyword hotKeyword = new EsHotKeyword();
            BeanUtil.copyProperties(hotKeywordDTO, hotKeyword);
            this.hotKeywordMapper.insert(hotKeyword);
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
     * @param hotKeywordDTO DTO
     * @auther: rm 2817512105@qq.com
     * @date: 2019-06-04
     * @return: com.shopx.common.model.result.DubboResult<EsHotKeywordDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateHotKeyword(EsHotKeywordDTO hotKeywordDTO) {
        try {
            if (hotKeywordDTO == null) {
                throw new ArgumentException(ErrorCode.PARAM_ERROR.getErrorCode(), ErrorCode.PARAM_ERROR.getErrorMsg());
            }
            EsHotKeyword hotKeyword = new EsHotKeyword();
            BeanUtil.copyProperties(hotKeywordDTO, hotKeyword);
            QueryWrapper<EsHotKeyword> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsHotKeyword::getId, hotKeywordDTO.getId());
            this.hotKeywordMapper.update(hotKeyword, queryWrapper);
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
     * @return: com.shopx.common.model.result.DubboResult<EsHotKeywordDO>
     */
    @Override
    public DubboResult<EsHotKeywordDO> getHotKeyword(Long id) {
        try {
            QueryWrapper<EsHotKeyword> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsHotKeyword::getId, id);
            EsHotKeyword hotKeyword = this.hotKeywordMapper.selectOne(queryWrapper);
            EsHotKeywordDO hotKeywordDO = new EsHotKeywordDO();
            if (hotKeyword == null) {
                throw new ArgumentException(ErrorCode.DATA_NOT_EXIST.getErrorCode(), ErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(hotKeyword, hotKeywordDO);
            return DubboResult.success(hotKeywordDO);
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
     * @param hotKeywordDTO DTO
     * @param pageSize      页码
     * @param pageNum       页数
     * @auther: rm 2817512105@qq.com
     * @date: 2019-06-04
     * @return: com.shopx.common.model.result.DubboPageResult<EsHotKeywordDO>
     */
    @Override
    public DubboPageResult<EsHotKeywordDO> getHotKeywordList(EsHotKeywordDTO hotKeywordDTO, int pageSize, int pageNum) {
        QueryWrapper<EsHotKeyword> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件

            Page<EsHotKeyword> page = new Page<>(pageNum, pageSize);
            IPage<EsHotKeyword> iPage = this.page(page, queryWrapper);
            List<EsHotKeywordDO> hotKeywordDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                hotKeywordDOList = iPage.getRecords().stream().map(hotKeyword -> {
                    EsHotKeywordDO hotKeywordDO = new EsHotKeywordDO();
                    BeanUtil.copyProperties(hotKeyword, hotKeywordDO);
                    return hotKeywordDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(iPage.getTotal(), hotKeywordDOList);
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
     * @return: com.shopx.common.model.result.DubboResult<EsHotKeywordDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteHotKeyword(Long id) {
        try {
            if (id == 0) {
                throw new ArgumentException(ErrorCode.PARAM_ERROR.getErrorCode(), String.format("参数传入错误ID不能为空[%s]", id));
            }
            QueryWrapper<EsHotKeyword> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.lambda().eq(EsHotKeyword::getId, id);
            this.hotKeywordMapper.delete(deleteWrapper);
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

    /**
     * 查询列表
     */
    @Override
    public DubboPageResult<EsHotKeywordDO> getList() {
        QueryWrapper<EsHotKeyword> queryWrapper = new QueryWrapper<>();
        try {
            queryWrapper.lambda().orderByAsc(EsHotKeyword::getSort);
            List<EsHotKeyword> data = hotKeywordMapper.selectList(queryWrapper);
            List<EsHotKeywordDO> doList = BeanUtil.copyList(data, EsHotKeywordDO.class);
            return DubboPageResult.success(doList);
        } catch (ArgumentException ae) {
            logger.error("查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("查询失败", th);
            return DubboPageResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }
}
