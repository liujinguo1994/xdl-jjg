package com.xdl.jjg.web.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jjg.shop.model.domain.EsGoodsDO;
import com.jjg.trade.model.domain.EsSellerFreightTemplateDetailDO;
import com.jjg.trade.model.domain.EsShipTemplateDO;
import com.jjg.trade.model.dto.EsFreightTemplateDetailDTO;
import com.jjg.trade.model.dto.EsShipCompanyDetailsDTO;
import com.jjg.trade.model.dto.EsShipTemplateDTO;
import com.xdl.jjg.constant.TradeErrorCode;
import com.xdl.jjg.entity.EsFreightTemplateDetail;
import com.xdl.jjg.entity.EsShipCompanyDetails;
import com.xdl.jjg.entity.EsShipTemplate;
import com.xdl.jjg.mapper.EsFreightTemplateDetailMapper;
import com.xdl.jjg.mapper.EsShipCompanyDetailsMapper;
import com.xdl.jjg.mapper.EsShipTemplateMapper;
import com.xdl.jjg.message.ShipCompanyMsg;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.roketmq.MQProducer;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.util.JsonUtil;
import com.xdl.jjg.web.service.IEsShipCompanyDetailsService;
import com.xdl.jjg.web.service.IEsShipTemplateService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 运费模板表 服务实现类
 * </p>
 *
 * @author LiuJG 344009799@qq.com
 * @since 2019-06-03
 */
@Service(version = "${dubbo.application.version}", interfaceClass = IEsShipTemplateService.class, timeout = 50000)
public class EsShipTemplateServiceImpl extends ServiceImpl<EsShipTemplateMapper, EsShipTemplate> implements IEsShipTemplateService {

    private static Logger logger = LoggerFactory.getLogger(EsShipTemplateServiceImpl.class);

    @Autowired
    private EsShipTemplateMapper shipTemplateMapper;

    @Autowired
    private EsFreightTemplateDetailMapper esFreightTemplateDetailMapper;

    @Autowired
    private EsShipCompanyDetailsMapper esShipCompanyDetailsMapper;

    @Autowired
    private IEsShipCompanyDetailsService iEsShipCompanyDetailsService;

    @Autowired
    private MQProducer mqProducer;

