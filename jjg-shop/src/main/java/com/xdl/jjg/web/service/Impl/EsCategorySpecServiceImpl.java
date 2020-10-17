package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jjg.shop.model.constant.GoodsErrorCode;
import com.jjg.shop.model.domain.EsCategorySpecDO;
import com.jjg.shop.model.dto.EsCategorySpecDTO;
import com.xdl.jjg.entity.EsCategorySpec;
import com.xdl.jjg.mapper.EsCategorySpecMapper;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.web.service.IEsCategorySpecService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wangaf 826988665@qq.com
 * @since 2019-06-03
 */
@Service
public class EsCategorySpecServiceImpl extends ServiceImpl<EsCategorySpecMapper, EsCategorySpec> implements IEsCategorySpecService {

    private static Logger logger = LoggerFactory.getLogger(EsCategorySpecServiceImpl.class);

    @Autowired
    private EsCategorySpecMapper categorySpecMapper;

    /**
     * 插入数据
     *
     * @param categorySpecDTO DTO
     * @auther: wangaf 826988665@qq.com
     * @date: 2019-06-03
     * @return: com.shopx.common.model.result.DubboResult<EsCategorySpecDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult<EsCategorySpecDO> insertCategorySpec(EsCategorySpecDTO categorySpecDTO) {
        try {
            EsCategorySpec categorySpec = new EsCategorySpec();
            BeanUtil.copyProperties(categorySpecDTO, categorySpec);
            this.categorySpecMapper.insert(categorySpec);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable ae) {
            logger.error("失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), GoodsErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
}
