package com.jjg.member.model.vo;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * <p>
 * 信任登录参数VO
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2019-07-18
 */
@Data
@ToString
public class ConnectSettingParametersVO {

    private String name;
    private List<ConnectSettingConfigItem> configList;


}
