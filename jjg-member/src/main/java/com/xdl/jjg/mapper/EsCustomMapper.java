package com.xdl.jjg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xdl.jjg.entity.EsCustom;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 店铺自定义分类 Mapper 接口
 * </p>
 *
 * @author HQL 236154186@qq.com
 * @since 2019-05-29
 */
public interface EsCustomMapper extends BaseMapper<EsCustom> {

    List<EsCustom> getShopClassifyTree(@Param("parentId") Long parentId, @Param("shopId") Long shopId);

    Long getLastId();
}
