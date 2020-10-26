package com.xdl.jjg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jjg.trade.model.domain.EsBuyerOrderDO;
import com.jjg.trade.model.dto.EsOrderItemsDTO;
import com.xdl.jjg.entity.EsOrderItems;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 订单商品明细表-es_order_items Mapper 接口
 * </p>
 *
 * @author LiuJG344009799@qq.com
 * @since 2019-05-29
 */
public interface EsOrderItemsMapper extends BaseMapper<EsOrderItems> {

    IPage<EsBuyerOrderDO> getBuyerOrderCommentList(Page page, @Param("esOrderItemsDTO") EsOrderItemsDTO esOrderItemsDTO);

    List<EsBuyerOrderDO> getBuyerOrderCommentCount(@Param("memberId") long memberId);

    EsBuyerOrderDO getBuyerCommentList(@Param("esOrderItemsDTO") EsOrderItemsDTO esOrderItemsDTO);

}
