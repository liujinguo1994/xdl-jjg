package com.xdl.jjg.web.service.Impl;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xdl.jjg.constant.GoodsErrorCode;
import com.xdl.jjg.constant.MemberErrorCode;
import com.xdl.jjg.entity.EsTemplateDetail;
import com.xdl.jjg.mapper.EsTemplateDetailMapper;
import com.xdl.jjg.model.domain.EsTemplateDetailDO;
import com.xdl.jjg.model.dto.EsCategoryTemDTO;
import com.xdl.jjg.model.dto.EsGoodsTemDTO;
import com.xdl.jjg.model.dto.EsTemplateDetailDTO;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.util.CollectionUtils;
import com.xdl.jjg.util.JsonUtil;
import com.xdl.jjg.web.service.IEsTemplateDetailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 店铺模板详情--es_template_detail 服务实现类
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2019-06-25
 */
@Service
public class EsTemplateDetailServiceImpl extends ServiceImpl<EsTemplateDetailMapper, EsTemplateDetail> implements IEsTemplateDetailService {

    private static Logger logger = LoggerFactory.getLogger(EsTemplateDetailServiceImpl.class);

    @Autowired
    private EsTemplateDetailMapper templateDetailMapper;

    /**
     * 插入店铺模板详情--es_template_detail数据
     *
     * @param templateDetailDTO 店铺模板详情--es_template_detailDTO
     * @auther: yuanj 595831329@qq.com
     * @date: 2019-06-27 10:05:30
     * @return: com.shopx.common.model.result.DubboResult<EsTemplateDetailDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertTemplateDetail(EsTemplateDetailDTO templateDetailDTO) {
        try {
            Assert.notNull(templateDetailDTO.getType(), "模板类型不能为空");
            Assert.notNull(templateDetailDTO.getName(), "模板名称不能为空");
            Assert.notNull(templateDetailDTO.getLocation(), "模板位置不能为空");
            EsTemplateDetail templateDetail = new EsTemplateDetail();
            //拷贝分类和商品信息除外的
            BeanUtil.copyProperties(templateDetailDTO, templateDetail);
            String tem = templateDetailDTO.getCategoryForTems();
            String good = templateDetailDTO.getHotSellGoods();
            List<EsGoodsTemDTO> hotGoods = JSONArray.parseArray(good, EsGoodsTemDTO.class);
            String hotSellGoods1 = JsonUtil.objectToJson(hotGoods);
            templateDetail.setHotSellGoods(hotSellGoods1);
            List<EsGoodsTemDTO> categoryForTem = JSONArray.parseArray(tem, EsGoodsTemDTO.class);
            String categoryForTems = JsonUtil.objectToJson(categoryForTem);
            templateDetail.setCategoryForTems(categoryForTems);
            this.templateDetailMapper.insert(templateDetail);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("店铺模板详情--es_template_detail新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable ae) {
            logger.error("店铺模板详情--es_template_detail新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新店铺模板详情--es_template_detail数据
     *
     * @param templateDetailDTO 店铺模板详情--es_template_detailDTO
     * @auther: yuanj 595831329@qq.com
     * @date: 2019-06-27 10:23:10
     * @return: com.shopx.common.model.result.DubboResult<EsTemplateDetailDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateTemplateDetail(EsTemplateDetailDTO templateDetailDTO) {
        try {
            EsTemplateDetail esTemplateDetail = this.templateDetailMapper.selectById(templateDetailDTO.getId());
            if (esTemplateDetail==null){
                throw new ArgumentException(MemberErrorCode.NOTEXIST_TEM.getErrorCode(), MemberErrorCode.NOTEXIST_TEM.getErrorMsg());
            }
            EsTemplateDetail templateDetail = new EsTemplateDetail();
            BeanUtil.copyProperties(templateDetailDTO, templateDetail);
            String categoryFortem = templateDetailDTO.getCategoryForTems();
            String good = templateDetailDTO.getHotSellGoods();
            List<EsGoodsTemDTO> hotGoods = JSONArray.parseArray(good, EsGoodsTemDTO.class);
            String hotSellGoods1 = JsonUtil.objectToJson(hotGoods);
            templateDetail.setHotSellGoods(hotSellGoods1);
            List<EsGoodsTemDTO> categoryForTem = JSONArray.parseArray(categoryFortem, EsGoodsTemDTO.class);
            String categoryForTems = JsonUtil.objectToJson(categoryForTem);
            templateDetail.setCategoryForTems(categoryForTems);
            QueryWrapper<EsTemplateDetail> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsTemplateDetail::getId, templateDetailDTO.getId());
            this.templateDetailMapper.update(templateDetail, queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("店铺模板详情--es_template_detail更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("店铺模板详情--es_template_detail更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据id获取店铺模板详情--es_template_detail详情
     *
     * @param id 主键id
     * @auther: yuanj 595831329@qq.com
     * @date: 2019-06-25 14:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsTemplateDetailDO>
     */
    @Override
    public DubboResult<EsTemplateDetailDO> getTemplateDetail(Long id) {
        try {
            Assert.notNull(id, "模板类型id不能为空");
            EsTemplateDetail esTemplateDetail = this.templateDetailMapper.selectById(id);
            if (esTemplateDetail==null){
                throw new ArgumentException(MemberErrorCode.NOTEXIST_TEM.getErrorCode(), MemberErrorCode.NOTEXIST_TEM.getErrorMsg());
            }
            EsTemplateDetailDO esTemplateDetailDO = new EsTemplateDetailDO();
            BeanUtil.copyProperties(esTemplateDetailDO,esTemplateDetail);
            List<EsCategoryTemDTO> categoryForTem = JSONArray.parseArray(esTemplateDetail.getCategoryForTems(), EsCategoryTemDTO.class);
            List<EsGoodsTemDTO> hotSellGoods = JSONArray.parseArray(esTemplateDetail.getHotSellGoods(), EsGoodsTemDTO.class);
            esTemplateDetailDO.setCategoryForTems(categoryForTem);
            esTemplateDetailDO.setHotSellGoods(hotSellGoods);
            return DubboResult.success(esTemplateDetailDO);
        } catch (ArgumentException ae){
            logger.error("店铺模板详情--es_template_detail查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("店铺模板详情--es_template_detail查询失败", th);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询店铺模板详情--es_template_detail列表
     *
     * @param templateDetailDTO 店铺模板详情--es_template_detailDTO
     * @param pageSize     页码
     * @param pageNum      页数
     * @auther: yuanj 595831329@qq.com
     * @date: 2019-06-25 14:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsTemplateDetailDO>
     */
    @Override
    public DubboPageResult<EsTemplateDetailDO> getTemplateDetailList(EsTemplateDetailDTO templateDetailDTO, int pageSize, int pageNum) {
        QueryWrapper<EsTemplateDetail> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件

            Page<EsTemplateDetail> page = new Page<>(pageNum, pageSize);
            IPage<EsTemplateDetail> iPage = this.page(page, queryWrapper);
            List<EsTemplateDetailDO> templateDetailDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                templateDetailDOList = iPage.getRecords().stream().map(templateDetail -> {
                    EsTemplateDetailDO templateDetailDO = new EsTemplateDetailDO();
                    BeanUtil.copyProperties(templateDetail, templateDetailDO);
                    return templateDetailDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(iPage.getTotal(),templateDetailDOList);
        } catch (ArgumentException ae){
            logger.error("店铺模板详情--es_template_detail分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("店铺模板详情--es_template_detail分页查询失败", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 批量删除店铺模板详情--es_template_detail数据
     *
     * @param ids 主键id
     * @auther: yuanj 595831329@qq.com
     * @date: 2019-06-27 10:45:44
     * @return: com.shopx.common.model.result.DubboResult<EsTemplateDetailDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteTemplateDetail(Integer[] ids) {
        try {
            QueryWrapper<EsTemplateDetail> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.lambda().in(EsTemplateDetail::getId, ids);
            this.templateDetailMapper.delete(deleteWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("批量删除商品相册失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("批量删除商品相册失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), GoodsErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
}
