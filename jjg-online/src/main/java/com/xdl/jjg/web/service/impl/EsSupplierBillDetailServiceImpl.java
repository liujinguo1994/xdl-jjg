package com.xdl.jjg.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jjg.member.model.domain.EsCompanyDO;
import com.jjg.member.model.domain.EsMemberDO;
import com.jjg.trade.model.domain.EsOrderDO;
import com.jjg.trade.model.domain.EsSupplierBillDetailDO;
import com.jjg.trade.model.dto.EsSupplierBillDetailDTO;
import com.xdl.jjg.constant.TradeErrorCode;
import com.xdl.jjg.entity.EsBillDetail;
import com.xdl.jjg.entity.EsOrderItems;
import com.xdl.jjg.entity.EsSupplierBillDetail;
import com.xdl.jjg.mapper.EsBillDetailMapper;
import com.xdl.jjg.mapper.EsOrderItemsMapper;
import com.xdl.jjg.mapper.EsSupplierBillDetailMapper;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.web.service.IEsSupplierBillDetailService;
import com.xdl.jjg.web.service.feign.member.CompanyService;
import com.xdl.jjg.web.service.feign.member.MemberService;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.apache.dubbo.config.annotation.Reference;
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
 *  服务实现类
 * </p>
 *
 * @author LiuJG 344009799@qq.com
 * @since 2019-08-20
 */
@Service(version = "${dubbo.application.version}", interfaceClass = IEsSupplierBillDetailService.class, timeout = 50000)
public class EsSupplierBillDetailServiceImpl extends ServiceImpl<EsSupplierBillDetailMapper, EsSupplierBillDetail> implements IEsSupplierBillDetailService {

    private static Logger logger = LoggerFactory.getLogger(EsSupplierBillDetailServiceImpl.class);

    @Autowired
    private EsSupplierBillDetailMapper supplierBillDetailMapper;

    @Autowired
    private EsOrderItemsMapper esOrderItemsMapper;

    @Autowired
    private EsBillDetailMapper esBillDetailMapper;

    @Autowired
    private MemberService memberService;

