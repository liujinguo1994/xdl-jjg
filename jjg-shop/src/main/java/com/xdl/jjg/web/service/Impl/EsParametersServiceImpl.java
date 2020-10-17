package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jjg.shop.model.constant.GoodsErrorCode;
import com.jjg.shop.model.domain.EsParametersDO;
import com.jjg.shop.model.dto.EsParametersDTO;
import com.xdl.jjg.entity.EsParameterGroup;
import com.xdl.jjg.entity.EsParameters;
import com.xdl.jjg.mapper.EsParameterGroupMapper;
import com.xdl.jjg.mapper.EsParametersMapper;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.util.CollectionUtils;
import com.xdl.jjg.web.service.IEsParametersService;
import org.apache.commons.lang3.StringUtils;
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
 * @author wangaf 826988665@qq.com
 * @since 2019-06-03
 */
@Service
public class EsParametersServiceImpl extends ServiceImpl<EsParametersMapper, EsParameters> implements IEsParametersService {

    private static Logger logger = LoggerFactory.getLogger(EsParametersServiceImpl.class);

    @Autowired
    private EsParametersMapper parametersMapper;
    @Autowired
    private EsParameterGroupMapper parameterGroupMapper;

    /**
     * 插入数据
     *
     * @param parametersDTO DTO
     * @auther: wangaf 826988665@qq.com
     * @date: 2019-06-03
     * @return: com.shopx.common.model.result.DubboResult<EsParametersDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult<EsParametersDO> insertParameters(EsParametersDTO parametersDTO) {
        try {
            Long parameGroupId = parametersDTO.getGroupId() == null ? -1 : parametersDTO.getGroupId();
            EsParameterGroup esParameterGroup = parameterGroupMapper.selectById(parameGroupId);
            if(esParameterGroup == null){
                throw new ArgumentException(GoodsErrorCode.DATA_NOT_EXIST.getErrorCode(), "参数组不存在");
            }
            //如果是选择项
            if(parametersDTO.getParamType() == 2 && StringUtils.isBlank(parametersDTO.getOptions())){
                throw new ArgumentException(GoodsErrorCode.DATA_NOT_EXIST.getErrorCode(), "选择项类型必须填写选择内容");
            }
            QueryWrapper<EsParameters> queryWrapper =  new QueryWrapper<>();
            queryWrapper.lambda().eq(EsParameters::getGroupId,parametersDTO.getGroupId());
            queryWrapper.lambda().orderByDesc(EsParameters::getSort);
            queryWrapper.last("limit 0,1");
            EsParameters  par = this.parametersMapper.selectOne(queryWrapper);
            if(par == null){
                parametersDTO.setSort(1);
            }else{
                parametersDTO.setSort(par.getSort()+1);
            }
            EsParameters parameters = new EsParameters();
            BeanUtil.copyProperties(parametersDTO, parameters);
            this.parametersMapper.insert(parameters);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("新增参数失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable ae) {
            logger.error("新增参数失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), GoodsErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新数据
     *
     * @param parametersDTO DTO
     * @auther: wangaf 826988665@qq.com
     * @date: 2019-06-03
     * @return: com.shopx.common.model.result.DubboResult<EsParametersDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult<EsParametersDO> updateParameters(EsParametersDTO parametersDTO,Long id) {
        try {
            EsParameters esParameters = this.parametersMapper.selectById(id);
            if(esParameters == null){
                throw new ArgumentException(GoodsErrorCode.DATA_NOT_EXIST.getErrorCode(),"商品参数不存在");
            }
            parametersDTO.setCategoryId(esParameters.getCategoryId());
            //如果是选择项
            if(parametersDTO.getParamType() == 2 && StringUtils.isBlank(parametersDTO.getOptions())){
                throw new ArgumentException(GoodsErrorCode.DATA_NOT_EXIST.getErrorCode(), "选择项类型必须填写选择内容");
            }
            BeanUtil.copyProperties(parametersDTO,esParameters);
            this.parametersMapper.updateById(esParameters);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("商品参数详情更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("商品参数详情更新失败", th);
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
     * @return: com.shopx.common.model.result.DubboResult<EsParametersDO>
     */
    @Override
    public DubboResult<EsParametersDO> getParameters(Long id) {
        try {
            QueryWrapper<EsParameters> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsParameters::getId,id);
            EsParameters parameters = this.parametersMapper.selectOne(queryWrapper);
            EsParametersDO parametersDO = new EsParametersDO();
            if (parameters == null) {
                throw new ArgumentException(GoodsErrorCode.DATA_NOT_EXIST.getErrorCode(), GoodsErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(parameters, parametersDO);
            return DubboResult.success(parametersDO);
        } catch (ArgumentException ae){
            logger.error("获取商品参数详情失败", ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("获取商品参数详情失败", th);
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询列表
     *
     * @param parametersDTO DTO
     * @param pageSize     页数
     * @param pageNum      页码
     * @auther: wangaf 826988665@qq.com
     * @date: 2019-06-03
     * @return: com.shopx.common.model.result.DubboPageResult<EsParametersDO>
     */
    @Override
    public DubboPageResult<EsParametersDO> getParametersList(EsParametersDTO parametersDTO, int pageSize, int pageNum) {
        QueryWrapper<EsParameters> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件

            Page<EsParameters> page = new Page<>(pageNum, pageSize);
            IPage<EsParameters> iPage = this.page(page, queryWrapper);
            List<EsParametersDO> parametersDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                parametersDOList = iPage.getRecords().stream().map(parameters -> {
                    EsParametersDO parametersDO = new EsParametersDO();
                    BeanUtil.copyProperties(parameters, parametersDO);
                    return parametersDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(iPage.getTotal(),parametersDOList);
        } catch (ArgumentException ae){
            logger.error("分页获取商品参数详情失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("分页获取商品参数详情失败", th);
            return DubboPageResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据主键删除数据
     *
     * @param id 主键id
     * @auther: wangaf 826988665@qq.com
     * @date: 2019-06-03
     * @return: com.shopx.common.model.result.DubboResult<EsParametersDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult<EsParametersDO> deleteParameters(Long id) {
        try {
            this.parametersMapper.deleteById(id);
            return DubboResult.success();
        } catch (ArgumentException ae){
             logger.error("商品参数删除失败", ae);
             TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
             return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("商品参数删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), GoodsErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据商品参数组删除删除
     * @param groupId    参数组ID
     * @return
     */
    @Override
    public DubboResult<EsParametersDO> deleteByGroupIdParameters(Long groupId) {
        try {
            QueryWrapper<EsParameters> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsParameters::getGroupId,groupId);
            this.parametersMapper.delete(queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("商品参数删除失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("商品参数删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), GoodsErrorCode.SYS_ERROR.getErrorMsg());
        }

    }
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult<EsParametersDO> sortParameters(Long id, String sortType) {
        try {
            EsParameters curParam = this.parametersMapper.selectById(id);
            if(curParam == null ){
                throw new ArgumentException(GoodsErrorCode.DATA_NOT_EXIST.getErrorCode(),"参数不存在");
            }
            QueryWrapper<EsParameters> queryWrapper = new QueryWrapper<>();
            if("up".equals(sortType)){
                //小于
                queryWrapper.lambda().lt(EsParameters::getSort,curParam.getSort()).
                        eq(EsParameters::getCategoryId,curParam.getCategoryId()).orderByDesc(EsParameters::getSort);
                queryWrapper.last("limit 0,1");
            }else if("down".equals(sortType)){
                //大于
                queryWrapper.lambda().gt(EsParameters::getSort,curParam.getSort()).
                        eq(EsParameters::getCategoryId,curParam.getCategoryId()).orderByAsc(EsParameters::getSort);
                queryWrapper.last("limit 0,1");
            }
            EsParameters changeParam =  this.parametersMapper.selectOne(queryWrapper);
            if(changeParam == null ){
                throw new ArgumentException(GoodsErrorCode.DATA_NOT_EXIST.getErrorCode(),"参数不存在");
            }
            EsParameters group = new EsParameters();
            group.setSort(changeParam.getSort());
            group.setId(curParam.getId());
            EsParameters group1 = new EsParameters();
            group1.setSort(curParam.getSort());
            group1.setId(changeParam.getId());
            this.parametersMapper.updateById(group);
            this.parametersMapper.updateById(group1);
            return DubboResult.success();
        }catch (ArgumentException ae){
            logger.error("商品参数移动失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable th){
            logger.error("商品参数移动失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), GoodsErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    public DubboPageResult<EsParametersDO> getParametersList(EsParametersDTO esParametersDTO) {
        try{
            QueryWrapper<EsParameters> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsParameters::getCategoryId,esParametersDTO.getCategoryId()).orderByAsc(EsParameters::getSort);
            List<EsParameters> esParametersList =  this.list(queryWrapper);
            List<EsParametersDO> esParametersDOList = esParametersList.stream().map(esParameters -> {
                EsParametersDO esParametersDO = new EsParametersDO();
                BeanUtil.copyProperties(esParameters,esParametersDO);
                return esParametersDO;
            }).collect(Collectors.toList());
            return DubboPageResult.success(esParametersDOList);
        }catch (ArgumentException ae){
            logger.error("商品参数查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable th){
            logger.error("商品参数查询失败", th);
            return DubboPageResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), GoodsErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
}
