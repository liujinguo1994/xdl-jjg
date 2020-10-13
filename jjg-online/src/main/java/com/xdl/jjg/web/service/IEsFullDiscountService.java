package com.xdl.jjg.web.service;


import com.xdl.jjg.model.domain.EsFullDiscountDO;
import com.xdl.jjg.model.dto.EsFullDiscountDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 * 满减满赠表 服务类
 * </p>
 *
 * @author LBW 981087977@qq.com
 * @since 2019-06-04 17:12:08
 */
public interface IEsFullDiscountService {

    /**
     * 插入数据
     *
     * @param fullDiscountDTO 满减满赠表DTO
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsFullDiscountDO>
     */
    DubboResult insertFullDiscount(EsFullDiscountDTO fullDiscountDTO);

    /**
     * 根据条件更新更新数据
     *
     * @param fullDiscountDTO 满减满赠表DTO
     * @param id              主键id
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsFullDiscountDO>
     */
    DubboResult updateFullDiscount(EsFullDiscountDTO fullDiscountDTO, Long id);

    /**
     * 根据id获取数据
     *
     * @param id 主键id
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsFullDiscountDO>
     */
    DubboResult<EsFullDiscountDO> getFullDiscount(Long id);

    /**
     * 根据查询条件查询列表
     *
     * @param fullDiscountDTO 满减满赠表DTO
     * @param pageSize        行数
     * @param pageNum         页码
     * @auther: libw 981087977@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsFullDiscountDO>
     */
    DubboPageResult<EsFullDiscountDO> getFullDiscountList(EsFullDiscountDTO fullDiscountDTO, int pageSize, int pageNum);

    /**
     * 根据主键删除数据
     *
     * @param id 主键id
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsFullDiscountDO>
     */
    DubboResult deleteFullDiscount(Long id);

    /**
     * 根据活动id获取满减活动缓存
     * @author: libw 981087977@qq.com
     * @date: 2019/06/18 16:57:03
     * @param activityId    活动ID
     * @return: com.shopx.common.model.result.DubboResult
     */
    DubboResult getFullDiscountForCache(Long activityId);

    DubboResult<EsFullDiscountDO> getSellerFullDiscount(Long id);

    DubboResult<EsFullDiscountDO> getFullDiscountByTime(Long shopId);

}
