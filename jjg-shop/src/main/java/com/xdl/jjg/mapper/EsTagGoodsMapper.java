package com.xdl.jjg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xdl.jjg.entity.EsTagGoods;
import com.xdl.jjg.model.domain.EsGoodsDO;
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
public interface EsTagGoodsMapper extends BaseMapper<EsTagGoods> {

   List<EsGoodsDO> queryTagGoods(@Param("shopId") Integer shopId, @Param("num") Integer num, @Param("mark") String mark);

   IPage<EsGoodsDO> queryTagGoodsById(Page page, Long tagId, Long shopId);
}
