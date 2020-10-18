package com.xdl.jjg.web.service.feign.member;

import com.jjg.member.model.domain.EsCommentSortConfigDO;
import com.jjg.member.model.dto.EsCommentConfigDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "jjg-member")
public interface CommentSortConfigService {

    /**
     * 插入数据
     */
    @PostMapping("/insertCommentSortConfig")
    DubboResult insertCommentSortConfig(@RequestBody EsCommentConfigDTO esCommentConfigDTO);

    /**
     * 根据查询条件查询列表
     */
    @GetMapping("/getCommentSortConfigList")
    DubboPageResult<EsCommentSortConfigDO> getCommentSortConfigList();

}
