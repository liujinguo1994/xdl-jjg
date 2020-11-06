package com.xdl.jjg.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jjg.member.model.domain.EsCompanyDO;
import com.jjg.shop.model.co.EsGoodsSkuCO;
import com.jjg.trade.model.domain.EsDeliveryMessageDO;
import com.jjg.trade.model.domain.EsDeliveryServiceDO;
import com.jjg.trade.model.domain.EsSelfDateDO;
import com.jjg.trade.model.domain.EsSelfTimeDO;
import com.jjg.trade.model.dto.EsDeliveryMessageDTO;
import com.jjg.trade.model.dto.EsDeliveryServiceDTO;
import com.xdl.jjg.constant.TradeErrorCode;
import com.xdl.jjg.entity.*;
import com.xdl.jjg.mapper.EsDeliveryDateMapper;
import com.xdl.jjg.mapper.EsDeliveryServiceMapper;
import com.xdl.jjg.mapper.EsSelfDateMapper;
import com.xdl.jjg.mapper.EsSelfTimeMapper;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.util.SnowflakeIdWorker;
import com.xdl.jjg.web.service.IEsDeliveryServiceService;
import com.xdl.jjg.web.service.feign.member.CompanyService;
import com.xdl.jjg.web.service.feign.member.MemberService;
import com.xdl.jjg.web.service.feign.shop.GoodsSkuService;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
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
 * 自提点信息维护 服务实现类
 * </p>
 *
 * @author LJG
 * @since 2019-06-05 09:20:40
 */
@Service
public class EsDeliveryServiceServiceImpl extends ServiceImpl<EsDeliveryServiceMapper, EsDeliveryService> implements IEsDeliveryServiceService {

    private static Logger logger = LoggerFactory.getLogger(EsDeliveryServiceServiceImpl.class);

    @Autowired
    private EsDeliveryServiceMapper deliveryServiceMapper;

    @Autowired
    private EsSelfDateMapper selfDateMapper;

    @Autowired
    private EsSelfTimeMapper selfTimeMapper;

    @Autowired
    private EsDeliveryDateMapper deliveryDateMapper;
    @Autowired
    private MemberService esMemberService;
    @Autowired
    private CompanyService esCompanyService;
    @Autowired
    private GoodsSkuService esGoodsSkuService;

    private static SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker(0,0);
    // TODO 调用店铺接口

