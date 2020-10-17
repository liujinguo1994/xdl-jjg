package com.xdl.jjg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jjg.shop.model.domain.EsBuyerParamsDO;
import com.jjg.shop.model.domain.EsGoodsParamsDO;
import com.xdl.jjg.entity.EsGoodsParams;
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
public interface EsGoodsParamsMapper extends BaseMapper<EsGoodsParams> {

    List<EsGoodsParamsDO> getGoodsParamsByGoodsId(Long goodsId);

    List<EsBuyerParamsDO> getParams(@Param("category") Long category, @Param("goodsId") Long goodsId);

}
