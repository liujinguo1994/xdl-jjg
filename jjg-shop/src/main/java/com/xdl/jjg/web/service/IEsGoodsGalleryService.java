package com.xdl.jjg.web.service;


import com.xdl.jjg.model.domain.EsGoodsGalleryDO;
import com.xdl.jjg.model.dto.EsGoodsGalleryDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wangaf 826988665@qq.com
 * @since 2019-06-03
 */
public interface IEsGoodsGalleryService {

    /**
     * 插入数据
     * @auther: wangaf 826988665@qq.com
     * @date: 2019-06-03
     * @param goodsGalleryDTO    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsGoodsGalleryDO>
     */
    DubboResult<EsGoodsGalleryDO> insertGoodsGallery(EsGoodsGalleryDTO goodsGalleryDTO, Long skuId);

    /**
     * 商品相册新增/修改
     * @param galleryDTOS
     * @return
     */
    DubboResult<EsGoodsGalleryDO> insertGoodsGallery(List<EsGoodsGalleryDTO> galleryDTOS, Long skuId);
    /**
     * 根据id获取数据
     * @auther: wangaf 826988665@qq.com
     * @date: 2019/05/31 16:37:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsGoodsGalleryDO>
     */
    DubboResult<EsGoodsGalleryDO> getGoodsGallery(Long id);
    /**
     * 根据id获取数据
     * @auther: wangaf 826988665@qq.com
     * @date: 2019/05/31 16:37:16
     * @param skuId    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsGoodsGalleryDO>
     */
    DubboPageResult<EsGoodsGalleryDO> getGoodsGalleryBySkuId(Long skuId);
    /**
     * 根据查询条件查询列表
     * @auther: wangaf 826988665@qq.com
     * @date: 2019-06-03
     * @param goodsGalleryDTO  DTO
     * @param pageSize      页码
     * @param pageNum       页数
     * @return: com.shopx.common.model.result.DubboPageResult<EsGoodsGalleryDO>
     */
    DubboPageResult<EsGoodsGalleryDO> getGoodsGalleryList(EsGoodsGalleryDTO goodsGalleryDTO, int pageSize, int pageNum);

    /**
     * 根据主键删除数据
     * @auther: wangaf 826988665@qq.com
     * @date: 2019-06-03
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsGoodsGalleryDO>
     */
    DubboResult<EsGoodsGalleryDO> deleteGoodsGallery(Long id);

    /**
     * 商品ID
     * @param ids
     * @return
     */
    DubboResult<EsGoodsGalleryDO> deleteGoodsGallery(long[] ids);
}
