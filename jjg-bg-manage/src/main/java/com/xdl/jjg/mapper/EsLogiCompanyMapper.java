package com.xdl.jjg.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jjg.system.model.domain.EsLogiCompanyDO;
import com.xdl.jjg.entity.EsLogiCompany;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
public interface EsLogiCompanyMapper extends BaseMapper<EsLogiCompany> {
    EsLogiCompanyDO getLogiByCode(@Param("code") String code);
}
