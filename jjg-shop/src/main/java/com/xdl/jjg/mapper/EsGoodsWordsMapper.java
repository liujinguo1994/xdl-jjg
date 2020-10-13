package com.xdl.jjg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xdl.jjg.entity.EsGoodsWords;
import com.xdl.jjg.model.dto.EsGoodsWordsDTO;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 自定义分词 Mapper 接口
 * </p>
 *
 * @author WANGAF 826988665@qq.com
 * @since 2019-07-01 13:54:19
 */
public interface EsGoodsWordsMapper extends BaseMapper<EsGoodsWords> {

    void updateGoodsWords(@Param("esGoodsWordsDTO") EsGoodsWordsDTO esGoodsWordsDTO);
}
