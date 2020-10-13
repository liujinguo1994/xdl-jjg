package com.xdl.jjg.web.service.Impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shopx.common.exception.ArgumentException;
import com.shopx.common.model.result.DubboPageResult;
import com.shopx.common.model.result.DubboResult;
import com.shopx.common.util.BeanUtil;
import com.shopx.common.util.DateUtils;
import com.shopx.common.util.JsonUtil;
import com.shopx.goods.api.constant.GoodsErrorCode;
import com.shopx.goods.api.model.domain.EsGoodsArchDO;
import com.shopx.goods.api.model.domain.EsGoodsSkuDO;
import com.shopx.goods.api.model.domain.EsSpecValuesDO;
import com.shopx.goods.api.model.domain.dto.EsGoodsArchDTO;
import com.shopx.goods.api.model.domain.dto.EsGoodsDTO;
import com.shopx.goods.api.model.domain.dto.EsGoodsSkuQueryDTO;
import com.shopx.goods.api.service.IEsGoodsArchService;
import com.shopx.goods.dao.entity.EsGoods;
import com.shopx.goods.dao.entity.EsGoodsArch;
import com.shopx.goods.dao.mapper.EsGoodsArchMapper;
import com.shopx.goods.dao.mapper.EsGoodsMapper;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.apache.dubbo.common.utils.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author WAF 826988665@qq.com
 * @since 2019-05-29
 */
