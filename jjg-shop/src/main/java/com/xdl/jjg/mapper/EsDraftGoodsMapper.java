package com.xdl.jjg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xdl.jjg.entity.EsDraftGoods;
import com.xdl.jjg.model.domain.EsDraftGoodsDO;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author WAF 826988665@qq.com
 * @since 2019-05-29
 */
public interface EsDraftGoodsMapper extends BaseMapper<EsDraftGoods> {

    IPage<EsDraftGoodsDO> getDraftEsGoodsPageList(Page page, @Param("goodsName") String goodsName, @Param("shopId") Long shopId, @Param("cateIds") Long[] cateIds, Integer isVirtual);
}
