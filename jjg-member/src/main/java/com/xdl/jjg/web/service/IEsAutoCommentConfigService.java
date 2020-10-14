package com.xdl.jjg.web.service;


import com.jjg.member.model.domain.EsAutoCommentConfigDO;
import com.jjg.member.model.dto.EsAutoCommentConfigDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 *  系统自动评论服务类
 * </p>
 *
 * @author LINS 1220316142@qq.com
 * @since 2019-09-16 14:19:40
 */
public interface IEsAutoCommentConfigService {

    /**
     * 插入数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @param autoCommentConfigDTO    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsAutoCommentConfigDO>
     */
    DubboResult insertAutoCommentConfig(EsAutoCommentConfigDTO autoCommentConfigDTO);

    /**
     * 根据条件更新更新数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:10
     * @param autoCommentConfigDTO   DTO
     * @param id                            主键id
     * @return: com.shopx.common.model.result.DubboResult<EsAutoCommentConfigDO>
     */
    DubboResult updateAutoCommentConfig(EsAutoCommentConfigDTO autoCommentConfigDTO, Long id);

    /**
     * 根据id获取数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsAutoCommentConfigDO>
     */
    DubboResult<EsAutoCommentConfigDO> getAutoCommentConfig(Long id);

    /**
     * 查询列表
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsAutoCommentConfigDO>
     */
    DubboResult<EsAutoCommentConfigDO> getAutoCommentConfigList();

    /**
     * 根据查询条件查询列表
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @param autoCommentConfigDTO  DTO
     * @param pageSize  行数
     * @param pageNum   页码
     * @return: com.shopx.common.model.result.DubboPageResult<EsAutoCommentConfigDO>
     */
    DubboPageResult<EsAutoCommentConfigDO> getAutoCommentConfig(EsAutoCommentConfigDTO autoCommentConfigDTO, int pageSize, int pageNum);

    /**
     * 根据主键删除数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:44
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsAutoCommentConfigDO>
     */
    DubboResult deleteAutoCommentConfig(Long id);
}
