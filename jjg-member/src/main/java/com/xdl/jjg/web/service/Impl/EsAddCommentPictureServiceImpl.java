package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xdl.jjg.constant.ErrorCode;
import com.xdl.jjg.entity.EsAddCommentPicture;
import com.xdl.jjg.mapper.EsAddCommentPictureMapper;
import com.xdl.jjg.model.domain.EsAddCommentPictureDO;
import com.xdl.jjg.model.dto.EsAddCommentPictureDTO;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.util.CollectionUtils;
import com.xdl.jjg.web.service.IEsAddCommentPictureService;
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
 *  追加评论图片 服务实现类
 * </p>
 *
 * @author LBW 981087977@qq.com
 * @since 2019-11-12 14:44:44
 */
@Service
public class EsAddCommentPictureServiceImpl extends ServiceImpl<EsAddCommentPictureMapper, EsAddCommentPicture> implements IEsAddCommentPictureService {

    private static Logger logger = LoggerFactory.getLogger(EsAddCommentPictureServiceImpl.class);

    @Autowired
    private EsAddCommentPictureMapper addCommentPictureMapper;

    /**
     * 插入 追加评论图片数据
     *
     * @param addCommentPictureDTO  追加评论图片DTO
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsAddCommentPictureDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertAddCommentPicture(EsAddCommentPictureDTO addCommentPictureDTO) {
        try {
            EsAddCommentPicture addCommentPicture = new EsAddCommentPicture();
            BeanUtil.copyProperties(addCommentPictureDTO, addCommentPicture);
            this.addCommentPictureMapper.insert(addCommentPicture);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error(" 追加评论图片新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable ae) {
            logger.error(" 追加评论图片新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新 追加评论图片数据
     *
     * @param addCommentPictureDTO  追加评论图片DTO
     * @param id                          主键id
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsAddCommentPictureDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateAddCommentPicture(EsAddCommentPictureDTO addCommentPictureDTO, Long id) {
        try {
            EsAddCommentPicture addCommentPicture = this.addCommentPictureMapper.selectById(id);
            if (addCommentPicture == null) {
                throw new ArgumentException(ErrorCode.DATA_NOT_EXIST.getErrorCode(), ErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(addCommentPictureDTO, addCommentPicture);
            QueryWrapper<EsAddCommentPicture> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsAddCommentPicture::getId, id);
            this.addCommentPictureMapper.update(addCommentPicture, queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error(" 追加评论图片更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error(" 追加评论图片更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据id获取 追加评论图片详情
     *
     * @param id 主键id
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsAddCommentPictureDO>
     */
    @Override
    public DubboResult<EsAddCommentPictureDO> getAddCommentPicture(Long id) {
        try {
            EsAddCommentPicture addCommentPicture = this.addCommentPictureMapper.selectById(id);
            if (addCommentPicture == null) {
                throw new ArgumentException(ErrorCode.DATA_NOT_EXIST.getErrorCode(), ErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            EsAddCommentPictureDO addCommentPictureDO = new EsAddCommentPictureDO();
            BeanUtil.copyProperties(addCommentPicture, addCommentPictureDO);
            return DubboResult.success(addCommentPictureDO);
        } catch (ArgumentException ae){
            logger.error(" 追加评论图片查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error(" 追加评论图片查询失败", th);
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询 追加评论图片列表
     *
     * @param addCommentPictureDTO  追加评论图片DTO
     * @param pageSize     页码
     * @param pageNum      页数
     * @auther: libw 981087977@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsAddCommentPictureDO>
     */
    @Override
    public DubboPageResult<EsAddCommentPictureDO> getAddCommentPictureList(EsAddCommentPictureDTO addCommentPictureDTO, int pageSize, int pageNum) {
        QueryWrapper<EsAddCommentPicture> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件

            Page<EsAddCommentPicture> page = new Page<>(pageNum, pageSize);
            IPage<EsAddCommentPicture> iPage = this.page(page, queryWrapper);
            List<EsAddCommentPictureDO> addCommentPictureDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                addCommentPictureDOList = iPage.getRecords().stream().map(addCommentPicture -> {
                    EsAddCommentPictureDO addCommentPictureDO = new EsAddCommentPictureDO();
                    BeanUtil.copyProperties(addCommentPicture, addCommentPictureDO);
                    return addCommentPictureDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(addCommentPictureDOList);
        } catch (ArgumentException ae){
            logger.error(" 追加评论图片分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error(" 追加评论图片分页查询失败", th);
            return DubboPageResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }
    public DubboPageResult<EsAddCommentPictureDO> getAddCommentPictureList(Long commentId) {
        QueryWrapper<EsAddCommentPicture> queryWrapper = new QueryWrapper<>();
        try {
            queryWrapper.lambda().eq(EsAddCommentPicture::getCommentId,commentId);
           List<EsAddCommentPicture> pictureList= this.list(queryWrapper);
            List<EsAddCommentPictureDO> addCommentPictureDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(pictureList)) {
                addCommentPictureDOList = pictureList.stream().map(addCommentPicture -> {
                    EsAddCommentPictureDO addCommentPictureDO = new EsAddCommentPictureDO();
                    BeanUtil.copyProperties(addCommentPicture, addCommentPictureDO);
                    return addCommentPictureDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(addCommentPictureDOList);
        } catch (ArgumentException ae){
            logger.error(" 追加评论图片查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error(" 追加评论图片查询失败", th);
            return DubboPageResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }
    /**
     * 根据主键删除 追加评论图片数据
     *
     * @param id 主键id
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsAddCommentPictureDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteAddCommentPicture(Long id) {
        try {
            this.addCommentPictureMapper.deleteById(id);
            return DubboResult.success();
        } catch (ArgumentException ae){
             logger.error(" 追加评论图片删除失败", ae);
             TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
             return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error(" 追加评论图片删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult addPictureBatch(Long addCommentId, List<String> list) {
        try {
            if(CollectionUtils.isEmpty(list)){
                return DubboResult.fail(ErrorCode.PARAM_ERROR.getErrorCode(), ErrorCode.PARAM_ERROR.getErrorMsg());
            }
            EsAddCommentPicture addCommentPicture = new EsAddCommentPicture();
            addCommentPicture.setCommentId(addCommentId);
            for(int i = 0; i < list.size(); i++){
                addCommentPicture.setOriginal(list.get(i));
                addCommentPicture.setSort(i);
                addCommentPictureMapper.insert(addCommentPicture);
            }
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error(" 追加评论图片删除失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error(" 追加评论图片删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
}
