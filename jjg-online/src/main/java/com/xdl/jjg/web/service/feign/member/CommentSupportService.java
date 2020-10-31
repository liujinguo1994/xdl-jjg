package com.xdl.jjg.web.service.feign.member;

import com.jjg.member.model.dto.EsCommentSupportDTO;
import com.xdl.jjg.response.service.DubboResult;

public interface CommentSupportService {

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



}
