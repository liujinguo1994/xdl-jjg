package com.xdl.jjg.web.service;


import com.jjg.member.model.domain.EsCommentSortConfigDO;
import com.jjg.member.model.dto.EsCommentConfigDTO;
import com.jjg.member.model.dto.EsCommentSortConfigDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-29 14:18:11
 */
public interface IEsCommentSortConfigService {

    /**
     * 插入数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @param commentSortConfigDTO    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsCommentSortConfigDO>
     */
    DubboResult insertCommentSortConfig(EsCommentConfigDTO esCommentConfigDTO);

    /**
     * 根据条件更新更新数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:10
     * @param commentSortConfigDTO   DTO
     * @param id                            主键id
     * @return: com.shopx.common.model.result.DubboResult<EsCommentSortConfigDO>
     */
    DubboResult updateCommentSortConfig(List<EsCommentSortConfigDTO> commentSortConfigDTOList);

    /**
     * 根据id获取数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsCommentSortConfigDO>
     */
    DubboResult<EsCommentSortConfigDO> getCommentSortConfig(Long id);

    /**
     * 根据查询条件查询列表
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsCommentSortConfigDO>
     */
    DubboPageResult<EsCommentSortConfigDO> getCommentSortConfigList();

    /**
     * 根据主键删除数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:44
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsCommentSortConfigDO>
     */
    DubboResult deleteCommentSortConfig(Long id);
}
