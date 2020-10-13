package com.xdl.jjg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shopx.goods.api.model.domain.EsGoodsSkuDO;
import com.shopx.goods.api.model.domain.dto.EsGoodsSkuQueryDTO;
import com.shopx.goods.dao.entity.EsGoodsSku;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author WAF 826988665@qq.com
 * @since 2019-05-29
 */
public interface EsGoodsSkuMapper extends BaseMapper<EsGoodsSku> {
    /**
     * 商品SKU预警信息 分页查询
     * @param page
     * @param esGoodsDTO
     * @param cateIds
     * @return
     */
    IPage<EsGoodsSkuDO> sellerGetGoodsSkuList(Page page, @Param("esGoodsDTO") EsGoodsSkuQueryDTO esGoodsDTO, @Param("cateIds") Long[] cateIds);
}
