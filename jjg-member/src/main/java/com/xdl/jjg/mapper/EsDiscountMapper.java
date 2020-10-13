package com.xdl.jjg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xdl.jjg.entity.EsDiscount;

/**
 * <p>
 * 公司折扣表 Mapper 接口
 * </p>
 *
 * @author HQL 236154186@qq.com
 * @since 2019-05-29
 */
public interface EsDiscountMapper extends BaseMapper<EsDiscount> {


    /**
     * 批量修改分类名称
     * @param categoryId 分类id
     * @param categoryName 分类名称
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/06/28 17:03:30
     * @return: com.shopx.common.model.result.DubboResult<EsDiscountDO>
     */
    int updateByCategory(Long categoryId, String categoryName);

}
