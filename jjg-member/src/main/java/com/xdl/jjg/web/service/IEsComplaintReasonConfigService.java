package com.xdl.jjg.web.service;


import com.jjg.member.model.domain.EsComplaintReasonConfigDO;
import com.jjg.member.model.dto.EsComplaintReasonConfigDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 *  投诉类型投诉原因配置表
 * </p>
 *
 * @author LINS 1220316142@qq.com
 * @since 2019-07-23 16:07:54
 */
public interface IEsComplaintReasonConfigService {

    /**
     * 插入数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @param complaintReasonConfigDTO    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsComplaintReasonConfigDO>
     */
    DubboResult insertComplaintReasonConfig(EsComplaintReasonConfigDTO complaintReasonConfigDTO);

    /**
     * 根据条件更新更新数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:10
     * @param complaintReasonConfigDTO   DTO
     * @return: com.shopx.common.model.result.DubboResult<EsComplaintReasonConfigDO>
     */
    DubboResult updateComplaintReasonConfig(EsComplaintReasonConfigDTO complaintReasonConfigDTO);

    /**
     * 根据id获取数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsComplaintReasonConfigDO>
     */
    DubboResult<EsComplaintReasonConfigDO> getComplaintReasonConfig(Long id);

    /**
     * 根据主键删除数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:44
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsComplaintReasonConfigDO>
     */
    DubboResult deleteComplaintReasonConfig(Long id);

    /**
     * 根据查询条件查询列表
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @param complaintReasonConfigDTO  DTO
     * @param pageSize  行数
     * @param pageNum   页码
     * @return: com.shopx.common.model.result.DubboPageResult<EsComplaintReasonConfigDO>
     */
    DubboPageResult<EsComplaintReasonConfigDO> getComplaintReasonConfigList(EsComplaintReasonConfigDTO complaintReasonConfigDTO, int pageSize, int pageNum);


    DubboPageResult<EsComplaintReasonConfigDO> getComplaintReasonConfigBuyerList();

    DubboPageResult<EsComplaintReasonConfigDO> getComplaintReasonConfigListBuyer(EsComplaintReasonConfigDTO complaintReasonConfigDTO, int pageSize, int pageNum);

    /**
     * 移动端-根据投诉类型ID查询投诉原因列表
     */
    DubboPageResult<EsComplaintReasonConfigDO> getByTypeId(Long complainTypeId);

}
