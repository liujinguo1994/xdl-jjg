package com.xdl.jjg.web.service.feign.member;

import com.jjg.member.model.dto.EsAddCommentDTO;
import com.xdl.jjg.response.service.DubboResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
@FeignClient(value = "jjg-member")
public interface AddCommentService {


    /**
     * 插入数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @param addCommentDTO    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsAddCommentDO>
     */
    @PostMapping("/insertAddComment")
    DubboResult insertAddComment(@RequestBody EsAddCommentDTO addCommentDTO);
}
