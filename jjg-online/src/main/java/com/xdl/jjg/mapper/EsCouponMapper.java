package com.xdl.jjg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xdl.jjg.entity.EsCoupon;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 优惠卷 Mapper 接口
 * </p>
 *
 * @author libw 981087977@qq.com
 * @since 2019-05-31
 */
public interface EsCouponMapper extends BaseMapper<EsCoupon> {

    List<EsCoupon> selectCoupons(@Param("isRegister") Integer isRegister, @Param("isDel") Integer isDel);

}
