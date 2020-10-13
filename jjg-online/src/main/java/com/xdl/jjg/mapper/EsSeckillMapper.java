package com.xdl.jjg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shopx.trade.dao.entity.EsSeckill;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author LiuJG344009799@qq.com
 * @since 2019-05-29
 */
public interface EsSeckillMapper extends BaseMapper<EsSeckill> {

    /**
     * 校验是否有同样的活动
     *
     * @param seckill 活动实体类
     * @author: libw 981087977@qq.com
     * @date: 2019/07/31 17:18:27
     * @return: java.util.List<com.shopx.trade.dao.entity.EsHalfPrice>
     */
    int verifyTime(EsSeckill seckill);
}
