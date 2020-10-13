package com.xdl.jjg.web.service;


import com.xdl.jjg.model.domain.EsMessageDO;
import com.xdl.jjg.model.dto.EsMessageDTO;
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
public interface IEsMessageService {

    /**
     * 插入数据
     *
     * @param messageDTO DTO
     * @author rm 2817512105@qq.com
     * @return: com.shopx.common.model.result.DubboResult<EsMessageDO>
     * @since 2019-06-04
     */
    DubboResult insertMessage(EsMessageDTO messageDTO);

    /**
     * 根据条件更新更新数据
     *
     * @param messageDTO DTO
     * @author rm 2817512105@qq.com
     * @return: com.shopx.common.model.result.DubboResult<EsMessageDO>
     * @since 2019-06-04
     */
    DubboResult updateMessage(EsMessageDTO messageDTO);

    /**
     * 根据id获取数据
     *
     * @param id 主键id
     * @author rm 2817512105@qq.com
     * @return: com.shopx.common.model.result.DubboResult<EsMessageDO>
     * @since 2019-06-04
     */
    DubboResult<EsMessageDO> getMessage(Long id);

    /**
     * 根据查询条件查询列表
     *
     * @param messageDTO DTO
     * @param pageSize   行数
     * @param pageNum    页码
     * @author rm 2817512105@qq.com
     * @return: com.shopx.common.model.result.DubboPageResult<EsMessageDO>
     * @since 2019-06-04
     */
    DubboPageResult<EsMessageDO> getMessageList(EsMessageDTO messageDTO, int pageSize, int pageNum);

    /**
     * 根据主键删除数据
     *
     * @param id 主键id
     * @author rm 2817512105@qq.com
     * @return: com.shopx.common.model.result.DubboResult<EsMessageDO>
     * @since 2019-06-04
     */
    DubboResult deleteMessage(Long id);
}
