package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shopx.common.exception.ArgumentException;
import com.shopx.common.model.result.DubboPageResult;
import com.shopx.common.model.result.DubboResult;
import com.shopx.common.util.BeanUtil;
import com.shopx.goods.api.model.domain.EsCategoryDO;
import com.shopx.goods.api.service.IEsCategoryService;
import com.shopx.member.api.constant.MemberErrorCode;
import com.shopx.member.api.model.domain.EsDiscountDO;
import com.shopx.member.api.model.domain.dto.EsDiscountDTO;
import com.shopx.member.api.service.IEsDiscountService;
import com.xdl.jjg.entity.EsCompany;
import com.xdl.jjg.entity.EsDiscount;
import  com.xdl.jjg.mapper.EsCompanyMapper;
import  com.xdl.jjg.mapper.EsDiscountMapper;
import com.shopx.trade.api.constant.TradeErrorCode;
import org.apache.dubbo.common.utils.Assert;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 公司折扣表 服务实现类
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2019-06-28
 */
@Service
public class EsDiscountServiceImpl extends ServiceImpl<EsDiscountMapper, EsDiscount> implements IEsDiscountService {

    private static Logger logger = LoggerFactory.getLogger(EsDiscountServiceImpl.class);

    @Reference(version = "${dubbo.application.version}" ,timeout = 50000,check = false)
    private IEsCategoryService iEsCategoryService;

    @Autowired
    private EsDiscountMapper discountMapper;

    @Autowired
    private EsCompanyMapper esCompanyMapper;


    /**
     * 插入公司折扣表数据
     *
     * @param discountDTO 公司折扣表DTO
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/06/28 16:30:30
     * @return: com.shopx.common.model.result.DubboResult<EsDiscountDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertDiscount(EsDiscountDTO discountDTO) {
        try {
            EsDiscount esDiscount = getDiscountByCompanyId(discountDTO.getCompanyId(), discountDTO.getCategoryId());

            if (esDiscount!=null){
                throw new ArgumentException(MemberErrorCode.EXIST_DISCOUNT.getErrorCode(), MemberErrorCode.EXIST_DISCOUNT.getErrorMsg());
            }
            EsDiscount discount = new EsDiscount();
            BeanUtil.copyProperties(discountDTO, discount);
            this.discountMapper.insert(discount);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("公司折扣表新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable ae) {
            logger.error("公司折扣表新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据分类id和公司id查询分类折扣
     * @param companyId
     * @param categoryId
     * @auther: yuanj 595831329@qq.com
     *  @date: 2019/06/28 15:27:16
     * @return EsShop
     */
    private EsDiscount getDiscountByCompanyId(Long companyId,Long categoryId) {
        Assert.notNull(companyId,"公司id不能为空");
        Assert.notNull(categoryId,"分类id不能为空");
        QueryWrapper<EsDiscount> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(EsDiscount::getCategoryId, categoryId).eq(EsDiscount::getCategoryId,companyId);
        EsDiscount esDiscount = this.discountMapper.selectOne(queryWrapper);
        return esDiscount;

    }

