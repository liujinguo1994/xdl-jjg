package com.xdl.jjg.message;

import com.jjg.trade.model.dto.EsShipTemplateDTO;
import lombok.Data;

import java.io.Serializable;


/**
 * 运费详情插入
 * @author yuanj
 * @version v2.0
 * @since v7.0.0
 * 2018年11月22日 上午9:52:13
 */
@Data
public class ShipCompanyMsg implements Serializable {

	private static final long serialVersionUID = 8915428082431868648L;

	/**
	 * 运费详情
	 */
	private EsShipTemplateDTO esShipTemplateDTO;

	private Long oldId;
	/**
	 * 0新增，1修改，2删除
	 */
	private Integer state;


}
