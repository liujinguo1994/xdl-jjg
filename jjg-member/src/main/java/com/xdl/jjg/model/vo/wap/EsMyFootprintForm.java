package com.xdl.jjg.model.vo.wap;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 会员足迹表
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2020-01-09 17:14:45
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsMyFootprintForm implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 商品ID
     */
	private Long goodsId;

    /**
     * 访问时间
     */
	private Long createTime;

    /**
     * 店铺ID
     */
	private Long shopId;

    /**
     * 商品价格
     */
	private Double money;

    /**
     * 商品图片
     */
	private String img;

    /**
     * 会员ID
     */
	private Long memberId;


}
