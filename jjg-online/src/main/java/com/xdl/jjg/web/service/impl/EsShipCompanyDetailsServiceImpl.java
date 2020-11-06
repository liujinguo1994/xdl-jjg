package com.xdl.jjg.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jjg.trade.model.domain.EsShipCompanyDetailsDO;
import com.jjg.trade.model.dto.EsShipCompanyDetailsDTO;
import com.jjg.trade.model.dto.EsShipTemplateDTO;
import com.xdl.jjg.constant.TradeErrorCode;
import com.xdl.jjg.entity.EsShipCompanyDetails;
import com.xdl.jjg.mapper.EsFreightTemplateDetailMapper;
import com.xdl.jjg.mapper.EsShipCompanyDetailsMapper;
import com.xdl.jjg.mapper.EsShipTemplateMapper;
import com.xdl.jjg.message.ShipCompanyMsg;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.util.JsonUtil;
import com.xdl.jjg.web.service.IEsShipCompanyDetailsService;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 公司运费模版详情 服务实现类
 * </p>
 *
 * @author LBW 981087977@qq.com
 * @since 2019-11-28 10:41:07
 */
@Service
public class EsShipCompanyDetailsServiceImpl extends ServiceImpl<EsShipCompanyDetailsMapper, EsShipCompanyDetails> implements IEsShipCompanyDetailsService {

    private static Logger logger = LoggerFactory.getLogger(EsShipCompanyDetailsServiceImpl.class);

    @Autowired
    private EsShipCompanyDetailsMapper shipCompanyDetailsMapper;

    @Autowired
    private EsShipCompanyDetailsMapper esShipCompanyDetailsMapper;

    @Autowired
    private EsShipTemplateMapper shipTemplateMapper;

    @Autowired
    private EsFreightTemplateDetailMapper esFreightTemplateDetailMapper;

