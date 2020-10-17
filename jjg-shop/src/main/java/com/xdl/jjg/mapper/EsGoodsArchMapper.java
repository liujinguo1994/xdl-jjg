package com.xdl.jjg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jjg.shop.model.domain.EsGoodsArchDO;
import com.xdl.jjg.entity.EsGoodsArch;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author WAF 826988665@qq.com
 * @since 2019-05-29
 */
public interface EsGoodsArchMapper extends BaseMapper<EsGoodsArch> {
    /**
     * 根据输入内容查询商品档案信息 (商品名称、商品编号)
     * @param keyContent
     * @return
     */
    IPage<EsGoodsArchDO> getEsGoodsArchList(Page page, @Param("keyContent") String keyContent, @Param("supplierId") Long supplierId);

    IPage<EsGoodsArchDO> getEsGoodsArchGiftsList(Page page, @Param("keyContent") String keyContent);

}
