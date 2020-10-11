package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xdl.jjg.constant.ErrorCode;
import com.xdl.jjg.entity.EsLogiCompany;
import com.xdl.jjg.mapper.EsLogiCompanyMapper;
import com.xdl.jjg.model.domain.EsLogiCompanyDO;
import com.xdl.jjg.model.dto.EsLogiCompanyDTO;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.util.CollectionUtils;
import com.xdl.jjg.web.service.IEsLogiCompanyService;
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
 * @author rm 2817512105@qq.com
 * @since 2019-06-04
 */
@Service
public class EsLogiCompanyServiceImpl extends ServiceImpl<EsLogiCompanyMapper, EsLogiCompany> implements IEsLogiCompanyService {

    private static Logger logger = LoggerFactory.getLogger(EsLogiCompanyServiceImpl.class);

    @Autowired
    private EsLogiCompanyMapper logiCompanyMapper;

    @Autowired
    private IEsShopLogiRelService shopLogiRelService;

    /**
     * 插入数据
     *
     * @param logiCompanyDTO DTO
     * @auther: rm 2817512105@qq.com
     * @date: 2019-06-04
     * @return: com.shopx.common.model.result.DubboResult<EsLogiCompanyDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertLogiCompany(EsLogiCompanyDTO logiCompanyDTO) {
        try {
            if (logiCompanyDTO == null) {
                throw new ArgumentException(ErrorCode.PARAM_ERROR.getErrorCode(), ErrorCode.PARAM_ERROR.getErrorMsg());
            }
            //校验物流公司代码是否重复
            QueryWrapper<EsLogiCompany> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.lambda().eq(EsLogiCompany::getCode,logiCompanyDTO.getCode());
            EsLogiCompany code = logiCompanyMapper.selectOne(queryWrapper1);
            if(code != null){
                throw new ArgumentException(ErrorCode.LOGI_COMPANY_CODE_IS_EXIT.getErrorCode(), "物流公司代码重复");
            }
            //校验物流公司名称是否重复
            QueryWrapper<EsLogiCompany> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.lambda().eq(EsLogiCompany::getName,logiCompanyDTO.getName());
            EsLogiCompany name = logiCompanyMapper.selectOne(queryWrapper2);
            if(name != null){
                throw new ArgumentException(ErrorCode.LOGI_COMPANY_NAME_IS_EXIT.getErrorCode(), "物流公司名称重复");
            }
            //校验快递鸟公司代码是否重复
            QueryWrapper<EsLogiCompany> queryWrapper3 = new QueryWrapper<>();
            queryWrapper3.lambda().eq(EsLogiCompany::getKdcode,logiCompanyDTO.getKdcode());
            EsLogiCompany kdcode = logiCompanyMapper.selectOne(queryWrapper3);
            if(kdcode != null){
                throw new ArgumentException(ErrorCode.LOGI_COMPANY_KDCODE_IS_EXIT.getErrorCode(), "快递鸟公司代码重复");
            }
            EsLogiCompany logiCompany = new EsLogiCompany();
            BeanUtil.copyProperties(logiCompanyDTO, logiCompany);
            this.logiCompanyMapper.insert(logiCompany);
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
     * 根据条件更新数据
     *
     * @param logiCompanyDTO DTO
     * @auther: rm 2817512105@qq.com
     * @date: 2019-06-04
     * @return: com.shopx.common.model.result.DubboResult<EsLogiCompanyDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateLogiCompany(EsLogiCompanyDTO logiCompanyDTO) {
        try {
            if (logiCompanyDTO == null) {
                throw new ArgumentException(ErrorCode.PARAM_ERROR.getErrorCode(), ErrorCode.PARAM_ERROR.getErrorMsg());
            }
            EsLogiCompany esLogiCompany = logiCompanyMapper.selectById(logiCompanyDTO.getId());
            if (esLogiCompany == null){
                throw new ArgumentException(ErrorCode.LOGI_COMPANY_NOT_EXIT.getErrorCode(), "物流公司不存在");
            }

            //校验物流公司代码是否重复
            QueryWrapper<EsLogiCompany> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.lambda().eq(EsLogiCompany::getCode,logiCompanyDTO.getCode());
            EsLogiCompany code = logiCompanyMapper.selectOne(queryWrapper1);
            if(code != null && !code.getId().equals(logiCompanyDTO.getId())){
                throw new ArgumentException(ErrorCode.LOGI_COMPANY_CODE_IS_EXIT.getErrorCode(), "物流公司代码重复");
            }
            //校验物流公司名称是否重复
            QueryWrapper<EsLogiCompany> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.lambda().eq(EsLogiCompany::getName,logiCompanyDTO.getName());
            EsLogiCompany name = logiCompanyMapper.selectOne(queryWrapper2);
            if(name != null && !name.getId().equals(logiCompanyDTO.getId())){
                throw new ArgumentException(ErrorCode.LOGI_COMPANY_NAME_IS_EXIT.getErrorCode(), "物流公司名称重复");
            }
            //校验快递鸟公司代码是否重复
            QueryWrapper<EsLogiCompany> queryWrapper3 = new QueryWrapper<>();
            queryWrapper3.lambda().eq(EsLogiCompany::getKdcode,logiCompanyDTO.getKdcode());
            EsLogiCompany kdcode = logiCompanyMapper.selectOne(queryWrapper3);
            if(kdcode != null && !kdcode.getId().equals(logiCompanyDTO.getId())){
                throw new ArgumentException(ErrorCode.LOGI_COMPANY_KDCODE_IS_EXIT.getErrorCode(), "快递鸟公司代码重复");
            }

            //如果电子面单由支持变为不支持，需置空相关字段
            if (esLogiCompany.getIsWaybill() == 1 && logiCompanyDTO.getIsWaybill() == 0){
                logiCompanyDTO.setCustomerName("");
                logiCompanyDTO.setCustomerPwd("");
            }
            EsLogiCompany logiCompany = new EsLogiCompany();
            BeanUtil.copyProperties(logiCompanyDTO, logiCompany);
            QueryWrapper<EsLogiCompany> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsLogiCompany::getId, logiCompanyDTO.getId());
            this.logiCompanyMapper.update(logiCompany, queryWrapper);
            //删除卖家端物流公司
            if (logiCompanyDTO.getState() == 1){
                DubboResult result = shopLogiRelService.adminDeleteShopLogiRel(logiCompanyDTO.getId());
                if (!result.isSuccess()){
                    throw new ArgumentException(ErrorCode.DELETE_SHOP_LOGI_COMPANY_ERROR.getErrorCode(), "删除卖家端物流公司出错");
                }
            }
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
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
     * @return: com.shopx.common.model.result.DubboResult<EsLogiCompanyDO>
     */
    @Override
    public DubboResult<EsLogiCompanyDO> getLogiCompany(Long id) {
        try {
            QueryWrapper<EsLogiCompany> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsLogiCompany::getId, id);
            EsLogiCompany logiCompany = this.logiCompanyMapper.selectOne(queryWrapper);
            EsLogiCompanyDO logiCompanyDO = new EsLogiCompanyDO();
            if (logiCompany == null) {
                throw new ArgumentException(ErrorCode.DATA_NOT_EXIST.getErrorCode(), ErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(logiCompany, logiCompanyDO);
            return DubboResult.success(logiCompanyDO);
        } catch (ArgumentException ae){
            logger.error("查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("查询失败", th);
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }


    /**
     * 根据id获取详情
     *
     * @auther: rm 2817512105@qq.com
     * @date: 2019-06-04
     * @return: com.shopx.common.model.result.DubboResult<EsLogiCompanyDO>
     */
    @Override
    public DubboResult<EsLogiCompanyDO> getLogiByCode(String code) {
        try {
            QueryWrapper<EsLogiCompany> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsLogiCompany::getCode, code);
            EsLogiCompany logiCompany = this.logiCompanyMapper.selectOne(queryWrapper);
            EsLogiCompanyDO logiCompanyDO = new EsLogiCompanyDO();
            if (logiCompany == null) {
                throw new ArgumentException(ErrorCode.DATA_NOT_EXIST.getErrorCode(), ErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(logiCompany, logiCompanyDO);
            return DubboResult.success(logiCompanyDO);
        } catch (ArgumentException ae){
            logger.error("查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("查询失败", th);
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询列表
     *
     * @param logiCompanyDTO DTO
     * @param pageSize     页码
     * @param pageNum      页数
     * @auther: rm 2817512105@qq.com
     * @date: 2019-06-04
     * @return: com.shopx.common.model.result.DubboPageResult<EsLogiCompanyDO>
     */
    @Override
    public DubboPageResult<EsLogiCompanyDO> getLogiCompanyList(EsLogiCompanyDTO logiCompanyDTO, int pageSize, int pageNum) {
        QueryWrapper<EsLogiCompany> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件

            Page<EsLogiCompany> page = new Page<>(pageNum, pageSize);
            IPage<EsLogiCompany> iPage = this.page(page, queryWrapper);
            List<EsLogiCompanyDO> logiCompanyDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                logiCompanyDOList = iPage.getRecords().stream().map(logiCompany -> {
                    EsLogiCompanyDO logiCompanyDO = new EsLogiCompanyDO();
                    BeanUtil.copyProperties(logiCompany, logiCompanyDO);
                    return logiCompanyDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(iPage.getTotal(),logiCompanyDOList);
        } catch (ArgumentException ae){
            logger.error("分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
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
     * @return: com.shopx.common.model.result.DubboResult<EsLogiCompanyDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteLogiCompany(Long id) {
        try {
            if (id == 0) {
                throw new ArgumentException(ErrorCode.PARAM_ERROR.getErrorCode(), String.format("参数传入错误ID不能为空[%s]", id));
            }
            QueryWrapper<EsLogiCompany> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.lambda().eq(EsLogiCompany::getId, id);
            this.logiCompanyMapper.delete(deleteWrapper);
            //删除卖家端物流公司
            DubboResult result = shopLogiRelService.adminDeleteShopLogiRel(id);
            if (!result.isSuccess()){
                throw new ArgumentException(ErrorCode.DELETE_SHOP_LOGI_COMPANY_ERROR.getErrorCode(), "删除卖家端物流公司出错");
            }
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
    public DubboPageResult<EsLogiCompanyDO> getLogiCompanyList() {
        try {
            QueryWrapper<EsLogiCompany> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsLogiCompany::getState,0);
            List<EsLogiCompany> companyList = logiCompanyMapper.selectList(queryWrapper);
            List<EsLogiCompanyDO> logiCompanyDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(companyList)) {
                logiCompanyDOList = companyList.stream().map(logiCompany -> {
                    EsLogiCompanyDO logiCompanyDO = new EsLogiCompanyDO();
                    BeanUtil.copyProperties(logiCompany, logiCompanyDO);
                    return logiCompanyDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(logiCompanyDOList);
        } catch (ArgumentException ae){
            logger.error("查询物流公司列表失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("查询物流公司列表失败", th);
            return DubboPageResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    @Override
    public DubboResult<EsLogiCompanyDO> getByName(String name) {
        try{
        QueryWrapper<EsLogiCompany> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(EsLogiCompany::getState,0).eq(EsLogiCompany::getName,name);
        EsLogiCompany logiCompany = logiCompanyMapper.selectOne(queryWrapper);
        EsLogiCompanyDO logiCompanyDO = new EsLogiCompanyDO();
        if (logiCompany != null){
            BeanUtil.copyProperties(logiCompany, logiCompanyDO);
        }
        return DubboResult.success(logiCompanyDO);
        } catch (ArgumentException ae){
            logger.error("查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("查询失败", th);
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }
}