    /**
     * 插入公司运费模版详情数据
     *
     * @param shipCompanyDetailsDTO 公司运费模版详情DTO
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsShipCompanyDetailsDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertShipCompanyDetails(EsShipCompanyDetailsDTO shipCompanyDetailsDTO) {
        try {
            EsShipCompanyDetails shipCompanyDetails = new EsShipCompanyDetails();
            BeanUtil.copyProperties(shipCompanyDetailsDTO, shipCompanyDetails);
            this.shipCompanyDetailsMapper.insert(shipCompanyDetails);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("公司运费模版详情新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable ae) {
            logger.error("公司运费模版详情新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新公司运费模版详情数据
     *
     * @param shipCompanyDetailsDTO 公司运费模版详情DTO
     * @param id                          主键id
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsShipCompanyDetailsDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateShipCompanyDetails(EsShipCompanyDetailsDTO shipCompanyDetailsDTO, Long id) {
        try {
            EsShipCompanyDetails shipCompanyDetails = this.shipCompanyDetailsMapper.selectById(id);
            if (shipCompanyDetails == null) {
                throw new ArgumentException(TradeErrorCode.DATA_NOT_EXIST.getErrorCode(), TradeErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(shipCompanyDetailsDTO, shipCompanyDetails);
            QueryWrapper<EsShipCompanyDetails> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsShipCompanyDetails::getId, id);
            this.shipCompanyDetailsMapper.update(shipCompanyDetails, queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("公司运费模版详情更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("公司运费模版详情更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据id获取公司运费模版详情详情
     *
     * @param id 主键id
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsShipCompanyDetailsDO>
     */
    @Override
    public DubboResult<EsShipCompanyDetailsDO> getShipCompanyDetails(Long id) {
        try {
            EsShipCompanyDetails shipCompanyDetails = this.shipCompanyDetailsMapper.selectById(id);
            if (shipCompanyDetails == null) {
                throw new ArgumentException(TradeErrorCode.DATA_NOT_EXIST.getErrorCode(), TradeErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            EsShipCompanyDetailsDO shipCompanyDetailsDO = new EsShipCompanyDetailsDO();
            BeanUtil.copyProperties(shipCompanyDetails, shipCompanyDetailsDO);
            return DubboResult.success(shipCompanyDetailsDO);
        } catch (ArgumentException ae){
            logger.error("公司运费模版详情查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("公司运费模版详情查询失败", th);
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询公司运费模版详情列表
     *
     * @param shipCompanyDetailsDTO 公司运费模版详情DTO
     * @param pageSize     页码
     * @param pageNum      页数
     * @auther: libw 981087977@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsShipCompanyDetailsDO>
     */
    @Override
    public DubboPageResult<EsShipCompanyDetailsDO> getShipCompanyDetailsList(EsShipCompanyDetailsDTO shipCompanyDetailsDTO, int pageSize, int pageNum) {
        QueryWrapper<EsShipCompanyDetails> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件

            Page<EsShipCompanyDetails> page = new Page<>(pageNum, pageSize);
            IPage<EsShipCompanyDetails> iPage = this.page(page, queryWrapper);
            List<EsShipCompanyDetailsDO> shipCompanyDetailsDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                shipCompanyDetailsDOList = iPage.getRecords().stream().map(shipCompanyDetails -> {
                    EsShipCompanyDetailsDO shipCompanyDetailsDO = new EsShipCompanyDetailsDO();
                    BeanUtil.copyProperties(shipCompanyDetails, shipCompanyDetailsDO);
                    return shipCompanyDetailsDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(shipCompanyDetailsDOList);
        } catch (ArgumentException ae){
            logger.error("公司运费模版详情分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("公司运费模版详情分页查询失败", th);
            return DubboPageResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据主键删除公司运费模版详情数据
     *
     * @param id 主键id
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsShipCompanyDetailsDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteShipCompanyDetails(Long id) {
        try {
            this.shipCompanyDetailsMapper.deleteById(id);
            return DubboResult.success();
        } catch (ArgumentException ae){
             logger.error("公司运费模版详情删除失败", ae);
             TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
             return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("公司运费模版详情删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    public DubboResult<Boolean> getByAreaId(int type, String areaId) {
        QueryWrapper<EsShipCompanyDetails> queryWrapper = new QueryWrapper<>();
        try{
            queryWrapper.lambda().eq(EsShipCompanyDetails::getType, type)
                    .eq(EsShipCompanyDetails::getAreaId, areaId);
            List<EsShipCompanyDetails> list = this.list(queryWrapper);
            boolean flag = false;
            if(list.size() > 0){
                flag = true;
            }
            return DubboPageResult.success(flag);
        }catch (ArgumentException ae){
            logger.error("公司运费模版详情查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable th) {
            logger.error("公司运费模版查询失败", th);
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    public DubboResult<Boolean> getByAreaIdAndShopId(int type, String areaId,Long shopId) {
        QueryWrapper<EsShipCompanyDetails> queryWrapper = new QueryWrapper<>();
        try{
            queryWrapper.lambda().eq(EsShipCompanyDetails::getType, type)
                    .eq(EsShipCompanyDetails::getShopId,shopId)
                    .eq(EsShipCompanyDetails::getAreaId, areaId);
            List<EsShipCompanyDetails> list = this.list(queryWrapper);
            boolean flag = false;
            if(list.size() > 0){
                flag = true;
            }
            return DubboPageResult.success(flag);
        }catch (ArgumentException ae){
            logger.error("公司运费模版详情查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable th) {
            logger.error("公司运费模版查询失败", th);
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    public DubboResult<EsShipCompanyDetailsDO> getByTypeAndPrice(int type, String areaId, Double price,Long shopId) {
        QueryWrapper<EsShipCompanyDetails> queryWrapper = new QueryWrapper<>();
        try{
            queryWrapper.lambda().eq(EsShipCompanyDetails::getAreaId, areaId)
                    .eq(EsShipCompanyDetails::getType, type)
                    .eq(EsShipCompanyDetails::getShopId,shopId)
                    .le(EsShipCompanyDetails::getLowerPrice, price)
                    .gt(EsShipCompanyDetails::getHighPrice, price);
            EsShipCompanyDetails shipCompanyDetails = shipCompanyDetailsMapper.selectOne(queryWrapper);
            EsShipCompanyDetailsDO shipCompanyDetailsDO = new EsShipCompanyDetailsDO();
            // 先通过价格区间查询 活动的商品的运费模板，否则就取非活动的运费模板的价格
//            if (shipCompanyDetails != null){
//                BeanUtil.copyProperties(shipCompanyDetails, shipCompanyDetailsDO);
//            }else {
//                QueryWrapper<EsShipCompanyDetails> queryWrapper1 = new QueryWrapper<>();
//                queryWrapper1.lambda().eq(EsShipCompanyDetails::getAreaId, areaId)
//                        .eq(EsShipCompanyDetails::getShopId,shopId)
//                        .eq(EsShipCompanyDetails::getType, type);
//                shipCompanyDetails = shipCompanyDetailsMapper.selectOne(queryWrapper1);
                if (shipCompanyDetails != null){
                    BeanUtil.copyProperties(shipCompanyDetails, shipCompanyDetailsDO);
                }

            return DubboPageResult.success(shipCompanyDetailsDO);
        }catch (ArgumentException ae){
            logger.error("公司运费模版详情删除失败", ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable th) {
            logger.error("公司运费模版查询失败", th);
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
    public DubboResult<EsShipCompanyDetailsDO> getPrice(int type, String areaId,Long shopId,Long templateId,Long isNg) {
        QueryWrapper<EsShipCompanyDetails> queryWrapper = new QueryWrapper<>();
        try{
            queryWrapper.lambda().eq(EsShipCompanyDetails::getAreaId, areaId)
                    .eq(EsShipCompanyDetails::getType, type)
                    .eq(EsShipCompanyDetails::getShopId,shopId).eq(EsShipCompanyDetails::getIsNg,isNg);
            if(templateId !=null){
                queryWrapper.lambda().eq(EsShipCompanyDetails::getModeId,templateId);
            }
            EsShipCompanyDetails shipCompanyDetails = shipCompanyDetailsMapper.selectOne(queryWrapper);
            EsShipCompanyDetailsDO shipCompanyDetailsDO = new EsShipCompanyDetailsDO();
            if (shipCompanyDetails != null){
                BeanUtil.copyProperties(shipCompanyDetails, shipCompanyDetailsDO);
            }

            return DubboPageResult.success(shipCompanyDetailsDO);
        }catch (ArgumentException ae){
            logger.error("公司运费模版查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable th) {
            logger.error("公司运费模版查询失败", th);
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
    public DubboResult<EsShipCompanyDetailsDO> getPrice(Long areaId) {
        QueryWrapper<EsShipCompanyDetails> queryWrapper = new QueryWrapper<>();
        try{
            queryWrapper.lambda().eq(EsShipCompanyDetails::getAreaId, areaId).eq(EsShipCompanyDetails::getIsNg,1);
            EsShipCompanyDetails shipCompanyDetails = shipCompanyDetailsMapper.selectOne(queryWrapper);
            EsShipCompanyDetailsDO shipCompanyDetailsDO = new EsShipCompanyDetailsDO();
            if (shipCompanyDetails != null){
                BeanUtil.copyProperties(shipCompanyDetails, shipCompanyDetailsDO);
            }
            return DubboPageResult.success(shipCompanyDetailsDO);
        }catch (ArgumentException ae){
            logger.error("公司运费模版详情获取失败", ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable th) {
            logger.error("公司运费模版查询失败", th);
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
    public DubboResult saveShipCompanyDetail(ShipCompanyMsg shipCompanyMsg){
        try {
            //处理运费模板详情信息
            EsShipTemplateDTO esShipTemplateDTO = shipCompanyMsg.getEsShipTemplateDTO();

            // 是否生鲜模板 1 生鲜 2 非生鲜
            Integer type = esShipTemplateDTO.getIsFresh();
            // 运费模板项
            List<EsShipCompanyDetailsDTO> esShipCompanyDetailsList = esShipTemplateDTO.getEsShipCompanyDetailsList();
            // 0新增，1修改，2删除
            Integer state = shipCompanyMsg.getState();
            if (state == 0){
                // 新增
                for (EsShipCompanyDetailsDTO esShipCompanyDetailsDTO:esShipCompanyDetailsList
                ) {
                    String areaId = esShipCompanyDetailsDTO.getAreaId();
                    List<Long> split = JsonUtil.jsonToList(areaId, Long.class);
                    for (Long aId:split) {
                        EsShipCompanyDetails shipCompanyDetails=new EsShipCompanyDetails();
                        shipCompanyDetails.setAreaId(Long.valueOf(aId));
                        shipCompanyDetails.setModeId(esShipTemplateDTO.getId());
                        shipCompanyDetails.setType(esShipCompanyDetailsDTO.getType());
                        shipCompanyDetails.setLowerPrice(esShipCompanyDetailsDTO.getLowerPrice());
                        shipCompanyDetails.setHighPrice(esShipCompanyDetailsDTO.getHighPrice());
                        shipCompanyDetails.setFirstCompany(esShipCompanyDetailsDTO.getFirstCompany());
                        shipCompanyDetails.setFirstPrice(esShipCompanyDetailsDTO.getFirstPrice());
                        shipCompanyDetails.setContinuedCompany(esShipCompanyDetailsDTO.getContinuedCompany());
                        shipCompanyDetails.setContinuedPrice(esShipCompanyDetailsDTO.getContinuedPrice());
                        shipCompanyDetails.setShopId(esShipTemplateDTO.getShopId());
                        esShipCompanyDetailsMapper.insert(shipCompanyDetails);
                    }
                }

            }else if (state == 1){
                // 修改
                for (EsShipCompanyDetailsDTO esShipCompanyDetailsDTO:esShipCompanyDetailsList
                ) {
                    String areaId = esShipCompanyDetailsDTO.getAreaId();
                    List<Long> split = JsonUtil.jsonToList(areaId, Long.class);
                    for (Long aId:split) {
                        QueryWrapper<EsShipCompanyDetails> queryWrapper = new QueryWrapper<>();
                        queryWrapper.lambda().eq(EsShipCompanyDetails::getModeId,esShipTemplateDTO.getId());
                        queryWrapper.lambda().eq(EsShipCompanyDetails::getType,type);
                        esShipCompanyDetailsMapper.delete(queryWrapper);
                        EsShipCompanyDetails shipCompanyDetails=new EsShipCompanyDetails();
                        shipCompanyDetails.setAreaId(Long.valueOf(aId));
                        shipCompanyDetails.setModeId(esShipTemplateDTO.getId());
                        shipCompanyDetails.setType(esShipCompanyDetailsDTO.getType());
                        shipCompanyDetails.setLowerPrice(esShipCompanyDetailsDTO.getLowerPrice());
                        shipCompanyDetails.setHighPrice(esShipCompanyDetailsDTO.getHighPrice());
                        shipCompanyDetails.setFirstCompany(esShipCompanyDetailsDTO.getFirstCompany());
                        shipCompanyDetails.setFirstPrice(esShipCompanyDetailsDTO.getFirstPrice());
                        shipCompanyDetails.setContinuedCompany(esShipCompanyDetailsDTO.getContinuedCompany());
                        shipCompanyDetails.setContinuedPrice(esShipCompanyDetailsDTO.getContinuedPrice());
                        shipCompanyDetails.setShopId(esShipTemplateDTO.getShopId());
                        esShipCompanyDetailsMapper.insert(shipCompanyDetails);
                    }
                }
            }
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("公司运费模版详情操作失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable th) {
            logger.error("公司运费模版操作失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }

    }
}
