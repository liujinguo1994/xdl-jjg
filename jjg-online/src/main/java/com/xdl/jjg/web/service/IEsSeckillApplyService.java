package com.xdl.jjg.web.service;


import com.jjg.trade.model.domain.EsSeckillApplyDO;
import com.jjg.trade.model.domain.EsSeckillTimetableDO;
import com.jjg.trade.model.dto.EsSeckillApplyDTO;
import com.jjg.trade.model.dto.EsSeckillTimelineGoodsDTO;
import com.jjg.trade.model.vo.SeckillGoodsVO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LiuJG 344009799@qq.com
 * @since 2019-06-03
 */
public interface IEsSeckillApplyService {

    /** 卖家端批量添加秒杀商品
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/07/29 15:23:30
     * @param seckillApplyDTOList    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsSeckillApplyDO>
     */
    DubboResult insertSeckillApply(List<EsSeckillApplyDTO> seckillApplyDTOList, Long shopId, Long seckillId);

    /**
     * 系统后台 审核商品
     * 根据条件更新更新数据
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:40:10
     * @param seckillApplyDTO    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsSeckillApplyDO>
     */
    DubboResult updateSeckillApply(EsSeckillApplyDTO seckillApplyDTO);

    /**
     * 系统后台 查看商品列表
     * 根据id获取数据
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:37:16
     * @param seckillId
     * @return: com.shopx.common.model.result.DubboResult<EsSeckillApplyDO>
     */
    DubboResult<EsSeckillApplyDO> getSeckillApply(Long seckillId);

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
    DubboPageResult<EsSeckillApplyDO> getSeckillApplyList(EsSeckillApplyDTO seckillApplyDTO, int pageSize, int pageNum);

    /**
     * 卖家端
     * 查询卖家端参加秒杀活动商品列表
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/06/03 13:42:53
     * @param seckillApplyDTO  DTO
     * @param pageSize      页码
     * @param pageNum       页数
     * @return: com.shopx.common.model.result.DubboPageResult<EsSeckillApplyDO>
     */
    DubboPageResult<EsSeckillApplyDO> getSellerSeckillApplyList(EsSeckillApplyDTO seckillApplyDTO, int pageSize, int pageNum);

    /**
     * 根据主键删除数据
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:40:44
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsSeckillApplyDO>
     */
    DubboResult deleteSeckillApply(Long id);

    /**
     * 根据时间查询通过申请的活动
     * @author: libw 981087977@qq.com
     * @date: 2019/06/18 13:24:17
     * @param seckillApplyDTO 数据限时购申请List
     * @return: com.shopx.common.model.result.DubboPageResult
     */
    DubboPageResult getSeckillApplyPassList(EsSeckillApplyDTO seckillApplyDTO);

    /**
     * 将商品压入缓存
     * @param startTime
     * @param timeLine
     * @param goodsVO
     */
    void addRedis(Long startTime, Integer timeLine, SeckillGoodsVO goodsVO);

    /**
     * 根据商品id获取正在进行的活动
     * @author: libw 981087977@qq.com
     * @date: 2019/06/18 13:24:17
     * @param shopIds 商品id
     * @return: com.shopx.common.model.result.DubboPageResult
     */
    DubboResult getSeckillApplyByShopId(List<Long> shopIds);

    /**
     * 获取今天的秒杀时刻表
     */
    DubboResult<List<EsSeckillTimetableDO>> getSeckillTimetableToday();

    /**
     * 获取时间点参加秒杀并审核通过的商品
     */
    DubboPageResult<EsSeckillApplyDO> seckillTimelineGoodsList(EsSeckillTimelineGoodsDTO esSeckillTimelineGoodsDTO, long pageNum, long pageSize);

    /**
     * 获取现在正在进行的秒杀场次
     */
    DubboResult<EsSeckillTimetableDO> getInSeckillNow();
}
