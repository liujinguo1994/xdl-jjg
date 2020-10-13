package com.xdl.jjg.web.service;


import com.xdl.jjg.model.domain.EsTemplateDetailDO;
import com.xdl.jjg.model.dto.EsTemplateDetailDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 * 店铺模板详情--es_template_detail 服务类
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2019-06-27
 */
public interface IEsTemplateDetailService {

    /**
     * 插入数据
     * @auther: yuanj 595831329@qq.com
     * @date: 2019-06-20 10:39:30
     * @param templateDetailDTO    店铺模板详情--es_template_detailDTO
     * @return: com.shopx.common.model.result.DubboResult<EsTemplateDetailDO>
     */
    DubboResult insertTemplateDetail(EsTemplateDetailDTO templateDetailDTO);

    /**
     * 根据条件更新更新数据
     * @auther: yuanj 595831329@qq.com
     * @date: 2019-06-27 10:40:10
     * @param templateDetailDTO    店铺模板详情--es_template_detailDTO
     * @return: com.shopx.common.model.result.DubboResult<EsTemplateDetailDO>
     */
    DubboResult updateTemplateDetail(EsTemplateDetailDTO templateDetailDTO);

    /**
     * 根据id获取数据
     * @auther: yuanj 595831329@qq.com
     * @date: 2019-06-27 10:37:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsTemplateDetailDO>
     */
    DubboResult<EsTemplateDetailDO> getTemplateDetail(Long id);

    /**
     * 根据查询条件查询列表
     * @auther: yuanj 595831329@qq.com
     * @date: 2019-06-27 10:42:53
     * @param templateDetailDTO  店铺模板详情--es_template_detailDTO
     * @param pageSize  行数
     * @param pageNum   页码
     * @return: com.shopx.common.model.result.DubboPageResult<EsTemplateDetailDO>
     */
    DubboPageResult<EsTemplateDetailDO> getTemplateDetailList(EsTemplateDetailDTO templateDetailDTO, int pageSize, int pageNum);

    /**
     * 根据主键删除数据
     * @auther: yuanj 595831329@qq.com
     * @date: 2019-06-27 10:40:44
     * @param ids   主键id
     * @return: com.shopx.common.model.result.DubboResult<EsTemplateDetailDO>
     */
    DubboResult deleteTemplateDetail(Integer[] ids);
}
