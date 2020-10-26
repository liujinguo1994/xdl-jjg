package com.xdl.jjg.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xdl.jjg.entity.EsSeckillApply;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author LiuJG344009799@qq.com
 * @since 2019-05-29
 */
public interface EsSeckillApplyMapper extends BaseMapper<EsSeckillApply> {

    void updateGoodsSkuQuantity(@Param("num") int num, @Param("goodsId") Long goodsId, @Param("activityId") Long activityId);
}
