package com.xdl.jjg.web.service;


import com.jjg.system.model.domain.EsSensitiveWordsDO;
import com.jjg.system.model.dto.EsSensitiveWordsDTO;
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
public interface IEsSensitiveWordsService {

    /**
     * 插入数据
     *
     * @param sensitiveWordsDTO DTO
     * @author rm 2817512105@qq.com
     * @return: com.shopx.common.model.result.DubboResult<EsSensitiveWordsDO>
     * @since 2019-06-04
     */
    DubboResult insertSensitiveWords(EsSensitiveWordsDTO sensitiveWordsDTO);

    /**
     * 根据条件更新更新数据
     *
     * @param sensitiveWordsDTO DTO
     * @author rm 2817512105@qq.com
     * @return: com.shopx.common.model.result.DubboResult<EsSensitiveWordsDO>
     * @since 2019-06-04
     */
    DubboResult updateSensitiveWords(EsSensitiveWordsDTO sensitiveWordsDTO);

    /**
     * 根据id获取数据
     *
     * @param id 主键id
     * @author rm 2817512105@qq.com
     * @return: com.shopx.common.model.result.DubboResult<EsSensitiveWordsDO>
     * @since 2019-06-04
     */
    DubboResult<EsSensitiveWordsDO> getSensitiveWords(Long id);

    /**
     * 根据查询条件查询列表
     *
     * @param sensitiveWordsDTO DTO
     * @param pageSize          行数
     * @param pageNum           页码
     * @author rm 2817512105@qq.com
     * @return: com.shopx.common.model.result.DubboPageResult<EsSensitiveWordsDO>
     * @since 2019-06-04
     */
    DubboPageResult<EsSensitiveWordsDO> getSensitiveWordsList(EsSensitiveWordsDTO sensitiveWordsDTO, int pageSize, int pageNum);

    /**
     * 根据主键删除数据
     *
     * @param id 主键id
     * @author rm 2817512105@qq.com
     * @return: com.shopx.common.model.result.DubboResult<EsSensitiveWordsDO>
     * @since 2019-06-04
     */
    DubboResult deleteSensitiveWords(Long id);
}
