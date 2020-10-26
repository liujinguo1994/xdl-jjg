package com.xdl.jjg.web.service;


import com.jjg.trade.model.domain.EsHikExceptionOrderDO;
import com.jjg.trade.model.dto.EsHikExceptionOrderDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 * 海康异常反馈
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2020-05-09
 */
public interface IEsHikExceptionService {

    /**
     * 买家端
     * 插入数据
     * @auther: yuanj 5958313259@qq.com
     * @date: 2020/05/09
     * @param hikExceptionOrderDTO    异常反馈DTO
     * @return: com.shopx.common.model.result.DubboResult<EsSelfDateDO>
     */
    DubboResult insertHikException(EsHikExceptionOrderDTO hikExceptionOrderDTO);


    /**
     *
     * 根据查询条件查询列表
     * @auther:yuanj 5958313259@qq.com
     * @date: 2020/05/09
     * @param hikExceptionOrderDTO  DTO
     * @return: com.shopx.common.model.result.DubboPageResult<EsRefundDO>
     */
    DubboPageResult getHIkExceptionOrderList(EsHikExceptionOrderDTO hikExceptionOrderDTO);

    /**
     * 根据条件更新更新数据
     * @auther:yuanj 5958313259@qq.com
     * @date: 2020/05/09
     * @param hikExceptionOrderDTO    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsSelfDateDO>
     */
    DubboResult updateHikException(EsHikExceptionOrderDTO hikExceptionOrderDTO);

    /**
     * 获取异常订单信息
     *
     * @param id 异常订单信息表主键
     * @return ExceptionOrderDO
     */
    DubboResult<EsHikExceptionOrderDO> getModel(Long id);

    /**
     *  系统后台
     * 根据主键删除数据
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/0702 16:40:44
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsSelfDateDO>
     */
    DubboResult deleteHikException(Long id);


    /**
     * 根据订单号查询异常订单信息
     * @auther: libw
     * @date: 2019/05/23 11:26:42
     * @param sn 订单编号
     * @return: com.enation.app.javashop.core.trade.order.model.dos.ExceptionOrderDO
     */
    //DubboResult<EsHikExceptionOrderDO> getModelFromSn(String sn);

}
