package com.xdl.jjg.web.service.feign.trade;

import com.jjg.trade.model.dto.EsSelfDateDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "jjg-online")
public interface SelfDateService {
    /**
     * 系统后台
     * 根据id获取数据
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:37:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsSelfDateDO>
     */
    @GetMapping("/getSelfDate")
    DubboResult getSelfDate(@RequestParam("id") Long id);

    /**
     *  系统后台
     * 0702 获取当前状态为有效的时间点
     * @return
     */
    @GetMapping("/getAllSelfDate")
    DubboResult getAllSelfDate();

    /**
     *   系统后台
     * 查询列表
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/07/02
     * @param selfDateDTO  自提日期DTO
     * @param pageSize      页码
     * @param pageNum       页数
     * @return: com.shopx.common.model.result.DubboPageResult<EsSelfDateDO>
     */
    @GetMapping("/getSelfDateList")
    DubboPageResult getSelfDateList(@RequestBody EsSelfDateDTO selfDateDTO,@RequestParam("pageSize") int pageSize,@RequestParam("pageNum") int pageNum);

    /**
     *  系统后台
     * 插入数据
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/07/02
     * @param selfDateDTO    自提日期DTO
     * @return: com.shopx.common.model.result.DubboResult<EsSelfDateDO>
     */
    @PostMapping("/insertSelfDate")
    DubboResult insertSelfDate(@RequestBody EsSelfDateDTO selfDateDTO);

    /**
     * 根据条件更新更新数据
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:40:10
     * @param selfDateDTO    自提日期DTO
     * @return: com.shopx.common.model.result.DubboResult<EsSelfDateDO>
     */
    @PostMapping("/updateSelfDate")
    DubboResult updateSelfDate(@RequestBody EsSelfDateDTO selfDateDTO);
    /**
     *  系统后台
     * 根据主键删除数据
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/0702 16:40:44
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsSelfDateDO>
     */
    @DeleteMapping("/deleteSelfDate")
    DubboResult deleteSelfDate(@RequestParam("id") Long id);

}
