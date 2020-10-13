package com.xdl.jjg.web.service;

import com.xdl.jjg.model.co.EsGoodsSkuCO;
import com.xdl.jjg.model.domain.EsGoodsSkuDO;
import com.xdl.jjg.model.domain.EsSellerGoodsSkuDO;
import com.xdl.jjg.model.dto.*;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

import java.util.List;

/**
 * <p>
 *  商品SKU 服务类
 * </p>
 *
 * @author wangaf 826988665@qq.com
 * @since 2019-06-03
 */
public interface IEsGoodsSkuService {

    /**
     * 管理后台 新增SKU信息数据
     * @auther: wangaf 826988665@qq.com
     * @date: 2019/05/31 16:39:30
     * @param goodsSkuDTO    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsGoodsSkuDO>
     */
    DubboResult<EsGoodsSkuDO> adminInsertGoodsSku(List<EsGoodsSkuDTO> goodsSkuDTO, EsGoodsArchDTO esGoodsArchDTO);
    /**
     * 管理后台 更新SKU信息数据
     * @auther: wangaf 826988665@qq.com
     * @date: 2019/05/31 16:40:10
     * @param goodsSkuDTO    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsGoodsSkuDO>
     */
    DubboResult<EsGoodsSkuDO> adminUpdateGoodsSku(List<EsGoodsSkuDTO> goodsSkuDTO, EsGoodsArchDTO esGoodsArchDTO);
    /**
     * 卖家中心 更新商品SKU信息
     * @auther: wangaf 826988665@qq.com
     * @date: 2019/05/31 16:40:10
     * @param goodsSkuDTO    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsGoodsSkuDO>
     */
    DubboResult<EsGoodsSkuDO> sellerUpdateGoodsSku(List<EsGoodsSkuDTO> goodsSkuDTO, EsGoodsDTO goodsDTO);

    /**
     * 卖家中心 更新商品SKU信息
     * @auther: wangaf 826988665@qq.com
     * @date: 2019/05/31 16:40:10
     * @param goodsSkuDTO    DTO
     * @return: com.shopx.common.model.result.DubboResult<EsGoodsSkuDO>
     */
    DubboResult<EsGoodsSkuDO> sellerUpdateGoodsSkuGift(List<EsGoodsSkuDTO> goodsSkuDTO, Long shopId, Long goodsId);

    /**
     * 根据id获取数据
     * @auther: wangaf 826988665@qq.com
     * @date: 2019/05/31 16:37:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsGoodsSkuDO>
     */
    DubboResult<EsGoodsSkuCO> getGoodsSku(Long id);
    /**
     * 根据id获取数据
     * @auther: wangaf 826988665@qq.com
     * @date: 2019/05/31 16:37:16
     * @param id    主键id
     * @return: com.shopx.common.model.result.DubboResult<EsGoodsSkuDO>
     */
    DubboResult<EsGoodsSkuCO> getGoodsSkuEnable(Long id);
    /**
     * 根据查询条件分页查询列表
     * @auther: wangaf 826988665@qq.com
     * @date: 2019/06/03 13:42:53
     * @param goodsSkuDTO  DTO
     * @param pageSize      页码
     * @param pageNum       页数
     * @return: com.shopx.common.model.result.DubboPageResult<EsGoodsSkuDO>
     */
    DubboPageResult<EsGoodsSkuDO> getGoodsSkuList(EsGoodsSkuQueryDTO goodsSkuDTO, int pageSize, int pageNum);

    /**
     * 根据商品Id删除数据 批量删除
     * @auther: wangaf 826988665@qq.com
     * @date: 2019/05/31 16:40:44
     * @param ids    主键id数组
     * @return: com.shopx.common.model.result.DubboResult<EsGoodsSkuDO>
     */
    DubboResult<EsGoodsSkuDO> deleteGoodsSku(Integer[] ids);

    /**
     * 根据DTO 获取商品SKU信息集合
     * @param goodsSkuDTO
     * @return
     */
    DubboPageResult<EsGoodsSkuDO> getGoodsSkuList(EsGoodsSkuQueryDTO goodsSkuDTO);
    DubboPageResult<EsGoodsSkuDO> getAdminGoodsSkuList(EsGoodsSkuQueryDTO goodsSkuDTO);
    DubboPageResult<EsGoodsSkuDO> getGoodsSkuListGifts(EsGoodsSkuQueryDTO goodsSkuDTO);

    /**
     * 根据DTO 获取商品SKU信息集合
     * @param goodsId
     * @return
     */
    DubboPageResult<EsSellerGoodsSkuDO> getGoodsSkuList(Long goodsId);
    /**
     * 根据DTO 获取商品SKU信息集合
     * @param goodsSkuDTO
     * @return
     */
    DubboPageResult<EsGoodsSkuDO> getSellerGoodsSkuList(EsGoodsSkuQueryDTO goodsSkuDTO);

    /**
     * 卖家中心 修改商品SKU 库存
     * @param skuQuantityDTO
     * @return
     */
    DubboResult<EsGoodsSkuDO> sellerUpdateQuantity(EsGoodsSkuQuantityDTO skuQuantityDTO, Long goodsId);

    /**
     * 买家端 根据商品SKU编号获取SKU信息，先判断商品是否存在
     * @param skuId
     * @param goodsId
     * @return
     */
    DubboResult<EsGoodsSkuDO> buyGetGoodsSku(Long skuId, Long goodsId);

    DubboResult<EsGoodsSkuCO> getSkuById(Long id);

    DubboPageResult<EsGoodsSkuDO> getSkuByIds(Long[] id);

    /**
     * 卖家中心库存预警数据获取
     * @param goodsSkuDTO
     * @param pageSize
     * @param pageNum
     * @return
     */
    DubboPageResult<EsGoodsSkuDO> sellerGetGoodsSkuList(EsGoodsSkuQueryDTO goodsSkuDTO, int pageSize, int pageNum);

    /**
     * 卖家中心商品SKU预警值设置
     * @param warningValue 预警值
     * @param skuId SKU主键ID
     * @return
     */
    DubboResult<EsGoodsSkuDO> sellerUpdateGoodsSkuWarning(Integer warningValue, Long skuId);
}
