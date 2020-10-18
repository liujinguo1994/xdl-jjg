package com.xdl.jjg.web.service;


import com.jjg.system.model.domain.EsReturnReasonDO;
import com.jjg.system.model.dto.EsReturnReasonDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 * 售后申请原因 服务类
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-12-16
 */
public interface IEsReturnReasonService {

    /**
     * 插入数据
     *
     * @param returnReasonDTO 售后申请原因DTO
     * @author rm 2817512105@qq.com
     * @return: com.shopx.common.model.result.DubboResult<EsReturnReasonDO>
     * @since 2019-12-16
     */
    DubboResult insertReturnReason(EsReturnReasonDTO returnReasonDTO);

    /**
     * 根据条件更新更新数据
     *
     * @param returnReasonDTO 售后申请原因DTO
     * @author rm 2817512105@qq.com
     * @return: com.shopx.common.model.result.DubboResult<EsReturnReasonDO>
     * @since 2019-12-16
     */
    DubboResult updateReturnReason(EsReturnReasonDTO returnReasonDTO);

    /**
     * 根据id获取数据
     *
     * @param id 主键id
     * @author rm 2817512105@qq.com
     * @return: com.shopx.common.model.result.DubboResult<EsReturnReasonDO>
     * @since 2019-12-16
     */
    DubboResult<EsReturnReasonDO> getReturnReason(Long id);

    /**
     * 根据查询条件查询列表
     *
     * @param returnReasonDTO 售后申请原因DTO
     * @param pageSize        行数
     * @param pageNum         页码
     * @author rm 2817512105@qq.com
     * @return: com.shopx.common.model.result.DubboPageResult<EsReturnReasonDO>
     * @since 2019-12-16
     */
    DubboPageResult<EsReturnReasonDO> getReturnReasonList(EsReturnReasonDTO returnReasonDTO, int pageSize, int pageNum);

    /**
     * 根据主键删除数据
     *
     * @param id 主键id
     * @author rm 2817512105@qq.com
     * @return: com.shopx.common.model.result.DubboResult<EsReturnReasonDO>
     * @since 2019-12-16
     */
    DubboResult deleteReturnReason(Long id);

    /**
     * 根据售后类型获取原因列表
     * 售后类型(RETURN_MONEY:退款,RETURN_GOODS:退货退款,CHANGE_GOODS:换货,REPAIR_GOODS:维修)
     */
    DubboPageResult<EsReturnReasonDO> getByType(String refundType);
}
