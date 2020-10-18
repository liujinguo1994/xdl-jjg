package com.xdl.jjg.web.service.feign.member;

import com.jjg.member.model.domain.EsCommentLabelDO;
import com.jjg.member.model.dto.EsCommentLabelDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "jjg-member")
public interface CommentLabelService {

    /**
     * 根据查询条件查询列表
     *
     * @param commentLabelDTO DTO
     * @param pageSize        行数
     * @param pageNum         页码
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsCommentLabelDO>
     */
    @GetMapping("/getCommentLabelList")
    DubboPageResult<EsCommentLabelDO> getCommentLabelList(@RequestBody EsCommentLabelDTO commentLabelDTO, @RequestParam("pageSize") int pageSize, @RequestParam("pageNum") int pageNum);

    /**
     * 插入数据
     *
     * @param commentLabelDTO DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsCommentLabelDO>
     */
    @PostMapping("/insertCommentLabel")
    DubboResult insertCommentLabel(@RequestBody EsCommentLabelDTO commentLabelDTO);

    /**
     * 根据条件更新更新数据
     *
     * @param commentLabelDTO DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsCommentLabelDO>
     */
    @PostMapping("/updateCommentLabel")
    DubboResult updateCommentLabel(@RequestBody EsCommentLabelDTO commentLabelDTO);

    /**
     * 批量删除标签内容
     *
     * @param ids 主键ids
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:44
     * @return: com.shopx.common.model.result.DubboResult
     */
    @DeleteMapping("/deleteEsCommentLabelBatch")
    DubboResult deleteEsCommentLabelBatch(@RequestParam("ids") Integer[] ids);
}
