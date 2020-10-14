package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jjg.member.model.domain.EsCompanyDO;
import com.jjg.member.model.dto.EsCompanyDTO;
import com.xdl.jjg.constant.MemberErrorCode;
import com.xdl.jjg.entity.EsCompany;
import com.xdl.jjg.mapper.EsCompanyMapper;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.util.CollectionUtils;
import com.xdl.jjg.web.service.IEsCompanyService;
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
 * 签约公司 服务实现类
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2019/06/26
 */
@Service
public class EsCompanyServiceImpl extends ServiceImpl<EsCompanyMapper, EsCompany> implements IEsCompanyService {

    private static Logger logger = LoggerFactory.getLogger(EsCompanyServiceImpl.class);

    @Autowired
    private EsCompanyMapper companyMapper;

    /**
     * 插入签约公司数据
     *
     * @param companyDTO 签约公司DTO
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/06/26 10:01:30
     * @return: com.shopx.common.model.result.DubboResult<EsCompanyDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertCompany(EsCompanyDTO companyDTO) {
        try {
            if (companyDTO == null) {
                throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), MemberErrorCode.PARAM_ERROR.getErrorMsg());
            }
            DubboResult<EsCompanyDO> companyByCode = this.getCompanyByCode(companyDTO.getCompanyCode());
            if(companyByCode.isSuccess() || companyByCode.getData() != null){
                throw new ArgumentException(MemberErrorCode.EXIST_COMPNAY.getErrorCode(), MemberErrorCode.EXIST_COMPNAY.getErrorMsg());
            }
//            EsCompany companyByName = this.getCompanyByName(companyDTO.getCompanyName());
//            if (companyByName !=null){
//                throw new ArgumentException(MemberErrorCode.EXIST_COMPNAY.getErrorCode(), MemberErrorCode.EXIST_COMPNAY.getErrorMsg());
//            }
            EsCompany company = new EsCompany();
            BeanUtil.copyProperties(companyDTO, company);
            //isDel = 0：正常
            company.setIsDel(0);
            this.companyMapper.insert(company);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("签约公司新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable ae) {
            logger.error("签约公司新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }


    /**
     * 根据公司名称查询公司
     * @param companyName
     * @auther: yuanj 595831329@qq.com
     *  @date: 2019/06/26 10:05:16
     * @return EsShop
     */
    private EsCompany getCompanyByName(String companyName) {
        if (companyName==null || companyName ==""){
            throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), MemberErrorCode.PARAM_ERROR.getErrorMsg());
        }
        QueryWrapper<EsCompany> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(EsCompany::getCompanyName, companyName);
        EsCompany company = this.companyMapper.selectOne(queryWrapper);
        return company;

    }


    /**
     * 根据标志符查询公司
     * @param companyCode
     * @auther: yuanj 595831329@qq.com
     *  @date: 2019/06/26 10:05:16
     * @return EsShop
     */
    @Override
    public DubboResult<EsCompanyDO> getCompanyByCode(String companyCode) {
        try{
            if (companyCode==null || companyCode ==""){
                throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), MemberErrorCode.PARAM_ERROR.getErrorMsg());
            }
            QueryWrapper<EsCompany> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsCompany::getCompanyCode, companyCode);
            EsCompany company = this.companyMapper.selectOne(queryWrapper);
            if (company ==null){
                throw new ArgumentException(MemberErrorCode.COMPANY_CODE_NOT_EXIST.getErrorCode(), MemberErrorCode.COMPANY_CODE_NOT_EXIST.getErrorMsg());
            }
            EsCompanyDO esCompanyDO=new EsCompanyDO();
            BeanUtil.copyProperties(company, esCompanyDO);
            return DubboResult.success(esCompanyDO);
        } catch (ArgumentException ae){
            logger.error("签约公司查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("签约公司查询失败", th);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }

    }

    /**
     * 根据条件更新签约公司数据
     *
     * @param companyDTO 签约公司DTO
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/06/26 10:20:30
     * @return: com.shopx.common.model.result.DubboResult<EsCompanyDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateCompany(EsCompanyDTO companyDTO) {
        try {
            if (companyDTO == null) {
                throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), MemberErrorCode.PARAM_ERROR.getErrorMsg());
            }
            EsCompany esCompany = this.companyMapper.selectById(companyDTO.getId());
            if (esCompany ==null){
                throw new ArgumentException(MemberErrorCode.NOTEXIST_COMPANY.getErrorCode(), MemberErrorCode.NOTEXIST_COMPANY.getErrorMsg());
            }
            EsCompany company = new EsCompany();
            BeanUtil.copyProperties(companyDTO, company);
            QueryWrapper<EsCompany> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsCompany::getId, companyDTO.getId());
            this.companyMapper.update(company, queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("签约公司更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("签约公司更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据id获取签约公司详情
     *
     * @param id 主键id
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/06/26 10:40:35
     * @return: com.shopx.common.model.result.DubboResult<EsCompanyDO>
     */
    @Override
    public DubboResult<EsCompanyDO> getCompany(Long id) {
        try {
            QueryWrapper<EsCompany> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsCompany::getId, id);
            EsCompany company = this.companyMapper.selectOne(queryWrapper);
            EsCompanyDO companyDO = new EsCompanyDO();
            if (company == null) {
                throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(company, companyDO);
            return DubboResult.success(companyDO);
        } catch (ArgumentException ae){
            logger.error("签约公司查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("签约公司查询失败", th);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询签约公司列表
     *
     * @param companyDTO 签约公司DTO
     * @param pageSize     页码
     * @param pageNum      页数
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/06/26 11:08:09
     * @return: com.shopx.common.model.result.DubboPageResult<EsCompanyDO>
     */
    @Override
    public DubboPageResult<EsCompanyDO> getCompanyList(EsCompanyDTO companyDTO, int pageSize, int pageNum) {
        QueryWrapper<EsCompany> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件
            Page<EsCompany> page = new Page<>(pageNum, pageSize);
            IPage<EsCompany> iPage = this.page(page, queryWrapper);
            List<EsCompanyDO> companyDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                companyDOList = iPage.getRecords().stream().map(company -> {
                    EsCompanyDO companyDO = new EsCompanyDO();
                    BeanUtil.copyProperties(company, companyDO);
                    return companyDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(iPage.getTotal(),companyDOList);
        } catch (ArgumentException ae){
            logger.error("签约公司分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("签约公司分页查询失败", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据主键删除签约公司数据
     *
     * @param ids 主键id
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/06/26 11:11:20
     * @return: com.shopx.common.model.result.DubboResult<EsCompanyDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteCompany(Integer [] ids) {
        try {
            QueryWrapper<EsCompany> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.lambda().in(EsCompany::getId, ids);
            this.companyMapper.delete(deleteWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae){
             logger.error("签约公司删除失败", ae);
             TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
             return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("签约公司删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
}
