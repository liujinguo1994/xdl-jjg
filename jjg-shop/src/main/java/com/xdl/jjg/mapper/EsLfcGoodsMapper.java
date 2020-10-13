package com.xdl.jjg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xdl.jjg.entity.EsLfcGoods;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author WAF 826988665@qq.com
 * @since 2019-05-29
 */
public interface EsLfcGoodsMapper extends BaseMapper<EsLfcGoods> {

    /**
     * 获取国寿商品
     */
    List<Long> getLfcGoods();



}
