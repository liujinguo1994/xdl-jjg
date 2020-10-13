package com.xdl.jjg.web.service;


import com.xdl.jjg.model.domain.EsMessageTemplateDO;
import com.xdl.jjg.model.dto.EsMessageTemplateDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-06-04
 */
public interface IEsMessageTemplateService {

    /**
     * 插入数据
     *
     * @param messageTemplateDTO DTO
     * @author rm 2817512105@qq.com
     * @return: com.shopx.common.model.result.DubboResult<EsMessageTemplateVO>
     * @since 2019-06-04
     */
    DubboResult insertMessageTemplate(EsMessageTemplateDTO messageTemplateDTO);

    /**
     * 根据条件更新更新数据
     *
     * @param messageTemplateDTO DTO
     * @author rm 2817512105@qq.com
     * @return: com.shopx.common.model.result.DubboResult<EsMessageTemplateVO>
     * @since 2019-06-04
     */
    DubboResult updateMessageTemplate(EsMessageTemplateDTO messageTemplateDTO);

    /**
     * 根据id获取数据
     *
     * @param id 主键id
     * @author rm 2817512105@qq.com
     * @return: com.shopx.common.model.result.DubboResult<EsMessageTemplateVO>
     * @since 2019-06-04
     */
    DubboResult<EsMessageTemplateDO> getMessageTemplate(Long id);

    /**
     * 根据查询条件查询列表
     *
     * @param messageTemplateDTO DTO
     * @param pageSize           行数
     * @param pageNum            页码
     * @author rm 2817512105@qq.com
     * @return: com.shopx.common.model.result.DubboPageResult<EsMessageTemplateVO>
     * @since 2019-06-04
     */
    DubboPageResult<EsMessageTemplateDO> getMessageTemplateList(EsMessageTemplateDTO messageTemplateDTO, int pageSize, int pageNum);

    /**
     * 根据主键删除数据
     *
     * @param id 主键id
     * @author rm 2817512105@qq.com
     * @return: com.shopx.common.model.result.DubboResult<EsMessageTemplateVO>
     * @since 2019-06-04
     */
    DubboResult deleteMessageTemplate(Long id);
}
