package com.xdl.jjg.web.service;


import com.xdl.jjg.model.dto.EsCouponReceiveCheckDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LiuJG 344009799@qq.com
 * @since 2020-07-06
 */
public interface IEsCouponReceiveCheckService {

    /**
     * 插入数据
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:39:30
     * @param couponReceiveCheckDTO    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsCouponReceiveCheckDO>
     */
    DubboResult insertCouponReceiveCheck(EsCouponReceiveCheckDTO couponReceiveCheckDTO);

    /**
     * 根据条件更新更新数据
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:40:10
     * @param couponReceiveCheckDTO    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsCouponReceiveCheckDO>
     */
    DubboResult updateCouponReceiveCheck(EsCouponReceiveCheckDTO couponReceiveCheckDTO);

    /**
     * 根据id获取数据
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:37:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsCouponReceiveCheckDO>
     */
    DubboResult getCouponReceiveCheck(Long id);

    /**
     * 根据查询条件查询列表
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/06/03 13:42:53
     * @param couponReceiveCheckDTO  DTO
     * @param pageSize      页码
     * @param pageNum       页数
     * @return: com.shopx.common.model.result.DubboPageResult<EsCouponReceiveCheckDO>
     */
    DubboPageResult getCouponReceiveCheckList(EsCouponReceiveCheckDTO couponReceiveCheckDTO, int pageSize, int pageNum);

    /**
     * 根据主键删除数据
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:40:44
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsCouponReceiveCheckDO>
     */
    DubboResult deleteCouponReceiveCheck(Long id);

    DubboResult<Boolean> getCouponReceiveCheckByMobile(String mobile);
}
