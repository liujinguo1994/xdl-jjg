package com.xdl.jjg.web.service;

import com.shopx.common.model.result.DubboPageResult;
import com.shopx.common.model.result.DubboResult;
import com.shopx.trade.api.model.domain.EsFullDiscountGiftDO;
import com.shopx.trade.api.model.domain.dto.EsFullDiscountGiftDTO;
import com.shopx.trade.api.model.domain.dto.EsGiftSkuQuantityDTO;

import java.util.List;

/**
 * <p>
 * 满减赠品表 服务类
 * </p>
 *
 * @author LBW 981087977@qq.com
 * @since 2019-06-04 17:12:08
 */
public interface IEsFullDiscountGiftService {

    /**
     * 插入数据
     *
     * @param fullDiscountGiftDTO 满减赠品表DTO
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsFullDiscountGiftDO>
     */
    DubboResult insertFullDiscountGift(EsFullDiscountGiftDTO fullDiscountGiftDTO);

    /**
     * 根据条件更新更新数据
     *
     * @param fullDiscountGiftDTO 满减赠品表DTO
     * @param id                  主键id
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsFullDiscountGiftDO>
     */
    DubboResult updateFullDiscountGift(EsFullDiscountGiftDTO fullDiscountGiftDTO, Long id);

    DubboResult updateFullDiscountGift(List<EsFullDiscountGiftDTO> fullDiscountGiftDTO);
    /**
     * 根据id获取数据
     *
     * @param id 主键id
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsFullDiscountGiftDO>
     */
    DubboResult<EsFullDiscountGiftDO> getFullDiscountGift(Long id);

    DubboResult<EsFullDiscountGiftDO> getSellerFullDiscountGift(Long id);

    /**
     * 根据查询条件查询列表
     *
     * @param fullDiscountGiftDTO 满减赠品表DTO
     * @param pageSize            行数
     * @param pageNum             页码
     * @auther: libw 981087977@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsFullDiscountGiftDO>
     */
    DubboPageResult<EsFullDiscountGiftDO> getFullDiscountGiftList(EsFullDiscountGiftDTO fullDiscountGiftDTO, int pageSize, int pageNum);

    /**
     * 根据主键删除数据
     *
     * @param id 主键id
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsFullDiscountGiftDO>
     */
    DubboResult deleteFullDiscountGift(Long id);
    /**
     * 赠品信息数量减少
     *
     * @param
     * @auther: LIUJG 344009799@qq.com
     * @date: 2019/08/08 16:40:10
     * @return: com.shopx.common.model.result.DubboResult<EsFullDiscountGiftDO>
     */
    DubboResult reduceFullDiscountGiftNum(EsGiftSkuQuantityDTO giftQuantity);

    DubboResult insertFullDiscountGiftNum(EsGiftSkuQuantityDTO giftQuantity);
    /**
     * 批量删除数据
     * @author: libw 981087977@qq.com
     * @date: 2019/08/13 14:20:01
     * @param ids   id列表
     * @return: com.shopx.common.model.result.DubboResult
     */
    DubboResult batchDelete(Integer[] ids);

    /**
     * 批量删除数据
     * @author: libw 981087977@qq.com
     * @date: 2019/08/13 14:20:01
     * @param ids   id列表
     * @return: com.shopx.common.model.result.DubboResult
     */
    DubboResult batchDelete(Long[] ids);
}
