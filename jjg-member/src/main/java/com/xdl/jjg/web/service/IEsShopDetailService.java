package com.xdl.jjg.web.service;


import com.xdl.jjg.model.domain.EsShopDetailDO;
import com.xdl.jjg.model.dto.EsShopDetailDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 * 店铺详细 服务类
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-04
 */
public interface IEsShopDetailService {

    /**
     * 插入数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @param shopDetailDTO    店铺详细DTO
     * @return: com.shopx.common.model.result.DubboResult<EsShopDetailDO>
     */
    DubboResult insertShopDetail(EsShopDetailDTO shopDetailDTO);

    /**
     * 根据条件更新更新数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:10
     * @param shopDetailDTO    店铺详细DTO
     * @return: com.shopx.common.model.result.DubboResult<EsShopDetailDO>
     */
    DubboResult updateShopDetail(EsShopDetailDTO shopDetailDTO);

    /**
     * 根据id获取数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsShopDetailDO>
     */
    DubboResult<EsShopDetailDO> getShopDetail(Long id);

    /**
     * 根据查询条件查询列表
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/03 13:42:53
     * @param shopDetailDTO  店铺详细DTO
     * @param pageSize  行数
     * @param pageNum   页码
     * @return: com.shopx.common.model.result.DubboPageResult<EsShopDetailDO>
     */
    DubboPageResult<EsShopDetailDO> getShopDetailList(EsShopDetailDTO shopDetailDTO, int pageSize, int pageNum);

    /**
     * 根据主键删除数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:44
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsShopDetailDO>
     */
    DubboResult deleteShopDetail(Long id);

    /**
     * 根据店铺id获取数据
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @param shopId    店铺id
     * @return: com.shopx.common.model.result.DubboResult<EsShopDetailDO>
     */
    DubboResult<EsShopDetailDO> getByShopId(Long shopId);
}