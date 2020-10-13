package com.xdl.jjg.web.service;

import com.xdl.jjg.model.domain.*;
import com.xdl.jjg.model.dto.*;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 * 店铺表 服务类
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2019-06-18
 */
public interface IEsShopService {

    /**
     * 插入数据
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/6/18 10:33:30
     * @param shopDTO    店铺表DTO
     * @return: com.shopx.common.model.result.DubboResult<EsShopDO>
     */
    DubboResult<Long> insertShop(EsShopDTO shopDTO);

    /**
     * 根据条件更新更新数据
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/06/24 15:58:10
     * @param esShopAndDetailDTO    店铺及详情
     * @return: com.shopx.common.model.result.DubboResult<EsShopAndDetailDO>
     */
    DubboResult updateShop(EsShopAndDetailDTO esShopAndDetailDTO);

    /**
     * 更新店铺状态
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/06/24 15:58:10
     * @param state    店铺状态
     * @param shopId    店铺id
     * @return: com.shopx.common.model.result.DubboResult<EsShopAndDetailDO>
     */
    DubboResult updateState(String state, Long shopId);

//    /**
//     * 店铺审核
//     * @auther: yuanj 595831329@qq.com
//     * @date: 2019/06/24 15:40:10
//     * @param id    店铺表主键
//     * @param state    0通过，1拒绝
//     * @return: com.shopx.common.model.result.DubboResult<EsShopDO>
//     */
//    DubboResult checkShop(Long id,int state);

    /**
     * 后台店铺关闭
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/05/31 16:40:10
     * @param id 店铺主键
     * @return: com.shopx.common.model.result.DubboResult<EsShopDO>
     */
    DubboResult underShop(long id);

    /**
     * 后台店铺开启
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/05/31 16:40:10
     * @param id 店铺主键
     * @return: com.shopx.common.model.result.DubboResult<EsShopDO>
     */
    DubboResult useShop(long id);

    /**
     * 根据id获取店铺
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/06/24 16:37:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsShopDO>
     */
    DubboResult<EsShopDO> getShop(Long id);

    /**
     * 根据会员id获取店铺
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/06/24 16:37:16
     * @param memberId    会员id
     * @return: com.shopx.common.model.result.DubboResult<EsShopDO>
     */
    DubboResult<EsShopDO> getShopByMemeberId(Long memberId);

    /**
     * 根据id获取店铺及店铺明细
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/06/24 16:37:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsShopAndDetailDO>
     */
    DubboResult<EsShopAndDetailDO> getShopDetail(Long id);

    /**
     * 买家-商品详情页根据id获取店铺及店铺明细
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/06/24 16:37:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsShopAndDetailDO>
     */
    DubboResult<EsShopDO> getBuyerShopDetail(Long id);

    /**
     * 根据查询条件查询列表
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/06/03 13:42:53
     * @param shopQueryParam  店铺查询参数
     * @param pageSize  行数
     * @param pageNum   页码
     * @return: com.shopx.common.model.result.DubboPageResult<EsShopDO>
     */
    DubboPageResult<EsShopDO> getShopList(ShopQueryParam shopQueryParam, int pageSize, int pageNum);

    /**
     * 根据主键删除数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:44
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsShopDO>
     */
    DubboResult deleteShop(Long id);


    /**
     * 初始化店铺信息
     */
    DubboResult saveInit(Long memberId) ;

    /**
     * 申请开店第一步
     * @param applyStep1 申请开店第一步VO
     * @return 申请开店第一步VO
     */
    DubboResult<ApplyStep1DO> step1(ApplyStep1DTO applyStep1, Long memberId);

    /**
     * 申请开店第二步
     * @param applyStep2 申请开店第二步VO
     * @return 申请开店第二步VO
     */
    DubboResult<ApplyStep2DO> step2(ApplyStep2DTO applyStep2, Long memberId);

    /**
     * 申请开店第三步
     * @param applyStep3 申请开店第三步VO
     * @return 申请开店第三步VO
     */
    DubboResult<ApplyStep3DO> step3(ApplyStep3DTO applyStep3, Long memberId);

    /**
     * 申请开店第四步
     * @param applyStep4 申请开店第四步VO
     * @return 申请开店第四步VO
     */
    DubboResult<ApplyStep4DO> step4(ApplyStep4DTO applyStep4, Long memberId);

}
