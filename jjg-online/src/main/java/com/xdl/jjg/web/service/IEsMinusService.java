package com.xdl.jjg.web.service;

import com.shopx.common.model.result.DubboPageResult;
import com.shopx.common.model.result.DubboResult;
import com.shopx.trade.api.model.domain.EsMinusDO;
import com.shopx.trade.api.model.domain.dto.EsMinusDTO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LiuJG 344009799@qq.com
 * @since 2019-06-03
 */
public interface IEsMinusService {

    /**
     * 插入数据
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:39:30
     * @param minusDTO    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsMinusDO>
     */
    DubboResult insertMinus(EsMinusDTO minusDTO);

    /**
     * 根据条件更新更新数据
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:40:10
     * @param minusDTO    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsMinusDO>
     */
    DubboResult updateMinus(EsMinusDTO minusDTO);

    /**
     * 根据id获取数据
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:37:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsMinusDO>
     */
    DubboResult getMinus(Long id);

    /**
     * 根据查询条件查询列表
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/06/03 13:42:53
     * @param minusDTO  DTO
     * @param pageSize      页码
     * @param pageNum       页数
     * @return: com.shopx.common.model.result.DubboPageResult<EsMinusDO>
     */
    DubboPageResult getMinusList(EsMinusDTO minusDTO, int pageSize, int pageNum);

    /**
     * 根据主键删除数据
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:40:44
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsMinusDO>
     */
    DubboResult deleteMinus(Long id);

    /**
     * 通过活动ID查询活动
     * @author: libw 981087977@qq.com
     * @date: 2019/06/19 15:23:34
     * @param activityId    活动id
     * @return: com.shopx.common.model.result.DubboResult
     */
    DubboResult getMinusForCache(Long activityId);

    DubboResult<EsMinusDO> getSellerMinus(Long id);

    DubboResult<EsMinusDO> getMinusByTime(Long shopId);
}
