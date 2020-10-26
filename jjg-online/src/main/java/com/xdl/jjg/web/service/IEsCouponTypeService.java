package com.xdl.jjg.web.service;


import com.jjg.trade.model.domain.EsCouponTypeDO;
import com.jjg.trade.model.dto.EsCouponTypeDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LBW 981087977@qq.com
 * @since 2019-11-21 10:38:45
 */
public interface IEsCouponTypeService {

    /**
     * 插入数据
     * @auther: LBW 981087977@qq.com
     * @date: 2019-11-21 10:38:45
     * @param couponTypeDTO    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsCouponTypeDO>
     */
    DubboResult insertCouponType(EsCouponTypeDTO couponTypeDTO);

    /**
     * 根据条件更新更新数据
     * @auther: LBW 981087977@qq.com
     * @date: 2019/05/31 16:40:10
     * @param couponTypeDTO   DTO
     * @param id                            主键id
     * @return: com.shopx.common.model.result.DubboResult<EsCouponTypeDO>
     */
    DubboResult updateCouponType(EsCouponTypeDTO couponTypeDTO, Long id);

    /**
     * 根据id获取数据
     * @auther: LBW 981087977@qq.com
     * @date: 2019/05/31 16:37:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsCouponTypeDO>
     */
    DubboResult<EsCouponTypeDO> getCouponType(Long id);

    /**
     * 根据查询条件查询列表
     * @auther: LBW 981087977@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsCouponTypeDO>
     */
    DubboPageResult<EsCouponTypeDO> getCouponTypeList();

    /**
     * 根据主键删除数据
     * @auther: LBW 981087977@qq.com
     * @date: 2019/05/31 16:40:44
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsCouponTypeDO>
     */
    DubboResult deleteCouponType(Long id);
}
