package com.xdl.jjg.constant;/**
 * @author wangaf
 * @date 2020/3/25 15:45
 **/


import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.util.StringUtil;
import com.xdl.jjg.utils.LfcMd5Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 @Author wangaf 826988665@qq.com
 @Date 2020/3/25
 @Version V1.0
 **/
public class OrderCheck {
    /**
     * 校验人寿appid等
     */
    public static void checkKey(String sign,String appSecret,String appKey,String method,String timestamp){

        //验证时间，5分钟以内有效
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            Date date2 = sdf.parse(timestamp);
            if ((date2.getTime() +300000)<System.currentTimeMillis()){
                throw new ArgumentException(TradeErrorCode.ORDER_FOFMAT.getErrorCode(), "Invalid input:timestamp="+timestamp);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            throw new ArgumentException(TradeErrorCode.SYSTEM_ERROR.getErrorCode(), TradeErrorCode.SYSTEM_ERROR.getErrorMsg());
        }


        if (StringUtil.isEmpty(sign)){
            throw new ArgumentException(TradeErrorCode.NO_LOGIN.getErrorCode(), TradeErrorCode.NO_LOGIN.getErrorMsg());

        }
        //appSecret+appKey+method+timestamp+appSecret
        StringBuffer stringBuffer=new StringBuffer();
        if (StringUtil.notEmpty(appSecret)){
            stringBuffer.append(appSecret);
        }
        if (StringUtil.notEmpty(appKey)){
            stringBuffer.append("appKey=").append(appKey);
        }else{
            throw new ArgumentException(TradeErrorCode.NO_LOGIN.getErrorCode(), "No appKey found");
        }
        if (StringUtil.notEmpty(method)){
            stringBuffer.append("method=").append(method);
        }
        if (StringUtil.notEmpty(timestamp)){
            stringBuffer.append("timestamp=").append(timestamp).append(appSecret);
        }
        String s = LfcMd5Utils.parseStrToMd5U32(stringBuffer.toString());
        if (!s.equals(sign)){
            throw new ArgumentException(TradeErrorCode.NO_LOGIN.getErrorCode(), "Invalid signature");
        }

    }
}
