package com.xdl.jjg.web.service.feign.member;

import com.jjg.member.model.domain.EsCommentCategoryDO;
import com.xdl.jjg.response.service.DubboPageResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "jjg-member")
public interface CommentCategoryService {

    /**
     * 根据三级类目id查询标签列表
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @param categoryId  Long
     * @return: com.shopx.common.model.result.DubboPageResult<EsCommentCategoryDO>
     */
    @GetMapping("/getEsCommentCategoryBindList")
    DubboPageResult<EsCommentCategoryDO> getEsCommentCategoryBindList(@RequestParam("categoryId") Long categoryId);

    /**
     * 插入数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @param     categoryId Long
     * @param     labelId Integer[]
     * @return: com.shopx.common.model.result.DubboResult<EsCommentCategoryDO>
     */
    @PostMapping("/saveCommentCategory")
    DubboPageResult saveCommentCategory(@RequestParam("categoryId") Long categoryId,@RequestParam("labelId") Integer[] labelId);

}
