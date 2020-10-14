package com.xdl.jjg.web.service;


import com.jjg.member.model.domain.EsReportDO;
import com.jjg.member.model.dto.EsReportDTO;
import com.jjg.member.model.dto.ReportQueryParam;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 * 店员 服务类
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2019-07-25
 */
public interface IEsReportService {

    /**
     * 插入数据
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/25 11:11:30
     * @param reportDTO    举报DTO
     * @return: com.shopx.common.model.result.DubboResult<EsReportDO>
     */
    DubboResult<EsReportDO> insertReport(EsReportDTO reportDTO);

    /**
     * 根据条件更新更新数据
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/25 14:40:10
     * @param dealContent    处理内容
     * @return: com.shopx.common.model.result.DubboResult<EsReportDO>
     */
    DubboResult dealReport(Long id, String state, String dealContent);

    /**
     * 根据id获取数据
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/25 14:17:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsReportDO>
     */
    DubboResult<EsReportDO> getReport(Long id);


    /**
     * 根据查询条件查询列表
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/25 10:37:16
     * @param reportQueryParam  举报查询
     * @param pageSize  行数
     * @param pageNum   页码
     * @return: com.shopx.common.model.result.DubboPageResult<EsReportDO>
     */
    DubboPageResult<EsReportDO> getReportList(ReportQueryParam reportQueryParam, int pageSize, int pageNum);

    /**
     * 根据主键删除数据
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/25 10:40:44
     * @param ids    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsReportDO>
     */
    DubboResult deleteReport(Integer[] ids);

}
