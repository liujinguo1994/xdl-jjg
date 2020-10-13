package com.xdl.jjg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shopx.goods.api.model.domain.EsBrandDO;
import com.shopx.goods.api.model.domain.EsBrandSelectDO;
import com.shopx.goods.dao.entity.EsCategoryBrand;
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
public interface EsCategoryBrandMapper extends BaseMapper<EsCategoryBrand> {

    List<EsBrandSelectDO> getCateBrandSelect(@Param("categoryId") Long categoryId);

    List<EsBrandDO> getBrandsByCategoryList(@Param("categoryId") Long categoryId);

}
