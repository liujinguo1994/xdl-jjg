package com.xdl.jjg.web.service;

import com.xdl.jjg.model.domain.EsUploaderDO;
import com.xdl.jjg.model.dto.EsUploaderDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-06-04
 */
public interface IEsUploaderService {

    /**
     * 插入数据
     * @author rm 2817512105@qq.com
     * @since 2019-06-04
     * @param uploaderDTO    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsUploaderDO>
     */
    DubboResult insertUploader(EsUploaderDTO uploaderDTO);

    /**
     * 根据条件更新更新数据
     * @author rm 2817512105@qq.com
     * @since 2019-06-04
     * @param uploaderDTO    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsUploaderDO>
     */
    DubboResult updateUploader(EsUploaderDTO uploaderDTO);

    /**
     * 根据id获取数据
     * @author rm 2817512105@qq.com
     * @since 2019-06-04
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsUploaderDO>
     */
    DubboResult<EsUploaderDO> getUploader(Long id);
    DubboResult<EsUploaderDO> getUploaderByName(String name);
    /**
     * 根据查询条件查询列表
     * @author rm 2817512105@qq.com
     * @since 2019-06-04
     * @param uploaderDTO  DTO
     * @param pageSize  行数
     * @param pageNum   页码
     * @return: com.shopx.common.model.result.DubboPageResult<EsUploaderDO>
     */
    DubboPageResult<EsUploaderDO> getUploaderList(EsUploaderDTO uploaderDTO, int pageSize, int pageNum);

    /**
     * 根据主键删除数据
     * @author rm 2817512105@qq.com
     * @since 2019-06-04
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsUploaderDO>
     */
    DubboResult deleteUploader(Long id);
}
