package com.xdl.jjg.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@ApiModel
@Data
public class EsCustomForm implements Serializable {

    /**店铺分组id*/
    @ApiModelProperty(name = "id",value = "店铺分组id")
    private Long id;
    /**店铺分组父ID*/
    @ApiModelProperty(name="parent_id",value="店铺分组父ID",required=false)
    private Long parentId;

    @ApiModelProperty(name="name",value="店铺分组名称",required=true)
    @NotEmpty(message = "店铺分组名称必填")
    private String name;

    /**排序*/
    @ApiModelProperty(name="sort",value="排序",required=true)
    @NotNull(message = "排序必填")
    private Integer sort;
    /**分组路径*/
    @ApiModelProperty(name="categoryPath",value="分组路径",required=false)
    private String categoryPath;

    /**子分组*/
    @ApiModelProperty(name="children",value="分组路径",required=false)
    private List<EsCustomForm> children;
}
