package com.xdl.jjg.web.service.feign.member;

import com.jjg.member.model.domain.EsCommentCategoryClassifyDO;
import com.xdl.jjg.response.service.DubboResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestParam;
@FeignClient(value = "jjg-member")
public interface CommentCategoryService {

    /**
     * 查询该分类已绑定的标签和未绑定标签
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @param categoryId  Long
     * @return: com.shopx.common.model.result.DubboPageResult<EsCommentCategoryDO>
     */
    DubboResult<EsCommentCategoryClassifyDO> getEsCommentCategoryList(@RequestParam("categoryId") Long categoryId);
}
