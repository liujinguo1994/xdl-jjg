package com.xdl.jjg.web.service.feign.system;

import com.jjg.system.model.dto.EsUserFeedbackDTO;
import com.xdl.jjg.response.service.DubboResult;

public interface UserFeedbackService {

    /**
     * 插入数据
     *
     * @param userFeedbackDTO DTO
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsUserFeedbackDO>
     */
    DubboResult insertUserFeedback(EsUserFeedbackDTO userFeedbackDTO);
}
