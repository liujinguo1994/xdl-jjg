package com.xdl.jjg.test;

import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.AppMessage;
import com.gexin.rp.sdk.base.impl.ListMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.base.payload.APNPayload;
import com.gexin.rp.sdk.base.uitls.AppConditions;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.NotificationTemplate;
import com.gexin.rp.sdk.template.style.Style0;
import com.xdl.jjg.util.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.assertj.core.util.Lists;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sanqi
 * @version 1.0.0
 * @data 2019年09月2019/9/5日
 * @Description TODO
 */
public class PushtoAPPTest {


    private static String appId = "67oBjvNdTJ7HCkrcckjFf2";
    private static String appKey = "KzIAfFzBSw8QmdxDA9m7c3";
    private static String masterSecret = "1esy8yaQOa8IkyXkgfpRg3";
    private static String host = "http://sdk.open.api.igexin.com/apiex.htm";

    static String CID1 = "932072d499913a8396279baa66d93e02?2";


    public static void main(String[] args) {
        List ids = Lists.newArrayList(CID1, "9bae049dd3ffdbfc838c24f87bed2bab?1");
                pushByCIds(ids);
//        pushAll();
    }


    public static void pushByCIds(List<String> cIds) {
        if (cIds == null || cIds.isEmpty()) {
            return;
        }
        List<Target> targetsIOS = new ArrayList();
        List<Target> targetsANDROID = new ArrayList();
        Target target = null;
        for (String cId : cIds) {
            String[] cids = cId.split("\\?");
            if (cids == null || cids.length < 1) {
                continue;
            }
            target = new Target();
            target.setAppId(appId);
            target.setClientId(cids[0]);
            if (StringUtils.equals("1", cids[1])) {

                targetsIOS.add(target);
            } else {
                targetsANDROID.add(target);
            }
        }
        if (CollectionUtils.isNotEmpty(targetsANDROID)) {
            pushANDROID(targetsANDROID);
        }
        if (CollectionUtils.isNotEmpty(targetsIOS)) {
            pushIOS(targetsIOS);
        }


    }

    public static void pushANDROID(List<Target> targetsANDROID) {
        // 配置返回每个用户返回用户状态，可选
        System.setProperty("gexin_pushList_needDetails", "true");

        IGtPush push = new IGtPush(host, appKey, masterSecret);
        // 通知透传模板
        NotificationTemplate templateANDROID = notificationTemplate(1);
        ListMessage message = new ListMessage();
        message.setData(templateANDROID);
        // 设置消息离线，并设置离线时间
        message.setOffline(true);
        // 离线有效时间，单位为毫秒
        message.setOfflineExpireTime(24 * 1000 * 3600);
        // 配置推送目标

        // taskId用于在推送时去查找对应的message
        String taskId = push.getContentId(message);
        IPushResult ret = push.pushMessageToList(taskId, targetsANDROID);
        System.out.println(ret.getResponse().toString());
    }

    public static void pushIOS(List<Target> targetsIOS) {
        // 配置返回每个用户返回用户状态，可选
        System.setProperty("gexin_pushList_needDetails", "true");

        IGtPush push = new IGtPush(host, appKey, masterSecret);
        // 通知透传模板
        NotificationTemplate templateIOS = notificationTemplate(2);
        ListMessage message = new ListMessage();
        message.setData(templateIOS);
        // 设置消息离线，并设置离线时间
        message.setOffline(true);
        // 离线有效时间，单位为毫秒
        message.setOfflineExpireTime(24 * 1000 * 3600);
        // 配置推送目标

        // taskId用于在推送时去查找对应的message
        String taskId = push.getContentId(message);
        IPushResult ret = push.pushMessageToList(taskId, targetsIOS);
        System.out.println(ret.getResponse().toString());
    }

    public static void pushAll() {
        IGtPush push = new IGtPush(host, appKey, masterSecret);
        NotificationTemplate template = notificationTemplate(1);
        AppMessage message = new AppMessage();
        message.setData(template);

        message.setOffline(true);
        // 离线有效时间，单位为毫秒
        message.setOfflineExpireTime(24 * 1000 * 3600);
        // 推送给App的目标用户需要满足的条件
        AppConditions cdt = new AppConditions();
        List<String> appIdList = new ArrayList();
        appIdList.add(appId);
        message.setAppIdList(appIdList);
        // 手机类型
        List<String> phoneTypeList = new ArrayList();
        phoneTypeList.add("IOS");
        phoneTypeList.add("ANDROID");
        cdt.addCondition(AppConditions.PHONE_TYPE, phoneTypeList, AppConditions.OptType.or);
        message.setConditions(cdt);
        IPushResult ret = push.pushMessageToApp(message, "活动已开始_toApp");
        System.out.println(ret.getResponse().toString());

    }

    public static NotificationTemplate notificationTemplate(Integer transmissionType) {
        NotificationTemplate template = new NotificationTemplate();
        // 设置APPID与APPKEY
        template.setAppId(appId);
        template.setAppkey(appKey);
        Style0 style = new Style0();
        // 设置通知栏标题与内容
        style.setTitle("加价购");
        style.setText("您收藏的商品活动已开始");
        // 配置通知栏图标
        //        style.setLogo("icon.png");
        // 配置通知栏网络图标
        style.setLogoUrl("http://bucket-cl-jjg20190624.oss-cn-hangzhou.aliyuncs.com/icon-jjg/2019/9/18/8a7ccb9d1edd427c89e92d2295873edb.png");
        // 设置通知是否响铃，震动，或者可清除
        style.setRing(true);
        style.setVibrate(true);
        style.setClearable(true);
        template.setStyle(style);
        // 透传消息接受方式设置，1：立即启动APP，2：客户端收到消息后需要自行处理
        template.setTransmissionType(transmissionType);
        template.setTransmissionContent("请输入您***************要透传的内容");
        APNPayload payload = new APNPayload();
        //在已有数字基础上加1显示，设置为-1时，在已有数字上减1显示，设置为数字时，显示指定数字
        payload.setAutoBadge("0");
        payload.setContentAvailable(0);
        //ios 12.0 以上可以使用 Dictionary 类型的 sound
        //payload.setSound("default");
        payload.setCategory("$由客户端定义");
        payload.addCustomMsg("payload", "payload");
        //字典模式使用APNPayload.DictionaryAlertMsg
        payload.setAlertMsg(getDictionaryAlertMsg());
        //设置语音播报类型，int类型，0.不可用 1.播放body 2.播放自定义文本
        // payload.setVoicePlayType(0);
        //设置语音播报内容，String类型，非必须参数，用户自定义播放内容，仅在voicePlayMessage=2时生效
        //注：当"定义类型"=2, "定义内容"为空时则忽略不播放
        // payload.setVoicePlayMessage("定义内容");
        template.setAPNInfo(payload);
        return template;
    }

    private static APNPayload.DictionaryAlertMsg getDictionaryAlertMsg() {
        APNPayload.DictionaryAlertMsg alertMsg = new APNPayload.DictionaryAlertMsg();
        alertMsg.setBody("您收藏的商品活动已开始");
        //        alertMsg.setActionLocKey("ActionLockey");
        //         alertMsg.setLocKey("LocKey");
        //         alertMsg.addLocArg("loc-args");
        //         alertMsg.setLaunchImage("launch-image");
        // iOS8.2以上版本支持
        alertMsg.setTitle("加价购");
        //        alertMsg.setTitleLocKey("TitleLocKey");
        //        alertMsg.addTitleLocArg("TitleLocArg");
        return alertMsg;
    }

}
