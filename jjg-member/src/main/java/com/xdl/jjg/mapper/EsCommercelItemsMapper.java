package com.xdl.jjg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xdl.jjg.entity.EsCommercelItems;
import com.xdl.jjg.model.domain.EsCommercelItemsDO;

import java.util.List;

/**
 * <p>
 * 购物车项 Mapper 接口
 * </p>
 *
 * @author HQL 236154186@qq.com
 * @since 2019-05-29
 */
public interface EsCommercelItemsMapper extends BaseMapper<EsCommercelItems> {

    /**
     * 根据会员id查询购物车项
     * @param memberId 店铺主键
     * @return
     */
    List<EsCommercelItemsDO> getListByMemberId(Long memberId);

}