    /**
     * 系统后台
     *  自提点新增
     * 插入自提点信息维护数据
     *
     * @param deliveryServiceDTO 自提点信息维护DTO
     * @auther: Liujg
     * @date: 2019/0702 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsDeliveryServiceDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertDeliveryService(EsDeliveryServiceDTO deliveryServiceDTO) {
        try {
                //测试数据
//            deliveryServiceDTO.setId(snowflakeIdWorker.nextId());
//            deliveryServiceDTO.setAddress("测试1");
//            deliveryServiceDTO.setDeliveryName("自提点1");
//
//            deliveryServiceDTO.setState(0);
//            List<Long> longList = new ArrayList<>();
//            longList.add(1l);
//            longList.add(2l);
//            deliveryServiceDTO.setSelfDateId(longList);

            EsDeliveryService deliveryService = new EsDeliveryService();
            BeanUtil.copyProperties(deliveryServiceDTO, deliveryService);
            this.deliveryServiceMapper.insert(deliveryService);
            List<Long> selfDateId = deliveryServiceDTO.getSelfDateId();
            selfDateId.forEach(id ->{
                EsDeliveryDate esDeliveryDate = new EsDeliveryDate();
                esDeliveryDate.setId(snowflakeIdWorker.nextId());
                esDeliveryDate.setDateId(id);
                esDeliveryDate.setDeliveryId(deliveryService.getId());
                this.deliveryDateMapper.insert(esDeliveryDate);
            });
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("自提点信息维护新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable ae) {
            logger.error("自提点信息维护新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 系统后台
     * 根据条件更新自提点信息维护数据
     *
     * @param deliveryServiceDTO 自提点信息维护DTO
     * @param id                          主键id
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsDeliveryServiceDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateDeliveryService(EsDeliveryServiceDTO deliveryServiceDTO, Long id) {
        try {
//            id = 15l;
//            //测试数据
//            deliveryServiceDTO.setId(snowflakeIdWorker.nextId());
//            deliveryServiceDTO.setAddress("测试22");
//            deliveryServiceDTO.setDeliveryName("自提点22");
//            deliveryServiceDTO.setCompanyId(1l);
//            deliveryServiceDTO.setCompanyName("gongsie");
//
//            deliveryServiceDTO.setState(0);
//            List<Long> longList = new ArrayList<>();
//            longList.add(22l);
//            longList.add(23l);
//            deliveryServiceDTO.setSelfDateId(longList);
            EsDeliveryService deliveryService = this.deliveryServiceMapper.selectById(id);
            if (deliveryService == null) {
                throw new ArgumentException(TradeErrorCode.DATA_NOT_EXIST.getErrorCode(), TradeErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            //  根据自提点Id进行解绑关联关系。
            QueryWrapper<EsDeliveryDate> deliveryDateDeleteWrapper = new QueryWrapper<>();
            deliveryDateDeleteWrapper.lambda().eq(EsDeliveryDate::getDeliveryId,id);
            this.deliveryDateMapper.delete(deliveryDateDeleteWrapper);
            //  重新遍历 时间点ID集合 进行重新绑定
            List<Long> selfDateId = deliveryServiceDTO.getSelfDateId();
            selfDateId.forEach(id1 ->{
                EsDeliveryDate esDeliveryDate = new EsDeliveryDate();
                esDeliveryDate.setId(snowflakeIdWorker.nextId());
                esDeliveryDate.setDateId(id1);
                esDeliveryDate.setDeliveryId(deliveryService.getId());
                this.deliveryDateMapper.insert(esDeliveryDate);
            });
            //  然后进行更新自提点信息
            BeanUtil.copyProperties(deliveryServiceDTO, deliveryService);
            QueryWrapper<EsDeliveryService> queryUpdateWrapper = new QueryWrapper<>();
            queryUpdateWrapper.lambda().eq(EsDeliveryService::getId, id);
            this.deliveryServiceMapper.update(deliveryService, queryUpdateWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("自提点信息维护更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("自提点信息维护更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     *   系统后台
     * 根据id获取自提点信息维护详情
     *
     * @param id 主键id
     * @auther: Liujg
     * @date: 2019/0702 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsDeliveryServiceDO>
     */
    @Override
    public DubboResult<EsDeliveryServiceDO> getDeliveryService(Long id) {
        try {
            EsDeliveryService deliveryService = this.deliveryServiceMapper.selectById(id);
            if (deliveryService == null) {
                throw new ArgumentException(TradeErrorCode.DATA_NOT_EXIST.getErrorCode(), TradeErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            EsDeliveryServiceDO deliveryServiceDO = new EsDeliveryServiceDO();

            QueryWrapper<EsSelfDate> queryDateWrapper = new QueryWrapper<>();
            queryDateWrapper.lambda().eq(EsSelfDate::getState,0);//0 表示有效
            // 查询所有状态为有效时间点
            // 根据自提点和日期关联表 检索出选中状态
            List<EsSelfDate> esSelfDates = this.selfDateMapper.selectDateList(queryDateWrapper,id);
            List<EsSelfDateDO> esSelfDateDOList = new ArrayList<>();

            esSelfDateDOList = esSelfDates.stream().map(esSelfDate -> {
                EsSelfDateDO esSelfDateDO = new EsSelfDateDO();
                BeanUtils.copyProperties(esSelfDate,esSelfDateDO);
                return esSelfDateDO;
            }).collect(Collectors.toList());
            BeanUtil.copyProperties(deliveryService, deliveryServiceDO);

            deliveryServiceDO.setSelfDateDOList(esSelfDateDOList);
            return DubboResult.success(deliveryServiceDO);
        } catch (ArgumentException ae){
            logger.error("自提点信息维护查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("自提点信息维护查询失败", th);
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 系统后台
     * 根据查询自提点信息维护列表
     *
     * @param deliveryServiceDTO 自提点信息维护DTO
     * @param pageSize     页码
     * @param pageNum      页数
     * @auther: libw 981087977@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsDeliveryServiceDO>
     */
    @Override
    public DubboPageResult<EsDeliveryServiceDO> getDeliveryServiceList(EsDeliveryServiceDTO deliveryServiceDTO, int pageSize, int pageNum) {
        QueryWrapper<EsDeliveryService> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件
            Page<EsDeliveryService> page = new Page<>(pageNum, pageSize);
            IPage<EsDeliveryService> iPage = this.page(page, queryWrapper);
            List<EsDeliveryServiceDO> deliveryServiceDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                deliveryServiceDOList = iPage.getRecords().stream().map(deliveryService -> {
                    EsDeliveryServiceDO deliveryServiceDO = new EsDeliveryServiceDO();
                    BeanUtil.copyProperties(deliveryService, deliveryServiceDO);
                    return deliveryServiceDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(deliveryServiceDOList);
        } catch (ArgumentException ae){
            logger.error("自提点信息维护分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("自提点信息维护分页查询失败", th);
            return DubboPageResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     *  系统后台
     * 根据主键删除自提点信息维护数据
     *
     * @param id 主键id
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsDeliveryServiceDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteDeliveryService(Long id) {
        try {
            // 删除该自提点
            this.deliveryServiceMapper.deleteById(id);
            // 删除关联关系
            QueryWrapper<EsDeliveryDate> deliveryDateDeleteWrapper = new QueryWrapper<>();
            deliveryDateDeleteWrapper.lambda().eq(EsDeliveryDate::getDeliveryId,id);
            this.deliveryDateMapper.delete(deliveryDateDeleteWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae){
             logger.error("自提点信息维护删除失败", ae);
             TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
             return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("自提点信息维护删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    public DubboPageResult<EsDeliveryServiceDO> getBuyerDeliveryList() {
        // 查询出所有有效的自提点信息
        QueryWrapper<EsDeliveryService> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(EsDeliveryService::getState,0);
        List<EsDeliveryService> selectList = this.deliveryServiceMapper.selectList(queryWrapper);

        List<EsDeliveryServiceDO> EsDeliveryServiceDOList = selectList.stream().map(esDeliveryService -> {
            EsDeliveryServiceDO esDeliveryServiceDO = new EsDeliveryServiceDO();
            // 通过自提点id 获取关联的自提日期，
            Long id = esDeliveryService.getId();
            QueryWrapper<EsDeliveryDate> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.lambda().eq(EsDeliveryDate::getDeliveryId,id);
            List<EsDeliveryDate> esDeliveryDatesList = this.deliveryDateMapper.selectList(queryWrapper1);

            List<Long> dateIdList = esDeliveryDatesList.stream().filter(
                    esDeliveryDate -> esDeliveryDate.getDateId().equals(esDeliveryDate.getDateId())
            ).map(EsDeliveryDate::getDateId).collect(Collectors.toList());
            // 通过dateId获取自提日期
            List<EsSelfDate> esSelfDates = this.selfDateMapper.selectBatchIds(dateIdList);
            List<EsSelfDateDO> esSelfDateDO1 = new ArrayList<>();
            // 通过自提日期ID dateID 获取自提时间
            esSelfDateDO1 = esSelfDates.stream().map(esSelfDate -> {
                EsSelfDateDO esSelfDateDO = new EsSelfDateDO();
                QueryWrapper<EsSelfTime> queryWrapper2 = new QueryWrapper<>();
                queryWrapper2.lambda().eq(EsSelfTime::getDateId,esSelfDate.getId());
                List<EsSelfTime> esSelfTimes = this.selfTimeMapper.selectList(queryWrapper2);
                List<EsSelfTimeDO> esSelfTimeDOS = BeanUtil.copyList(esSelfTimes, EsSelfTimeDO.class);
                BeanUtils.copyProperties(esSelfDate,esSelfDateDO);
                esSelfDateDO.setSelfTimeDOList(esSelfTimeDOS);
                return esSelfDateDO;
            }).collect(Collectors.toList());

            BeanUtils.copyProperties(esDeliveryService,esDeliveryServiceDO);
            esDeliveryServiceDO.setSelfDateDOList(esSelfDateDO1);
            return esDeliveryServiceDO;
        }).collect(Collectors.toList());

        // 封装自提点和自提日期 自提时间
        return DubboPageResult.success(EsDeliveryServiceDOList);
    }

    @Override
    public DubboPageResult<EsDeliveryServiceDO> getDeliveryList(String companyCode) {

        //先筛选未过期
        QueryWrapper<EsSelfDate> queryWrapper1 = new QueryWrapper<>();

        List<EsSelfDate> esSelfDates = selfDateMapper.selectList(queryWrapper1);
        List<Long> dateIdList = esSelfDates.stream().filter(
                esSelfDate -> esSelfDate.getSelfDate() > System.currentTimeMillis()
        ).map(EsSelfDate::getId).collect(Collectors.toList());

        QueryWrapper<EsDeliveryDate> queryWrapper2 = new QueryWrapper<>();
        if(CollectionUtils.isNotEmpty(dateIdList)){
            queryWrapper2.lambda().in(EsDeliveryDate::getDateId,dateIdList);
        }
        List<EsDeliveryDate> esDeliveryDates = deliveryDateMapper.selectList(queryWrapper2);

        List<Long> collect = esDeliveryDates.stream().map(EsDeliveryDate::getDeliveryId).collect(Collectors.toList());
        // 查询出所有有效的自提点信息
        QueryWrapper<EsDeliveryService> queryWrapper = new QueryWrapper<>();

        if(CollectionUtils.isNotEmpty(collect)){
            queryWrapper.lambda().eq(EsDeliveryService::getState,0);
            queryWrapper.lambda().eq(EsDeliveryService::getCompanyCode,companyCode);
            queryWrapper.lambda().in(EsDeliveryService::getId,collect);
        }else{
            queryWrapper.lambda().eq(EsDeliveryService::getState,1);
            queryWrapper.lambda().eq(EsDeliveryService::getCompanyCode,companyCode);
        }
        List<EsDeliveryService> selectList = this.deliveryServiceMapper.selectList(queryWrapper);

        List<EsDeliveryServiceDO> EsDeliveryServiceDOList = selectList.stream().map(esDeliveryService -> {
            EsDeliveryServiceDO esDeliveryServiceDO = new EsDeliveryServiceDO();

            BeanUtils.copyProperties(esDeliveryService,esDeliveryServiceDO);

            return esDeliveryServiceDO;
        }).collect(Collectors.toList());

        return DubboPageResult.success(EsDeliveryServiceDOList);
    }

    @Override
    public DubboResult isSupport(List<Long> checkedSkuIds, String companyCode) {
        try{

            DubboResult<EsCompanyDO> company = esCompanyService.getCompanyByCode(companyCode);
            if(!company.isSuccess()){
                return DubboResult.success(false);
//                throw new ArgumentException(member.getCode(),member.getMsg());
            }
            EsCompanyDO companyDO = company.getData();
            // 企业禁用或者已过期
            if(System.currentTimeMillis() > companyDO.getEndTime() || companyDO.getState() != 0){
                return DubboResult.success(false);
            }
            // 开始验证商品是否支持自提
            for (Long checkedSkuId : checkedSkuIds) {
                DubboResult<EsGoodsSkuCO> goodsSku = esGoodsSkuService.getGoodsSku(checkedSkuId);
                // 不支持自提
                if(goodsSku.isSuccess() && goodsSku.getData().getIsSelf() != 1){
                    return DubboResult.success(false);
                }
            }
            return DubboResult.success(true);
        }catch (ArgumentException ae){
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable throwable){
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(),TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    public DubboResult getDeliveryTextMessage(EsDeliveryMessageDTO deliveryMessageDTO) {
        try {
            Long dateId = deliveryMessageDTO.getDateId();
            Long deliveryId = deliveryMessageDTO.getDeliveryId();
            Long timeId = deliveryMessageDTO.getTimeId();

            EsDeliveryMessage delivery = this.deliveryServiceMapper.getDelivery(deliveryId, dateId, timeId);
            EsDeliveryMessageDO esDeliveryMessageDO = new EsDeliveryMessageDO();
            if (delivery != null){
                delivery.setReceiverName(deliveryMessageDTO.getReceiverName());
                delivery.setReceiverMobile(deliveryMessageDTO.getReceiverMobile());
                BeanUtils.copyProperties(delivery,esDeliveryMessageDO);
            }
            return DubboResult.success(esDeliveryMessageDO);
        }  catch (Throwable th) {
            logger.error("自提点信息查询失败", th);
            return DubboPageResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }
}
