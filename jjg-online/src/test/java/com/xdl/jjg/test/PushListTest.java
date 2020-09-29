package com.xdl.jjg.test;


import com.gexin.rp.sdk.base.impl.ListMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.NotificationTemplate;
import com.gexin.rp.sdk.template.style.Style0;

import java.util.ArrayList;
import java.util.List;
/**
 * @author sanqi
 * @version 1.0.0
 * @data 2019年09月2019/9/5日
 * @Description TODO
 */
@SuppressWarnings("all")
public class PushListTest {
    // 详见【概述】-【服务端接入步骤】-【STEP1】说明，获得的应用配置
    private static String appId = "67oBjvNdTJ7HCkrcckjFf2";
    private static String appKey = "KzIAfFzBSw8QmdxDA9m7c3";
    private static String masterSecret = "1esy8yaQOa8IkyXkgfpRg3";
    private static String host = "http://sdk.open.api.igexin.com/apiex.htm";

    static String CID1 = "932072d499913a8396279baa66d93e02?2";
    static String CID2 = "344fea5ac1dd4de54bae1ae4cd7bcd50?1";



    public static void main(String[] args) throws Exception {
        // 配置返回每个用户返回用户状态，可选
        System.setProperty("gexin_pushList_needDetails", "true");
        // 配置返回每个别名及其对应cid的用户状态，可选
        // System.setProperty("gexin_pushList_needAliasDetails", "true");
        IGtPush push = new IGtPush(host, appKey, masterSecret);
        // 通知透传模板
        NotificationTemplate template = notificationTemplateDemo();
        ListMessage listMessage = new ListMessage();
        listMessage.setData(template);
        // 设置消息离线，并设置离线时间
        listMessage.setOffline(true);
        // 离线有效时间，单位为毫秒
        listMessage.setOfflineExpireTime(24 * 1000 * 3600);
        // 配置推送目标
        List targets = new ArrayList();
        Target target1 = new Target();
        Target target2 = new Target();
        target1.setAppId(appId);
        target1.setClientId(CID1);
        // target1.setAlias(Alias1);
        target2.setAppId(appId);
        target2.setClientId(CID2);
//         target2.setAlias(Alias2);
        targets.add(target1);
        targets.add(target2);
        // taskId用于在推送时去查找对应的message
//        String taskId = push.getContentId(listMessage);
//        push.pushMessageToList(taskId,targets);
//        System.out.println();
    }



    public static NotificationTemplate notificationTemplateDemo() {
        NotificationTemplate template = new NotificationTemplate();
        // 设置APPID与APPKEY
        template.setAppId(appId);
        template.setAppkey(appKey);
        Style0 style = new Style0();
        // 设置通知栏标题与内容
        style.setTitle("加价购");
        style.setText("您收藏的商品活动已开始");
        // 配置通知栏图标
        style.setLogo("icon.png");
        // 配置通知栏网络图标
//        style.setLogoUrl("http://bucket-cl-jjg20190624.oss-cn-hangzhou.aliyuncs.com/android/2019/09/05/12da20b7e3e4437f3770d17eb661547d.jpg");
        // 设置通知是否响铃，震动，或者可清除
        style.setRing(true);
        style.setVibrate(true);
        style.setClearable(true);
//        style.setChannel("通知渠道id");
//        style.setChannelName("通知渠道名称");
//        style.setChannelLevel(3); //设置通知渠道重要性
        template.setStyle(style);

        // 透传消息接受方式设置，1：立即启动APP，2：客户端收到消息后需要自行处理
        template.setTransmissionType(1);
        template.setTransmissionContent("请输入您要透传的内容");
        return template;
    }
}
