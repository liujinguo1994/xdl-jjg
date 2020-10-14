package com.xdl.jjg.web.service;


import com.jjg.member.model.dto.EsCommentReplyDTO;
import com.xdl.jjg.model.domain.EsCommentReplyDO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 * 评论回复(店家回复) 服务类
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2019-07-24
 */
public interface IEsCommentReplyService {

    /**
     * 插入数据
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/05/31 16:39:30
     * @param commentReplyDTO    评论回复(店家回复)DTO
     * @return: com.shopx.common.model.result.DubboResult<EsCommentReplyDO>
     */
    DubboResult insertCommentReply(EsCommentReplyDTO commentReplyDTO, Long[] ids);

    /**
     * 根据条件更新更新数据
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/05/31 16:40:10
     * @param commentReplyDTO    评论回复(店家回复)DTO
     * @return: com.shopx.common.model.result.DubboResult<EsCommentReplyDO>
     */
    DubboResult updateCommentReply(EsCommentReplyDTO commentReplyDTO);

    /**
     * 根据id获取数据
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/05/31 16:37:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsCommentReplyDO>
     */
    DubboResult<EsCommentReplyDO> getCommentReply(Long id);

    /**
     * 根据评论id获取数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @param commentId    评论id
     * @return: com.shopx.common.model.result.DubboResult<EsCommentReplyDO>
     */
    DubboResult<EsCommentReplyDO> getCommentReplyByCommentId(Long commentId);

    /**
     * 根据查询条件查询列表
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/06/03 13:42:53
     * @param commentReplyDTO  评论回复(店家回复)DTO
     * @param pageSize  行数
     * @param pageNum   页码
     * @return: com.shopx.common.model.result.DubboPageResult<EsCommentReplyDO>
     */
    DubboPageResult<EsCommentReplyDO> getCommentReplyList(EsCommentReplyDTO commentReplyDTO, int pageSize, int pageNum);

    /**
     * 根据主键删除数据
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/05/31 16:40:44
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsCommentReplyDO>
     */
    DubboResult deleteCommentReply(Long id);
}
