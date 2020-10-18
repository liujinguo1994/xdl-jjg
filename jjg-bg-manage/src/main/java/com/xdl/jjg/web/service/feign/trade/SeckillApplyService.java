package com.xdl.jjg.web.service.feign.trade;

import com.jjg.trade.model.domain.EsSeckillApplyDO;
import com.jjg.trade.model.dto.EsSeckillApplyDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "jjg-online")
public interface SeckillApplyService {

    /**
     * 系统后台
     * 查询限时抢购待审核与审核通过的商品列表接口 根据审核状态 秒杀id 查询该活动下的商品查询列表
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/06/03 13:42:53
     * @param seckillApplyDTO  DTO
     * @param pageSize      页码
     * @param pageNum       页数
     * @return: com.shopx.common.model.result.DubboPageResult<EsSeckillApplyDO>
     */
    @GetMapping("/getSeckillApplyList")
    DubboPageResult<EsSeckillApplyDO> getSeckillApplyList(@RequestBody EsSeckillApplyDTO seckillApplyDTO, @RequestParam("pageSize") int pageSize, @RequestParam("pageNum") int pageNum);
    /**
     * 系统后台 审核商品
     * 根据条件更新更新数据
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:40:10
     * @param seckillApplyDTO    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsSeckillApplyDO>
     */
    @PostMapping("/updateSeckillApply")
    DubboResult updateSeckillApply(@RequestBody EsSeckillApplyDTO seckillApplyDTO);

}
