package com.xdl.jjg.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jjg.member.model.domain.EsClerkListDO;
import com.jjg.member.model.dto.ClerkQueryParamDTO;
import com.xdl.jjg.entity.EsClerk;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 店员 Mapper 接口
 * </p>
 *
 * @author HQL 236154186@qq.com
 * @since 2019-05-29
 */
public interface EsClerkMapper extends BaseMapper<EsClerk> {


    /**
     * 店员分页查询
     * @param page
     * @return
     */
    Page<EsClerkListDO> getAllClerk(Page page, @Param("es") ClerkQueryParamDTO es);

    Integer updateState(@Param("state") Integer state, @Param("id") Long id);

}
