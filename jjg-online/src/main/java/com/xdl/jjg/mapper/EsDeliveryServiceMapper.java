package com.xdl.jjg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xdl.jjg.entity.EsDeliveryMessage;
import com.xdl.jjg.entity.EsDeliveryService;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 自提点信息维护 Mapper 接口
 * </p>
 *
 * @author libw 981087977@qq.com
 * @since 2019-05-31
 */
public interface EsDeliveryServiceMapper extends BaseMapper<EsDeliveryService> {

    EsDeliveryMessage getDelivery(@Param("deliveryId") Long deliveryId, @Param("dateId") Long dateId, @Param("timeId") Long timeId);

}
