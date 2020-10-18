package com.xdl.jjg.web.service;


import com.jjg.system.model.domain.EsEmailDO;
import com.jjg.system.model.dto.EsEmailDTO;
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
public interface IEsEmailService {

    /**
     * 插入数据
     *
     * @param emailDTO DTO
     * @author rm 2817512105@qq.com
     * @return: com.shopx.common.model.result.DubboResult<EsEmailDO>
     * @since 2019-06-04
     */
    DubboResult insertEmail(EsEmailDTO emailDTO);

    /**
     * 根据条件更新更新数据
     *
     * @param emailDTO DTO
     * @author rm 2817512105@qq.com
     * @return: com.shopx.common.model.result.DubboResult<EsEmailDO>
     * @since 2019-06-04
     */
    DubboResult updateEmail(EsEmailDTO emailDTO);

    /**
     * 根据id获取数据
     *
     * @param id 主键id
     * @author rm 2817512105@qq.com
     * @return: com.shopx.common.model.result.DubboResult<EsEmailDO>
     * @since 2019-06-04
     */
    DubboResult<EsEmailDO> getEmail(Long id);

    /**
     * 根据查询条件查询列表
     *
     * @param emailDTO DTO
     * @param pageSize 行数
     * @param pageNum  页码
     * @author rm 2817512105@qq.com
     * @return: com.shopx.common.model.result.DubboPageResult<EsEmailDO>
     * @since 2019-06-04
     */
    DubboPageResult<EsEmailDO> getEmailList(EsEmailDTO emailDTO, int pageSize, int pageNum);

    /**
     * 根据主键删除数据
     *
     * @param id 主键id
     * @author rm 2817512105@qq.com
     * @return: com.shopx.common.model.result.DubboResult<EsEmailDO>
     * @since 2019-06-04
     */
    DubboResult deleteEmail(Long id);
}
