package com.xdl.jjg.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: AreaVO
 * @Description: 区域VO
 * @Author: libw  981087977@qq.com
 * @Date: 7/16/2019 20:01
 * @Version: 1.0
 */
@Data
@Api
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsCheckPasswordVO implements Serializable {

    @ApiModelProperty(value = "uuid")
    private String uuid;

    @ApiModelProperty(value = "手机号")
    private String mobile;
}
