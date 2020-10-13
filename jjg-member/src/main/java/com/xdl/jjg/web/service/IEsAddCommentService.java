package com.xdl.jjg.web.service;


import com.xdl.jjg.model.domain.EsAddCommentDO;
import com.xdl.jjg.model.dto.EsAddCommentDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 *  追加评论服务类
 * </p>
 *
 * @author LINS 1220316142@qq.com
 * @since 2019-10-08 15:19:22
 */
public interface IEsAddCommentService {

    /**
     * 插入数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @param addCommentDTO    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsAddCommentDO>
     */
    DubboResult insertAddComment(EsAddCommentDTO addCommentDTO);

    /**
     * 根据条件更新更新数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:10
     * @param addCommentDTO   DTO
     * @param id                            主键id
     * @return: com.shopx.common.model.result.DubboResult<EsAddCommentDO>
     */
    DubboResult updateAddComment(EsAddCommentDTO addCommentDTO, Long id);

    /**
     * 根据id获取数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsAddCommentDO>
     */
    DubboResult<EsAddCommentDO> getAddComment(Long id);

    /**
     * 根据评论commentId查询
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsAddCommentDO>
     */
    DubboResult<EsAddCommentDO> getAddCommentByCommentId(Long commentId);

    /**
     * 根据查询条件查询列表
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @param addCommentDTO  DTO
     * @param pageSize  行数
     * @param pageNum   页码
     * @return: com.shopx.common.model.result.DubboPageResult<EsAddCommentDO>
     */
    DubboPageResult<EsAddCommentDO> getAddCommentList(EsAddCommentDTO addCommentDTO, int pageSize, int pageNum);

    /**
     * 根据主键删除数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:44
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsAddCommentDO>
     */
    DubboResult deleteAddComment(Long id);
}
