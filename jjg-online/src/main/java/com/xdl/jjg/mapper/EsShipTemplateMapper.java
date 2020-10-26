package com.xdl.jjg.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xdl.jjg.entity.EsShipTemplate;

/**
 * <p>
 * 运费模板表 Mapper 接口
 * </p>
 *
 * @author LiuJG344009799@qq.com
 * @since 2019-05-29
 */
public interface EsShipTemplateMapper extends BaseMapper<EsShipTemplate> {

    void insertTemp(EsShipTemplate shipTemplate);

}
