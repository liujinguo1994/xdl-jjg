package com.xdl.jjg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xdl.jjg.entity.EsMemberShop;

/**
 * <p>
 * 会员店铺关联表 Mapper 接口
 * </p>
 *
 * @author HQL 236154186@qq.com
 * @since 2019-05-29
 */
public interface EsMemberShopMapper extends BaseMapper<EsMemberShop> {

    /**
     * 依据会员id查询店铺id
     */
    Long getShopIdByMemberId(Long memberId);

}
