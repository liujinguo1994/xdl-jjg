package com.xdl.jjg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xdl.jjg.entity.EsComplaint;
import com.xdl.jjg.model.domain.EsComplaintDO;
import com.xdl.jjg.model.dto.ComplaintQueryParam;

/**
 * <p>
 * 投诉 Mapper 接口
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2019-07-31
 */
public interface EsComplaintMapper extends BaseMapper<EsComplaint> {

    /**
     * 投诉分页查询
     * @param page
     * @param complaintQueryParam
     * @return
     */
    Page<EsComplaintDO> getAllComplaint(Page page, ComplaintQueryParam complaintQueryParam);





}
