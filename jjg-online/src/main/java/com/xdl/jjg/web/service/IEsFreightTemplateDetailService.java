package com.xdl.jjg.web.service;


import com.jjg.trade.model.domain.EsFreightTemplateDetailDO;
import com.jjg.trade.model.dto.EsFreightTemplateDetailDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 * 运费模板详情表 服务类
 * </p>
 *
 * @author LBW 981087977@qq.com
 * @since 2019-06-04 17:12:08
 */
public interface IEsFreightTemplateDetailService {

    /**
     * 插入数据
     *
     * @param freightTemplateDetailDTO 运费模板详情表DTO
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsFreightTemplateDetailDO>
     */
    DubboResult insertFreightTemplateDetail(EsFreightTemplateDetailDTO freightTemplateDetailDTO);

    /**
     * 根据条件更新更新数据
     *
     * @param freightTemplateDetailDTO 运费模板详情表DTO
     * @param id                       主键id
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsFreightTemplateDetailDO>
     */
    DubboResult updateFreightTemplateDetail(EsFreightTemplateDetailDTO freightTemplateDetailDTO, Long id);

    /**
     * 根据id获取数据
     *
     * @param id 主键id
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsFreightTemplateDetailDO>
     */
    DubboResult<EsFreightTemplateDetailDO> getFreightTemplateDetail(Long id);

    /**
     * 根据查询条件查询列表
     *
     * @param freightTemplateDetailDTO 运费模板详情表DTO
     * @param pageSize                 行数
     * @param pageNum                  页码
     * @auther: libw 981087977@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsFreightTemplateDetailDO>
     */
    DubboPageResult<EsFreightTemplateDetailDO> getFreightTemplateDetailList(EsFreightTemplateDetailDTO freightTemplateDetailDTO, int pageSize, int pageNum);

    /**
     * 根据主键删除数据
     *
     * @param id 主键id
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsFreightTemplateDetailDO>
     */
    DubboResult deleteFreightTemplateDetail(Long id);

    /**
     * 根据模板ID查询模板详情
     *
     * @param modeId 模板ID
     * @auther: libw 981087977@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsFreightTemplateDetailDO>
     */
    DubboPageResult<EsFreightTemplateDetailDO> getFreightTemplateDetailListByModeId(Long modeId);
}
