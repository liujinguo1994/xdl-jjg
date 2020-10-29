package com.xdl.jjg.web.service.feign.member;

import com.jjg.member.model.domain.EsAutoCommentConfigDO;
import com.xdl.jjg.response.service.DubboResult;

public interface AutoCommentConfigService {


    /**
     * 查询列表
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsAutoCommentConfigDO>
     */
    DubboResult<EsAutoCommentConfigDO> getAutoCommentConfigList();
}
