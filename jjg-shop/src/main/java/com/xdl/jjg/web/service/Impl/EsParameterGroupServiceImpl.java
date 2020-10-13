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
import com.shopx.goods.api.model.domain.EsParameterGroupDO;
import com.shopx.goods.api.model.domain.EsParametersDO;
import com.shopx.goods.api.model.domain.ParameterGroupDO;
import com.shopx.goods.api.model.domain.dto.EsParameterGroupDTO;
import com.shopx.goods.api.model.domain.dto.EsParametersDTO;
import com.shopx.goods.api.service.IEsParameterGroupService;
import com.shopx.goods.api.service.IEsParametersService;
import com.shopx.goods.dao.entity.EsCategory;
import com.shopx.goods.dao.entity.EsParameterGroup;
import com.shopx.goods.dao.mapper.EsCategoryMapper;
import com.shopx.goods.dao.mapper.EsParameterGroupMapper;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
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
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wangaf 826988665@qq.com
 * @since 2019-06-03
 */
@Service(version = "${dubbo.application.version}", interfaceClass = IEsParameterGroupService.class, timeout = 50000)
public class EsParameterGroupServiceImpl extends ServiceImpl<EsParameterGroupMapper, EsParameterGroup> implements IEsParameterGroupService {

    private static Logger logger = LoggerFactory.getLogger(EsParameterGroupServiceImpl.class);

    @Autowired
    private EsParameterGroupMapper parameterGroupMapper;
    @Autowired
    private EsCategoryMapper esCategoryMapper;
    @Autowired
    private IEsParametersService esParametersService;

