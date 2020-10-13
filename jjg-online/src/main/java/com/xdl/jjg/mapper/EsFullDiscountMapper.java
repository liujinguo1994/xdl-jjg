package com.xdl.jjg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shopx.trade.dao.entity.EsFullDiscount;

/**
 * <p>
 * 满减满赠-es_full_discount Mapper 接口
 * </p>
 *
 * @author libw 981087977@qq.com
 * @since 2019-05-31
 */
public interface EsFullDiscountMapper extends BaseMapper<EsFullDiscount> {

    /**
     * 校验是否有同样的活动
     * @author: libw 981087977@qq.com
     * @date: 2019/07/31 17:18:27
     * @param fullDiscount 活动实体类
     * @return: java.util.List<com.shopx.trade.dao.entity.EsHalfPrice>
     */
    int verifyTime(EsFullDiscount fullDiscount);
}
