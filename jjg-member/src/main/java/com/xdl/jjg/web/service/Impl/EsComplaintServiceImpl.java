package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jjg.member.model.domain.EsOrderItemsDO;
import com.jjg.member.model.dto.ComplaintQueryParam;
import com.jjg.member.model.dto.EsComplaintDTO;
import com.jjg.member.model.dto.EsComrImglDTO;
import com.jjg.member.model.enums.ComplaintEnumType;
import com.jjg.member.model.vo.wap.EsWapComplaintOrderItemsVO;
import com.jjg.member.model.vo.wap.EsWapComplaintVO;
import com.xdl.jjg.constant.MemberConstant;
import com.xdl.jjg.constant.MemberErrorCode;
import com.xdl.jjg.entity.EsComplaint;
import com.xdl.jjg.entity.EsComplaintReasonConfig;
import com.xdl.jjg.entity.EsComplaintTypeConfig;
import com.xdl.jjg.entity.EsShop;
import com.xdl.jjg.model.domain.EsComplaintDO;
import com.xdl.jjg.model.domain.EsComplaintOrderDO;
import com.xdl.jjg.model.domain.EsComrImglDO;
import com.xdl.jjg.model.domain.EsMemberDO;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.util.CollectionUtils;
import com.xdl.jjg.util.DateUtils;
import com.xdl.jjg.util.StringUtil;
import com.xdl.jjg.web.service.IEsComplaintService;
import com.xdl.jjg.web.service.IEsComrImglService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.Arrays;
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
public class EsComplaintServiceImpl extends ServiceImpl<EsComplaintMapper, EsComplaint> implements IEsComplaintService {

    private static Logger logger = LoggerFactory.getLogger(EsComplaintServiceImpl.class);

    @Autowired
    private EsComplaintMapper esComplaintMapper;

    @Autowired
    private EsMemberMapper memberMapper;

    @Autowired
    private EsShopMapper shopMapper;

    @Autowired
    private EsComplaintReasonConfigMapper complaintReasonConfigMapper;

    @Autowired
    private EsComplaintTypeConfigMapper complaintTypeConfigMapper;

