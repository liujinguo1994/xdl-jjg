package com.jjg.system.model.domain;/**
 * @author wangaf
 * @date 2020/3/10 18:37
 **/

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Author wangaf 826988665@qq.com
 * @Date 2020/3/10
 * @Version V1.0
 **/
@Data
public class EsSiteSettingDO implements Serializable {
    /**
     * 站点名称
     */
    @ApiModelProperty(name = "site_name", value = "站点名称")
    private String siteName;
    /**
     * 网站标题
     */
    @ApiModelProperty(name = "title", value = "网站标题")
    private String title;
    /**
     * 网站关键字
     */
    @ApiModelProperty(name = "keywords", value = "网站关键字")
    private String keywords;
    /**
     * 网站描述
     */
    @ApiModelProperty(name = "descript", value = "网站描述")
    private String descript;
    /**
     * 网站是否开启，0开启，-1关闭
     */
    @ApiModelProperty(name = "siteon", value = "网站是否开启，1开启，0关闭")
    @Min(message = "必须为数字且,1为开启,0为关闭", value = 0)
    @Max(message = "必须为数字且,1为开启,0为关闭", value = 1)
    @NotNull(message = "网站是否关闭不能为空")
    private Integer siteon;
    /**
     * 关闭原因
     */
    @ApiModelProperty(name = "close_reson", value = "关闭原因")
    private String closeReson;
    /**
     * 网站logo
     */
    @ApiModelProperty(name = "logo", value = "网站logo")
    private String logo;

    /**
     * 加密秘钥
     */
    @ApiModelProperty(name = "global_auth_key", value = "加密秘钥")
    private String globalAuthKey;
    /**
     * 默认图片
     */
    @ApiModelProperty(name = "default_img", value = "默认图片")
    private String defaultImg;
    /**
     * 测试模式
     */
    @ApiModelProperty(name = "test_mode", value = "测试模式,1为开启,0为关闭")
    @Min(message = "必须为数字且,1为开启,0为关闭", value = 0)
    @Max(message = "必须为数字且,1为开启,0为关闭", value = 1)
    @NotNull(message = "是否为测试模式不能为空")
    public Integer testMode;

}
