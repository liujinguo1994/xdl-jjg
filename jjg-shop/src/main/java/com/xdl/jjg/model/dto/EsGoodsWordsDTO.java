package com.jjg.member.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 自定义分词
 * </p>
 *
 * @author WANGAF 826988665@qq.com
 * @since 2019-07-01 13:54:20
 */
@Data
public class EsGoodsWordsDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 分词名称
     */
	private String words;

    /**
     * 商品数量
     */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long goodsNum;

    /**
     * 全拼
     */
	private String quanpin;

    /**
     * 首字母
     */
	private String szm;

}
