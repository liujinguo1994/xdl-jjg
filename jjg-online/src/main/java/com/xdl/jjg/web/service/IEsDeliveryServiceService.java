package com.xdl.jjg.web.service;


import com.xdl.jjg.model.domain.EsDeliveryServiceDO;
import com.xdl.jjg.model.dto.EsDeliveryMessageDTO;
import com.xdl.jjg.model.dto.EsDeliveryServiceDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

import java.util.List;

/**
 * <p>
 * 自提点信息维护 服务类
 * </p>
 *
 * @author LBW 981087977@qq.com
 * @since 2019-06-04 17:12:08
 */
public interface IEsDeliveryServiceService {

    /**
     *系统后台
     *  自提点新增
     * 插入自提点信息维护数据
     * @param deliveryServiceDTO 自提点信息维护DTO
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsDeliveryServiceDO>
     */
    DubboResult insertDeliveryService(EsDeliveryServiceDTO deliveryServiceDTO);

    /**
     * 系统后台
     * 根据条件更新更新数据
     *
     * @param deliveryServiceDTO 自提点信息维护DTO
     * @param id                 主键id
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsDeliveryServiceDO>
     */
    DubboResult updateDeliveryService(EsDeliveryServiceDTO deliveryServiceDTO, Long id);

    /**
     *系统后台
     * 根据id获取自提点信息维护详情
     *
     * @param id 主键id
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsDeliveryServiceDO>
     */
    DubboResult<EsDeliveryServiceDO> getDeliveryService(Long id);

    /**
     * 系统后台
     * 根据查询自提点信息维护列表
     *
     * @param deliveryServiceDTO 自提点信息维护DTO
     * @param pageSize           行数
     * @param pageNum            页码
     * @auther: libw 981087977@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsDeliveryServiceDO>
     */
    DubboPageResult<EsDeliveryServiceDO> getDeliveryServiceList(EsDeliveryServiceDTO deliveryServiceDTO, int pageSize, int pageNum);

    /**
     * 系统后台
     *  根据主键删除自提点信息维护数据
     * @param id 主键id
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsDeliveryServiceDO>
     */
    DubboResult deleteDeliveryService(Long id);

    /**
     *  买家端 结算页面获取自提时间接口
     *
     * @param
     * @auther: LIUJG
     * @date: 2019/10/09 16:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsDeliveryServiceDO>
     */
    DubboPageResult<EsDeliveryServiceDO> getBuyerDeliveryList();

    DubboPageResult<EsDeliveryServiceDO> getDeliveryList(String companyCode);

    /**
     * 是否支持自提
     * @param checkedSkuIds 已选中要去结算的商品SkuId
     * @param userId 用户id
     * @return 是否支持
     */
    DubboResult<Boolean> isSupport(List<Long> checkedSkuIds, String userId);

    /**
     *  买家端 结算页面获取自提信息文本
     *
     * @param
     * @auther: LIUJG
     * @date: 2019/10/09 16:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsDeliveryServiceDO>
     */
    DubboResult getDeliveryTextMessage(EsDeliveryMessageDTO deliveryMessageDTO);
}
