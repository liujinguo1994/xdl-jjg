package com.xdl.jjg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jjg.member.model.domain.EsMemberLevelConfigDO;
import com.jjg.member.model.dto.EsQueryMemberLevelConfigDTO;
import com.xdl.jjg.entity.EsMemberLevelConfig;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  会员等级配置 接口
 * </p>
 *
 * @author LINS 1220316142@qq.com
 * @since 2019-07-04 09:42:03
 */
public interface EsMemberLevelConfigMapper extends BaseMapper<EsMemberLevelConfig> {

    /**
     * 带条件查询会员等级配置
     * @param page
     * @param esQueryMemberLevelConfigDTO
     * @return
     */
    IPage<EsMemberLevelConfigDO> getMemberLevelConfigList(Page page, @Param("es") EsQueryMemberLevelConfigDTO esQueryMemberLevelConfigDTO);

}
