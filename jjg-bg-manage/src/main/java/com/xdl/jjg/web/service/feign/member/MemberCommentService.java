package com.xdl.jjg.web.service.feign.member;

import com.jjg.member.model.domain.EsAdminManagerDO;
import com.jjg.member.model.dto.QueryCommentListDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "jjg-member")
public interface MemberCommentService {

    /**
     * 查询评价列表
     * @auther: lins 1220316142@qq.com
     * @param queryCommentListDTO
     * @param pageSize
     * @param pageNum
     * @return
     */
    @GetMapping("/getAdminManagerList")
    DubboPageResult<EsAdminManagerDO> getAdminManagerList(@RequestBody QueryCommentListDTO queryCommentListDTO, @RequestParam("pageSize") int pageSize, @RequestParam("pageNum") int pageNum);


    /**
     * 批量删除数据
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/06/28 16:40:44
     * @param ids    主键ids
     * @return: com.shopx.common.model.result.DubboResult<EsDiscountDO>
     */
    @DeleteMapping("/deleteDiscountComment")
    DubboResult deleteDiscountComment(@RequestParam("ids") Integer[] ids);
}
