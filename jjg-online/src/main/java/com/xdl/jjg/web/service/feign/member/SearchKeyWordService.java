package com.xdl.jjg.web.service.feign.member;

import com.jjg.member.model.dto.EsSearchKeyWordDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "jjg-member")
public interface SearchKeyWordService {
    /**
     * 商城首页 根据会员查询该会员的搜索历史
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsSearchKeyWordDO>
     */
    @GetMapping("/getSearchKeyWordList")
    DubboPageResult getSearchKeyWordList(@RequestParam("memberId") Long memberId);

    /**
     * 会员搜索历史数据添加
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:39:30
     * @param searchKeyWordDTO    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsSearchKeyWordDO>
     */
    @PostMapping("/insertSearchKeyWord")
    DubboResult insertSearchKeyWord(@RequestBody EsSearchKeyWordDTO searchKeyWordDTO);

    /**
     * 根据主键删除数据
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:40:44
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsSearchKeyWordDO>
     */
    @DeleteMapping("/deleteSearchKeyWord")
    DubboResult deleteSearchKeyWord(@RequestParam("id") Long id);

    /**
     * 根据会员ID 清空所有搜索历史
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:40:44
     * @param memberId    memberId
     * @return: com.shopx.common.model.result.DubboResult<EsSearchKeyWordDO>
     */
    @DeleteMapping("/deleteSearchKeyWordBatch")
    DubboResult deleteSearchKeyWordBatch(@RequestParam("memberId") Long memberId);

    /**
     * 购物车页面根据会员ID 获取该会员的前三条 查询历史
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsSearchKeyWordDO>
     */
    @GetMapping("/getSearchKeyWord")
    DubboPageResult getSearchKeyWord(@RequestParam("memberId") Long memberId);

}
