package com.xdl.jjg.web.service;


import com.xdl.jjg.model.domain.EsAdminTagsDO;
import com.xdl.jjg.model.dto.EsAdminTagsDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author WAF 826988665@qq.com
 * @since 2019-07-27 14:57:56
 */
public interface IEsAdminTagsService {

    /**
     * 插入数据
     * @auther: WAF 826988665@qq.com
     * @date: 2019-07-27 14:57:56
     * @param adminTagsDTO    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsAdminTagsDO>
     */
    DubboResult insertAdminTags(EsAdminTagsDTO adminTagsDTO);

    /**
     * 根据条件更新更新数据
     * @auther: WAF 826988665@qq.com
     * @date: 2019/05/31 16:40:10
     * @param adminTagsDTO   DTO
     * @param id                            主键id
     * @return: com.shopx.common.model.result.DubboResult<EsAdminTagsDO>
     */
    DubboResult updateAdminTags(EsAdminTagsDTO adminTagsDTO, Long id);

    /**
     * 根据id获取数据
     * @auther: WAF 826988665@qq.com
     * @date: 2019/05/31 16:37:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsAdminTagsDO>
     */
    DubboResult<EsAdminTagsDO> getAdminTags(Long id);

    /**
     * 根据查询条件查询列表
     * @auther: WAF 826988665@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsAdminTagsDO>
     */
    DubboPageResult<EsAdminTagsDO> getAdminTagsList();
    DubboPageResult<EsAdminTagsDO> getAdminTagsList(EsAdminTagsDTO esAdminTagsDTO, int pageSize, int pageNum);
    /**
     * 根据主键删除数据
     * @auther: WAF 826988665@qq.com
     * @date: 2019/05/31 16:40:44
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsAdminTagsDO>
     */
    DubboResult deleteAdminTags(Long id);
}
