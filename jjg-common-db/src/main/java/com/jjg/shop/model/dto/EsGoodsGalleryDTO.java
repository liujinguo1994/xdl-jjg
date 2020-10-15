package com.jjg.shop.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author WAF 826988665@qq.com
 * @since 2019-05-29
 */
@Data
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsGoodsGalleryDTO implements Serializable  {

    private static final long serialVersionUID = 1L;

	private String albumNo;

	private List<EsSellerGoodsGalleryDTO> galleryList;

}