@Service(version = "${dubbo.application.version}",interfaceClass = IEsGoodsArchService.class,timeout = 50000)
public class EsGoodsArchServiceImpl extends ServiceImpl<EsGoodsArchMapper, EsGoodsArch> implements IEsGoodsArchService {
	private static Logger logger = LoggerFactory.getLogger(EsGoodsArchServiceImpl.class);
	@Autowired
	private EsGoodsArchMapper esGoodsArchMapper;
	@Autowired
	private EsGoodsSkuServiceImpl esGoodsSkuService;
	@Autowired
	private EsGoodsMapper esGoodsMapper;
	@Override
	public DubboResult<EsGoodsArchDO> getGoodsArch(Long id) {
		try {
			EsGoodsArch goods =this.getById(id);
			EsGoodsArchDO goodsDO = new EsGoodsArchDO();
			if(goods == null){
				throw new ArgumentException(GoodsErrorCode.DATA_NOT_EXIST.getErrorCode(), GoodsErrorCode.DATA_NOT_EXIST.getErrorMsg());
			}
			BeanUtil.copyProperties(goods,goodsDO);
			EsGoodsSkuQueryDTO esGoodsSkuQueryDTO = new EsGoodsSkuQueryDTO();
			esGoodsSkuQueryDTO.setGoodsId(id);

			DubboPageResult<EsGoodsSkuDO> skuList = esGoodsSkuService.getAdminGoodsSkuList(esGoodsSkuQueryDTO);
			if(skuList.isSuccess() && CollectionUtils.isNotEmpty(skuList.getData().getList())){
				skuList.getData().getList().forEach(sku->{
					String specText = sku.getSpecs();
					List<EsSpecValuesDO> specList =	JsonUtil.jsonToList(specText,EsSpecValuesDO.class);
					sku.setSpecList(specList);
				});
				goodsDO.setSkuList(skuList.getData().getList());
			}
			return DubboResult.success(goodsDO);
		}catch (ArgumentException ae){
			logger.error("商品档案查询失败",ae);
			return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
		}catch(Throwable th){
			logger.error("商品档案查询失败",th);
			return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(),"系统错误");
		}
	}
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public DubboResult<EsGoodsArchDO> adminInsertGoodsArch(EsGoodsArchDTO goodsArchDTO) {
		try{
			QueryWrapper<EsGoodsArch> queryWrapper = new QueryWrapper<>();
			queryWrapper.lambda().eq(EsGoodsArch::getGoodsSn,goodsArchDTO.getGoodsSn());
			EsGoodsArch goods =this.esGoodsArchMapper.selectOne(queryWrapper);
			if(goods != null){
				throw new ArgumentException(GoodsErrorCode.GOODS_EXIS.getErrorCode(),String.format("商品档案 %s 已存在请重新输入",goodsArchDTO.getGoodsSn()));
			}
			EsGoodsArch goodsArch = new EsGoodsArch();
			BeanUtil.copyProperties(goodsArchDTO,goodsArch);
			goodsArch.setState(0L);
			this.save(goodsArch);
			goodsArchDTO.setId(goodsArch.getId());
			esGoodsSkuService.adminInsertGoodsSku(goodsArchDTO.getSkuList(),goodsArchDTO);
			return  DubboResult.success();
		}catch (ArgumentException ae){
			logger.error("商品档案插入错误",ae);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //手动开启事务回滚
			return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
		}catch (Throwable th){
			logger.error("商品档案插入错误",th);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //手动开启事务回滚
			return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), GoodsErrorCode.SYS_ERROR.getErrorMsg());
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public DubboResult<EsGoodsArchDO> adminUpdateGoodsArch(EsGoodsArchDTO goodsArchDTO, Long id) {
		try{
			EsGoodsArch goodsArch = this.getById(id);
			if(goodsArch == null){
				throw new ArgumentException(GoodsErrorCode.DATA_NOT_EXIST.getErrorCode(),String.format("商品档案不存在:%s ",id));
			}
			BeanUtil.copyProperties(goodsArchDTO,goodsArch);
			goodsArch.setId(id);
			this.updateById(goodsArch);
			goodsArchDTO.setId(id);
			this.esGoodsSkuService.adminUpdateGoodsSku(goodsArchDTO.getSkuList(),goodsArchDTO);
			return DubboResult.success();
		}catch (ArgumentException ae){
			logger.error("商品档案更新错误",ae);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //手动开启事务回滚
			return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
		}catch (Throwable th){
			logger.error("商品档案更新错误",th);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //手动开启事务回滚
			return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), GoodsErrorCode.SYS_ERROR.getErrorMsg());
		}
	}

	/**
	 * 删除商品档案
	 * @param ids 主键ID集合
	 * @return
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public DubboResult<EsGoodsArchDO> deleteGoodsArch(Integer[] ids) {
		try{
			QueryWrapper<EsGoodsArch> queryWrapper = new QueryWrapper<>();
			EsGoodsDTO goodsDTO = new EsGoodsDTO();
			goodsDTO.setGoods_ids(ids);
			List<EsGoods>  goodsList = this.esGoodsMapper.getEsGoodsList(goodsDTO,null);
			goodsList.stream().forEach(esGoods -> {
				if (esGoods.getMarketEnable() == 0) {
					throw new ArgumentException(GoodsErrorCode.DATA_NOT_DELETE.getErrorCode(),String.format("商品正在上架 请先下架再删除 %s",esGoods.getId()));
				}
			});
			queryWrapper.lambda().in(EsGoodsArch::getId,ids);
			this.esGoodsArchMapper.delete(queryWrapper);
			this.esGoodsMapper.deleteEsGoods(ids,null);
			this.esGoodsSkuService.deleteGoodsSku(ids);
			return  DubboResult.success();
		}catch (ArgumentException ae){
			logger.error("商品档案删除失败",ae);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //手动开启事务回滚
			return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
		}catch (Throwable th){
			logger.error("商品档案删除失败",th);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //手动开启事务回滚
			return DubboPageResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(),"系统错误");
		}
	}

	/**
	 * 禁用/启用 商品档案信息
	 * @param ids 主键ID集合
	 * @param state 档案状态 0 启用 1禁用
	 * @return
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public DubboResult<EsGoodsArchDO> updateGoodsArch(Integer[] ids,Long state) {
		try{
			QueryWrapper<EsGoodsArch> queryWrapper = new QueryWrapper<>();
			queryWrapper.lambda().in(EsGoodsArch::getId,ids);
			List<EsGoodsArch> goodsArchList = this.list(queryWrapper);
			if(CollectionUtils.isEmpty(goodsArchList) || goodsArchList.size() != ids.length){
				throw new ArgumentException(GoodsErrorCode.GOODS_EXIS.getErrorCode(),String.format(" 无权限操作 %s",ids));
			}
			goodsArchList.stream().forEach(esGoodsArch -> {
				esGoodsArch.setState(state);
			});
			this.updateBatchById(goodsArchList);
			return  DubboResult.success();
		}catch (ArgumentException ae){
			logger.error("商品档案禁用/启用失败",ae);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //手动开启事务回滚
			return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
		}catch (Throwable th){
			logger.error("商品档案禁用/启用失败",th);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //手动开启事务回滚
			return DubboPageResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(),"系统错误");
		}
	}
	@Override
	public DubboPageResult<EsGoodsArchDO> getGoodsArchList(EsGoodsArchDTO goodsArchDTO,int pageSize,int pageNum) {
		QueryWrapper<EsGoodsArch> queryWrapper = new QueryWrapper<>();
		try{
			queryWrapper.orderByDesc("id");
			Page<EsGoodsArch> page = new Page<>(pageNum,pageSize);
			if(!StringUtils.isBlank(goodsArchDTO.getKeyword())){
				queryWrapper.lambda().like(EsGoodsArch::getSupplierName,goodsArchDTO.getKeyword()).or().
						eq(EsGoodsArch::getChargePerson,goodsArchDTO.getKeyword()).or()
						.eq(EsGoodsArch::getPurchaseManager,goodsArchDTO.getKeyword()).or().like(EsGoodsArch::getGoodsName,goodsArchDTO.getKeyword());
			}
			if(goodsArchDTO.getCreateTimeStart() !=null){
				queryWrapper.lambda().gt(EsGoodsArch::getCreateTime,goodsArchDTO.getCreateTimeStart());
			}
			if(goodsArchDTO.getCreateTimeEnd() !=null){
				String endTimeStr = DateUtils.format(new Date(goodsArchDTO.getCreateTimeEnd()),DateUtils.TIMESTAMP_PATTERN);
				endTimeStr = endTimeStr.replaceAll("00:00:00","23:59:59");
				queryWrapper.lambda().lt(EsGoodsArch::getCreateTime,DateUtils.parseDate(endTimeStr).getTime());
			}
			//只获取有效的商品档案
			IPage<EsGoodsArch> goodsList =  this.page(page,queryWrapper);
			List<EsGoodsArchDO> goodsDoList = new ArrayList<>();
			if(CollectionUtils.isNotEmpty(goodsList.getRecords())){
				goodsDoList = goodsList.getRecords().stream().map(esGoodsArch -> {
					EsGoodsArchDO goodsDo = new EsGoodsArchDO();
					BeanUtil.copyProperties(esGoodsArch,goodsDo);
					return goodsDo;
				}).collect(Collectors.toList());
			}
			return DubboPageResult.success(goodsList.getTotal(),goodsDoList);
		}catch (ArgumentException ae){
			logger.error("商品档案分页查询失败",ae);
			return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
		}catch (Throwable th){
			logger.error("商品档案分页查询失败",th);
			return DubboPageResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(),"系统错误");
		}
	}

	@Override
	public DubboPageResult getGoodsArchList(Page page,String keyContent,Long supplierId) {
		try{
			IPage<EsGoodsArchDO> pageList =	this.esGoodsArchMapper.getEsGoodsArchList(page,keyContent,supplierId);
			if(pageList.getTotal() <= 0){
				return DubboPageResult.success(pageList.getTotal(),new ArrayList<>());
			}
			pageList.getRecords().forEach(arch->{
				EsGoodsSkuQueryDTO query = new EsGoodsSkuQueryDTO();
				query.setGoodsId(arch.getId());
				DubboPageResult<EsGoodsSkuDO> result = esGoodsSkuService.getSellerGoodsSkuList(query);
				if(result.isSuccess()){
					arch.setSkuList(result.getData().getList());
				}
			});
			return DubboPageResult.success(pageList.getTotal(),pageList.getRecords());
		}catch (ArgumentException ae){
			logger.error("商品档案分页查询失败",ae);
			return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
		}catch (Throwable th){
			logger.error("商品档案分页查询失败",th);
			return DubboPageResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(),"系统错误");
		}
	}
	@Override
	public DubboPageResult getEsGoodsArchGiftsList(Page page,String keyContent) {
		try{
			IPage<EsGoodsArchDO> pageList =	this.esGoodsArchMapper.getEsGoodsArchGiftsList(page,keyContent);
			if(pageList.getTotal() <= 0){
				return DubboPageResult.success(pageList.getTotal(),new ArrayList<>());
			}
			pageList.getRecords().forEach(arch->{
				EsGoodsSkuQueryDTO query = new EsGoodsSkuQueryDTO();
				query.setGoodsId(arch.getId());
				DubboPageResult<EsGoodsSkuDO> result = esGoodsSkuService.getGoodsSkuListGifts(query);
				if(result.isSuccess()){
					arch.setSkuList(result.getData().getList());
				}
			});
			return DubboPageResult.success(pageList.getTotal(),pageList.getRecords());
		}catch (ArgumentException ae){
			logger.error("商品档案分页查询失败",ae);
			return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
		}catch (Throwable th){
			logger.error("商品档案分页查询失败",th);
			return DubboPageResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(),"系统错误");
		}
	}

	@Override
	public DubboResult<EsGoodsArchDO> getGoodsArchGifts(Long id) {
		try{
			QueryWrapper<EsGoodsArch> queryWrapper = new QueryWrapper<>();
			queryWrapper.lambda().eq(EsGoodsArch::getIsGifts,1).eq(EsGoodsArch::getId,id).eq(EsGoodsArch::getState,0);
			EsGoodsArch goodsArch = this.getOne(queryWrapper);
			if(goodsArch == null){
				throw new ArgumentException(GoodsErrorCode.DATA_NOT_EXIST.getErrorCode(),String.format("赠品档案信息不存在",id));
			}
			EsGoodsArchDO goodsArchDO = new EsGoodsArchDO();
			BeanUtil.copyProperties(goodsArch,goodsArchDO);
			return DubboResult.success(goodsArchDO);
		}catch (ArgumentException ae){
			logger.error("赠品档案信息查询失败",ae);
			return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
		}catch (Throwable th){
			logger.error("赠品档案信息查询失败",th);
			return DubboPageResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(),"系统错误");
		}

	}
	@Override
	public DubboResult<EsGoodsArchDO> getGoodsArchSn(String goodsSn) {
		try{
			QueryWrapper<EsGoodsArch> queryWrapper = new QueryWrapper<>();
			queryWrapper.lambda().eq(EsGoodsArch::getIsGifts,2).eq(EsGoodsArch::getGoodsSn,goodsSn).eq(EsGoodsArch::getState,0);
			EsGoodsArch goodsArch = this.getOne(queryWrapper);
			if(goodsArch == null){
				throw new ArgumentException(GoodsErrorCode.DATA_NOT_EXIST.getErrorCode(),String.format("档案信息不存在",goodsSn));
			}
			EsGoodsArchDO goodsArchDO = new EsGoodsArchDO();
			BeanUtil.copyProperties(goodsArch,goodsArchDO);
			return DubboResult.success(goodsArchDO);
		}catch (ArgumentException ae){
			logger.error("档案信息查询失败",ae);
			return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
		}catch (Throwable th){
			logger.error("档案信息查询失败",th);
			return DubboPageResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(),"系统错误");
		}

	}
}
