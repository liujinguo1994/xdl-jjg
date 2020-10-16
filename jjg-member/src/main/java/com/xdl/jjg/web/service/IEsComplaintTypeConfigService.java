package com.xdl.jjg.web.service;


import com.jjg.member.model.domain.EsComplaintTypeConfigDO;
import com.jjg.member.model.dto.EsComplaintTypeConfigDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LINS 1220316142@qq.com
 * @since 2019-07-23 16:07:54
 */
public interface IEsComplaintTypeConfigService {

    /**
     * 插入数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @param complaintTypeConfigDTO    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsComplaintTypeConfigDO>
     */
    DubboResult insertComplaintTypeConfig(EsComplaintTypeConfigDTO complaintTypeConfigDTO);

    /**
     * 根据条件更新更新数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:10
     * @param complaintTypeConfigDTO   DTO
     * @return: com.shopx.common.model.result.DubboResult<EsComplaintTypeConfigDO>
     */
    DubboResult updateComplaintTypeConfig(EsComplaintTypeConfigDTO complaintTypeConfigDTO);

    /**
     * 根据id获取数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsComplaintTypeConfigDO>
     */
    DubboResult<EsComplaintTypeConfigDO> getComplaintTypeConfig(Long id);

    /**
     * 根据查询条件查询列表
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @param complaintTypeConfigDTO  DTO
     * @param pageSize  行数
     * @param pageNum   页码
     * @return: com.shopx.common.model.result.DubboPageResult<EsComplaintTypeConfigDO>
     */
    DubboPageResult<EsComplaintTypeConfigDO> getComplaintTypeConfigList(EsComplaintTypeConfigDTO complaintTypeConfigDTO, int pageSize, int pageNum);

    /**
     * 根据主键删除数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:44
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsComplaintTypeConfigDO>
     */
    DubboResult deleteComplaintTypeConfig(Long id);

    /**
     * 无参查询列表
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsComplaintTypeConfigDO>
     */
    DubboPageResult<EsComplaintTypeConfigDO> getComplaintTypeConfigListInfo();
}
