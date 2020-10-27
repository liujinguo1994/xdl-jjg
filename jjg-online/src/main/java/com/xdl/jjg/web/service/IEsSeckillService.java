package com.xdl.jjg.web.service;


import com.jjg.shop.model.dto.EsGoodsSkuQuantityDTO;
import com.jjg.trade.model.domain.EsSeckillDO;
import com.jjg.trade.model.dto.EsSeckillDTO;
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
public interface IEsSeckillService {

    /**
     * 系统后台
     * 添加活动时间
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/07/29 15:23:30
     * @param seckillDTO    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsSeckillDO>
     */
    DubboResult insertSeckill(EsSeckillDTO seckillDTO);

    /**
     * 根据条件更新更新数据
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:40:10
     * @param seckillDTO    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsSeckillDO>
     */
    DubboResult updateSeckill(EsSeckillDTO seckillDTO);

    /** 系统后台
     * 根据id获取数据
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:37:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsSeckillDO>
     */
    DubboResult<EsSeckillDO> getSeckill(Long id);

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
    DubboPageResult<EsSeckillDO> getSeckillList(EsSeckillDTO seckillDTO, int pageSize, int pageNum);

    /**
     * 卖家端
     *
     * 根据查询条件查询列表
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/08/23 13:42:53
     * @param seckillDTO  DTO
     * @param pageSize      页码
     * @param pageNum       页数
     * @return: com.shopx.common.model.result.DubboPageResult<EsSeckillDO>
     */
    DubboPageResult<EsSeckillDO> getSellerSeckillList(EsSeckillDTO seckillDTO, int pageSize, int pageNum);

    /**
     * 根据主键删除数据
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:40:44
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsSeckillDO>
     */
    DubboResult deleteSeckill(Long id);

    DubboResult<EsSeckillDO> unloadSeckill(Long id);

    /**
     * 秒杀商品 增加已销售库存数量
     * @param quantityDTO
     */
    DubboResult addSoldNum(List<EsGoodsSkuQuantityDTO> quantityDTO);

}
