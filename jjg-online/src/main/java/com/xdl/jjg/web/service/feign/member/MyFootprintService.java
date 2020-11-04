package com.xdl.jjg.web.service.feign.member;

import com.jjg.member.model.domain.EsMyFootprintDO;
import com.jjg.member.model.dto.EsMyFootprintDTO;
import com.xdl.jjg.response.service.DubboResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
@FeignClient(value = "jjg-member")
public interface MyFootprintService {



    /**
     * 根据查询条件查询列表
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsMyFootprintDO>
     */
    @GetMapping("/getMyFootprintList")
    DubboResult<EsMyFootprintDO> getMyFootprintList(@RequestBody EsMyFootprintDTO myFootprintDTO);

    /**
     * 根据主键删除数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:44
     * @param memberId
     * @param viewTime
     * @return: com.shopx.common.model.result.DubboResult<EsMyFootprintDO>
     */
    @DeleteMapping("/deleteMyFoot")
    DubboResult deleteMyFoot(@RequestParam("memberId") Long memberId, @RequestParam("viewTime") String viewTime);

    /**
     * 根据店铺id查询
     * @auther: xiaoLin
     * @date: 2019/10/21 16:40:44
     * @param memberId
     * @param shopId
     * @return: com.shopx.common.model.result.DubboResult<EsMyFootprintDO>
     */
    @GetMapping("/getTopMyFoot")
    DubboResult getTopMyFoot(@RequestParam("memberId") Long memberId, @RequestParam("shopId") Long shopId);

    /**
     * 根据主键删除数据
     * @auther: xiaoLin
     * @date: 2019/10/21 16:40:44
     * @param id
     * @return: com.shopx.common.model.result.DubboResult<EsMyFootprintDO>
     */
    @DeleteMapping("/deleteMyFootById")
    DubboResult deleteMyFootById(@RequestParam("id") Long id);


}
