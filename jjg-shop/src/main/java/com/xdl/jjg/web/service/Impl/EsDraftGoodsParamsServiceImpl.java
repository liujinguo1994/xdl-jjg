package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shopx.common.exception.ArgumentException;
import com.shopx.common.model.result.DubboPageResult;
import com.shopx.common.model.result.DubboResult;
import com.shopx.common.util.BeanUtil;
import com.shopx.goods.api.constant.GoodsErrorCode;
import com.shopx.goods.api.model.domain.EsDraftGoodsParamsDO;
import com.shopx.goods.api.model.domain.dto.EsDraftGoodsParamsDTO;
import com.shopx.goods.api.service.IEsDraftGoodsParamsService;
import com.shopx.goods.dao.entity.EsDraftGoodsParams;
import com.shopx.goods.dao.mapper.EsDraftGoodsParamsMapper;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.apache.dubbo.config.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
 * @author wangaf 826988665@qq.com
 * @since 2019-06-03
 */
@Service(version = "${dubbo.application.version}", interfaceClass = IEsDraftGoodsParamsService.class, timeout = 50000)
public class EsDraftGoodsParamsServiceImpl extends ServiceImpl<EsDraftGoodsParamsMapper, EsDraftGoodsParams> implements IEsDraftGoodsParamsService {

    private static Logger logger = LoggerFactory.getLogger(EsDraftGoodsParamsServiceImpl.class);

    @Autowired
    private EsDraftGoodsParamsMapper draftGoodsParamsMapper;