    /**
     * 插入数据
     *
     * @param parameterGroupDTO DTO
     * @auther: wangaf 826988665@qq.com
     * @date: 2019-06-03
     * @return: com.shopx.common.model.result.DubboResult<EsParameterGroupDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult<EsParameterGroupDO> insertParameterGroup(EsParameterGroupDTO parameterGroupDTO) {
        try {
            Long categoryId = parameterGroupDTO.getCategoryId() == null ? -1 : parameterGroupDTO.getCategoryId();
            EsCategory esCategory =esCategoryMapper.selectById(categoryId);
            if(esCategory == null){
                throw new ArgumentException(GoodsErrorCode.DATA_NOT_EXIST.getErrorCode(), "商品分类不存在");
            }
            QueryWrapper<EsParameterGroup> queryWrapper =  new QueryWrapper<>();
            queryWrapper.lambda().eq(EsParameterGroup::getCategoryId,parameterGroupDTO.getCategoryId());
            queryWrapper.lambda().orderByDesc(EsParameterGroup::getSort);
            queryWrapper.last(" limit 0,1");
            EsParameterGroup  groupTemp = this.parameterGroupMapper.selectOne(queryWrapper);
            if(groupTemp == null){
                parameterGroupDTO.setSort(1);
            }else{
                if(StringUtils.equals( parameterGroupDTO.getGroupName(),groupTemp.getGroupName())){
                    throw new ArgumentException(GoodsErrorCode.PARAMETER_EXIST.getErrorCode(),GoodsErrorCode.PARAMETER_EXIST.getErrorMsg());
                }
                parameterGroupDTO.setSort(groupTemp.getSort()+1);
            }
            EsParameterGroup esParameterGroup =  new EsParameterGroup();
            BeanUtil.copyProperties(parameterGroupDTO, esParameterGroup);
            this.parameterGroupMapper.insert(esParameterGroup);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("新增参数组失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable ae) {
            logger.error("新增参数组失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), GoodsErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新数据
     *
     * @auther: wangaf 826988665@qq.com
     * @date: 2019-06-03
     * @return: com.shopx.common.model.result.DubboResult<EsParameterGroupDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult<EsParameterGroupDO> updateParameterGroup(EsParameterGroupDTO  esParameterGroupDTO,Long id) {
        try {
            EsParameterGroup group = this.getById(id);
            if(group == null){
                throw new ArgumentException(GoodsErrorCode.DATA_NOT_EXIST.getErrorCode(),"参数组不存在");
            }
            BeanUtil.copyProperties(esParameterGroupDTO,group);
            group.setId(id);
            this.parameterGroupMapper.updateById(group);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("更新参数组失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("更新参数组失败", th);
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
     * @return: com.shopx.common.model.result.DubboResult<EsParameterGroupDO>
     */
    @Override
    public DubboResult<EsParameterGroupDO> getParameterGroup(Long id) {
        try {
            QueryWrapper<EsParameterGroup> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsParameterGroup::getId, id);
            EsParameterGroup parameterGroup = this.parameterGroupMapper.selectOne(queryWrapper);
            EsParameterGroupDO parameterGroupDO = new EsParameterGroupDO();
            if (parameterGroup == null) {
                throw new ArgumentException(GoodsErrorCode.DATA_NOT_EXIST.getErrorCode(), GoodsErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(parameterGroup, parameterGroupDO);
            return DubboResult.success(parameterGroupDO);
        } catch (ArgumentException ae){
            logger.error("获取参数组信息失败", ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("获取参数组信息失败", th);
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询列表
     *
     * @param parameterGroupDTO DTO
     * @param pageSize     页数
     * @param pageNum      页码
     * @auther: wangaf 826988665@qq.com
     * @date: 2019-06-03
     * @return: com.shopx.common.model.result.DubboPageResult<EsParameterGroupDO>
     */
    @Override
    public DubboPageResult<EsParameterGroupDO> getParameterGroupList(EsParameterGroupDTO parameterGroupDTO, int pageSize, int pageNum) {
        QueryWrapper<EsParameterGroup> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件
            Page<EsParameterGroup> page = new Page<>(pageNum, pageSize);
            IPage<EsParameterGroup> iPage = this.page(page, queryWrapper);
            List<EsParameterGroupDO> parameterGroupDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                parameterGroupDOList = iPage.getRecords().stream().map(parameterGroup -> {
                    EsParameterGroupDO parameterGroupDO = new EsParameterGroupDO();
                    BeanUtil.copyProperties(parameterGroup, parameterGroupDO);
                    return parameterGroupDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(iPage.getTotal(), parameterGroupDOList);
        } catch (ArgumentException ae){
            logger.error("分页查询参数组失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("分页查询参数组失败", th);
            return DubboPageResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据主键删除数据
     *
     * @param id 主键id
     * @auther: wangaf 826988665@qq.com
     * @date: 2019-06-03
     * @return: com.shopx.common.model.result.DubboResult<EsParameterGroupDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult<EsParameterGroupDO> deleteParameterGroup(Long id) {
        try {
            QueryWrapper<EsParameterGroup> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.lambda().eq(EsParameterGroup::getId, id);
            this.parameterGroupMapper.delete(deleteWrapper);
            this.esParametersService.deleteByGroupIdParameters(id);
            return DubboResult.success();
        } catch (ArgumentException ae){
             logger.error("参数组删除失败", ae);
             TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
             return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("参数组删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), GoodsErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     *
     * @param groupId 参数组ID
     * @param sortType 移动类型
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult<EsParameterGroupDO> sortParameterGroup(Long groupId, String sortType) {
        try {
            EsParameterGroup curGroup = this.parameterGroupMapper.selectById(groupId);
            if(curGroup == null ){
                throw new ArgumentException(GoodsErrorCode.DATA_NOT_EXIST.getErrorCode(),"参数组不存在");
            }
            QueryWrapper<EsParameterGroup> queryWrapper = new QueryWrapper<>();
            if(StringUtils.equals(sortType,"up")){
                //小于
                queryWrapper.lambda().lt(EsParameterGroup::getSort,curGroup.getSort()).
                        eq(EsParameterGroup::getCategoryId,curGroup.getCategoryId()).orderByDesc(EsParameterGroup::getSort);
                queryWrapper.last(" limit 0,1");
            }else if(StringUtils.equals(sortType,"down")){
                //大于
                queryWrapper.lambda().gt(EsParameterGroup::getSort,curGroup.getSort()).
                        eq(EsParameterGroup::getCategoryId,curGroup.getCategoryId()).orderByAsc(EsParameterGroup::getSort);
                queryWrapper.last(" limit 0,1");
            }
            EsParameterGroup changeGroup =  this.parameterGroupMapper.selectOne(queryWrapper);
            if(changeGroup == null ){
                throw new ArgumentException(GoodsErrorCode.DATA_NOT_EXIST.getErrorCode(),"参数组不存在");
            }
            EsParameterGroup group = new EsParameterGroup();
            group.setSort(changeGroup.getSort());
            group.setId(curGroup.getId());
            EsParameterGroup group1 = new EsParameterGroup();
            group1.setSort(curGroup.getSort());
            group1.setId(changeGroup.getId());
            this.parameterGroupMapper.updateById(group);
            this.parameterGroupMapper.updateById(group1);
            return DubboResult.success();
        }catch (ArgumentException ae){
            logger.error("参数组移动失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable th){
            logger.error("参数组移动失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), GoodsErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 查询某个分类下面的参数组 参数
     * @param categoryId 商品分类ID
     * @return
     */
    @Override
    public DubboPageResult<ParameterGroupDO> getParameterGroupList(Long categoryId) {
        try{
            //获取商品参数组
            QueryWrapper<EsParameterGroup> queryWrapper =  new QueryWrapper<>();
            queryWrapper.lambda().eq(EsParameterGroup::getCategoryId,categoryId).orderByAsc(EsParameterGroup::getSort);
            List<EsParameterGroup> esParameterGroupList = this.list(queryWrapper);
            //获取商品参数
            EsParametersDTO esParametersDTO = new EsParametersDTO();
            esParametersDTO.setCategoryId(categoryId);
            DubboPageResult<EsParametersDO> esParametersDOList = this.esParametersService.getParametersList(esParametersDTO);
            if(!esParametersDOList.isSuccess()){
                throw new ArgumentException(GoodsErrorCode.DATA_NOT_EXIST.getErrorCode(),"商品参数不存在");
            }
            Map<Long,List<EsParametersDO>> paramMap =  new HashedMap();
            for(EsParametersDO parametersDO :esParametersDOList.getData().getList()){
                parametersDO.setOptionList(parametersDO.getOptions().replaceAll("\r|\n", "").split(","));
                List<EsParametersDO> parametersDOList =paramMap.get(parametersDO.getGroupId());
                if(parametersDOList == null ){
                    parametersDOList = new ArrayList<>();
                }
                parametersDOList.add(parametersDO);
                paramMap.put(parametersDO.getGroupId(),parametersDOList);
            }
            List<ParameterGroupDO> parameterGroupDOList = esParameterGroupList.stream().map(esParameterGroup -> {
                ParameterGroupDO groupDO  =  new ParameterGroupDO();
                groupDO.setGroupId(esParameterGroup.getId());
                groupDO.setGroupName(esParameterGroup.getGroupName());
                groupDO.setParams(paramMap.get(esParameterGroup.getId()) == null ? new ArrayList<>() : paramMap.get(esParameterGroup.getId()));
                return groupDO;
            }).collect(Collectors.toList());
            return DubboPageResult.success(parameterGroupDOList);
        }catch (ArgumentException ae){
            logger.error("商品参数组查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable th){
            logger.error("商品参数组查询失败", th);
            return DubboPageResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), GoodsErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    public DubboPageResult<EsParameterGroupDO> getBuyerParameterGroup(Long categoryId) {
        try{
                QueryWrapper<EsParameterGroup> queryWrapper = new QueryWrapper<>();
                queryWrapper.lambda().eq(EsParameterGroup::getCategoryId,categoryId);
                List<EsParameterGroup> parameterGroupList = this.list(queryWrapper);
                List<EsParameterGroupDO> parameterGroupDOList = BeanUtil.copyList(parameterGroupList,EsParameterGroupDO.class);
                return DubboPageResult.success(parameterGroupDOList);
            }catch (ArgumentException ae){
                logger.error("商品参数组查询失败", ae);
                return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
            }catch (Throwable th){
                logger.error("商品参数组查询失败", th);
                return DubboPageResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), GoodsErrorCode.SYS_ERROR.getErrorMsg());
            }

    }
}
