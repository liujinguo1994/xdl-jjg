package com.xdl.jjg.web.service;

import com.shopx.common.model.result.DubboPageResult;
import com.shopx.common.model.result.DubboResult;
import com.shopx.trade.api.model.domain.EsDeliveryInfoDO;
import com.shopx.trade.api.model.domain.dto.EsDeliveryInfoDTO;

/**
 * <p>
 * 自提信息表 服务类
 * </p>
 *
 * @author LBW 981087977@qq.com
 * @since 2019-06-04 17:12:08
 */
public interface IEsDeliveryInfoService {

    /**
     * 插入数据
     *
     * @param deliveryInfoDTO 自提信息表DTO
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsDeliveryInfoDO>
     */
    DubboResult insertDeliveryInfo(EsDeliveryInfoDTO deliveryInfoDTO);

    /**
     * 根据条件更新更新数据
     *
     * @param deliveryInfoDTO 自提信息表DTO
     * @param id              主键id
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsDeliveryInfoDO>
     */
    DubboResult updateDeliveryInfo(EsDeliveryInfoDTO deliveryInfoDTO, Long id);

    /**
     * 根据id获取数据
     *
     * @param id 主键id
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsDeliveryInfoDO>
     */
    DubboResult<EsDeliveryInfoDO> getDeliveryInfo(Long id);

    /**
     * 根据查询条件查询列表
     *
     * @param deliveryInfoDTO 自提信息表DTO
     * @param pageSize        行数
     * @param pageNum         页码
     * @auther: libw 981087977@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsDeliveryInfoDO>
     */
    DubboPageResult<EsDeliveryInfoDO> getDeliveryInfoList(EsDeliveryInfoDTO deliveryInfoDTO, int pageSize, int pageNum);

    /**
     * 根据主键删除数据
     *
     * @param id 主键id
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsDeliveryInfoDO>
     */
    DubboResult deleteDeliveryInfo(Long id);
}
