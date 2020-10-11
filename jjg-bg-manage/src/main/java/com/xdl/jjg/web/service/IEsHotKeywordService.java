package com.xdl.jjg.web.service;


import com.xdl.jjg.model.domain.EsHotKeywordDO;
import com.xdl.jjg.model.dto.EsHotKeywordDTO;
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
public interface IEsHotKeywordService {

    /**
     * 插入数据
     * @author rm 2817512105@qq.com
     * @since 2019-06-04
     * @param hotKeywordDTO    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsHotKeywordDO>
     */
    DubboResult insertHotKeyword(EsHotKeywordDTO hotKeywordDTO);

    /**
     * 根据条件更新更新数据
     * @author rm 2817512105@qq.com
     * @since 2019-06-04
     * @param hotKeywordDTO    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsHotKeywordDO>
     */
    DubboResult updateHotKeyword(EsHotKeywordDTO hotKeywordDTO);

    /**
     * 根据id获取数据
     * @author rm 2817512105@qq.com
     * @since 2019-06-04
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsHotKeywordDO>
     */
    DubboResult<EsHotKeywordDO> getHotKeyword(Long id);

    /**
     * 根据查询条件查询列表
     * @author rm 2817512105@qq.com
     * @since 2019-06-04
     * @param hotKeywordDTO  DTO
     * @param pageSize  行数
     * @param pageNum   页码
     * @return: com.shopx.common.model.result.DubboPageResult<EsHotKeywordDO>
     */
    DubboPageResult<EsHotKeywordDO> getHotKeywordList(EsHotKeywordDTO hotKeywordDTO, int pageSize, int pageNum);

    /**
     * 根据主键删除数据
     * @author rm 2817512105@qq.com
     * @since 2019-06-04
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsHotKeywordDO>
     */
    DubboResult deleteHotKeyword(Long id);

    //查询热门关键字列表
    DubboPageResult<EsHotKeywordDO> getList();
}