    @Autowired
    private CompanyService companyService;
    /**
     * 插入数据
     *
     * @param supplierBillDetailDTO DTO
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsSupplierBillDetailDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertSupplierBillDetail(EsSupplierBillDetailDTO supplierBillDetailDTO) {
        try {
            if (supplierBillDetailDTO == null) {
                throw new ArgumentException(TradeErrorCode.PARAM_ERROR.getErrorCode(), TradeErrorCode.PARAM_ERROR.getErrorMsg());
            }

            EsSupplierBillDetail supplierBillDetail = new EsSupplierBillDetail();
            BeanUtils.copyProperties(supplierBillDetailDTO, supplierBillDetail);
            this.supplierBillDetailMapper.insert(supplierBillDetail);
            return DubboResult.success();
        } catch (Throwable ae) {
            logger.error("失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新数据
     *
     * @param supplierBillDetailDTO DTO
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsSupplierBillDetailDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateSupplierBillDetail(EsSupplierBillDetailDTO supplierBillDetailDTO) {
        try {
            if (supplierBillDetailDTO == null) {
                throw new ArgumentException(TradeErrorCode.PARAM_ERROR.getErrorCode(), TradeErrorCode.PARAM_ERROR.getErrorMsg());
            }
            EsSupplierBillDetail supplierBillDetail = new EsSupplierBillDetail();
            BeanUtils.copyProperties(supplierBillDetailDTO, supplierBillDetail);
            QueryWrapper<EsSupplierBillDetail> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsSupplierBillDetail::getId, supplierBillDetailDTO.getId());
            this.supplierBillDetailMapper.update(supplierBillDetail, queryWrapper);
            return DubboResult.success();
        } catch (Throwable th) {
            logger.error("更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据id获取详情
     *
     * @param id 主键id
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsSupplierBillDetailDO>
     */
    @Override
    public DubboResult getSupplierBillDetail(Long id) {
        try {
            QueryWrapper<EsSupplierBillDetail> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsSupplierBillDetail::getId, id);
            EsSupplierBillDetail supplierBillDetail = this.supplierBillDetailMapper.selectOne(queryWrapper);
            EsSupplierBillDetailDO supplierBillDetailDO = new EsSupplierBillDetailDO();
            if (supplierBillDetail != null) {
                throw new ArgumentException(TradeErrorCode.DATA_NOT_EXIST.getErrorCode(), TradeErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtils.copyProperties(supplierBillDetail, supplierBillDetailDO);
            return DubboResult.success(supplierBillDetailDO);
        } catch (Throwable th) {
            logger.error("查询失败", th);
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询列表
     *
     * @param supplierBillDetailDTO DTO
     * @param pageSize  行数
     * @param pageNum   页码
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsSupplierBillDetailDO>
     */
    @Override
    public DubboPageResult getSupplierBillDetailList(EsSupplierBillDetailDTO supplierBillDetailDTO, int pageSize, int pageNum) {
        QueryWrapper<EsSupplierBillDetail> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件

            Page page = new Page(pageNum,pageSize);

            IPage<EsSupplierBillDetail> iPage = this.page(page, queryWrapper);
            List<EsSupplierBillDetailDO> supplierBillDetailDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                supplierBillDetailDOList = iPage.getRecords().stream().map(supplierBillDetail -> {
                    EsSupplierBillDetailDO supplierBillDetailDO = new EsSupplierBillDetailDO();
                    BeanUtils.copyProperties(supplierBillDetail, supplierBillDetailDO);
                    return supplierBillDetailDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(supplierBillDetailDOList);
        } catch (Throwable th) {
            logger.error("查询分页查询失败", th);
            return DubboPageResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据主键删除数据
     *
     * @param id 主键id
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsSupplierBillDetailDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteSupplierBillDetail(Long id) {
        try {
            if (id == 0) {
                throw new ArgumentException(TradeErrorCode.PARAM_ERROR.getErrorCode(), String.format("参数传入错误ID不能为空[%s]", id));
            }
            QueryWrapper<EsSupplierBillDetail> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.lambda().eq(EsSupplierBillDetail::getId, id);
            this.supplierBillDetailMapper.delete(deleteWrapper);
            return DubboResult.success();
        } catch (Throwable th) {
            logger.error("查询删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(TradeErrorCode.SYS_ERROR.getErrorCode(), TradeErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 生成结算单
     * @param esOrder
     */
    @Override
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    public DubboResult generateBill(EsOrderDO esOrder) {
        try {
            // 获取会员的签约公司字符串

            DubboResult<EsMemberDO> member = memberService.getMember(esOrder.getMemberId());

            String companyCode = null;
            if (member.isSuccess()){
                EsMemberDO memberDO  = member.getData();
                companyCode = memberDO.getCompanyCode();
            }

            //验证该公司是否合作
            DubboResult<EsCompanyDO> companyByCode = companyService.getCompanyByCode(companyCode);

            EsBillDetail esBillDetail = new EsBillDetail();
            esBillDetail.setOrderSn(esOrder.getOrderSn());
            esBillDetail.setState(0);
            esBillDetail.setType(1);

            this.esBillDetailMapper.insert(esBillDetail);

            // 通过订单编号查询订单商品明细表数据
            QueryWrapper<EsOrderItems> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsOrderItems::getOrderSn,esOrder.getOrderSn());
            List<EsOrderItems> esOrderItems = this.esOrderItemsMapper.selectList(queryWrapper);
            esOrderItems.forEach(orderItems -> {
                EsSupplierBillDetail esSupplierBillDetail = new EsSupplierBillDetail();
                esSupplierBillDetail.setGoodsId(orderItems.getGoodsId());
                esSupplierBillDetail.setGoodsName(orderItems.getName());
                esSupplierBillDetail.setNum(orderItems.getNum());
                esSupplierBillDetail.setOrderSn(orderItems.getOrderSn());
                esSupplierBillDetail.setState(0);
                this.supplierBillDetailMapper.insert(esSupplierBillDetail);

            });

            // 如果等于0 需多插入一条签约公司的
            Integer isDel = 1;
            if (companyByCode.isSuccess()){
                EsCompanyDO esCompany = companyByCode.getData();
                isDel = esCompany.getIsDel();
            }
            if (isDel == 0) {
                esBillDetail.setOrderSn(esOrder.getOrderSn());
                esBillDetail.setState(0);
                esBillDetail.setType(2);

                this.esBillDetailMapper.insert(esBillDetail);
            }

            return DubboResult.success();
        }catch (ArgumentException ae) {
//            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚事务
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }
    }

}
