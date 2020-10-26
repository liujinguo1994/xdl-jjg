package com.xdl.jjg.web.service;

import com.jjg.trade.model.domain.EsLfcAreaDO;
import com.xdl.jjg.response.service.DubboResult;

/**
 * 订单号业务层
 * 
 * @author fk
 * @version v2.0
 * @since v7.0.0 2018-03-21 11:23:10
 */
public interface IEsLfcAreaService {

	/**
	 * 根据商品id查询
	 * @return
	 */
	DubboResult<EsLfcAreaDO> getByAreaId(String areaId);

}