package com.xdl.jjg.constant;/**
 * Description: zhuox-shop-trade
 * <p>
 * Created by Administrator on 2019/10/25 13:37
 */

import eu.bitwalker.useragentutils.DeviceType;
import eu.bitwalker.useragentutils.UserAgent;

/**
 * @ClassName AgentTypeUtils
 * @Description:  客户端设备类型
 * @Author Administrator
 * @Date 2019/10/25 
 * @Version V1.0
 **/
public class AgentTypeUtils {
    /**
     * 获取用户代理对象
     * @return
     */
    public static UserAgent getUserAgent(String userAgent){
        return UserAgent.parseUserAgentString(userAgent);
    }

    /**
     * 获取设备类型
     * @return
     */
    public static DeviceType getDeviceType(String userAgent){
        return getUserAgent(userAgent).getOperatingSystem().getDeviceType();
    }

    /**
     * 是否是手机或PC或iPad
     * @return
     */
    public static String isMobileOrTablet(String userAgent){
        DeviceType deviceType = getDeviceType(userAgent);
        String type = "";
        switch (deviceType){
            case COMPUTER:
                type = "PC";
                break;
            case MOBILE:
                type = "WAP";
                break;
            case TABLET:
                type = "iPad";
                break;
        }
        if(userAgent.equals("axios/0.18.0")){
            type = "PC";
        }
        return type;
    }
}
