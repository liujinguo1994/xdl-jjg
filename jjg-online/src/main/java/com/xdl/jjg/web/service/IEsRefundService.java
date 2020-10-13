package com.xdl.jjg.web.service;


import com.xdl.jjg.model.domain.EsRefundDO;
import com.xdl.jjg.model.dto.EsReFundQueryDTO;
import com.xdl.jjg.model.dto.EsRefundDTO;
import com.xdl.jjg.model.dto.EsWapAfterSaleRecordQueryDTO;
import com.xdl.jjg.model.vo.EsWapRefundCountVO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

import java.util.Map;

/**
 * <p>
 * 退货 服务类
 * </p>
 *
 * @author LiuJG 344009799@qq.com
 * @since 2019-06-03
 */
public interface IEsRefundService {

    /**
     * 插入数据
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:39:30
     * @param refundDTO    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsRefundDO>
     */
    DubboResult insertRefund(EsRefundDTO refundDTO);

    /**
     * 根据条件更新更新数据
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:40:10
     * @param refundDTO    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsRefundDO>
     */
    DubboResult updateRefund(EsRefundDTO refundDTO);

    /**
     * @Description: 根据id获取数据
     * @Author       LiuJG 344009799@qq.com
     * @Date         2019/8/10 16:07
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsRefundDO>
     */
    DubboResult getRefund(Long id);

    /**
     * 买家端 售后商品列表
     * 根据查询条件查询列表
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/08/10 13:42:53
     * @param esReFundQueryDTO  DTO
     * @param pageSize      页码
     * @param pageNum       页数
     * @return: com.shopx.common.model.result.DubboPageResult<EsRefundDO>
     */
    DubboPageResult<EsRefundDO> getServiceRefundList(EsReFundQueryDTO esReFundQueryDTO, int pageSize, int pageNum);

    /**
     * 后台管理 卖家端 通用
     * 根据查询条件查询列表
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/08/10 13:42:53
     * @param esReFundQueryDTO  DTO
     * @param pageSize      页码
     * @param pageNum       页数
     * @return: com.shopx.common.model.result.DubboPageResult<EsRefundDO>
     */
    DubboPageResult getRefundList(EsReFundQueryDTO esReFundQueryDTO, int pageSize, int pageNum);

    /**
     * 根据主键删除数据
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:40:44
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsRefundDO>
     */
    DubboResult deleteRefund(Long id);

    /**
     *  卖家端
     * 根据OrderSn获取退换货数据信息
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/07/05 09:40
     * @param sn
     * @return: com.shopx.common.model.result.DubboResult<EsRefundDO>
     */
    DubboResult getRefundBySn(String sn, Long shopId);

    /**
    /**
     * @Description: 退款时获取退款单详情信息
     * @Author       LiuJG 344009799@qq.com
     * @Date         2019/12/17 9:08
     * @param
     * @return       com.shopx.common.model.result.DubboResult<com.shopx.trade.api.model.domain.EsRefundDO>
     * @exception
     *
     */
    DubboResult<EsRefundDO> getRefundDetailBySn(String sn);

    /**
     *  系统后台
     * 根据Sn获取退换货数据信息
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/07/05 09:40
     * @param sn
     * @return: com.shopx.common.model.result.DubboResult<EsRefundDO>
     */
    DubboResult getAdminRefundBySn(String sn);

    /**
     *  卖家端
     * 根据OrderSn 和status 获取退款总金额
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/07/05 09:40
     * @param orderSn
     * @return: com.shopx.common.model.result.DubboResult<EsRefundDO>
     */
    DubboResult getRefundPayMoneyByOrderSnAndStatus(String orderSn, Long shopId);



    /**
     *  卖家端
     * 处理MQ发送的售后退款成功 ，处理优惠券的等活动信息
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/07/05 09:40
     * @param
     * @return: com.shopx.common.model.result.DubboResult<EsRefundDO>
     */
    DubboResult operationServiceExpandMessage(Map<String, Object> afterMap);
    /**
     * @Description: 获取售后单详情
     * @Author       LiuJG 344009799@qq.com
     * @Date         2019/12/15 9:09
     * @param
     * @return       com.shopx.common.model.result.DubboResult<com.shopx.trade.api.model.domain.EsRefundDO>
     * @exception
     *
     */
    DubboResult<EsRefundDO> getServiceDetail(String sn, Long memberId);
    /**
     * @Description: 取消售后操作
     * @Author       LiuJG 344009799@qq.com
     * @Date         2019/12/15 9:09
     * @exception
     *
     */
    DubboResult updateCancelAfterSale(String sn, Long memberId);

    //处理中/已完成售后单列表
    DubboPageResult<EsRefundDO> getAfterSalesRecords(EsWapAfterSaleRecordQueryDTO dto, int pageSize, int pageNum);

    DubboResult<EsRefundDO> getRefundSn(String orderSn, Long skuId);

    /**
     * @Description: 获取售后数量
     * @Author       yuanj 595831329@qq.com
     * @Date         2020/04/07 16:07
     * @return: com.shopx.common.model.result.DubboResult<EsWapRefundCountVO>
     */
    DubboResult<EsWapRefundCountVO> getCount(Long memberId);
}
