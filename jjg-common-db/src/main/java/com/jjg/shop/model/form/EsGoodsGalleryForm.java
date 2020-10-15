package com.jjg.shop.model.form;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * Form
 * </p>
 *
 * @author WNAGAF 826988665@qq.com
 * @since 2019-06-05 17:30:01
 */
@Data
@Api
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsGoodsGalleryForm implements Serializable {

	@ApiModelProperty(value = "相册组编号")
	@NotBlank(message = "相册组编号不能为空")
	private String albumNo;
	@ApiModelProperty(value = "相册信息")
	private List<EsSellerGoodsGalleryForm> galleryList;

}
