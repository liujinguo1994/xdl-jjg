package com.xdl.jjg.web.controller.api;

import com.shopx.common.model.result.DubboResult;
import com.shopx.common.util.JsonUtil;
import com.shopx.common.util.StringUtil;
import com.shopx.goods.api.model.domain.EsGoodsGalleryDO;
import com.shopx.goods.api.model.domain.cache.EsGoodsCO;
import com.shopx.goods.api.model.domain.cache.EsGoodsSkuCO;
import com.shopx.goods.api.model.domain.vo.EsSpecValuesVO;
import com.shopx.goods.api.service.IEsGoodsService;
import com.shopx.goods.api.service.IEsLfcGoodsService;
import com.shopx.system.api.model.domain.vo.ExpressDetailVO;
import com.shopx.system.api.service.IEsExpressPlatformService;
import com.shopx.trade.api.constant.TradeErrorCode;
import com.shopx.trade.api.model.domain.EsLfcAreaDO;
import com.shopx.trade.api.model.domain.EsOrderDO;
import com.shopx.trade.api.model.domain.EsOrderItemsDO;
import com.shopx.trade.api.model.domain.dto.EsLfcOrderDTO;
import com.shopx.trade.api.model.domain.vo.*;
import com.shopx.trade.api.service.IEsLfcAreaService;
import com.shopx.trade.api.service.IEsOrderItemsService;
import com.shopx.trade.api.service.IEsOrderService;
import com.shopx.trade.api.service.IEsTradeIntodbService;
import com.shopx.trade.web.constant.OrderCheck;
import com.shopx.trade.web.request.LfcResultForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 商品控制器
 *
 * @author yuanj
 * @version v2.0
 * @since v7.0.0 2019-09-19 11:23:10
 */
@RestController
@RequestMapping("/goods")
@Api(description = "人寿商品相关API")
public class GoodsLfcBuyerController {

	private final String appSecret="ftgoSwWtTsEExyELzbGKCmSFM9TKGhw5";

	@Reference(version = "${dubbo.application.version}",timeout = 5000)
	private IEsLfcAreaService esLfcAreaService;
	@Reference(version = "${dubbo.application.version}", timeout = 5000)
	private IEsLfcGoodsService lfcGoodsService;

	@Reference(version = "${dubbo.application.version}", timeout = 5000)
	private IEsGoodsService goodsService;

	@Reference(version = "${dubbo.application.version}", timeout = 5000)
	private IEsOrderService orderService;

	@Reference(version = "${dubbo.application.version}", timeout = 5000)
	private IEsOrderItemsService orderItemsService;

	@Reference(version = "${dubbo.application.version}", timeout = 5000)
	private IEsExpressPlatformService expressPlatformService;

	@Reference(version = "${dubbo.application.version}",timeout = 5000)
	private IEsTradeIntodbService esTradeIntodbService;

