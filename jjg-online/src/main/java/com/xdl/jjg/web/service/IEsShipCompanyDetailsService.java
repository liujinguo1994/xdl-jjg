package com.xdl.jjg.web.service;


import com.xdl.jjg.message.ShipCompanyMsg;
import com.xdl.jjg.model.domain.EsShipCompanyDetailsDO;
import com.xdl.jjg.model.dto.EsShipCompanyDetailsDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 * 公司运费模版详情 服务类
 * </p>
 *
 * @author LBW 981087977@qq.com
 * @since 2019-11-28 10:41:07
 */
public interface IEsShipCompanyDetailsService {

    /**
     * 插入数据
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:39:30
     * @param shipCompanyDetailsDTO    公司运费模版详情DTO
     * @return: com.shopx.common.model.result.DubboResult<EsShipCompanyDetailsDO>
     */
    DubboResult insertShipCompanyDetails(EsShipCompanyDetailsDTO shipCompanyDetailsDTO);

    /**
     * 根据条件更新更新数据
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:40:10
     * @param shipCompanyDetailsDTO   公司运费模版详情DTO
     * @param id                            主键id
     * @return: com.shopx.common.model.result.DubboResult<EsShipCompanyDetailsDO>
     */
    DubboResult updateShipCompanyDetails(EsShipCompanyDetailsDTO shipCompanyDetailsDTO, Long id);

    /**
     * 根据id获取数据
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:37:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsShipCompanyDetailsDO>
     */
    DubboResult<EsShipCompanyDetailsDO> getShipCompanyDetails(Long id);

    /**
     * 根据查询条件查询列表
     * @auther: libw 981087977@qq.com
     * @date: 2019/06/03 13:42:53
     * @param shipCompanyDetailsDTO  公司运费模版详情DTO
     * @param pageSize  行数
     * @param pageNum   页码
     * @return: com.shopx.common.model.result.DubboPageResult<EsShipCompanyDetailsDO>
     */
    DubboPageResult<EsShipCompanyDetailsDO> getShipCompanyDetailsList(EsShipCompanyDetailsDTO shipCompanyDetailsDTO, int pageSize, int pageNum);

    /**
     * 根据主键删除数据
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:40:44
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsShipCompanyDetailsDO>
     */
    DubboResult deleteShipCompanyDetails(Long id);

    /**
     * 根据区域和类型判断是否存在配送
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:37:16
     * @param type 类型
     * @param areaId 地区id
     * @return: com.shopx.common.model.result.DubboResult<EsShipCompanyDetailsDO>
     */
    DubboResult<Boolean> getByAreaId(int type, String areaId);

    /**
     * 根据价格地区获取运费详情
     * @auther: libw 981087977@qq.com
     * @date: 2019/05/31 16:37:16
     * @param type 类型
     * @param areaId 地区id
     * @return: com.shopx.common.model.result.DubboResult<EsShipCompanyDetailsDO>
     */
    DubboResult<EsShipCompanyDetailsDO> getByTypeAndPrice(int type, String areaId, Double price, Long shopId);
    DubboResult<EsShipCompanyDetailsDO> getPrice(int type, String areaId, Long shopId, Long templateId, Long isNg);
    DubboResult<EsShipCompanyDetailsDO> getPrice(Long areaId);
    DubboResult saveShipCompanyDetail(ShipCompanyMsg shipCompanyMsg);

    DubboResult<Boolean> getByAreaIdAndShopId(int type, String areaId, Long shopId);
}
