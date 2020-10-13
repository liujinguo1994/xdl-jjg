package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shopx.common.exception.ArgumentException;
import com.shopx.common.model.result.DubboPageResult;
import com.shopx.common.model.result.DubboResult;
import com.shopx.common.util.BeanUtil;
import com.shopx.goods.api.constant.GoodsErrorCode;
import com.shopx.goods.api.model.domain.EsShopLogiRelDO;
import com.shopx.goods.api.service.IEsShopLogiRelService;
import com.shopx.goods.dao.entity.EsShopLogiRel;
import com.shopx.goods.dao.mapper.EsShopLogiRelMapper;
import com.shopx.system.api.constant.ErrorCode;
import com.shopx.system.api.model.domain.EsLogiCompanyDO;
import com.shopx.system.api.service.IEsLogiCompanyService;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author WAF 826988665@qq.com
 * @since 2019-07-17 14:59:14
 */
@Service(version = "${dubbo.application.version}", interfaceClass = IEsShopLogiRelService.class, timeout = 50000)
public class EsShopLogiRelServiceImpl extends ServiceImpl<EsShopLogiRelMapper, EsShopLogiRel> implements IEsShopLogiRelService {

    private static Logger logger = LoggerFactory.getLogger(EsShopLogiRelServiceImpl.class);

    @Autowired
    private EsShopLogiRelMapper shopLogiRelMapper;

    @Reference(version = "${dubbo.application.version}",timeout = 5000,check = false)
    private IEsLogiCompanyService esLogiCompanyService;
    /**
     * 插入数据
     *
     * @auther: WAF 826988665@qq.com
     * @date: 2019-07-17 14:59:14
     * @return: com.shopx.common.model.result.DubboResult<EsShopLogiRelDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertShopLogiRel(Long shopId,Long id) {
        try {
            EsShopLogiRel shopLogiRel = new EsShopLogiRel();
            shopLogiRel.setShopId(shopId);
            shopLogiRel.setLogiId(id);
            this.shopLogiRelMapper.insert(shopLogiRel);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable ae) {
            logger.error("新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
    /**
     * 根据主键删除数据
     *
     * @param id 主键id
     * @auther: WAF 826988665@qq.com
     * @date: 2019/05/31 16:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsShopLogiRelDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult adminDeleteShopLogiRel(Long id) {
        try {
            QueryWrapper<EsShopLogiRel> queryWrapper =  new QueryWrapper();
            queryWrapper.lambda().eq(EsShopLogiRel::getLogiId,id);
            this.shopLogiRelMapper.delete(queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae){
             logger.error("删除失败", ae);
             TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
             return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    public DubboResult sellerDeleteShopLogiRel(Long id, Long shopId) {
        try {
            QueryWrapper<EsShopLogiRel> queryWrapper =  new QueryWrapper();
            queryWrapper.lambda().eq(EsShopLogiRel::getLogiId,id).eq(EsShopLogiRel::getShopId,shopId);
            this.shopLogiRelMapper.delete(queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("删除失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    public DubboPageResult<EsShopLogiRelDO> getShopLogiRelList(Long shopId) {
       try{
           DubboPageResult<EsLogiCompanyDO> result =  esLogiCompanyService.getLogiCompanyList();
           if(!result.isSuccess() || result.getData()==null){
               throw new ArgumentException(GoodsErrorCode.DATA_NOT_EXIST.getErrorCode(), GoodsErrorCode.DATA_NOT_EXIST.getErrorMsg());
           }
            //获取系统物流公司
           List<EsLogiCompanyDO> logiCompanyDOList = result.getData().getList();
           List<EsShopLogiRelDO> companyDOList= BeanUtil.copyList(logiCompanyDOList,EsShopLogiRelDO.class);

           companyDOList.forEach(company->{
               QueryWrapper<EsShopLogiRel> queryWrapper = new QueryWrapper<>();
               queryWrapper.lambda().eq(EsShopLogiRel::getLogiId,company.getId())
               .eq(EsShopLogiRel::getShopId,shopId);
               List<EsShopLogiRel> logiRelList = this.list(queryWrapper);
               if(CollectionUtils.isNotEmpty(logiRelList)){
                   company.setShopId(shopId);
               }
           });
           return DubboPageResult.success(companyDOList);
       } catch (ArgumentException ae){
           logger.error("获取物流公司信息失败", ae);
           return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
       }  catch (Throwable th) {
           logger.error("获取物流公司信息失败", th);
           return DubboPageResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), ErrorCode.SYS_ERROR.getErrorMsg());
       }
    }
    @Override
    public DubboPageResult<EsShopLogiRelDO> getMyShopLogiRelList(Long shopId) {
        try{
            DubboPageResult<EsLogiCompanyDO> result =  esLogiCompanyService.getLogiCompanyList();
            if(!result.isSuccess() || result.getData()==null){
                throw new ArgumentException(GoodsErrorCode.DATA_NOT_EXIST.getErrorCode(), GoodsErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            //获取系统物流公司
            List<EsLogiCompanyDO> logiCompanyDOList = result.getData().getList();
            List<EsShopLogiRelDO> companyDOList= BeanUtil.copyList(logiCompanyDOList,EsShopLogiRelDO.class);

            companyDOList.stream().forEach(company->{
                QueryWrapper<EsShopLogiRel> queryWrapper = new QueryWrapper<>();
                queryWrapper.lambda().eq(EsShopLogiRel::getLogiId,company.getId())
                        .eq(EsShopLogiRel::getShopId,shopId);
                List<EsShopLogiRel> logiRelList = this.list(queryWrapper);
                if(CollectionUtils.isNotEmpty(logiRelList)){
                    company.setShopId(shopId);
                }
            });
            companyDOList = companyDOList.stream().filter(company -> company.getShopId()==shopId).collect(Collectors.toList());
            return DubboPageResult.success(companyDOList);
        } catch (ArgumentException ae){
            logger.error("获取物流公司信息失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("获取物流公司信息失败", th);
            return DubboPageResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
}