    /**
     * 根据分类id查询分类折扣
     * @param categoryId
     * @auther: yuanj 595831329@qq.com
     *  @date: 2019/06/28 15:30:16
     * @return EsShop
     */
    @Override
    public DubboResult<EsDiscountDO> getByCategoryId(Long categoryId) {
        try{
            Assert.notNull(categoryId,"分类id不能为空");
            QueryWrapper<EsDiscount> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsDiscount::getCategoryId, categoryId);
            EsDiscount esDiscount = this.discountMapper.selectOne(queryWrapper);
            if (esDiscount == null) {
                throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            EsDiscountDO discountDO = new EsDiscountDO();
            BeanUtil.copyProperties(discountDO,esDiscount);
            return DubboResult.success(esDiscount);
        } catch (ArgumentException ae){
            logger.error("公司折扣表查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("公司折扣表查询失败", th);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }

    }

    @Override
    public DubboResult updateByCategoryId(Long categoryId,String categoryName) {
        try {
            this.discountMapper.updateByCategory(categoryId,categoryName);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("公司折扣表删除失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("公司折扣表删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新公司折扣表数据
     *
     * @param discountDTO 公司折扣表DTO
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/06/28 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsDiscountDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateDiscount(EsDiscountDTO discountDTO) {
        try {
            if (discountDTO == null) {
                throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), MemberErrorCode.PARAM_ERROR.getErrorMsg());
            }
            EsDiscount esDiscount = this.discountMapper.selectById(discountDTO.getId());
            if (esDiscount==null){
                throw new ArgumentException(MemberErrorCode.NOTEXIST_DISCOUNT.getErrorCode(), MemberErrorCode.NOTEXIST_DISCOUNT.getErrorMsg());
            }
            EsDiscount discount = new EsDiscount();
            BeanUtil.copyProperties(discountDTO, discount);
            QueryWrapper<EsDiscount> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsDiscount::getId, discountDTO.getId());
            this.discountMapper.update(discount, queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("公司折扣表更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("公司折扣表更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据id获取公司折扣表详情
     *
     * @param id 主键id
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/06/28 17:17:16
     * @return: com.shopx.common.model.result.DubboResult<EsDiscountDO>
     */
    @Override
    public DubboResult<EsDiscountDO> getDiscount(Long id) {
        try {
            if (id==null){
                throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), MemberErrorCode.PARAM_ERROR.getErrorMsg());
            }
            EsDiscount discount = this.discountMapper.selectById(id);
            EsDiscountDO discountDO = new EsDiscountDO();
            if (discount == null) {
                throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(discount, discountDO);
            return DubboResult.success(discountDO);
        } catch (ArgumentException ae){
            logger.error("公司折扣表查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("公司折扣表查询失败", th);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    @Override
    public DubboPageResult<EsDiscountDO> getDiscountByCompanyId(Long id) {

        try{
            QueryWrapper<EsDiscount> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsDiscount::getCompanyId,id);
            List<EsDiscount> list = this.list(queryWrapper);

            List<EsDiscountDO> collect = list.stream().map(esDiscount -> {
                EsDiscountDO esDiscountDO = new EsDiscountDO();
                BeanUtils.copyProperties(esDiscount, esDiscountDO);
                return esDiscountDO;
            }).collect(Collectors.toList());
            return DubboPageResult.success(collect);
        } catch (ArgumentException ae) {
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Exception e) {
            logger.error("公司折扣查询失败",e);
            return DubboPageResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(),"系统错误");
        }
    }
    @Override
    public DubboResult<EsDiscountDO> getDiscountByCompanyCodeAndCategoryId(String companyCode,Long categoryId) {

        try{
            QueryWrapper<EsCompany> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.lambda().eq(EsCompany::getCompanyCode,companyCode);
            EsCompany esCompany = esCompanyMapper.selectOne(queryWrapper1);
            EsDiscountDO esDiscountDO = new EsDiscountDO();
            // 判断是否合作
            if (esCompany != null && esCompany.getState() == 0){
                QueryWrapper<EsDiscount> queryWrapper = new QueryWrapper<>();
                queryWrapper.lambda().eq(EsDiscount::getCompanyId,esCompany.getId()).eq(EsDiscount::getCategoryId,categoryId);
                EsDiscount esDiscount = this.discountMapper.selectOne(queryWrapper);
                if (esDiscount != null){
                    BeanUtils.copyProperties(esDiscount, esDiscountDO);
                }else {
                    while (esDiscount == null){
                        // 如果未找到当前分类的折扣则 递归查询父级分类
                        DubboResult<EsCategoryDO> category = iEsCategoryService.getCategory(categoryId);
                        if (!category.isSuccess()){
                            break;
                        }

                        EsCategoryDO esCategoryDO = category.getData();
                        DubboResult<EsDiscountDO> discount = this.getDiscountByCompanyCodeAndCategoryId(companyCode, esCategoryDO.getParentId());
                        if (discount.getData().getId()!= null){
                            BeanUtils.copyProperties(discount.getData(),esDiscountDO);
                            break;
                        }
                        return DubboResult.success(esDiscountDO);
                    }
                }
            }
            return DubboResult.success(esDiscountDO);
        } catch (ArgumentException ae) {
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Exception e) {
            logger.error("公司折扣查询失败",e);
            return DubboPageResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(),"系统错误");
        }
    }


    /**
     * 根据查询公司折扣表列表
     *
     * @param discountDTO 公司折扣表DTO
     * @param pageSize     页码
     * @param pageNum      页数
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/06/28 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsDiscountDO>
     */
    @Override
    public DubboPageResult<EsDiscountDO> getDiscountList(EsDiscountDTO discountDTO, int pageSize, int pageNum) {
        QueryWrapper<EsDiscount> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件
            queryWrapper.lambda().eq(EsDiscount::getCompanyId,discountDTO.getCompanyId());
            Page<EsDiscount> page = new Page<>(pageNum, pageSize);
            IPage<EsDiscount> iPage = this.page(page, queryWrapper);
            List<EsDiscountDO> discountDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                discountDOList = iPage.getRecords().stream().map(discount -> {
                    EsDiscountDO discountDO = new EsDiscountDO();
                    BeanUtil.copyProperties(discount, discountDO);
                    return discountDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(iPage.getTotal(),discountDOList);
        } catch (ArgumentException ae){
            logger.error("公司折扣表分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("公司折扣表分页查询失败", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据主键删除公司折扣表数据
     *
     * @param ids 主键ids
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/06/28 16:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsDiscountDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteDiscount(Integer [] ids) {
        try {
            QueryWrapper<EsDiscount> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.lambda().in(EsDiscount::getId, ids);
            this.discountMapper.delete(deleteWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae){
             logger.error("公司折扣表删除失败", ae);
             TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
             return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("公司折扣表删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
}
