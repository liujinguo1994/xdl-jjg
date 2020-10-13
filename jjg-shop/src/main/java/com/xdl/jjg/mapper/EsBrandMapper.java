package com.xdl.jjg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shopx.goods.api.model.domain.EsBrandDO;
import com.shopx.goods.dao.entity.EsBrand;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author WAF 826988665@qq.com
 * @since 2019-05-29
 */
public interface EsBrandMapper extends BaseMapper<EsBrand> {

    List<EsBrandDO> getBrandsByCategory(@Param("categoryId") Long categoryId);
}
