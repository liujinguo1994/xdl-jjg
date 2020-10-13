package com.xdl.jjg.web.service;


import com.xdl.jjg.model.domain.EsComplaintDO;
import com.xdl.jjg.model.domain.EsComplaintOrderDO;
import com.xdl.jjg.model.domain.EsComplaintStatDO;
import com.xdl.jjg.model.dto.ComplaintQueryParam;
import com.xdl.jjg.model.dto.EsComplaintDTO;
import com.xdl.jjg.model.vo.wap.EsWapComplaintVO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 *  投诉业务层
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2019-07-31 09:07:54
 */
public interface IEsComplaintService {

    /**
     * 插入数据
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/31 09:39:30
     * @param esComplaintDTO    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsComplaintDO>
     */
    DubboResult insertComplaint(EsComplaintDTO esComplaintDTO);

    DubboResult insertComplaintBuyer(EsComplaintDTO esComplaintDTO);

    /**
     * 开始处理和已完成处理
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/31 09:40:10
     * @param state   状态
     * @return: com.shopx.common.model.result.DubboResult<EsComplaintDO>
     */
    DubboResult dealCompla(Long id, String state, String dealContent);

    /**
     * 根据id获取数据
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/31 09:37:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsComplaintDO>
     */
    DubboResult<EsComplaintDO> getComplaint(Long id);

    /**
     * 根据查询条件查询列表分页
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/31 10:42:53
     * @param complaintQueryParam  complaintDTO
     * @param pageSize  行数
     * @param pageNum   页码
     * @return: com.shopx.common.model.result.DubboPageResult<EsComplaintDO>
     */
    DubboPageResult<EsComplaintDO> getComplaintListPage(ComplaintQueryParam complaintQueryParam, int pageSize, int pageNum);

    /**
     * 买家端-根据查询条件查询列表分页
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/31 10:42:53
     * @param memberId  memberId
     * @param pageSize  行数
     * @param pageNum   页码
     * @return: com.shopx.common.model.result.DubboPageResult<EsComplaintDO>
     */
    DubboPageResult<EsComplaintOrderDO> getComplaintListBuyerPage(Long memberId, int pageSize, int pageNum);

    /**
     * 根据查询条件查询列表
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/31 10:42:53
     * @param complaintDTO  complaintDTO
     * @return: com.shopx.common.model.result.DubboPageResult<EsComplaintDO>
     */
    DubboResult getComplaintList(EsComplaintDTO complaintDTO);

    /**
     * 根据主键删除数据
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/07/31 10:40:44
     * @param ids    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsComplaintDO>
     */
    DubboResult deleteComplaint(Integer[] ids);

    DubboResult updateComplaint(EsComplaintDTO esComplaintDTO);

    /**
     * 移动端-查询投诉列表分页
     */
    DubboPageResult<EsWapComplaintVO> getWapComplaintList(EsComplaintDTO dto, int pageSize, int pageNum);

    /**
     * app端投诉数量统计
     */
    DubboResult<EsComplaintStatDO> getComplaintStat(Long memberId);

}
