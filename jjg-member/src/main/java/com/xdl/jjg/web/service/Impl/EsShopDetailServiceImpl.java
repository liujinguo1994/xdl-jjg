package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jjg.member.model.dto.EsShopDetailDTO;
import com.xdl.jjg.constant.MemberErrorCode;
import com.xdl.jjg.entity.EsShopDetail;
import com.xdl.jjg.mapper.EsShopDetailMapper;
import com.xdl.jjg.model.domain.EsShopDetailDO;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.util.CollectionUtils;
import com.xdl.jjg.web.service.IEsShopDetailService;
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
 * 店铺详细 服务实现类
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-04
 */
@Service
public class EsShopDetailServiceImpl extends ServiceImpl<EsShopDetailMapper, EsShopDetail> implements IEsShopDetailService {

    private static Logger logger = LoggerFactory.getLogger(EsShopDetailServiceImpl.class);

    @Autowired
    private EsShopDetailMapper shopDetailMapper;

    /**
     * 插入店铺详细数据
     *
     * @param shopDetailDTO 店铺详细DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsShopDetailDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertShopDetail(EsShopDetailDTO shopDetailDTO) {
        try {
            if (shopDetailDTO == null) {
                throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), MemberErrorCode.PARAM_ERROR.getErrorMsg());
            }

            EsShopDetail shopDetail = new EsShopDetail();
            BeanUtil.copyProperties(shopDetailDTO, shopDetail);
            this.shopDetailMapper.insert(shopDetail);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("店铺详细新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable ae) {
            logger.error("店铺详细新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新店铺详细数据
     *
     * @param shopDetailDTO 店铺详细DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsShopDetailDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateShopDetail(EsShopDetailDTO shopDetailDTO) {
        try {
            if (shopDetailDTO == null) {
                throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), MemberErrorCode.PARAM_ERROR.getErrorMsg());
            }
            EsShopDetail shopDetail = new EsShopDetail();
            BeanUtil.copyProperties(shopDetailDTO, shopDetail);
            QueryWrapper<EsShopDetail> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsShopDetail::getShopId, shopDetailDTO.getShopId());
            this.shopDetailMapper.update(shopDetail, queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("店铺详细更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("店铺详细更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据id获取店铺详细详情
     *
     * @param id 主键id
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsShopDetailDO>
     */
    @Override
    public DubboResult<EsShopDetailDO> getShopDetail(Long id) {
        try {
            EsShopDetail shopDetail = this.shopDetailMapper.selectById(id);
            EsShopDetailDO shopDetailDO = new EsShopDetailDO();
            if (shopDetail == null) {
                throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(shopDetail, shopDetailDO);
            return DubboResult.success(shopDetailDO);
        } catch (ArgumentException ae){
            logger.error("店铺详细查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("店铺详细查询失败", th);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询店铺详细列表
     *
     * @param shopDetailDTO 店铺详细DTO
     * @param pageSize     页码
     * @param pageNum      页数
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsShopDetailDO>
     */
    @Override
    public DubboPageResult<EsShopDetailDO> getShopDetailList(EsShopDetailDTO shopDetailDTO, int pageSize, int pageNum) {
        QueryWrapper<EsShopDetail> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件

            Page<EsShopDetail> page = new Page<>(pageNum, pageSize);
            IPage<EsShopDetail> iPage = this.page(page, queryWrapper);
            List<EsShopDetailDO> shopDetailDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                shopDetailDOList = iPage.getRecords().stream().map(shopDetail -> {
                    EsShopDetailDO shopDetailDO = new EsShopDetailDO();
                    BeanUtil.copyProperties(shopDetail, shopDetailDO);
                    return shopDetailDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(shopDetailDOList);
        } catch (ArgumentException ae){
            logger.error("店铺详细分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("店铺详细分页查询失败", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据主键删除店铺详细数据
     *
     * @param id 主键id
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsShopDetailDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteShopDetail(Long id) {
        try {
            if (id == 0) {
                throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), String.format("参数传入错误ID不能为空[%s]", id));
            }
            QueryWrapper<EsShopDetail> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.lambda().eq(EsShopDetail::getId, id);
            this.shopDetailMapper.delete(deleteWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae){
             logger.error("店铺详细删除失败", ae);
             TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
             return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("店铺详细删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    public DubboResult<EsShopDetailDO> getByShopId(Long shopId) {
        try {
            QueryWrapper<EsShopDetail> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsShopDetail::getShopId, shopId);
            EsShopDetail shopDetail = this.shopDetailMapper.selectOne(queryWrapper);
            EsShopDetailDO shopDetailDO = new EsShopDetailDO();
            if (shopDetail == null) {
                throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(shopDetail, shopDetailDO);
            return DubboResult.success(shopDetailDO);
        } catch (ArgumentException ae){
            logger.error("店铺详细查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("店铺详细查询失败", th);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }
}
