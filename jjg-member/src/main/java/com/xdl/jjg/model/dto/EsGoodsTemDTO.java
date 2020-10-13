package com.xdl.jjg.model.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 商品信息
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2019-06-27
 */
@Data
@ToString
public class EsGoodsTemDTO implements Serializable {

	/**分类id*/
	private String operationType;
	/**操作内容*/
	private String operationParam;
	/**附加标题*/
	private String picTitle;
	/**附加描叙*/
	private String picDesc;
	/**图片路径*/
	private String picUrl;

}
