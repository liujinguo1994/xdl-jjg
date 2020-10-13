package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xdl.jjg.constant.GoodsErrorCode;
import com.xdl.jjg.entity.EsGoodsParams;
import com.xdl.jjg.mapper.EsGoodsParamsMapper;
import com.xdl.jjg.model.domain.EsBuyerGoodsParamsDO;
import com.xdl.jjg.model.domain.EsBuyerParamsDO;
import com.xdl.jjg.model.domain.EsGoodsParamsDO;
import com.xdl.jjg.model.domain.EsParameterGroupDO;
import com.xdl.jjg.model.dto.EsGoodsParamsDTO;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.util.CollectionUtils;
import com.xdl.jjg.web.service.IEsGoodsParamsService;
import com.xdl.jjg.web.service.IEsParameterGroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.HashMap;
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
@Service
public class EsGoodsParamsServiceImpl extends ServiceImpl<EsGoodsParamsMapper, EsGoodsParams> implements IEsGoodsParamsService {

    private static Logger logger = LoggerFactory.getLogger(EsGoodsParamsServiceImpl.class);

    @Autowired
    private EsGoodsParamsMapper goodsParamsMapper;
    @Autowired
    private IEsParameterGroupService esParameterGroupService;

    /**
     * 插入数据
     *
     * @param goodsParamsDTO DTO
     * @auther: wangaf 826988665@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsGoodsParamsDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult<EsGoodsParamsDO> insertGoodsParams(EsGoodsParamsDTO goodsParamsDTO) {
        try {
            EsGoodsParams goodsParams = new EsGoodsParams();
            BeanUtil.copyProperties(goodsParamsDTO, goodsParams);
            this.goodsParamsMapper.insert(goodsParams);
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

    /**
     *
     * @param esGoodsParamsDTOList
     * @param goodsId
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public DubboResult<EsGoodsParamsDO> insertGoodsParams(List<EsGoodsParamsDTO> esGoodsParamsDTOList, Long goodsId) {
        try {
            if (CollectionUtils.isNotEmpty(esGoodsParamsDTOList)) {
                //先把原来商品的参数删除再批量添加
                QueryWrapper<EsGoodsParams> queryWrapper = new QueryWrapper<>();
                queryWrapper.lambda().eq(EsGoodsParams::getGoodsId,goodsId);
                this.goodsParamsMapper.delete(queryWrapper);
                List<EsGoodsParams> esGoodsParamsList = new ArrayList<>();
                if (CollectionUtils.isNotEmpty(esGoodsParamsDTOList)) {
                    esGoodsParamsList = esGoodsParamsDTOList.stream().map(goodsParamsDTO -> {
                        EsGoodsParams esGoodsParams = new EsGoodsParams();
                        BeanUtil.copyProperties(goodsParamsDTO, esGoodsParams);
                        esGoodsParams.setGoodsId(goodsId);
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
     * @param goodsParamsDTO DTO
     * @auther: wangaf 826988665@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsGoodsParamsDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult<EsGoodsParamsDO> updateGoodsParams(EsGoodsParamsDTO goodsParamsDTO,Long id) {
        try {
            EsGoodsParams goodsParams = new EsGoodsParams();
            BeanUtil.copyProperties(goodsParamsDTO, goodsParams);
            QueryWrapper<EsGoodsParams> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsGoodsParams::getId, id);
            this.goodsParamsMapper.update(goodsParams, queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("更新商品参数失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("更新商品参数失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), GoodsErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据id获取详情
     *
     * @param id 主键id
     * @auther: wangaf 826988665@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsGoodsParamsDO>
     */
    @Override
    public DubboResult<EsGoodsParamsDO> getGoodsParams(Long id) {
        try {
            QueryWrapper<EsGoodsParams> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsGoodsParams::getId, id);
            EsGoodsParams goodsParams = this.goodsParamsMapper.selectOne(queryWrapper);
            EsGoodsParamsDO goodsParamsDO = new EsGoodsParamsDO();
            if (goodsParams == null) {
                throw new ArgumentException(GoodsErrorCode.DATA_NOT_EXIST.getErrorCode(), GoodsErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(goodsParams, goodsParamsDO);
            return DubboResult.success(goodsParamsDO);
        } catch (ArgumentException ae){
            logger.error("查询商品参数失败", ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("查询商品参数失败", th);
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询列表
     *
     * @param goodsParamsDTO DTO
     * @param pageSize     页码
     * @param pageNum      页数
     * @auther: wangaf 826988665@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsGoodsParamsDO>
     */
    @Override
    public DubboPageResult<EsGoodsParamsDO> getGoodsParamsList(EsGoodsParamsDTO goodsParamsDTO, int pageSize, int pageNum) {
        QueryWrapper<EsGoodsParams> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件

            Page<EsGoodsParams> page = new Page<>(pageNum, pageSize);
            IPage<EsGoodsParams> iPage = this.page(page, queryWrapper);
            List<EsGoodsParamsDO> goodsParamsDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                goodsParamsDOList = iPage.getRecords().stream().map(goodsParams -> {
                    EsGoodsParamsDO goodsParamsDO = new EsGoodsParamsDO();
                    BeanUtil.copyProperties(goodsParams, goodsParamsDO);
                    return goodsParamsDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(iPage.getTotal(),goodsParamsDOList);
        } catch (ArgumentException ae){
            logger.error("分页查询商品参数失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("分页查询商品参数失败", th);
            return DubboPageResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据主键删除数据
     *
     * @param id 主键id
     * @auther: wangaf 826988665@qq.com
     * @date: 2019/05/31 16:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsGoodsParamsDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult<EsGoodsParamsDO> deleteGoodsParams(Long id) {
        try {
            if (id == 0) {
                throw new ArgumentException(GoodsErrorCode.PARAM_ERROR.getErrorCode(), String.format("参数传入错误ID不能为空[%s]", id));
            }
            QueryWrapper<EsGoodsParams> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.lambda().eq(EsGoodsParams::getId, id);
            this.goodsParamsMapper.delete(deleteWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae){
             logger.error("删除商品参数失败", ae);
             TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
             return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("删除商品参数失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), GoodsErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    public DubboPageResult<EsGoodsParamsDO> getGoodsParamsByGoodsId(Long goodsId) {
        try {
           List<EsGoodsParamsDO> goodsParamsDOList = goodsParamsMapper.getGoodsParamsByGoodsId(goodsId);
            return DubboPageResult.success(goodsParamsDOList);
        } catch (ArgumentException ae){
            logger.error("获取商品参数失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("获取商品参数失败", th);
            return DubboPageResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), GoodsErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    public DubboPageResult<EsBuyerGoodsParamsDO> queryGoodsParams(Long categoryId, Long goodsId) {
        try{
            DubboPageResult<EsParameterGroupDO>  result= esParameterGroupService.getBuyerParameterGroup(categoryId);
            if(!result.isSuccess()){
                return DubboPageResult.fail(GoodsErrorCode.DATA_NOT_EXIST.getErrorCode(),"参数组为空");
            }
            //参数组
            List<EsParameterGroupDO> groupList = result.getData().getList();
            Map<Integer, List<EsBuyerParamsDO>> map = new HashMap<>(16);
            List<EsBuyerParamsDO> paramsDOList =   goodsParamsMapper.getParams(categoryId,goodsId);
            for (EsBuyerParamsDO param : paramsDOList) {
                if (map.get(param.getGroupId().intValue()) != null) {
                    map.get(param.getGroupId().intValue()).add(param);
                } else {
                    List<EsBuyerParamsDO> list = new ArrayList<>();
                    list.add(param);
                    map.put(param.getGroupId().intValue(), list);
                }
            }
            List<EsBuyerGoodsParamsDO> buyerGoodsParamsDOList =  groupList.stream().map(group->{
                EsBuyerGoodsParamsDO buyerGoodsParamsDO = new EsBuyerGoodsParamsDO();
                buyerGoodsParamsDO.setGroupId(group.getId().intValue());
                buyerGoodsParamsDO.setGroupName(group.getGroupName());
                buyerGoodsParamsDO.setParams(map.get(group.getId().intValue()));

                return buyerGoodsParamsDO;
            }).collect(Collectors.toList());
            return DubboPageResult.success(buyerGoodsParamsDOList);
        } catch (ArgumentException ae){
            logger.error("获取商品参数失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("获取商品参数失败", th);
            return DubboPageResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), GoodsErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
}
