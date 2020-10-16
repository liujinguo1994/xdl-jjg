package com.xdl.jjg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jjg.member.model.domain.EsComplaintReasonConfigDO;
import com.xdl.jjg.entity.EsComplaintReasonConfig;

/**
 * <p>
 *  投诉原因配置Mapper 接口
 * </p>
 *
 * @author LINS 1220316142@qq.com
 * @since 2019-07-23 16:07:55
 */
public interface EsComplaintReasonConfigMapper extends BaseMapper<EsComplaintReasonConfig> {

    /**
     * 列表查询投诉原因配置
     * @param page
     * @return
     */
    IPage<EsComplaintReasonConfigDO> getComplaintReasonConfigList(Page page);

}
