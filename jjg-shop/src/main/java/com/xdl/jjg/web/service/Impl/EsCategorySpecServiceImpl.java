package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shopx.common.exception.ArgumentException;
import com.shopx.common.model.result.DubboResult;
import com.shopx.common.util.BeanUtil;
import com.shopx.goods.api.constant.GoodsErrorCode;
import com.shopx.goods.api.model.domain.EsCategorySpecDO;
import com.shopx.goods.api.model.domain.dto.EsCategorySpecDTO;
import com.shopx.goods.api.service.IEsCategorySpecService;
import com.shopx.goods.dao.entity.EsCategorySpec;
import com.shopx.goods.dao.mapper.EsCategorySpecMapper;
import org.apache.dubbo.config.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
@Service(version = "${dubbo.application.version}", interfaceClass = IEsCategorySpecService.class, timeout = 50000)
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
