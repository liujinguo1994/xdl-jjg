package com.xdl.jjg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jjg.member.model.domain.EsCartNumDO;
import com.xdl.jjg.entity.EsCart;


/**
 * <p>
 * 购物车 Mapper 接口
 * </p>
 *
 * @author HQL 236154186@qq.com
 * @since 2019-05-29
 */
public interface EsCartMapper extends BaseMapper<EsCart> {

    /**
     * 根据用户id查询购物车数量
     * @param memberId 店铺主键
     * @return
     */
    EsCartNumDO getCartNumBYMemberId(Long memberId);

}
