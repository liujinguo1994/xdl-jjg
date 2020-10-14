package com.xdl.jjg.web.service;


import com.jjg.member.model.domain.EsCommentSupportDO;
import com.jjg.member.model.dto.EsCommentSupportDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 *  评论点赞服务类
 * </p>
 *
 * @author LINS 1220316142@qq.com
 * @since 2019-09-07 16:55:59
 */
public interface IEsCommentSupportService {

    /**
     * 插入数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @param commentSupportDTO    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsCommentSupportDO>
     */
    DubboResult insertCommentSupport(EsCommentSupportDTO commentSupportDTO);

    /**
     * 根据条件更新更新数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:10
     * @param commentSupportDTO   DTO
     * @param id                            主键id
     * @return: com.shopx.common.model.result.DubboResult<EsCommentSupportDO>
     */
    DubboResult updateCommentSupport(EsCommentSupportDTO commentSupportDTO, Long id);

    /**
     * 根据id获取数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsCommentSupportDO>
     */
    DubboResult<EsCommentSupportDO> getCommentSupport(Long id);

    /**
     * 根据评论id查询点赞数
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @param commentId    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsCommentSupportDO>
     */
    DubboResult<Integer> getCommentSupportNum(Long commentId);



    /**
     * 根据查询条件查询列表
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @param commentSupportDTO  DTO
     * @param pageSize  行数
     * @param pageNum   页码
     * @return: com.shopx.common.model.result.DubboPageResult<EsCommentSupportDO>
     */
    DubboPageResult<EsCommentSupportDO> getCommentSupportList(EsCommentSupportDTO commentSupportDTO, int pageSize, int pageNum);

    /**
     * 根据主键删除数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:44
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsCommentSupportDO>
     */
    DubboResult deleteCommentSupport(Long id);

    /**
     * 根据主键删除数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:44
     * @param commentId    主键id
     * @param memberId     会员id
     * @return: com.shopx.common.model.result.DubboResult<EsCommentSupportDO>
     */
    DubboResult judgeCommentSupport(Long commentId, Long memberId);
}
