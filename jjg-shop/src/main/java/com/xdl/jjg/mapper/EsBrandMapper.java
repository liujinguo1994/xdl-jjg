package com.xdl.jjg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jjg.shop.model.domain.EsBrandDO;
import com.xdl.jjg.entity.EsBrand;
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