    @Value("${rocketmq.ship.topic}")
    private String ship_topic;
    @Reference(version = "${dubbo.application.version}",timeout = 5000)
    private IEsGoodsService esGoodsService;
    /**
     * 插入运费模板表数据
     *
     * @param shipTemplateDTO 运费模板表DTO
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsShipTemplateDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertShipTemplate(EsShipTemplateDTO shipTemplateDTO) {
        try {
            if (shipTemplateDTO == null) {
                throw new ArgumentException(TradeErrorCode.PARAM_ERROR.getErrorCode(), TradeErrorCode.PARAM_ERROR.getErrorMsg());
            }

            EsShipTemplate shipTemplate = new EsShipTemplate();
            BeanUtil.copyProperties(shipTemplateDTO, shipTemplate);
            this.shipTemplateMapper.insert(shipTemplate);
            return DubboResult.success();
        } catch (Throwable ae) {
            logger.error("运费模板表失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新运费模板表数据
     *
     * @param shipTemplateDTO 运费模板表DTO
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsShipTemplateDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateShipTemplate(EsShipTemplateDTO shipTemplateDTO) {
        try {
            if (shipTemplateDTO == null) {
                throw new ArgumentException(TradeErrorCode.PARAM_ERROR.getErrorCode(), TradeErrorCode.PARAM_ERROR.getErrorMsg());
            }
            EsShipTemplate esShipTemplate = this.shipTemplateMapper.selectById(shipTemplateDTO.getId());
            if (esShipTemplate == null){
                throw new ArgumentException(TradeErrorCode.TEMPLATE_DATE_NOT_EXIST.getErrorCode(), TradeErrorCode.TEMPLATE_DATE_NOT_EXIST.getErrorMsg());
            }
            EsShipTemplate shipTemplate = new EsShipTemplate();
            BeanUtil.copyProperties(shipTemplateDTO, shipTemplate);
            QueryWrapper<EsShipTemplate> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsShipTemplate::getId, shipTemplateDTO.getId());
            this.shipTemplateMapper.update(shipTemplate, queryWrapper);
            return DubboResult.success();
        }catch (ArgumentException ae){
            logger.error("运费模板表更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable th) {
            logger.error("运费模板表更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据id获取运费模板表详情
     *
     * @param id 主键id
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsShipTemplateDO>
     */
    @Override
    public DubboResult getShipTemplate(Long id) {
        try {
            QueryWrapper<EsShipTemplate> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsShipTemplate::getId, id);
            EsShipTemplate shipTemplate = this.shipTemplateMapper.selectOne(queryWrapper);
            EsShipTemplateDO shipTemplateDO = new EsShipTemplateDO();
            if (shipTemplate == null) {
                throw new ArgumentException(TradeErrorCode.DATA_NOT_EXIST.getErrorCode(), TradeErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(shipTemplate, shipTemplateDO);
            return DubboResult.success(shipTemplateDO);
        } catch (Throwable th) {
            logger.error("运费模板表查询失败", th);
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询运费模板表列表
     *
     * @param shipTemplateDTO 运费模板表DTO
     * @param pageSize  行数
     * @param pageNum   页码
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsShipTemplateDO>
     */
    @Override
    public DubboPageResult getShipTemplateList(EsShipTemplateDTO shipTemplateDTO, int pageSize, int pageNum) {
        QueryWrapper<EsShipTemplate> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件
            if(StringUtils.isNotEmpty(shipTemplateDTO.getModeName())){
                queryWrapper.lambda().eq(EsShipTemplate::getModeName,shipTemplateDTO.getModeName());
            }
            queryWrapper.lambda().eq(EsShipTemplate::getShopId,shipTemplateDTO.getShopId());
            Page<EsShipTemplate> page = new Page<>(pageNum, pageSize);
            IPage<EsShipTemplate> iPage = this.page(page, queryWrapper);
            List<EsShipTemplateDO> shipTemplateDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                shipTemplateDOList = iPage.getRecords().stream().map(shipTemplate -> {
                    EsShipTemplateDO shipTemplateDO = new EsShipTemplateDO();
                    BeanUtil.copyProperties(shipTemplate, shipTemplateDO);
                    QueryWrapper<EsFreightTemplateDetail> wrapper = new QueryWrapper<>();
                    wrapper.lambda().eq(EsFreightTemplateDetail::getModeId,shipTemplate.getId());
                    List<EsFreightTemplateDetail> esFreightTemplateDetails = this.esFreightTemplateDetailMapper.selectList(wrapper);
                    if (CollectionUtils.isNotEmpty(esFreightTemplateDetails)) {
                        List<EsSellerFreightTemplateDetailDO> esSellerFreightTemplateDetailDOS = BeanUtil.copyList(esFreightTemplateDetails, EsSellerFreightTemplateDetailDO.class);
                        shipTemplateDO.setDetailList(esSellerFreightTemplateDetailDOS);
                    }
                    return shipTemplateDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(shipTemplateDOList);
        } catch (Throwable th) {
            logger.error("运费模板表查询分页查询失败", th);
            return DubboPageResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据主键删除运费模板表数据
     *

     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsShipTemplateDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteSellerShipTemplate(Long id) {
        try {
            DubboPageResult<EsGoodsDO> result = esGoodsService.getGoodsList(id);
            if(result.isSuccess()){
                List<EsGoodsDO> goodsDOList  = result.getData().getList();
                if(CollectionUtils.isNotEmpty(goodsDOList)){
                    throw new ArgumentException(TradeErrorCode.DATA_NOT_EXIST.getErrorCode(), "该运费模板已绑定商品不允许删除");
                }
            }
            QueryWrapper<EsShipTemplate> deleteTemplateWrapper = new QueryWrapper<>();
            deleteTemplateWrapper.lambda().eq(EsShipTemplate::getId, id);
            EsShipTemplate esShipTemplate = this.shipTemplateMapper.selectOne(deleteTemplateWrapper);
            this.shipTemplateMapper.delete(deleteTemplateWrapper);
            QueryWrapper<EsFreightTemplateDetail> deleteDetailWrapper = new QueryWrapper<>();
            deleteDetailWrapper.lambda().in(EsFreightTemplateDetail::getModeId, id);
            this.esFreightTemplateDetailMapper.delete(deleteDetailWrapper);
            QueryWrapper<EsShipCompanyDetails> deleteCompanyWrapper = new QueryWrapper<>();
            deleteCompanyWrapper.lambda().in(EsShipCompanyDetails::getModeId, id);
            this.esShipCompanyDetailsMapper.delete(deleteCompanyWrapper);
//            EsShipTemplateDTO esShipTemplateDTO = new EsShipTemplateDTO();
//            List<Integer> list2 = Arrays.asList(ids);
//            esShipTemplateDTO.setModeId(list2);
//            esShipTemplateDTO.setIsFresh(esShipTemplate.getIsFresh());
//            ShipCompanyMsg shipCompanyMsg=new ShipCompanyMsg();
//            shipCompanyMsg.setEsShipTemplateDTO(esShipTemplateDTO);
//            shipCompanyMsg.setState(2);
//            // 由于消息系统不稳定 暂时使用代码实现
//            mqProducer.send(ship_topic, JsonUtil.objectToJson(shipCompanyMsg));
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("运费模板表查询删除失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable th) {
            logger.error("运费模板表查询删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 系统模块
     * 获取所有运费模板信息列表（给我提供个运费模板的下拉框数据 阮明）
     *@auther: LiuJG 344009799@qq.com
     * @date: 2019/06/22 17:00
     */
    @Override
    public DubboPageResult<EsShipTemplateDO> getComboBox() {
        try {
            QueryWrapper<EsShipTemplate> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsShipTemplate::getIsDel,0);
            List<EsShipTemplate> esShipTemplates = this.shipTemplateMapper.selectList(queryWrapper);
            if (CollectionUtils.isEmpty(esShipTemplates)){
                throw new ArgumentException(TradeErrorCode.DATA_NOT_EXIST.getErrorCode(), TradeErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            List<EsShipTemplateDO> esShipTemplateDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(esShipTemplates)) {
                esShipTemplateDOList = esShipTemplates.stream().map(esShipTemplate -> {
                    EsShipTemplateDO esShipTemplateDO = new EsShipTemplateDO();
                    BeanUtils.copyProperties(esShipTemplate, esShipTemplateDO);
                    return esShipTemplateDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(esShipTemplateDOList);
        } catch (Exception e) {
            logger.error("运费模板信息列表查询失败", e);
            return DubboPageResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertSellerShipTemplate(EsShipTemplateDTO shipTemplateDTO) {
        List<String> list=new ArrayList<>();
        ShipCompanyMsg shipCompanyMsg=new ShipCompanyMsg();
        shipCompanyMsg.setOldId(shipTemplateDTO.getId());
        try {
            // 通过店铺ID 查询该店铺中添加的地区 是否 有冲突
            QueryWrapper<EsShipTemplate> wrapperTemplate = new QueryWrapper<>();
            wrapperTemplate.lambda().eq(EsShipTemplate::getShopId,shipTemplateDTO.getShopId()).eq(EsShipTemplate::getIsFresh,shipTemplateDTO.getIsFresh())
            .eq(EsShipTemplate::getSign,shipTemplateDTO.getSign()).eq(EsShipTemplate::getIsNg,shipTemplateDTO.getIsNg());
            List<EsShipTemplate> esShipTemplates = this.shipTemplateMapper.selectList(wrapperTemplate);
            List<Long> collect = esShipTemplates.stream().map(EsShipTemplate::getId).collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(collect)){
                QueryWrapper<EsFreightTemplateDetail> wrapper = new QueryWrapper<>();
                wrapper.lambda().in(EsFreightTemplateDetail::getModeId,collect);
                List<EsFreightTemplateDetail> esFreightTemplateDetails = this.esFreightTemplateDetailMapper.selectList(wrapper);
                //将所有的区域id放在一个集合
                if (esFreightTemplateDetails != null){
                    // 遍历运费模板详情表数据
                    for (EsFreightTemplateDetail esFreightTemplateDetail:esFreightTemplateDetails) {
                        String areaIds = esFreightTemplateDetail.getAreaId();
                        String[] areaId = areaIds.split(",");
                        for (String id:areaId) {
                            System.out.println(id);
                            list.add(id);
                        }
                    }
                }
            }

            List<EsFreightTemplateDetailDTO> freightDetailList1 = shipTemplateDTO.getFreightDetailList();
            List<EsShipCompanyDetailsDTO> esShipCompanyDetailsList = new ArrayList<>();

            if (freightDetailList1 != null){
                for (EsFreightTemplateDetailDTO freightTemplateDetailDTO:freightDetailList1) {
                    String areaIds = freightTemplateDetailDTO.getAreaId();
                    EsShipCompanyDetailsDTO esShipCompanyDetails = new EsShipCompanyDetailsDTO();
                    esShipCompanyDetails.setAreaId(areaIds);
                    esShipCompanyDetails.setType(shipTemplateDTO.getIsFresh());
                    esShipCompanyDetails.setLowerPrice(freightTemplateDetailDTO.getMinMoney());
                    esShipCompanyDetails.setHighPrice(freightTemplateDetailDTO.getMaxMoney());
                    esShipCompanyDetails.setFirstCompany(freightTemplateDetailDTO.getFirstWeight());
                    esShipCompanyDetails.setFirstPrice(freightTemplateDetailDTO.getFirstTip());
                    esShipCompanyDetails.setContinuedCompany(freightTemplateDetailDTO.getSequelWeight());
                    esShipCompanyDetails.setContinuedPrice(freightTemplateDetailDTO.getSequelTip());
                    esShipCompanyDetails.setIsNg(shipTemplateDTO.getIsNg());
                    esShipCompanyDetailsList.add(esShipCompanyDetails);

                    String[] areaId = areaIds.split(",");
                    for (String id:areaId) {
                        if (list.contains(id)){
                            throw new ArgumentException(TradeErrorCode.CAN_NOT_ADD_AREA.getErrorCode(), TradeErrorCode.CAN_NOT_ADD_AREA.getErrorMsg());
                        }
                    }
                }
            }

            if (shipTemplateDTO == null) {
                throw new ArgumentException(TradeErrorCode.PARAM_ERROR.getErrorCode(), TradeErrorCode.PARAM_ERROR.getErrorMsg());
            }
            EsShipTemplate shipTemplate = new EsShipTemplate();
            shipTemplate.setIsDel(1);
            BeanUtil.copyProperties(shipTemplateDTO, shipTemplate);
            this.shipTemplateMapper.insert(shipTemplate);
            List<EsFreightTemplateDetailDTO> freightDetailList = shipTemplateDTO.getFreightDetailList();
            freightDetailList.forEach(esFreightTemplateDetailDTO -> {
                EsFreightTemplateDetail esFreightTemplateDetail = new EsFreightTemplateDetail();
                BeanUtils.copyProperties(esFreightTemplateDetailDTO,esFreightTemplateDetail);
                esFreightTemplateDetail.setModeId(shipTemplate.getId());
                esFreightTemplateDetail.setIsFresh(shipTemplateDTO.getIsFresh() == null ? 2 : shipTemplateDTO.getIsFresh());
                this.esFreightTemplateDetailMapper.insert(esFreightTemplateDetail);
            });

            shipTemplateDTO.setEsShipCompanyDetailsList(esShipCompanyDetailsList);
            shipTemplateDTO.setId(shipTemplate.getId());
            // MQ 处理 公司运费模板详情
            //发送消息插入详情

            shipCompanyMsg.setEsShipTemplateDTO(shipTemplateDTO);
            shipCompanyMsg.setState(0);

            // 由于消息系统不稳定 暂时使用代码实现
            mqProducer.send(ship_topic, JsonUtil.objectToJson(shipCompanyMsg));
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("运费模板表插入失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable ae) {
            logger.error("运费模板表插入失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateSellerShipTemplate(EsShipTemplateDTO shipTemplateDTO) {
        try {
            this.deleteShipTemplate(shipTemplateDTO.getId());

            QueryWrapper<EsFreightTemplateDetail> deleteDetailWrapper = new QueryWrapper<>();
            deleteDetailWrapper.lambda().in(EsFreightTemplateDetail::getModeId, shipTemplateDTO.getId());
            this.esFreightTemplateDetailMapper.delete(deleteDetailWrapper);
            QueryWrapper<EsShipCompanyDetails> deleteCompanyWrapper = new QueryWrapper<>();
            deleteCompanyWrapper.lambda().in(EsShipCompanyDetails::getModeId, shipTemplateDTO.getId());
            this.esShipCompanyDetailsMapper.delete(deleteCompanyWrapper);

            this.insertSellerShipTemplate(shipTemplateDTO);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("运费模板表更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable th) {
            logger.error("运费模板表更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    public DubboResult<EsShipTemplateDO> getShipTemplateByTemplateId(Long templateId) {
        try {
            EsShipTemplate esShipTemplate = this.shipTemplateMapper.selectById(templateId);
            if (esShipTemplate == null){
                throw new ArgumentException(TradeErrorCode.TEMPLATE_DATE_NOT_EXIST.getErrorCode(), TradeErrorCode.TEMPLATE_DATE_NOT_EXIST.getErrorMsg());
            }
            QueryWrapper<EsFreightTemplateDetail> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsFreightTemplateDetail::getModeId,templateId);
            EsShipTemplateDO esShipTemplateDO = new EsShipTemplateDO();
            BeanUtils.copyProperties(esShipTemplate,esShipTemplateDO);
            List<EsFreightTemplateDetail> esFreightTemplateDetails = this.esFreightTemplateDetailMapper.selectList(queryWrapper);
            if (CollectionUtils.isNotEmpty(esFreightTemplateDetails)) {
                List<EsSellerFreightTemplateDetailDO> esSellerFreightTemplateDetailDOS = BeanUtil.copyList(esFreightTemplateDetails, EsSellerFreightTemplateDetailDO.class);
                esShipTemplateDO.setDetailList(esSellerFreightTemplateDetailDOS);
            }
            return DubboResult.success(esShipTemplateDO);
        } catch (ArgumentException e) {
            return DubboResult.fail(e.getExceptionCode(),e.getMessage());
        } catch (BeansException e) {
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
    @Override
    public DubboResult deleteShipTemplate(Long id) {
        try {
            QueryWrapper<EsShipTemplate> deleteTemplateWrapper = new QueryWrapper<>();
            deleteTemplateWrapper.lambda().eq(EsShipTemplate::getId, id);
            this.shipTemplateMapper.delete(deleteTemplateWrapper);
            QueryWrapper<EsFreightTemplateDetail> deleteDetailWrapper = new QueryWrapper<>();
            deleteDetailWrapper.lambda().eq(EsFreightTemplateDetail::getModeId, id);
            this.esFreightTemplateDetailMapper.delete(deleteDetailWrapper);

            ShipCompanyMsg shipCompanyMsg=new ShipCompanyMsg();
            shipCompanyMsg.setState(2);
            // 由于消息系统不稳定 暂时使用代码实现
//            mqProducer.send(ship_template_topic, JsonUtil.objectToJson(shipCompanyMsg));
            // 删除
            QueryWrapper<EsShipCompanyDetails> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().in(EsShipCompanyDetails::getModeId, id);
            esShipCompanyDetailsMapper.delete(queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("运费模板表查询删除失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable th) {
            logger.error("运费模板表查询删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

/*
    public static void main(String[] args) {

        String body = "{\"esShipTemplateDTO\":{\"id\":null,\"modeName\":\"杭州市测试\",\"createTime\":null,\"updateTime\":null,\"isDel\":null,\"isFresh\":2,\"shopId\":14,\"logiId\":5,\"logiName\":null,\"sign\":0,\"freightDetailList\":[{\"id\":null,\"modeId\":null,\"area\":\"[{\\\"id\\\":\\\"1213\\\",\\\"parentId\\\":\\\"15\\\",\\\"regionGrade\\\":2,\\\"localName\\\":\\\"杭州市\\\"},{\\\"id\\\":\\\"1214\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"余杭区\\\"},{\\\"id\\\":\\\"1215\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"萧山区\\\"},{\\\"id\\\":\\\"1217\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"富阳区\\\"},{\\\"id\\\":\\\"1218\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"桐庐县\\\"},{\\\"id\\\":\\\"1219\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"建德市\\\"},{\\\"id\\\":\\\"1220\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"淳安县\\\"},{\\\"id\\\":\\\"2963\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"江干区\\\"},{\\\"id\\\":\\\"3038\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"滨江区\\\"},{\\\"id\\\":\\\"3408\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"上城区\\\"},{\\\"id\\\":\\\"3409\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"下城区\\\"},{\\\"id\\\":\\\"3410\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"拱墅区\\\"},{\\\"id\\\":\\\"3411\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"西湖区\\\"},{\\\"id\\\":\\\"4285\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"下沙区\\\"},{\\\"id\\\":\\\"49711\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"临安市\\\"}]\",\"firstWeight\":1.0,\"firstTip\":5.0,\"sequelWeight\":1.0,\"sequelTip\":2.0,\"isDel\":null,\"createTime\":null,\"updateTime\":null,\"minMoney\":0.0,\"maxMoney\":50.0,\"areaId\":\"[\\\"1213\\\",\\\"1214\\\",\\\"1215\\\",\\\"1217\\\",\\\"1218\\\",\\\"1219\\\",\\\"1220\\\",\\\"2963\\\",\\\"3038\\\",\\\"3408\\\",\\\"3409\\\",\\\"3410\\\",\\\"3411\\\",\\\"4285\\\",\\\"49711\\\"]\",\"areaJson\":\"[{\\\"id\\\":\\\"1213\\\",\\\"parentId\\\":\\\"15\\\",\\\"regionGrade\\\":2,\\\"localName\\\":\\\"杭州市\\\"},{\\\"id\\\":\\\"1214\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"余杭区\\\"},{\\\"id\\\":\\\"1215\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"萧山区\\\"},{\\\"id\\\":\\\"1217\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"富阳区\\\"},{\\\"id\\\":\\\"1218\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"桐庐县\\\"},{\\\"id\\\":\\\"1219\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"建德市\\\"},{\\\"id\\\":\\\"1220\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"淳安县\\\"},{\\\"id\\\":\\\"2963\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"江干区\\\"},{\\\"id\\\":\\\"3038\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"滨江区\\\"},{\\\"id\\\":\\\"3408\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"上城区\\\"},{\\\"id\\\":\\\"3409\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"下城区\\\"},{\\\"id\\\":\\\"3410\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"拱墅区\\\"},{\\\"id\\\":\\\"3411\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"西湖区\\\"},{\\\"id\\\":\\\"4285\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"下沙区\\\"},{\\\"id\\\":\\\"49711\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"临安市\\\"}]\"},{\"id\":null,\"modeId\":null,\"area\":\"[{\\\"id\\\":\\\"1213\\\",\\\"parentId\\\":\\\"15\\\",\\\"regionGrade\\\":2,\\\"localName\\\":\\\"杭州市\\\"},{\\\"id\\\":\\\"1214\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"余杭区\\\"},{\\\"id\\\":\\\"1215\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"萧山区\\\"},{\\\"id\\\":\\\"1217\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"富阳区\\\"},{\\\"id\\\":\\\"1218\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"桐庐县\\\"},{\\\"id\\\":\\\"1219\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"建德市\\\"},{\\\"id\\\":\\\"1220\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"淳安县\\\"},{\\\"id\\\":\\\"2963\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"江干区\\\"},{\\\"id\\\":\\\"3038\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"滨江区\\\"},{\\\"id\\\":\\\"3408\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"上城区\\\"},{\\\"id\\\":\\\"3409\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"下城区\\\"},{\\\"id\\\":\\\"3410\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"拱墅区\\\"},{\\\"id\\\":\\\"3411\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"西湖区\\\"},{\\\"id\\\":\\\"4285\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"下沙区\\\"},{\\\"id\\\":\\\"49711\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"临安市\\\"}]\",\"firstWeight\":1.0,\"firstTip\":5.0,\"sequelWeight\":1.0,\"sequelTip\":3.0,\"isDel\":null,\"createTime\":null,\"updateTime\":null,\"minMoney\":50.0,\"maxMoney\":100.0,\"areaId\":\"[\\\"1213\\\",\\\"1214\\\",\\\"1215\\\",\\\"1217\\\",\\\"1218\\\",\\\"1219\\\",\\\"1220\\\",\\\"2963\\\",\\\"3038\\\",\\\"3408\\\",\\\"3409\\\",\\\"3410\\\",\\\"3411\\\",\\\"4285\\\",\\\"49711\\\"]\",\"areaJson\":\"[{\\\"id\\\":\\\"1213\\\",\\\"parentId\\\":\\\"15\\\",\\\"regionGrade\\\":2,\\\"localName\\\":\\\"杭州市\\\"},{\\\"id\\\":\\\"1214\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"余杭区\\\"},{\\\"id\\\":\\\"1215\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"萧山区\\\"},{\\\"id\\\":\\\"1217\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"富阳区\\\"},{\\\"id\\\":\\\"1218\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"桐庐县\\\"},{\\\"id\\\":\\\"1219\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"建德市\\\"},{\\\"id\\\":\\\"1220\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"淳安县\\\"},{\\\"id\\\":\\\"2963\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"江干区\\\"},{\\\"id\\\":\\\"3038\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"滨江区\\\"},{\\\"id\\\":\\\"3408\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"上城区\\\"},{\\\"id\\\":\\\"3409\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"下城区\\\"},{\\\"id\\\":\\\"3410\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"拱墅区\\\"},{\\\"id\\\":\\\"3411\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"西湖区\\\"},{\\\"id\\\":\\\"4285\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"下沙区\\\"},{\\\"id\\\":\\\"49711\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"临安市\\\"}]\"},{\"id\":null,\"modeId\":null,\"area\":\"[{\\\"id\\\":\\\"1213\\\",\\\"parentId\\\":\\\"15\\\",\\\"regionGrade\\\":2,\\\"localName\\\":\\\"杭州市\\\"},{\\\"id\\\":\\\"1214\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"余杭区\\\"},{\\\"id\\\":\\\"1215\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"萧山区\\\"},{\\\"id\\\":\\\"1217\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"富阳区\\\"},{\\\"id\\\":\\\"1218\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"桐庐县\\\"},{\\\"id\\\":\\\"1219\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"建德市\\\"},{\\\"id\\\":\\\"1220\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"淳安县\\\"},{\\\"id\\\":\\\"2963\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"江干区\\\"},{\\\"id\\\":\\\"3038\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"滨江区\\\"},{\\\"id\\\":\\\"3408\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"上城区\\\"},{\\\"id\\\":\\\"3409\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"下城区\\\"},{\\\"id\\\":\\\"3410\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"拱墅区\\\"},{\\\"id\\\":\\\"3411\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"西湖区\\\"},{\\\"id\\\":\\\"4285\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"下沙区\\\"},{\\\"id\\\":\\\"49711\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"临安市\\\"}]\",\"firstWeight\":1.0,\"firstTip\":5.0,\"sequelWeight\":1.0,\"sequelTip\":4.0,\"isDel\":null,\"createTime\":null,\"updateTime\":null,\"minMoney\":100.0,\"maxMoney\":200.0,\"areaId\":\"[\\\"1213\\\",\\\"1214\\\",\\\"1215\\\",\\\"1217\\\",\\\"1218\\\",\\\"1219\\\",\\\"1220\\\",\\\"2963\\\",\\\"3038\\\",\\\"3408\\\",\\\"3409\\\",\\\"3410\\\",\\\"3411\\\",\\\"4285\\\",\\\"49711\\\"]\",\"areaJson\":\"[{\\\"id\\\":\\\"1213\\\",\\\"parentId\\\":\\\"15\\\",\\\"regionGrade\\\":2,\\\"localName\\\":\\\"杭州市\\\"},{\\\"id\\\":\\\"1214\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"余杭区\\\"},{\\\"id\\\":\\\"1215\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"萧山区\\\"},{\\\"id\\\":\\\"1217\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"富阳区\\\"},{\\\"id\\\":\\\"1218\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"桐庐县\\\"},{\\\"id\\\":\\\"1219\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"建德市\\\"},{\\\"id\\\":\\\"1220\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"淳安县\\\"},{\\\"id\\\":\\\"2963\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"江干区\\\"},{\\\"id\\\":\\\"3038\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"滨江区\\\"},{\\\"id\\\":\\\"3408\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"上城区\\\"},{\\\"id\\\":\\\"3409\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"下城区\\\"},{\\\"id\\\":\\\"3410\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"拱墅区\\\"},{\\\"id\\\":\\\"3411\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"西湖区\\\"},{\\\"id\\\":\\\"4285\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"下沙区\\\"},{\\\"id\\\":\\\"49711\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"临安市\\\"}]\"},{\"id\":null,\"modeId\":null,\"area\":\"[{\\\"id\\\":\\\"1213\\\",\\\"parentId\\\":\\\"15\\\",\\\"regionGrade\\\":2,\\\"localName\\\":\\\"杭州市\\\"},{\\\"id\\\":\\\"1214\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"余杭区\\\"},{\\\"id\\\":\\\"1215\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"萧山区\\\"},{\\\"id\\\":\\\"1217\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"富阳区\\\"},{\\\"id\\\":\\\"1218\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"桐庐县\\\"},{\\\"id\\\":\\\"1219\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"建德市\\\"},{\\\"id\\\":\\\"1220\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"淳安县\\\"},{\\\"id\\\":\\\"2963\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"江干区\\\"},{\\\"id\\\":\\\"3038\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"滨江区\\\"},{\\\"id\\\":\\\"3408\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"上城区\\\"},{\\\"id\\\":\\\"3409\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"下城区\\\"},{\\\"id\\\":\\\"3410\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"拱墅区\\\"},{\\\"id\\\":\\\"3411\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"西湖区\\\"},{\\\"id\\\":\\\"4285\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"下沙区\\\"},{\\\"id\\\":\\\"49711\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"临安市\\\"}]\",\"firstWeight\":1.0,\"firstTip\":5.0,\"sequelWeight\":1.0,\"sequelTip\":5.0,\"isDel\":null,\"createTime\":null,\"updateTime\":null,\"minMoney\":200.0,\"maxMoney\":300.0,\"areaId\":\"[\\\"1213\\\",\\\"1214\\\",\\\"1215\\\",\\\"1217\\\",\\\"1218\\\",\\\"1219\\\",\\\"1220\\\",\\\"2963\\\",\\\"3038\\\",\\\"3408\\\",\\\"3409\\\",\\\"3410\\\",\\\"3411\\\",\\\"4285\\\",\\\"49711\\\"]\",\"areaJson\":\"[{\\\"id\\\":\\\"1213\\\",\\\"parentId\\\":\\\"15\\\",\\\"regionGrade\\\":2,\\\"localName\\\":\\\"杭州市\\\"},{\\\"id\\\":\\\"1214\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"余杭区\\\"},{\\\"id\\\":\\\"1215\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"萧山区\\\"},{\\\"id\\\":\\\"1217\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"富阳区\\\"},{\\\"id\\\":\\\"1218\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"桐庐县\\\"},{\\\"id\\\":\\\"1219\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"建德市\\\"},{\\\"id\\\":\\\"1220\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"淳安县\\\"},{\\\"id\\\":\\\"2963\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"江干区\\\"},{\\\"id\\\":\\\"3038\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"滨江区\\\"},{\\\"id\\\":\\\"3408\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"上城区\\\"},{\\\"id\\\":\\\"3409\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"下城区\\\"},{\\\"id\\\":\\\"3410\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"拱墅区\\\"},{\\\"id\\\":\\\"3411\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"西湖区\\\"},{\\\"id\\\":\\\"4285\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"下沙区\\\"},{\\\"id\\\":\\\"49711\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"临安市\\\"}]\"},{\"id\":null,\"modeId\":null,\"area\":\"[{\\\"id\\\":\\\"1213\\\",\\\"parentId\\\":\\\"15\\\",\\\"regionGrade\\\":2,\\\"localName\\\":\\\"杭州市\\\"},{\\\"id\\\":\\\"1214\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"余杭区\\\"},{\\\"id\\\":\\\"1215\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"萧山区\\\"},{\\\"id\\\":\\\"1217\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"富阳区\\\"},{\\\"id\\\":\\\"1218\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"桐庐县\\\"},{\\\"id\\\":\\\"1219\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"建德市\\\"},{\\\"id\\\":\\\"1220\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"淳安县\\\"},{\\\"id\\\":\\\"2963\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"江干区\\\"},{\\\"id\\\":\\\"3038\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"滨江区\\\"},{\\\"id\\\":\\\"3408\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"上城区\\\"},{\\\"id\\\":\\\"3409\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"下城区\\\"},{\\\"id\\\":\\\"3410\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"拱墅区\\\"},{\\\"id\\\":\\\"3411\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"西湖区\\\"},{\\\"id\\\":\\\"4285\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"下沙区\\\"},{\\\"id\\\":\\\"49711\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"临安市\\\"}]\",\"firstWeight\":1.0,\"firstTip\":5.0,\"sequelWeight\":1.0,\"sequelTip\":6.0,\"isDel\":null,\"createTime\":null,\"updateTime\":null,\"minMoney\":300.0,\"maxMoney\":400.0,\"areaId\":\"[\\\"1213\\\",\\\"1214\\\",\\\"1215\\\",\\\"1217\\\",\\\"1218\\\",\\\"1219\\\",\\\"1220\\\",\\\"2963\\\",\\\"3038\\\",\\\"3408\\\",\\\"3409\\\",\\\"3410\\\",\\\"3411\\\",\\\"4285\\\",\\\"49711\\\"]\",\"areaJson\":\"[{\\\"id\\\":\\\"1213\\\",\\\"parentId\\\":\\\"15\\\",\\\"regionGrade\\\":2,\\\"localName\\\":\\\"杭州市\\\"},{\\\"id\\\":\\\"1214\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"余杭区\\\"},{\\\"id\\\":\\\"1215\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"萧山区\\\"},{\\\"id\\\":\\\"1217\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"富阳区\\\"},{\\\"id\\\":\\\"1218\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"桐庐县\\\"},{\\\"id\\\":\\\"1219\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"建德市\\\"},{\\\"id\\\":\\\"1220\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"淳安县\\\"},{\\\"id\\\":\\\"2963\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"江干区\\\"},{\\\"id\\\":\\\"3038\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"滨江区\\\"},{\\\"id\\\":\\\"3408\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"上城区\\\"},{\\\"id\\\":\\\"3409\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"下城区\\\"},{\\\"id\\\":\\\"3410\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"拱墅区\\\"},{\\\"id\\\":\\\"3411\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"西湖区\\\"},{\\\"id\\\":\\\"4285\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"下沙区\\\"},{\\\"id\\\":\\\"49711\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"临安市\\\"}]\"},{\"id\":null,\"modeId\":null,\"area\":\"[{\\\"id\\\":\\\"1213\\\",\\\"parentId\\\":\\\"15\\\",\\\"regionGrade\\\":2,\\\"localName\\\":\\\"杭州市\\\"},{\\\"id\\\":\\\"1214\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"余杭区\\\"},{\\\"id\\\":\\\"1215\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"萧山区\\\"},{\\\"id\\\":\\\"1217\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"富阳区\\\"},{\\\"id\\\":\\\"1218\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"桐庐县\\\"},{\\\"id\\\":\\\"1219\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"建德市\\\"},{\\\"id\\\":\\\"1220\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"淳安县\\\"},{\\\"id\\\":\\\"2963\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"江干区\\\"},{\\\"id\\\":\\\"3038\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"滨江区\\\"},{\\\"id\\\":\\\"3408\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"上城区\\\"},{\\\"id\\\":\\\"3409\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"下城区\\\"},{\\\"id\\\":\\\"3410\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"拱墅区\\\"},{\\\"id\\\":\\\"3411\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"西湖区\\\"},{\\\"id\\\":\\\"4285\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"下沙区\\\"},{\\\"id\\\":\\\"49711\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"临安市\\\"}]\",\"firstWeight\":1.0,\"firstTip\":5.0,\"sequelWeight\":1.0,\"sequelTip\":7.0,\"isDel\":null,\"createTime\":null,\"updateTime\":null,\"minMoney\":400.0,\"maxMoney\":500.0,\"areaId\":\"[\\\"1213\\\",\\\"1214\\\",\\\"1215\\\",\\\"1217\\\",\\\"1218\\\",\\\"1219\\\",\\\"1220\\\",\\\"2963\\\",\\\"3038\\\",\\\"3408\\\",\\\"3409\\\",\\\"3410\\\",\\\"3411\\\",\\\"4285\\\",\\\"49711\\\"]\",\"areaJson\":\"[{\\\"id\\\":\\\"1213\\\",\\\"parentId\\\":\\\"15\\\",\\\"regionGrade\\\":2,\\\"localName\\\":\\\"杭州市\\\"},{\\\"id\\\":\\\"1214\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"余杭区\\\"},{\\\"id\\\":\\\"1215\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"萧山区\\\"},{\\\"id\\\":\\\"1217\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"富阳区\\\"},{\\\"id\\\":\\\"1218\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"桐庐县\\\"},{\\\"id\\\":\\\"1219\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"建德市\\\"},{\\\"id\\\":\\\"1220\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"淳安县\\\"},{\\\"id\\\":\\\"2963\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"江干区\\\"},{\\\"id\\\":\\\"3038\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"滨江区\\\"},{\\\"id\\\":\\\"3408\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"上城区\\\"},{\\\"id\\\":\\\"3409\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"下城区\\\"},{\\\"id\\\":\\\"3410\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"拱墅区\\\"},{\\\"id\\\":\\\"3411\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"西湖区\\\"},{\\\"id\\\":\\\"4285\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"下沙区\\\"},{\\\"id\\\":\\\"49711\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"临安市\\\"}]\"},{\"id\":null,\"modeId\":null,\"area\":\"[{\\\"id\\\":\\\"1213\\\",\\\"parentId\\\":\\\"15\\\",\\\"regionGrade\\\":2,\\\"localName\\\":\\\"杭州市\\\"},{\\\"id\\\":\\\"1214\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"余杭区\\\"},{\\\"id\\\":\\\"1215\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"萧山区\\\"},{\\\"id\\\":\\\"1217\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"富阳区\\\"},{\\\"id\\\":\\\"1218\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"桐庐县\\\"},{\\\"id\\\":\\\"1219\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"建德市\\\"},{\\\"id\\\":\\\"1220\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"淳安县\\\"},{\\\"id\\\":\\\"2963\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"江干区\\\"},{\\\"id\\\":\\\"3038\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"滨江区\\\"},{\\\"id\\\":\\\"3408\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"上城区\\\"},{\\\"id\\\":\\\"3409\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"下城区\\\"},{\\\"id\\\":\\\"3410\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"拱墅区\\\"},{\\\"id\\\":\\\"3411\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"西湖区\\\"},{\\\"id\\\":\\\"4285\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"下沙区\\\"},{\\\"id\\\":\\\"49711\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"临安市\\\"}]\",\"firstWeight\":1.0,\"firstTip\":5.0,\"sequelWeight\":1.0,\"sequelTip\":8.0,\"isDel\":null,\"createTime\":null,\"updateTime\":null,\"minMoney\":500.0,\"maxMoney\":10000.0,\"areaId\":\"[\\\"1213\\\",\\\"1214\\\",\\\"1215\\\",\\\"1217\\\",\\\"1218\\\",\\\"1219\\\",\\\"1220\\\",\\\"2963\\\",\\\"3038\\\",\\\"3408\\\",\\\"3409\\\",\\\"3410\\\",\\\"3411\\\",\\\"4285\\\",\\\"49711\\\"]\",\"areaJson\":\"[{\\\"id\\\":\\\"1213\\\",\\\"parentId\\\":\\\"15\\\",\\\"regionGrade\\\":2,\\\"localName\\\":\\\"杭州市\\\"},{\\\"id\\\":\\\"1214\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"余杭区\\\"},{\\\"id\\\":\\\"1215\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"萧山区\\\"},{\\\"id\\\":\\\"1217\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"富阳区\\\"},{\\\"id\\\":\\\"1218\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"桐庐县\\\"},{\\\"id\\\":\\\"1219\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"建德市\\\"},{\\\"id\\\":\\\"1220\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"淳安县\\\"},{\\\"id\\\":\\\"2963\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"江干区\\\"},{\\\"id\\\":\\\"3038\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"滨江区\\\"},{\\\"id\\\":\\\"3408\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"上城区\\\"},{\\\"id\\\":\\\"3409\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"下城区\\\"},{\\\"id\\\":\\\"3410\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"拱墅区\\\"},{\\\"id\\\":\\\"3411\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"西湖区\\\"},{\\\"id\\\":\\\"4285\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"下沙区\\\"},{\\\"id\\\":\\\"49711\\\",\\\"parentId\\\":\\\"1213\\\",\\\"regionGrade\\\":3,\\\"localName\\\":\\\"临安市\\\"}]\"}],\"esShipCompanyDetailsList\":[{\"id\":null,\"areaId\":\"[\\\"1213\\\",\\\"1214\\\",\\\"1215\\\",\\\"1217\\\",\\\"1218\\\",\\\"1219\\\",\\\"1220\\\",\\\"2963\\\",\\\"3038\\\",\\\"3408\\\",\\\"3409\\\",\\\"3410\\\",\\\"3411\\\",\\\"4285\\\",\\\"49711\\\"]\",\"type\":2,\"lowerPrice\":0.0,\"highPrice\":50.0,\"firstCompany\":1.0,\"firstPrice\":5.0,\"continuedCompany\":1.0,\"continuedPrice\":2.0},{\"id\":null,\"areaId\":\"[\\\"1213\\\",\\\"1214\\\",\\\"1215\\\",\\\"1217\\\",\\\"1218\\\",\\\"1219\\\",\\\"1220\\\",\\\"2963\\\",\\\"3038\\\",\\\"3408\\\",\\\"3409\\\",\\\"3410\\\",\\\"3411\\\",\\\"4285\\\",\\\"49711\\\"]\",\"type\":2,\"lowerPrice\":50.0,\"highPrice\":100.0,\"firstCompany\":1.0,\"firstPrice\":5.0,\"continuedCompany\":1.0,\"continuedPrice\":3.0},{\"id\":null,\"areaId\":\"[\\\"1213\\\",\\\"1214\\\",\\\"1215\\\",\\\"1217\\\",\\\"1218\\\",\\\"1219\\\",\\\"1220\\\",\\\"2963\\\",\\\"3038\\\",\\\"3408\\\",\\\"3409\\\",\\\"3410\\\",\\\"3411\\\",\\\"4285\\\",\\\"49711\\\"]\",\"type\":2,\"lowerPrice\":100.0,\"highPrice\":200.0,\"firstCompany\":1.0,\"firstPrice\":5.0,\"continuedCompany\":1.0,\"continuedPrice\":4.0},{\"id\":null,\"areaId\":\"[\\\"1213\\\",\\\"1214\\\",\\\"1215\\\",\\\"1217\\\",\\\"1218\\\",\\\"1219\\\",\\\"1220\\\",\\\"2963\\\",\\\"3038\\\",\\\"3408\\\",\\\"3409\\\",\\\"3410\\\",\\\"3411\\\",\\\"4285\\\",\\\"49711\\\"]\",\"type\":2,\"lowerPrice\":200.0,\"highPrice\":300.0,\"firstCompany\":1.0,\"firstPrice\":5.0,\"continuedCompany\":1.0,\"continuedPrice\":5.0},{\"id\":null,\"areaId\":\"[\\\"1213\\\",\\\"1214\\\",\\\"1215\\\",\\\"1217\\\",\\\"1218\\\",\\\"1219\\\",\\\"1220\\\",\\\"2963\\\",\\\"3038\\\",\\\"3408\\\",\\\"3409\\\",\\\"3410\\\",\\\"3411\\\",\\\"4285\\\",\\\"49711\\\"]\",\"type\":2,\"lowerPrice\":300.0,\"highPrice\":400.0,\"firstCompany\":1.0,\"firstPrice\":5.0,\"continuedCompany\":1.0,\"continuedPrice\":6.0},{\"id\":null,\"areaId\":\"[\\\"1213\\\",\\\"1214\\\",\\\"1215\\\",\\\"1217\\\",\\\"1218\\\",\\\"1219\\\",\\\"1220\\\",\\\"2963\\\",\\\"3038\\\",\\\"3408\\\",\\\"3409\\\",\\\"3410\\\",\\\"3411\\\",\\\"4285\\\",\\\"49711\\\"]\",\"type\":2,\"lowerPrice\":400.0,\"highPrice\":500.0,\"firstCompany\":1.0,\"firstPrice\":5.0,\"continuedCompany\":1.0,\"continuedPrice\":7.0},{\"id\":null,\"areaId\":\"[\\\"1213\\\",\\\"1214\\\",\\\"1215\\\",\\\"1217\\\",\\\"1218\\\",\\\"1219\\\",\\\"1220\\\",\\\"2963\\\",\\\"3038\\\",\\\"3408\\\",\\\"3409\\\",\\\"3410\\\",\\\"3411\\\",\\\"4285\\\",\\\"49711\\\"]\",\"type\":2,\"lowerPrice\":500.0,\"highPrice\":10000.0,\"firstCompany\":1.0,\"firstPrice\":5.0,\"continuedCompany\":1.0,\"continuedPrice\":8.0}]},\"state\":0}";
                ShipCompanyMsg shipCompanyMsg = JsonUtil.jsonToObject(body, ShipCompanyMsg.class);
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
                    shipCompanyDetails.setAreaId(aId);
                    shipCompanyDetails.setType(esShipCompanyDetailsDTO.getType());
                    shipCompanyDetails.setLowerPrice(esShipCompanyDetailsDTO.getLowerPrice());
                    shipCompanyDetails.setHighPrice(esShipCompanyDetailsDTO.getHighPrice());
                    shipCompanyDetails.setFirstCompany(esShipCompanyDetailsDTO.getFirstCompany());
                    shipCompanyDetails.setFirstPrice(esShipCompanyDetailsDTO.getFirstPrice());
                    shipCompanyDetails.setContinuedCompany(esShipCompanyDetailsDTO.getContinuedCompany());
                    shipCompanyDetails.setContinuedPrice(esShipCompanyDetailsDTO.getContinuedPrice());
//                    esShipCompanyDetailsMapper.insert(shipCompanyDetails);
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
                    queryWrapper.lambda().eq(EsShipCompanyDetails::getAreaId,aId);
                    queryWrapper.lambda().eq(EsShipCompanyDetails::getType,type);
//                    esShipCompanyDetailsMapper.delete(queryWrapper);
                    EsShipCompanyDetails shipCompanyDetails=new EsShipCompanyDetails();
                    shipCompanyDetails.setAreaId(aId);
                    shipCompanyDetails.setType(esShipCompanyDetailsDTO.getType());
                    shipCompanyDetails.setLowerPrice(esShipCompanyDetailsDTO.getLowerPrice());
                    shipCompanyDetails.setHighPrice(esShipCompanyDetailsDTO.getHighPrice());
                    shipCompanyDetails.setFirstCompany(esShipCompanyDetailsDTO.getFirstCompany());
                    shipCompanyDetails.setFirstPrice(esShipCompanyDetailsDTO.getFirstPrice());
                    shipCompanyDetails.setContinuedCompany(esShipCompanyDetailsDTO.getContinuedCompany());
                    shipCompanyDetails.setContinuedPrice(esShipCompanyDetailsDTO.getContinuedPrice());
//                    esShipCompanyDetailsMapper.insert(shipCompanyDetails);
                }
            }
        }else {
            // 删除
            for (EsShipCompanyDetailsDTO esShipCompanyDetailsDTO:esShipCompanyDetailsList
            ) {
                String areaId = esShipCompanyDetailsDTO.getAreaId();
                List<Long> split = JsonUtil.jsonToList(areaId, Long.class);
                for (Long aId:split) {
                    QueryWrapper<EsShipCompanyDetails> queryWrapper = new QueryWrapper<>();
                    queryWrapper.lambda().eq(EsShipCompanyDetails::getAreaId,aId);
                    queryWrapper.lambda().eq(EsShipCompanyDetails::getType,type);
//                    esShipCompanyDetailsMapper.delete(queryWrapper);
                }
            }
            // 删除其他关联数据

//            shipTemplateMapper.deleteById(esShipTemplateDTO.getId());

            List<EsFreightTemplateDetailDTO> freightDetailList = esShipTemplateDTO.getFreightDetailList();
            for (EsFreightTemplateDetailDTO esFreightTemplateDetailDTO:freightDetailList
            ) {
//                esFreightTemplateDetailMapper.deleteById(esFreightTemplateDetailDTO.getId());
            }

        }
//        return DubboResult.success();
    }*/

}
