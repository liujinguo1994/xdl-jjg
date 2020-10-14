package com.xdl.jjg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jjg.member.model.domain.EsReportDO;
import com.jjg.member.model.dto.ReportQueryParam;
import com.xdl.jjg.entity.EsReport;

/**
 * <p>
 * 举报 Mapper 接口
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2019-07-25
 */
public interface EsReportMapper extends BaseMapper<EsReport> {

    /**
     * 举报分页查询
     * @param page
     * @param reportQueryParam
     * @return
     */
    Page<EsReportDO> getAllreport(Page page, ReportQueryParam reportQueryParam);




}