	@ApiOperation(value = "获取商品id集合")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "sign", value = "签名", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "app_key", value = "app_key", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "method", value = "方法名", dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "timestamp", value = "時間", dataType = "int", paramType = "query"),
	})
	@PostMapping("/getGoodsIds")
	public LfcResult getLfcGoodsId(String sign,String appKey,String method,String timestamp) {
		OrderCheck.checkKey(sign,appSecret,appKey,method,timestamp);
		LfcResult lfcResult=new LfcResult();
		DubboResult<Long> result = this.lfcGoodsService.getGoodsList();
		if (result.isSuccess()){
			lfcResult.setCode("200");
			lfcResult.setResult(result.getData());
		}
		return lfcResult;

	}

    @ApiOperation(value = "浏览商品的详情,静态部分使用")
    @PostMapping("/getGoodsInfo")
    public LfcResult getLfcGoodsDetail(String sign, String appKey, String method, String timestamp, String goodsIds) {


		OrderCheck.checkKey(sign,appSecret,appKey,method,timestamp);
		LfcResult lfcResult=new LfcResult();

		String[] goodsIdss = goodsIds.split(",");

		List<Long> listGoods=new ArrayList<>();
		for (String goodsId:goodsIdss
			 ) {
			listGoods.add(Long.parseLong(goodsId));
		}

		List<LfcGoodsVO> lfcGoodsVOList=new ArrayList<>();
    	for (Long goodsId:listGoods){
    		//商品信息
			DubboResult result = this.goodsService.getEsGoods(goodsId);
			List<EsGoodsGalleryDO> listGall = new ArrayList<>();
			if (result.isSuccess()){
				EsGoodsCO goods=(EsGoodsCO) result.getData();
				LfcGoodsVO lfcGoodsVO=new LfcGoodsVO(goods);
				//sku信息
				List<EsGoodsSkuCO> skuList = goods.getSkuList();
				List<LfcSkuVO> lfcSkuVOS=new ArrayList<>();
				for (EsGoodsSkuCO sku:skuList) {
					List<EsGoodsGalleryDO> galleryList = sku.getGalleryList();
					galleryList.stream().forEach(gallery->
							listGall.add(gallery));
					LfcSkuVO lfcSkuVO=new LfcSkuVO(sku);
					List<EsSpecValuesVO> specList = null;
					if (sku.getSpecs() !=null){
						specList= JsonUtil.jsonToList(sku.getSpecs(), EsSpecValuesVO.class);
					}

					String desplayString = null;
					int j=0;
					if (StringUtil.isNotEmpty(specList)) {
						List<ItemSkuSpecValueListVO> listSpe = new ArrayList<>();//规格集合
						for (EsSpecValuesVO spec : specList) {
							SkuSpecValueVO skuSpecValueVO = new SkuSpecValueVO();
							SkuSpecVO skuSpecVO = new SkuSpecVO();//名称
							skuSpecVO.setName(spec.getSpecName());//值
							ItemSkuSpecValueListVO itemSkuSpecValueListVO = new ItemSkuSpecValueListVO();
							itemSkuSpecValueListVO.setSkuSpec(skuSpecVO);
							skuSpecValueVO.setValue(spec.getSpecValue());
							itemSkuSpecValueListVO.setSkuSpecValue(skuSpecValueVO);
							if (specList.size()==1){
								desplayString=spec.getSpecValue();
							}else{
								if (j==0){
									desplayString=spec.getSpecValue();
								}else{
									desplayString=desplayString+" + "+ spec.getSpecValue();
								}
							}
							j++;
							listSpe.add(itemSkuSpecValueListVO);
						}
						lfcSkuVO.setItemSkuSpecValueList(listSpe);
						lfcSkuVO.setDisplayString(desplayString);
						lfcSkuVOS.add(lfcSkuVO);

					}else{
						List<ItemSkuSpecValueListVO> listSpe = new ArrayList<>();//规格集合
						SkuSpecValueVO skuSpecValueVO = new SkuSpecValueVO();
						SkuSpecVO skuSpecVO = new SkuSpecVO();//名称
						skuSpecVO.setName("名称");//值
						ItemSkuSpecValueListVO itemSkuSpecValueListVO = new ItemSkuSpecValueListVO();
						itemSkuSpecValueListVO.setSkuSpec(skuSpecVO);
						skuSpecValueVO.setValue(goods.getGoodsName());
						itemSkuSpecValueListVO.setSkuSpecValue(skuSpecValueVO);
						listSpe.add(itemSkuSpecValueListVO);
						lfcSkuVO.setItemSkuSpecValueList(listSpe);
						lfcSkuVO.setDisplayString(goods.getGoodsName());
						lfcSkuVOS.add(lfcSkuVO);
					}
				}
				lfcGoodsVO.setSkuList(lfcSkuVOS);

				//相册

				LfcItemDetailVO itemDetailVO=new LfcItemDetailVO();
				itemDetailVO.setDetailHtml(goods.getIntro());
				if (StringUtil.isNotEmpty(listGall)){
					if (listGall.size()==1){
						itemDetailVO.setPicUrl1(listGall.get(0).getImage());
					}else if (listGall.size()==2){
						itemDetailVO.setPicUrl1(listGall.get(0).getImage());
						itemDetailVO.setPicUrl2(listGall.get(1).getImage());
					}else if (listGall.size()==3){
						itemDetailVO.setPicUrl1(listGall.get(0).getImage());
						itemDetailVO.setPicUrl2(listGall.get(1).getImage());
						itemDetailVO.setPicUrl3(listGall.get(2).getImage());
					}else {
						itemDetailVO.setPicUrl1(listGall.get(0).getImage());
						itemDetailVO.setPicUrl2(listGall.get(1).getImage());
						itemDetailVO.setPicUrl3(listGall.get(2).getImage());
						itemDetailVO.setPicUrl4(listGall.get(3).getImage());
					}
				}
				lfcGoodsVO.setItemDetail(itemDetailVO);
				lfcGoodsVOList.add(lfcGoodsVO);
			}
		}

    	lfcResult.setCode("200");
    	lfcResult.setResult(lfcGoodsVOList);

		return lfcResult;
	}



	@ApiOperation(value = "根据订单号查询物流轨迹")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "sign", value = "签名", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "appKey", value = "加密参数", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "method", value = "方法名称", dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "timestamp", value = "时间", dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "orderId", value = "订单号", dataType = "int", paramType = "query"),
	})
	@PostMapping("/getOrderExpress")
	public LfcResult getOrderExpress(String sign,String appKey,String method,String timestamp,String orderId) {
		OrderCheck.checkKey(sign,appSecret,appKey,method,timestamp);
		//根据订单号查询查询详情里的快递公司id和单号

		LfcResult lfcResult=new LfcResult();
		DubboResult<EsOrderDO> result = orderService.queryLfcOrder(orderId);
		if(result.isSuccess()){
			DubboResult<EsOrderItemsDO> queryLfcItem = orderItemsService.queryLfcItem(result.getData().getOrderSn());
			if (queryLfcItem.isSuccess()){
				EsOrderItemsDO orderItemsDO = queryLfcItem.getData();
				if (orderItemsDO.getLogiId() !=null && StringUtil.notEmpty(orderItemsDO.getShipNo())){
					DubboResult<ExpressDetailVO> expressResult = this.expressPlatformService.getExpressFormDetail(orderItemsDO.getLogiId(), orderItemsDO.getShipNo());
					if (expressResult.isSuccess() && expressResult.getData().getName() !=null){
						ExpressDetailVO expressDetailVO = expressResult.getData();
						LfcExpressVO lfcExpressVO=new LfcExpressVO();
						List<Map> content = expressDetailVO.getData();

						List<LfcContentDetailVO> contentDetailVOS=new ArrayList<>();
						for (Map map:content) {
							LfcContentDetailVO lfcContentDetailVO=new LfcContentDetailVO();
							lfcContentDetailVO.setTime(map.get("time").toString());
							lfcContentDetailVO.setDesc(map.get("context").toString());
							contentDetailVOS.add(lfcContentDetailVO);
						}
						lfcExpressVO.setCompany(expressDetailVO.getName());
						lfcExpressVO.setNumber(expressDetailVO.getCourierNum());
						lfcExpressVO.setContent(contentDetailVOS);
						lfcResult.setResult(lfcExpressVO);
					}
				}
			}

		}else{
			LfcNoExpressVO lfcNoExpressVO=new LfcNoExpressVO();
			lfcResult.setResult(lfcNoExpressVO);
	    }
		lfcResult.setCode("200");
		return lfcResult;

	}
	@ApiOperation(value = "人寿插入订单")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "sign", value = "签名", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "appKey", value = "加密参数", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "method", value = "方法名称", dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "timestamp", value = "时间", dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "order", value = "订单号", dataType = "String", paramType = "query"),
	})
	@PostMapping("/pushOrder")
	public LfcResultForm pushOrder(String sign, String appKey, String method, String timestamp,
								 String order) {
		//权限校验
		OrderCheck.checkKey(sign,appSecret,appKey,method,timestamp);
		LfcResultForm lfcResult=new LfcResultForm();
		EsLfcOrderDTO lfcOrder = JsonUtil.jsonToObject(order, EsLfcOrderDTO.class);
		//订单号校验
		if(lfcOrder!=null){
			String orderId = lfcOrder.getOrderId().substring(0, 6);
			DubboResult<EsLfcAreaDO> result = esLfcAreaService.getByAreaId(orderId);
			if (!result.isSuccess()){
				lfcResult.setCode(TradeErrorCode.ORDER_FOFMAT.getErrorCode()+"");
				lfcResult.setResult(TradeErrorCode.ORDER_FOFMAT.getErrorMsg());
				return lfcResult;
			}
		}
		esTradeIntodbService.intoLfcOrderDB(lfcOrder);
		lfcResult.setCode("200");
		lfcResult.setResult("下单成功");
		return lfcResult;

	}

}
