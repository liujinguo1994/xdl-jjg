package com.xdl.jjg.web.service;


import com.jjg.trade.model.domain.EsSeckillRangeDO;
import com.jjg.trade.model.dto.EsSeckillRangeDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LiuJG 344009799@qq.com
 * @since 2019-06-03
 */
public interface IEsSeckillRangeService {

    /**
     * 插入数据
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:39:30
     * @param seckillRangeDTO    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsSeckillRangeDO>
     */
    DubboResult insertSeckillRange(EsSeckillRangeDTO seckillRangeDTO);

    /**
     * 根据条件更新更新数据
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:40:10
     * @param seckillRangeDTO    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsSeckillRangeDO>
     */
    DubboResult updateSeckillRange(EsSeckillRangeDTO seckillRangeDTO);

    /**
     * 根据id获取数据
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:37:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsSeckillRangeDO>
     */
    DubboResult getSeckillRange(Long id);

    /**
     * 根据查询条件查询列表
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/06/03 13:42:53
     * @param seckillRangeDTO  DTO
     * @param pageSize      页码
     * @param pageNum       页数
     * @return: com.shopx.common.model.result.DubboPageResult<EsSeckillRangeDO>
     */
    DubboPageResult getSeckillRangeList(EsSeckillRangeDTO seckillRangeDTO, int pageSize, int pageNum);

    /**
     * 根据主键删除数据
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:40:44
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsSeckillRangeDO>
     */
    DubboResult deleteSeckillRange(Long id);
    /**
     * 买家端
     * 查询当天的秒杀活动时刻列表
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/09/16 16:40:44
     * @return: List<TimeLineVO>
     */
    DubboPageResult readTimeList();

    DubboPageResult<EsSeckillRangeDO> getSeckillRangeBySeckillId(Long seckillId);

}
