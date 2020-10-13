package com.xdl.jjg.plugin.sms;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.xdl.jjg.model.dto.EsSmsSendDTO;
import com.xdl.jjg.model.vo.EsConfigItemVO;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;

/**
 * @Description: 阿里云短信接口实现
 */
@Component
public class SmsAliPlugin implements SmsPlatformManage {

    private static Logger logger = LoggerFactory.getLogger(SmsAliPlugin.class);

    /**
     * 产品名称:云通信短信API产品,开发者无需替换
     */
    private static final String PRODUCT = "Dysmsapi";

    /**
     * 产品域名,开发者无需替换
     */
    private static final String DOMAIN = "dysmsapi.aliyuncs.com";

    @Override
    public List<EsConfigItemVO> definitionConfigItem() {
        List<EsConfigItemVO> list = new ArrayList();

        EsConfigItemVO accessKeyId = new EsConfigItemVO();
        accessKeyId.setType("text");
        accessKeyId.setName("accessKeyId");
        accessKeyId.setText("ACCESS_KEY_ID");

        EsConfigItemVO accessKeySecret = new EsConfigItemVO();
        accessKeySecret.setType("text");
        accessKeySecret.setName("accessKeySecret");
        accessKeySecret.setText("ACCESS_KEY_SECRET");

        EsConfigItemVO password = new EsConfigItemVO();
        password.setType("text");
        password.setName("signName");
        password.setText("SIGN_NAME");

        list.add(accessKeyId);
        list.add(accessKeySecret);
        list.add(password);

        return list;
    }

    @Override
    public boolean onSend(EsSmsSendDTO smsSendDTO, Map param) {
        boolean flag = false;
        //可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", (String) param.get("accessKeyId"), (String) param.get("accessKeySecret"));
        try {
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", PRODUCT, DOMAIN);

            IAcsClient acsClient = new DefaultAcsClient(profile);

            //组装请求对象-具体描述见控制台-文档部分内容
            SendSmsRequest request = new SendSmsRequest();

            //必填:待发送手机号
            request.setPhoneNumbers(smsSendDTO.getMobile());

            //必填:短信签名-可在短信控制台中找到
            request.setSignName((String) param.get("signName"));

            //必填:短信模板-可在短信控制台中找到
            request.setTemplateCode(smsSendDTO.getTemplateId());

            // code:验证码; sn:订单号; deliveryCorp:物流公司; content:物流单号; name:用户名;goodsName:商品名称;
            // 每个模板所需参数不同，将所有参数全部传到Json中，避免根据模板ID进行判断
            request.setTemplateParam("{\"name\":\"" + smsSendDTO.getUserName() + "\",\"deliveryCorp\":\"" + smsSendDTO.getLogisticsCompany()
                    + "\",\"code\":\"" + smsSendDTO.getCode() + "\",\"sn\":\"" + smsSendDTO.getOrderSn()
                    + "\", \"content\":\"" + smsSendDTO.getShipSn() + "\",\"password\":\"" + smsSendDTO.getPassword() + "\"," +
                    " \"goodsProductName\":\"" + smsSendDTO.getGoodsName() + "\"}");

            SendSmsResponse smsResponse = acsClient.getAcsResponse(request);
            logger.info("短信平台响应结果:" + JSONObject.fromObject(smsResponse).toString());
            if (smsResponse.getCode() != null && smsResponse.getCode().equals("OK")) {
                flag = true;
            } else {
                flag = false;
            }
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return flag;
    }

    @Override
    public String getPluginId() {
        return "smsAliPlugin";
    }

    @Override
    public String getPluginName() {
        return "阿里云短信";
    }

    @Override
    public Integer getIsOpen() {
        return 0;
    }
}