    @Autowired
    private IEsComrImglService comrImglService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000, check = false)
    private IEsOrderService orderService;


    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertComplaint(EsComplaintDTO esComplaintDTO) {

        try {
            esComplaintDTO.setCreateTime(DateUtils.MILLIS_PER_SECOND);
            EsComplaint esComplaint = new EsComplaint();
            BeanUtil.copyProperties(esComplaintDTO, esComplaint);
            esComplaint.setState(ComplaintEnumType.WAIT.value());
            this.esComplaintMapper.insert(esComplaint);
            Long comId = esComplaint.getId();

            //插入图片
            List<EsComrImglDTO> comrImglDTOList = esComplaintDTO.getComrImglDTOList();
            for (int i = 0; i < comrImglDTOList.size(); i++) {
                EsComrImglDTO com = comrImglDTOList.get(i);
                com.setComId(comId);
                com.setType(MemberConstant.complaint);
                com.setSort(i);
                this.comrImglService.insertComr(com);
            }
            return DubboResult.success(esComplaintDTO);
        } catch (ArgumentException ae){
            logger.error("投诉新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable ae) {
            logger.error("投诉新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertComplaintBuyer(EsComplaintDTO esComplaintDTO) {

        try {
            QueryWrapper<EsComplaint> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsComplaint::getMemberId, esComplaintDTO.getMemberId())
                    .eq(EsComplaint::getOrderSn, esComplaintDTO.getOrderSn());
            EsComplaint complaint = esComplaintMapper.selectOne(queryWrapper);
            if(complaint != null){
                return DubboResult.fail(MemberErrorCode.RESUBMIT.getErrorCode(), MemberErrorCode.RESUBMIT.getErrorMsg());
            }
            EsMemberDO member = memberMapper.selectById(esComplaintDTO.getMemberId());
            esComplaintDTO.setMemberName(member.getName());
            esComplaintDTO.setCreateTime(DateUtils.MILLIS_PER_SECOND);
            EsComplaint esComplaint = new EsComplaint();
            BeanUtil.copyProperties(esComplaintDTO, esComplaint);
            esComplaint.setState(ComplaintEnumType.WAIT.value());
            esComplaint.setIsDel(MemberConstant.complaintIsNotDel);
            this.esComplaintMapper.insert(esComplaint);
            Long comId = esComplaint.getId();

            //插入图片
            List<EsComrImglDTO> comrImglDTOList = esComplaintDTO.getComrImglDTOList();
            if (CollectionUtils.isNotEmpty(comrImglDTOList)){
                comrImglDTOList.stream().peek(e -> {
                    e.setComId(comId);
                    comrImglService.insertComr(e);
                }).collect(Collectors.toList());
            }
            return DubboResult.success(esComplaintDTO);
        } catch (ArgumentException ae){
            logger.error("投诉新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable ae) {
            logger.error("投诉新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult dealCompla(Long id,String state,String dealContent) {
        try {
            EsComplaint esComplaint = this.esComplaintMapper.selectById(id);
            if (esComplaint==null){
                throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            esComplaint.setState(state);
            if (StringUtil.notEmpty(dealContent)){
                esComplaint.setDealContent(dealContent);
            }
            this.esComplaintMapper.updateById(esComplaint);

            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("投诉更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("投诉更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    public DubboResult<EsComplaintDO> getComplaint(Long id) {
        try {
            if (id==null){
                throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), MemberErrorCode.PARAM_ERROR.getErrorMsg());
            }
            EsComplaint esComplaint = this.esComplaintMapper.selectById(id);
            EsComplaintDO complaintDO = new EsComplaintDO();
            if (esComplaint == null) {
                throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            DubboPageResult<EsComrImglDO> imgList = comrImglService.getComrList(esComplaint.getId(), MemberConstant.complaint);
            BeanUtil.copyProperties(esComplaint, complaintDO);
            if(imgList.isSuccess() && imgList.getData() != null){
                complaintDO.setComrImglDOList(imgList.getData().getList());
            }

            if(esComplaint.getTypeId() != null){
                EsComplaintTypeConfig complaintTypeConfig = complaintTypeConfigMapper.selectById(esComplaint.getTypeId());
                complaintDO.setTypeName(complaintTypeConfig.getComplainType());
            }
            if(esComplaint.getReasonId() != null){
                EsComplaintReasonConfig complaintReasonConfig = complaintReasonConfigMapper.selectById(esComplaint.getReasonId());
                complaintDO.setReasonName(complaintReasonConfig.getComplaintReason());
            }

            return DubboResult.success(complaintDO);
        } catch (ArgumentException ae){
            logger.error("投诉查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("投诉查询失败", th);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    @Override
    public DubboPageResult<EsComplaintDO> getComplaintListPage(ComplaintQueryParam complaintQueryParam, int pageSize, int pageNum) {
        try{

            IPage<EsComplaintDO> page = this.esComplaintMapper.getAllComplaint(new Page(pageNum,pageSize),complaintQueryParam);
            List<EsComplaintDO> data = page.getRecords();
            if (data!=null){
                for (EsComplaintDO comp:data) {
                    DubboPageResult<EsComrImglDO> result = this.comrImglService.getComrList(comp.getId(), 0);
                    if (result.getData()!=null){
                        comp.setComrImglDOList(result.getData().getList());
                    }
                    EsShop shop = shopMapper.selectById(comp.getShopId());
                    if(shop != null){
                        comp.setShopName(shop.getShopName());
                    }
                }
            }
            return DubboPageResult.success(page.getTotal(),page.getRecords());
        } catch (ArgumentException ae){
            logger.error("获取分类绑定的品牌，包括未选中的", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable th){
            logger.error("获取分类绑定的品牌，包括未选中的", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    @Override
    public DubboPageResult<EsComplaintOrderDO> getComplaintListBuyerPage(Long memberId, int pageSize, int pageNum) {
        try{
            QueryWrapper<EsComplaint> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsComplaint::getMemberId, memberId);
            IPage<EsComplaint> page =  esComplaintMapper.selectPage(new Page(pageNum,pageSize), queryWrapper);
            List<EsComplaint> data = page.getRecords();
            if (CollectionUtils.isEmpty(data)){
                return DubboPageResult.success(page.getTotal(),page.getRecords());
            }
            List<EsComplaintDO> complaintDOS = BeanUtil.copyList(data, EsComplaintDO.class);
            List<EsComplaintOrderDO> list = complaintDOS.stream().map(e -> {
                EsComplaintOrderDO complaintOrderDO = new EsComplaintOrderDO();
                BeanUtil.copyProperties(e, complaintOrderDO);
                DubboResult<com.xdl.jjg.model.domain.EsOrderDO> orderDOResult = orderService.getEsBuyerOrderInfo(e.getOrderSn());
                com.xdl.jjg.model.domain.EsOrderDO orderDO = orderDOResult.getData();
                if(orderDOResult.isSuccess() && orderDO != null){
                    complaintOrderDO.setShopId(orderDO.getShopId());
                    complaintOrderDO.setShopName(orderDO.getShopName());
                    complaintOrderDO.setPayMoney(orderDO.getPayMoney());
                    complaintOrderDO.setShipName(orderDO.getShipName());
                    List<EsOrderItemsDO> orderItemsDOList = orderDO.getEsOrderItemsDO();
                    List<com.xdl.jjg.model.domain.EsComplaintBuyerOrderItemsDO> complaintBuyerOrderItemsDOList = BeanUtil.copyList(orderItemsDOList, com.xdl.jjg.model.domain.EsComplaintBuyerOrderItemsDO.class);
                    complaintOrderDO.setEsBuyerOrderItemsDO(complaintBuyerOrderItemsDOList);
                }
                return complaintOrderDO;
            }).collect(Collectors.toList());
            return DubboPageResult.success(page.getTotal(),list);
        } catch (ArgumentException ae){
            logger.error("获取分类绑定的品牌，包括未选中的", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable th){
            logger.error("获取分类绑定的品牌，包括未选中的", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }


    @Override
    public DubboResult getComplaintList(EsComplaintDTO complaintDTO) {
        if(complaintDTO == null){
            throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
        }
        QueryWrapper<EsComplaint> queryWrapper = new QueryWrapper<>();
        try{
            EsComplaint complaint = new EsComplaint();
            BeanUtil.copyProperties(complaintDTO, complaint);
            queryWrapper.lambda().eq(EsComplaint::getOrderSn, complaint.getOrderSn())
                    .eq(EsComplaint::getMemberId, complaint.getMemberId());
            List<EsComplaint> list = this.esComplaintMapper.selectList(queryWrapper);
            if (list == null){
                return DubboPageResult.success(Arrays.asList());
            }
            List<EsComplaintDO> complaintDOList = BeanUtil.copyList(list, EsComplaintDO.class);
            return DubboResult.success(complaintDOList);
        } catch (ArgumentException ae){
            logger.error("获取投诉列表失败", ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable th){
            logger.error("获取投诉列表失败", th);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateComplaint(EsComplaintDTO esComplaintDTO) {
        try {
            QueryWrapper<EsComplaint> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(EsComplaint::getId, esComplaintDTO.getId());
            EsComplaint complaint = esComplaintMapper.selectOne(wrapper);
            if(complaint == null){
                throw new ArgumentException(MemberErrorCode.COMPLAINT_NOT_EXIST.getErrorCode(), MemberErrorCode.COMPLAINT_NOT_EXIST.getErrorMsg());
            }
            EsComplaint complaint1 = new EsComplaint();
            BeanUtil.copyProperties(esComplaintDTO, complaint1);
            esComplaintMapper.updateById(complaint1);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("批量删除失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("批量删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteComplaint(Integer [] ids) {
        try {
            QueryWrapper<EsComplaint> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.lambda().in(EsComplaint::getId, ids);
            this.esComplaintMapper.delete(deleteWrapper);
            //删除图片
            this.comrImglService.deleteComr(ids,0);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("批量删除失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("批量删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 移动端-查询投诉列表分页
     */
    @Override
    public DubboPageResult<EsWapComplaintVO> getWapComplaintList(EsComplaintDTO dto, int pageSize, int pageNum) {
        try{
            QueryWrapper<EsComplaint> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsComplaint::getMemberId, dto.getMemberId()).eq(EsComplaint::getState,dto.getState());
            IPage<EsComplaint> page =  esComplaintMapper.selectPage(new Page(pageNum,pageSize), queryWrapper);
            List<EsComplaint> data = page.getRecords();
            if (CollectionUtils.isEmpty(data)){
                return DubboPageResult.success(page.getTotal(),page.getRecords());
            }
            List<EsWapComplaintVO> wapComplaintVOList = BeanUtil.copyList(data, EsWapComplaintVO.class);
            wapComplaintVOList.stream().forEach(wapComplaintVO -> {
                //设置原因
                EsComplaintReasonConfig reasonConfig = complaintReasonConfigMapper.selectById(wapComplaintVO.getReasonId());
                wapComplaintVO.setReasonName(reasonConfig.getComplaintReason());
                //设置类型
                EsComplaintTypeConfig typeConfig = complaintTypeConfigMapper.selectById(wapComplaintVO.getTypeId());
                wapComplaintVO.setTypeName(typeConfig.getComplainType());
                //设置店铺
                EsShop shop = shopMapper.selectById(wapComplaintVO.getShopId());
                wapComplaintVO.setShopName(shop.getShopName());
                //设置投诉图片
                DubboPageResult<EsComrImglDO> result = comrImglService.getComrList(wapComplaintVO.getId(), 0);
                if (result.isSuccess()){
                    List<EsComrImglDO> comrImglDOList = result.getData().getList();
                    if(CollectionUtils.isNotEmpty(comrImglDOList)){
                        wapComplaintVO.setComrImglDOList(comrImglDOList);
                    }
                }
                //订单商品集合
                DubboResult<com.xdl.jjg.model.domain.EsOrderDO> orderDODubboResult = orderService.getEsBuyerOrderInfo(wapComplaintVO.getOrderSn());
                if (orderDODubboResult.isSuccess() && orderDODubboResult.getData() != null){
                    com.xdl.jjg.model.domain.EsOrderDO orderDO = orderDODubboResult.getData();
                    List<EsOrderItemsDO> orderItemsDO = orderDO.getEsOrderItemsDO();
                    if (CollectionUtils.isNotEmpty(orderItemsDO)){
                        List<EsWapComplaintOrderItemsVO> itemsVOList = BeanUtil.copyList(orderItemsDO, EsWapComplaintOrderItemsVO.class);
                        wapComplaintVO.setItemsVOList(itemsVOList);
                    }
                }
            });
            return DubboPageResult.success(page.getTotal(),wapComplaintVOList);
        } catch (ArgumentException ae){
            logger.error("移动端-查询投诉列表分页失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable th){
            logger.error("移动端-查询投诉列表分页失败", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    @Override
    public DubboResult<com.xdl.jjg.model.domain.EsComplaintStatDO> getComplaintStat(Long memberId) {
        try {
            QueryWrapper<EsComplaint> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.lambda().eq(EsComplaint::getMemberId, memberId).eq(EsComplaint::getState,"WAIT");
            Integer count1 = esComplaintMapper.selectCount(queryWrapper1);
            QueryWrapper<EsComplaint> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.lambda().eq(EsComplaint::getMemberId, memberId).eq(EsComplaint::getState,"APPLYING");
            Integer count2 = esComplaintMapper.selectCount(queryWrapper2);
            QueryWrapper<EsComplaint> queryWrapper3 = new QueryWrapper<>();
            queryWrapper3.lambda().eq(EsComplaint::getMemberId, memberId).eq(EsComplaint::getState,"APPLYED");
            Integer count3 = esComplaintMapper.selectCount(queryWrapper3);
            com.xdl.jjg.model.domain.EsComplaintStatDO complaintStatDO = new com.xdl.jjg.model.domain.EsComplaintStatDO();
            complaintStatDO.setWaitCount(count1);
            complaintStatDO.setRunningCount(count2);
            complaintStatDO.setFinishCount(count3);
            return DubboResult.success(complaintStatDO);
        }catch (ArgumentException ae) {
            logger.error("查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        }catch (Exception th) {
            logger.error("查询失败", th);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
}
