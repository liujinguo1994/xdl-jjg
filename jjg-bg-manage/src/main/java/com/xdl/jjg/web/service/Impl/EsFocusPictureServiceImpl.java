package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jjg.system.model.domain.EsFocusPictureDO;
import com.jjg.system.model.dto.EsFocusPictureDTO;
import com.xdl.jjg.constant.ErrorCode;
import com.xdl.jjg.entity.EsFocusPicture;
import com.xdl.jjg.mapper.EsFocusPictureMapper;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.util.CollectionUtils;
import com.xdl.jjg.web.service.IEsFocusPictureService;
import com.xdl.jjg.web.service.IEsPageCreateManagerService;
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
public class EsFocusPictureServiceImpl extends ServiceImpl<EsFocusPictureMapper, EsFocusPicture> implements IEsFocusPictureService {

    private static Logger logger = LoggerFactory.getLogger(EsFocusPictureServiceImpl.class);

    @Autowired
    private EsFocusPictureMapper focusPictureMapper;

    @Autowired
    private IEsPageCreateManagerService pageCreateManagerService;

    /**
     * 插入数据
     *
     * @param focusPictureDTO DTO
     * @auther: rm 2817512105@qq.com
     * @date: 2019-06-04
     * @return: com.shopx.common.model.result.DubboResult<EsFocusPictureDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult<EsFocusPictureDO> insertFocusPicture(EsFocusPictureDTO focusPictureDTO) {
        try {
            if (focusPictureDTO == null) {
                throw new ArgumentException(ErrorCode.PARAM_ERROR.getErrorCode(), ErrorCode.PARAM_ERROR.getErrorMsg());
            }
            //焦点图不能超过5个
            QueryWrapper<EsFocusPicture> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsFocusPicture::getClientType, focusPictureDTO.getClientType());
            List<EsFocusPicture> pictureList = focusPictureMapper.selectList(queryWrapper);
            if (pictureList.size() > 5) {
                throw new ArgumentException(ErrorCode.PICTURE_SIZE_TOO_BIG.getErrorCode(), "焦点图数量不能超过五张");
            }

            EsFocusPicture focusPicture = new EsFocusPicture();
            BeanUtil.copyProperties(focusPictureDTO, focusPicture);
            this.focusPictureMapper.insert(focusPicture);

            EsFocusPictureDO pictureDO = new EsFocusPictureDO();
            BeanUtil.copyProperties(focusPicture, pictureDO);

            //发送mq消息
           /* String[] choosePages = {"INDEX"};
            pageCreateManagerService.create(choosePages);*/

            return DubboResult.success(pictureDO);
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
     * @param focusPictureDTO DTO
     * @auther: rm 2817512105@qq.com
     * @date: 2019-06-04
     * @return: com.shopx.common.model.result.DubboResult<EsFocusPictureDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult<EsFocusPictureDO> updateFocusPicture(EsFocusPictureDTO focusPictureDTO) {
        try {
            if (focusPictureDTO == null) {
                throw new ArgumentException(ErrorCode.PARAM_ERROR.getErrorCode(), ErrorCode.PARAM_ERROR.getErrorMsg());
            }
            EsFocusPicture focusPicture = new EsFocusPicture();
            BeanUtil.copyProperties(focusPictureDTO, focusPicture);
            QueryWrapper<EsFocusPicture> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsFocusPicture::getId, focusPictureDTO.getId());
            this.focusPictureMapper.update(focusPicture, queryWrapper);

            EsFocusPictureDO pictureDO = new EsFocusPictureDO();
            BeanUtil.copyProperties(focusPicture, pictureDO);

            //发送mq消息
           /* String[] choosePages = {"INDEX"};
            pageCreateManagerService.create(choosePages);*/

            return DubboResult.success(pictureDO);
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
     * @return: com.shopx.common.model.result.DubboResult<EsFocusPictureDO>
     */
    @Override
    public DubboResult<EsFocusPictureDO> getFocusPicture(Long id) {
        try {
            QueryWrapper<EsFocusPicture> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsFocusPicture::getId, id);
            EsFocusPicture focusPicture = this.focusPictureMapper.selectOne(queryWrapper);
            EsFocusPictureDO focusPictureDO = new EsFocusPictureDO();
            if (focusPicture == null) {
                throw new ArgumentException(ErrorCode.DATA_NOT_EXIST.getErrorCode(), ErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(focusPicture, focusPictureDO);
            return DubboResult.success(focusPictureDO);
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
     * @param focusPictureDTO DTO
     * @param pageSize        页码
     * @param pageNum         页数
     * @auther: rm 2817512105@qq.com
     * @date: 2019-06-04
     * @return: com.shopx.common.model.result.DubboPageResult<EsFocusPictureDO>
     */
    @Override
    public DubboPageResult<EsFocusPictureDO> getFocusPictureList(EsFocusPictureDTO focusPictureDTO, int pageSize, int pageNum) {
        QueryWrapper<EsFocusPicture> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件

            Page<EsFocusPicture> page = new Page<>(pageNum, pageSize);
            IPage<EsFocusPicture> iPage = this.page(page, queryWrapper);
            List<EsFocusPictureDO> focusPictureDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                focusPictureDOList = iPage.getRecords().stream().map(focusPicture -> {
                    EsFocusPictureDO focusPictureDO = new EsFocusPictureDO();
                    BeanUtil.copyProperties(focusPicture, focusPictureDO);
                    return focusPictureDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(focusPictureDOList);
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
     * @return: com.shopx.common.model.result.DubboResult<EsFocusPictureDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteFocusPicture(Long id) {
        try {
            if (id == 0) {
                throw new ArgumentException(ErrorCode.PARAM_ERROR.getErrorCode(), String.format("参数传入错误ID不能为空[%s]", id));
            }
            QueryWrapper<EsFocusPicture> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.lambda().eq(EsFocusPicture::getId, id);
            this.focusPictureMapper.delete(deleteWrapper);

            //发送mq消息
           /* String[] choosePages = {"INDEX"};
            pageCreateManagerService.create(choosePages);*/

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

    //根据客户端类型查询轮播图列表
    @Override
    public DubboPageResult<EsFocusPictureDO> getList(String clientType) {
        try {
            QueryWrapper<EsFocusPicture> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsFocusPicture::getClientType, clientType).orderByAsc(EsFocusPicture::getId);
            List<EsFocusPicture> data = focusPictureMapper.selectList(queryWrapper);
            List<EsFocusPictureDO> doList = (List<EsFocusPictureDO>) BeanUtil.copyList(data, new EsFocusPictureDO().getClass());
            return DubboPageResult.success(doList);
        } catch (ArgumentException ae) {
            logger.error("根据客户端类型查询轮播图列表失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("根据客户端类型查询轮播图列表失败", th);
            return DubboPageResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }
}
