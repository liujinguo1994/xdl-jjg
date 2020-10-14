package com.jjg.member.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 店铺菜单管理
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
@Data
@ApiModel
public class EsShopMenuAdminListVO implements Serializable {

    /**
     * 菜单列表
     */
    @ApiModelProperty(required = false,value = "菜单列表")
    private List<EsShopMenuListVO> menuListVOList;

}
