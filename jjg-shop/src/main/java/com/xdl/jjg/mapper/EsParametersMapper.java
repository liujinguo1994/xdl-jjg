package com.xdl.jjg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xdl.jjg.entity.EsParameters;
import com.xdl.jjg.model.domain.EsParametersDO;
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
public interface EsParametersMapper extends BaseMapper<EsParameters> {

  List<EsParametersDO> getParametersList(@Param("goodsId") Long goodsId, @Param("categoryId") Long categoryId);

  List<EsParametersDO> getDraftParametersList(@Param("goodsId") Long goodsId, @Param("categoryId") Long categoryId);


}
