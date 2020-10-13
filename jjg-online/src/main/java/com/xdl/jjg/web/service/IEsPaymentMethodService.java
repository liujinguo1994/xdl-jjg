package com.xdl.jjg.web.service;


import com.xdl.jjg.model.domain.EsPaymentMethodDO;
import com.xdl.jjg.model.dto.EsPaymentMethodDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LiuJG 344009799@qq.com
 * @since 2019-06-03
 */
public interface IEsPaymentMethodService {

    /**
     * 根据条件更新更新数据
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:40:10
     * @param paymentMethod     插件方法
     * @param paymentPluginId   插件id
     * @return: com.shopx.common.model.result.DubboResult<EsPaymentMethodDO>
     */
    DubboResult updatePaymentMethod(EsPaymentMethodDTO paymentMethod, String paymentPluginId);

    /**
     * 根据支付插件ID获取数据
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:37:16
     * @param pluginId    支付插件ID
     * @return: com.shopx.common.model.result.DubboResult<PaymentPluginVO>
     */
    DubboResult getPaymentMethod(String pluginId);

    /**
     * 根据客户端类型获取数据
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:37:16
     * @param clientType    客户端类型
     * @return: com.shopx.common.model.result.DubboResult<EsPaymentMethodDO>
     */
    DubboPageResult getPaymentMethodListByClient(String clientType);

    /**
     * 根据查询条件查询列表
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/06/03 13:42:53
     * @param paymentMethodDTO  DTO
     * @param pageSize      页码
     * @param pageNum       页数
     * @return: com.shopx.common.model.result.DubboPageResult<EsPaymentMethodDO>
     */
    DubboPageResult getPaymentMethodList(EsPaymentMethodDTO paymentMethodDTO, int pageSize, int pageNum);

    /**
     * 通过pluginId 获取支付信息
     * @date 20190710
     * LiuJG TO LiBW
     * @param pluginId
     * @return
     */
    DubboResult<EsPaymentMethodDO> getByPluginId(String pluginId);
}
