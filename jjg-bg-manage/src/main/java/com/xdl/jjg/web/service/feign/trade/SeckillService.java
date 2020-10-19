package com.xdl.jjg.web.service.feign.trade;

import com.jjg.trade.model.domain.EsSeckillDO;
import com.jjg.trade.model.dto.EsSeckillDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "jjg-online")
public interface SeckillService {
    /** 系统后台
     * 根据id获取数据
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:37:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsSeckillDO>
     */
    @GetMapping("/getSeckill")
    DubboResult<EsSeckillDO> getSeckill(@RequestParam("id") Long id);

    /**
     * 系统后台
     * 已调试
     * 根据查询条件查询列表
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/06/03 13:42:53
     * @param seckillDTO  DTO
     * @param pageSize      页码
     * @param pageNum       页数
     * @return: com.shopx.common.model.result.DubboPageResult<EsSeckillDO>
     */
    @GetMapping("/getSeckillList")
    DubboPageResult<EsSeckillDO> getSeckillList(@RequestBody EsSeckillDTO seckillDTO,@RequestParam("pageSize") int pageSize,@RequestParam("pageNum") int pageNum);
    /**
     * 系统后台
     * 添加活动时间
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/07/29 15:23:30
     * @param seckillDTO    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsSeckillDO>
     */
    @PostMapping("/insertSeckill")
    DubboResult insertSeckill(@RequestBody EsSeckillDTO seckillDTO);

    /**
     * 根据条件更新更新数据
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:40:10
     * @param seckillDTO    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsSeckillDO>
     */
    @PostMapping("/updateSeckill")
    DubboResult updateSeckill(@RequestBody EsSeckillDTO seckillDTO);

    /**
     * 根据主键删除数据
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:40:44
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsSeckillDO>
     */
    @DeleteMapping("/deleteSeckill")
    DubboResult deleteSeckill(@RequestParam("id") Long id);

    @PostMapping("/unloadSeckill")
    DubboResult<EsSeckillDO> unloadSeckill(@RequestParam("id")Long id);

}
