package com.xdl.jjg.model.domain;

import com.xdl.jjg.model.vo.ConnectSettingParametersVO;
import com.xdl.jjg.util.JsonUtil;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 第三方登录参数
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
@Data
public class EsConnectSettingDO implements Serializable {


    /**
     * Id
     */
	private Long id;
    /**
     * 参数配置名称
     */
	private String type;
    /**
     * 信任登录类型
     */
	private String config;
    /**
     * 信任登录配置参数
     */
	private String name;


    public List<ConnectSettingParametersVO> getClientList(){
        return JsonUtil.jsonToList(this.config,ConnectSettingParametersVO.class);
    };


}
