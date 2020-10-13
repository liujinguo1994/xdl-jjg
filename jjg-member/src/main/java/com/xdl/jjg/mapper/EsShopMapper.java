package com.xdl.jjg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xdl.jjg.entity.EsShop;
import com.xdl.jjg.model.domain.EsShopAndDetailDO;
import com.xdl.jjg.model.domain.EsShopDO;
import com.xdl.jjg.model.dto.ShopQueryParam;

/**
 * <p>
 * 店铺表 Mapper 接口
 * </p>
 *
 * @author HQL 236154186@qq.com
 * @since 2019-05-29
 */
public interface EsShopMapper extends BaseMapper<EsShop> {


    /**
     * 店铺分页查询
     * @param page
     * @param shopQueryParam
     * @return
     */
    Page<EsShopDO> getAllShop(Page page, ShopQueryParam shopQueryParam);

    /**
     * 店铺详情查询
     * @param shopId 店铺主键
     * @return
     */
    EsShopAndDetailDO selectShopDetailById(Long shopId);

}
