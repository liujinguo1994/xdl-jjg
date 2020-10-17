package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jjg.shop.model.constant.GoodsErrorCode;
import com.jjg.shop.model.domain.EsGoodsUserTokenDO;
import com.jjg.shop.model.dto.EsGoodsUserTokenDTO;
import com.xdl.jjg.entity.EsGoodsUserToken;
import com.xdl.jjg.mapper.EsGoodsUserTokenMapper;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.util.CollectionUtils;
import com.xdl.jjg.web.service.IEsGoodsUserTokenService;
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
 * @author WANGAF 826988665@qq.com
 * @since 2019-07-10 16:43:10
 */
@Service
public class EsGoodsUserTokenServiceImpl extends ServiceImpl<EsGoodsUserTokenMapper, EsGoodsUserToken> implements IEsGoodsUserTokenService {

    private static Logger logger = LoggerFactory.getLogger(EsGoodsUserTokenServiceImpl.class);

    @Autowired
    private EsGoodsUserTokenMapper goodsUserTokenMapper;

    /**
     * 插入数据
     *
     * @param goodsUserTokenDTO DTO
     * @auther: WAF 826988665@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsGoodsUserTokenDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertGoodsUserToken(EsGoodsUserTokenDTO goodsUserTokenDTO) {
        try {
            EsGoodsUserToken goodsUserToken = new EsGoodsUserToken();
            BeanUtil.copyProperties(goodsUserTokenDTO, goodsUserToken);
            this.goodsUserTokenMapper.insert(goodsUserToken);
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
     * @param goodsUserTokenDTO DTO
     * @auther: WAF 826988665@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsGoodsUserTokenDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateGoodsUserToken(EsGoodsUserTokenDTO goodsUserTokenDTO, Long id) {
        try {
            EsGoodsUserToken goodsUserToken = new EsGoodsUserToken();
            BeanUtil.copyProperties(goodsUserTokenDTO, goodsUserToken);
            QueryWrapper<EsGoodsUserToken> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsGoodsUserToken::getUserId, id);
            this.goodsUserTokenMapper.update(goodsUserToken, queryWrapper);
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
     * @auther: WAF 826988665@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsGoodsUserTokenDO>
     */
    @Override
    public DubboResult<EsGoodsUserTokenDO> getGoodsUserToken(EsGoodsUserTokenDTO goodsUserTokenDTO) {
        try {
            QueryWrapper<EsGoodsUserToken> queryWrapper = new QueryWrapper<>();
            if(goodsUserTokenDTO.getUserId() !=null){
                queryWrapper.lambda().eq(EsGoodsUserToken::getUserId, goodsUserTokenDTO.getUserId());
            }
            if(goodsUserTokenDTO.getToken() !=null){
                queryWrapper.lambda().eq(EsGoodsUserToken::getToken, goodsUserTokenDTO.getToken());
            }
            EsGoodsUserToken goodsUserToken = this.goodsUserTokenMapper.selectOne(queryWrapper);
            EsGoodsUserTokenDO goodsUserTokenDO = new EsGoodsUserTokenDO();
            if (goodsUserToken == null) {
                return DubboResult.success();
            }
            BeanUtil.copyProperties(goodsUserToken, goodsUserTokenDO);
            return DubboResult.success(goodsUserTokenDO);
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
     * @param goodsUserTokenDTO DTO
     * @param pageSize     页码
     * @param pageNum      页数
     * @auther: WAF 826988665@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsGoodsUserTokenDO>
     */
    @Override
    public DubboPageResult<EsGoodsUserTokenDO> getGoodsUserTokenList(EsGoodsUserTokenDTO goodsUserTokenDTO, int pageSize, int pageNum) {
        QueryWrapper<EsGoodsUserToken> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件

            Page<EsGoodsUserToken> page = new Page<>(pageNum, pageSize);
            IPage<EsGoodsUserToken> iPage = this.page(page, queryWrapper);
            List<EsGoodsUserTokenDO> goodsUserTokenDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                goodsUserTokenDOList = iPage.getRecords().stream().map(goodsUserToken -> {
                    EsGoodsUserTokenDO goodsUserTokenDO = new EsGoodsUserTokenDO();
                    BeanUtil.copyProperties(goodsUserToken, goodsUserTokenDO);
                    return goodsUserTokenDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(iPage.getTotal(),goodsUserTokenDOList);
        } catch (ArgumentException ae){
            logger.error("分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("分页查询失败", th);
            return DubboPageResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据主键删除数据
     *
     * @param id 主键id
     * @auther: WAF 826988665@qq.com
     * @date: 2019/05/31 16:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsGoodsUserTokenDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteGoodsUserToken(Long id) {
        try {
            QueryWrapper<EsGoodsUserToken> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.lambda().eq(EsGoodsUserToken::getUserId, id);
            this.goodsUserTokenMapper.delete(deleteWrapper);
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
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteGoodsUserToken(String token) {
        try {
            QueryWrapper<EsGoodsUserToken> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.lambda().eq(EsGoodsUserToken::getToken, token);
            this.goodsUserTokenMapper.delete(deleteWrapper);
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