    /**
     * 插入数据
     *
     * @param draftGoodsParamsDTO DTO
     * @auther: wangaf 826988665@qq.com
     * @date: 2019-06-03
     * @return: com.shopx.common.model.result.DubboResult<EsDraftGoodsParamsDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult<EsDraftGoodsParamsDO> insertDraftGoodsParams(EsDraftGoodsParamsDTO draftGoodsParamsDTO) {
        try {
            EsDraftGoodsParams draftGoodsParams = new EsDraftGoodsParams();
            BeanUtil.copyProperties(draftGoodsParamsDTO, draftGoodsParams);
            this.draftGoodsParamsMapper.insert(draftGoodsParams);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("草稿商品参数新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable ae) {
            logger.error("草稿商品参数新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), GoodsErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    public DubboResult<EsDraftGoodsParamsDO> insertDraftGoodsParams(List<EsDraftGoodsParamsDTO> esGoodsParamsDTOList, Long goodsId) {
        try {
            if (CollectionUtils.isNotEmpty(esGoodsParamsDTOList)) {
                //先把原来商品的参数删除再批量添加
                QueryWrapper<EsDraftGoodsParams> queryWrapper = new QueryWrapper<>();
                queryWrapper.lambda().eq(EsDraftGoodsParams::getDraftGoodsId,goodsId);
                this.draftGoodsParamsMapper.delete(queryWrapper);
                List<EsDraftGoodsParams> esGoodsParamsList = new ArrayList<>();
                if (CollectionUtils.isNotEmpty(esGoodsParamsDTOList)) {
                    esGoodsParamsList = esGoodsParamsDTOList.stream().map(goodsParamsDTO -> {
                        EsDraftGoodsParams esGoodsParams = new EsDraftGoodsParams();
                        BeanUtil.copyProperties(goodsParamsDTO, esGoodsParams);
                        esGoodsParams.setDraftGoodsId(goodsId);
                        return esGoodsParams;
                    }).collect(Collectors.toList());
                }
                this.saveBatch(esGoodsParamsList);
            }
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("新增商品参数失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable ae) {
            logger.error("新增商品参数失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), GoodsErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新数据
     *
     * @param draftGoodsParamsDTO DTO
     * @auther: wangaf 826988665@qq.com
     * @date: 2019-06-03
     * @return: com.shopx.common.model.result.DubboResult<EsDraftGoodsParamsDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult<EsDraftGoodsParamsDO> updateDraftGoodsParams(EsDraftGoodsParamsDTO draftGoodsParamsDTO,Long id) {
        try {
            EsDraftGoodsParams draftGoodsParams = new EsDraftGoodsParams();
            BeanUtil.copyProperties(draftGoodsParamsDTO, draftGoodsParams);
            QueryWrapper<EsDraftGoodsParams> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsDraftGoodsParams::getId, id);
            this.draftGoodsParamsMapper.update(draftGoodsParams, queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("草稿商品参数更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("草稿商品参数更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), GoodsErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据id获取详情
     *
     * @param id 主键id
     * @auther: wangaf 826988665@qq.com
     * @date: 2019-06-03
     * @return: com.shopx.common.model.result.DubboResult<EsDraftGoodsParamsDO>
     */
    @Override
    public DubboResult<EsDraftGoodsParamsDO> getDraftGoodsParams(Long id) {
        try {
            QueryWrapper<EsDraftGoodsParams> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsDraftGoodsParams::getId, id);
            EsDraftGoodsParams draftGoodsParams = this.draftGoodsParamsMapper.selectOne(queryWrapper);
            EsDraftGoodsParamsDO draftGoodsParamsDO = new EsDraftGoodsParamsDO();
            if (draftGoodsParams == null) {
                throw new ArgumentException(GoodsErrorCode.DATA_NOT_EXIST.getErrorCode(), GoodsErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(draftGoodsParams, draftGoodsParamsDO);
            return DubboResult.success(draftGoodsParamsDO);
        } catch (ArgumentException ae){
            logger.error("草稿商品参数查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("草稿商品参数查询失败", th);
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询列表
     *
     * @param draftGoodsParamsDTO DTO
     * @param pageSize     页数
     * @param pageNum      页码
     * @auther: wangaf 826988665@qq.com
     * @date: 2019-06-03
     * @return: com.shopx.common.model.result.DubboPageResult<EsDraftGoodsParamsDO>
     */
    @Override
    public DubboPageResult<EsDraftGoodsParamsDO> getDraftGoodsParamsList(EsDraftGoodsParamsDTO draftGoodsParamsDTO, int pageSize, int pageNum) {
        QueryWrapper<EsDraftGoodsParams> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件

            Page<EsDraftGoodsParams> page = new Page<>(pageNum, pageSize);
            IPage<EsDraftGoodsParams> iPage = this.page(page, queryWrapper);
            List<EsDraftGoodsParamsDO> draftGoodsParamsDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                draftGoodsParamsDOList = iPage.getRecords().stream().map(draftGoodsParams -> {
                    EsDraftGoodsParamsDO draftGoodsParamsDO = new EsDraftGoodsParamsDO();
                    BeanUtil.copyProperties(draftGoodsParams, draftGoodsParamsDO);
                    return draftGoodsParamsDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(iPage.getTotal(),draftGoodsParamsDOList);
        } catch (ArgumentException ae){
            logger.error("草稿商品参数分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("草稿商品参数分页查询失败", th);
            return DubboPageResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据主键删除数据
     *
     * @param id 主键id
     * @auther: wangaf 826988665@qq.com
     * @date: 2019-06-03
     * @return: com.shopx.common.model.result.DubboResult<EsDraftGoodsParamsDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult<EsDraftGoodsParamsDO> deleteDraftGoodsParams(Long id) {
        try {
            if (id == 0) {
                throw new ArgumentException(GoodsErrorCode.PARAM_ERROR.getErrorCode(), String.format("参数传入错误ID不能为空[%s]", id));
            }
            QueryWrapper<EsDraftGoodsParams> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.lambda().eq(EsDraftGoodsParams::getId, id);
            this.draftGoodsParamsMapper.delete(deleteWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae){
             logger.error("草稿商品参数删除失败", ae);
             TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
             return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("草稿商品参数删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), GoodsErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
}
