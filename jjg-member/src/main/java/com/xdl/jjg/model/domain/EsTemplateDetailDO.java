package com.xdl.jjg.model.domain;


import com.xdl.jjg.model.dto.EsCategoryTemDTO;
import com.xdl.jjg.model.dto.EsGoodsTemDTO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 店铺模板详情--es_template_detail
 * </p>
 *
 * @author lins 595831329@qq.com
 * @since 2019-06-27
 */
@Data
public class EsTemplateDetailDO implements Serializable {


    /**
     * 主键ID
     */
	private Long id;
    /**
     * 模板类型
     */
	private Integer type;
    /**
     * 模板名称
     */
	private String name;
    /**
     * 位置
     */
	private Integer location;
    /**
     * 修改时间
     */
	private Long updateTime;
    /**
     * 模板规格
     */
	private Integer tempSize;
    /**
     * 应用商品
     */
    private List<EsGoodsTemDTO> hotSellGoods;
    /**
     * 应用分类
     */
    private List<EsCategoryTemDTO> categoryForTems;
    /**
     * 客户端类型
     */
	private Integer clientType;



}
