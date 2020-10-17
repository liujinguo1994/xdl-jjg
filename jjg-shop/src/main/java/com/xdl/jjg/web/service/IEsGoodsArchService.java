package com.xdl.jjg.web.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jjg.shop.model.domain.EsGoodsArchDO;
import com.jjg.shop.model.dto.EsGoodsArchDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author WAF 826988665@qq.com
 * @since 2019-05-29
 */
public interface IEsGoodsArchService  {

	/**
	 * 根据商品id 查询
	 * @param id
	 * @return
	 */
	DubboResult<EsGoodsArchDO> getGoodsArch(Long id);

	/**
	 * 商品档案入库
	 * @param goodsArchDTO 查询条件实体
	 * @return
	 */
	DubboResult<EsGoodsArchDO> adminInsertGoodsArch(EsGoodsArchDTO goodsArchDTO);
	/**
	 * 商品档案修改
	 * @param goodsArchDTO 查询条件实体
	 * @return
	 */
	DubboResult<EsGoodsArchDO> adminUpdateGoodsArch(EsGoodsArchDTO goodsArchDTO, Long id);

	/**
	 * 商品档案逻辑删除
	 * @param ids 主键ID集合
	 * @return
	 */
	DubboResult<EsGoodsArchDO> deleteGoodsArch(Integer[] ids);
	/**
	 * 商品档案状态修改（禁用/启用）
	 * @param ids 主键ID集合
	 * @param state 档案状态 0 启用 1禁用
	 * @return
	 */
	DubboResult<EsGoodsArchDO> updateGoodsArch(Integer[] ids, Long state);

	/**
	 *
	 * @param goodsArchDTO 查询条件实体
	 * @param pageSize 页数
	 * @param pageNum 页码
	 * @return
	 */
	DubboPageResult<EsGoodsArchDO> getGoodsArchList(EsGoodsArchDTO goodsArchDTO, int pageSize, int pageNum);

	/**
	 * 根据输入的值获取商品档案信息(关联商品主表)
	 * @param keyContent
	 * @return
	 */
	DubboPageResult<EsGoodsArchDO> getGoodsArchList(Page page, String keyContent, Long supplierId);

	/**
	 * 根据输入的值获取商品档案信息(关联商品主表)
	 * @param keyContent
	 * @return
	 */
	DubboPageResult<EsGoodsArchDO> getEsGoodsArchGiftsList(Page page, String keyContent);

	/**
	 * 根据商品id查询赠品信息
	 * @param id
	 * @return
	 */
	DubboResult<EsGoodsArchDO> getGoodsArchGifts(Long id);

	/**
	 * 根据商品goodsSn查询 档案信息
	 * @param goodsSn
	 * @return
	 */
	DubboResult<EsGoodsArchDO> getGoodsArchSn(String goodsSn);
}
