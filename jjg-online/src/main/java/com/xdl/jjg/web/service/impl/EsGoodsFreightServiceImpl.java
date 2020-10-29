package com.xdl.jjg.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jjg.trade.model.domain.EsGoodsFreightDO;
import com.jjg.trade.model.domain.EsShipTemplateDO;
import com.jjg.trade.model.dto.EsGoodsFreightDTO;
import com.xdl.jjg.constant.TradeErrorCode;
import com.xdl.jjg.entity.EsGoodsFreight;
import com.xdl.jjg.entity.EsShipTemplate;
import com.xdl.jjg.mapper.EsGoodsFreightMapper;
import com.xdl.jjg.mapper.EsShipTemplateMapper;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.web.service.IEsGoodsFreightService;
import org.apache.dubbo.common.utils.CollectionUtils;
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
 * 分类运费模板关联表 服务实现类
 * </p>
 *
 * @author LBW 981087977@qq.com
 * @since 2019-06-05 09:20:40
 */
@Service(version = "${dubbo.application.version}", interfaceClass = IEsGoodsFreightService.class, timeout = 50000)
public class EsGoodsFreightServiceImpl extends ServiceImpl<EsGoodsFreightMapper, EsGoodsFreight> implements IEsGoodsFreightService {

    private static Logger logger = LoggerFactory.getLogger(EsGoodsFreightServiceImpl.class);

    @Autowired
    private EsGoodsFreightMapper goodsFreightMapper;
    @Autowired
    private EsShipTemplateMapper esShipTemplateMapper;


    /**
     * 插入分类运费模板关联表数据
     *
     * @param goodsFreightDTO 分类运费模板关联表DTO
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsGoodsFreightDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertGoodsFreight(EsGoodsFreightDTO goodsFreightDTO) {
        try {
            EsGoodsFreight goodsFreight = new EsGoodsFreight();
            BeanUtil.copyProperties(goodsFreightDTO, goodsFreight);
            this.goodsFreightMapper.insert(goodsFreight);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("分类运费模板关联表新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable ae) {
            logger.error("分类运费模板关联表新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新分类运费模板关联表数据
     *
     * @param goodsFreightDTO 分类运费模板关联表DTO
     * @param id                          主键id
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsGoodsFreightDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateGoodsFreight(EsGoodsFreightDTO goodsFreightDTO, Long id) {
        try {
            EsGoodsFreight goodsFreight = this.goodsFreightMapper.selectById(id);
            if (goodsFreight == null) {
                throw new ArgumentException(TradeErrorCode.DATA_NOT_EXIST.getErrorCode(), TradeErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(goodsFreightDTO, goodsFreight);
            QueryWrapper<EsGoodsFreight> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsGoodsFreight::getId, id);
            this.goodsFreightMapper.update(goodsFreight, queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("分类运费模板关联表更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("分类运费模板关联表更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据id获取分类运费模板关联表详情
     *
     * @param id 主键id
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsGoodsFreightDO>
     */
    @Override
    public DubboResult<EsGoodsFreightDO> getGoodsFreight(Long id) {
        try {
            EsGoodsFreight goodsFreight = this.goodsFreightMapper.selectById(id);
            if (goodsFreight == null) {
                throw new ArgumentException(TradeErrorCode.DATA_NOT_EXIST.getErrorCode(), TradeErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            EsGoodsFreightDO goodsFreightDO = new EsGoodsFreightDO();
            BeanUtil.copyProperties(goodsFreight, goodsFreightDO);
            return DubboResult.success(goodsFreightDO);
        } catch (ArgumentException ae){
            logger.error("分类运费模板关联表查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("分类运费模板关联表查询失败", th);
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    @Override
    public DubboResult<EsShipTemplateDO> getGoodsFreightByCategoryId(Long categoryId, Long shopId) {
        try {
            QueryWrapper<EsGoodsFreight> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsGoodsFreight::getCategoryId,categoryId);
            EsGoodsFreight esGoodsFreight = this.baseMapper.selectOne(queryWrapper);
            //模板id
            Long modeId = esGoodsFreight.getModeId();

            QueryWrapper<EsShipTemplate> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.lambda().eq(EsShipTemplate::getShopId,shopId);
            queryWrapper1.lambda().eq(EsShipTemplate::getId,modeId);
            EsShipTemplate esShipTemplate = esShipTemplateMapper.selectOne(queryWrapper1);
            if (esShipTemplate == null){
                throw new ArgumentException(TradeErrorCode.DATA_NOT_EXIST.getErrorCode(), TradeErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            EsShipTemplateDO esShipTemplateDO = new EsShipTemplateDO();
            BeanUtils.copyProperties(esShipTemplate,esShipTemplateDO);
            return DubboResult.success(esShipTemplateDO);
        } catch (ArgumentException ae) {
            logger.error("查询运费模板失败！", ae);
            return DubboResult.fail(TradeErrorCode.DATA_NOT_EXIST.getErrorCode(), TradeErrorCode.DATA_NOT_EXIST.getErrorMsg());
        }
    }

    /**
     * 根据查询分类运费模板关联表列表
     *
     * @param goodsFreightDTO 分类运费模板关联表DTO
     * @param pageSize     页码
     * @param pageNum      页数
     * @auther: libw 981087977@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsGoodsFreightDO>
     */
    @Override
    public DubboPageResult<EsGoodsFreightDO> getGoodsFreightList(EsGoodsFreightDTO goodsFreightDTO, int pageSize, int pageNum) {
        QueryWrapper<EsGoodsFreight> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件

            Page<EsGoodsFreight> page = new Page<>(pageNum, pageSize);
            IPage<EsGoodsFreight> iPage = this.page(page, queryWrapper);
            List<EsGoodsFreightDO> goodsFreightDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                goodsFreightDOList = iPage.getRecords().stream().map(goodsFreight -> {
                    EsGoodsFreightDO goodsFreightDO = new EsGoodsFreightDO();
                    BeanUtil.copyProperties(goodsFreight, goodsFreightDO);
                    return goodsFreightDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(goodsFreightDOList);
        } catch (ArgumentException ae){
            logger.error("分类运费模板关联表分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("分类运费模板关联表分页查询失败", th);
            return DubboPageResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据主键删除分类运费模板关联表数据
     *
     * @param id 主键id
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsGoodsFreightDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteGoodsFreight(Long id) {
        try {
            this.goodsFreightMapper.deleteById(id);
            return DubboResult.success();
        } catch (ArgumentException ae){
             logger.error("分类运费模板关联表删除失败", ae);
             TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
             return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("分类运费模板关联表删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 系统模块
     * 分类绑定运费模板的接口
     *@auther: LiuJG 344009799@qq.com
     * @date: 2019/06/22 17:20
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult saveCatShipTemplate(Long id, Long shipTemplateId) {
        try {
            EsGoodsFreight esGoodsFreight = new EsGoodsFreight();
            esGoodsFreight.setModeId(shipTemplateId);
            esGoodsFreight.setCategoryId(id);
            esGoodsFreight.setIsDel(0);
            this.goodsFreightMapper.insert(esGoodsFreight);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("分类运费模板关联插入失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable t) {
            logger.error("分类运费模板关联插入失败", t);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
}
