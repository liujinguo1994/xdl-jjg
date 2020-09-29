package com.xdl.jjg.test;

import java.util.UUID;

/**
 * @author sanqi
 * @version 1.0.0
 * @data 2019年08月2019/8/21日
 * @Description TODO
 */
public class WechatTest {

    public static void main(String[] args) throws Exception{
//        String  reqInfo="EK5qtPacjYl3BSanJ1rC0mQtiVTd0Mf0MZ+0VgrKn8xcquCyl8y8wzHfpU3TbGiXFQ0zdAsuKpHtkpOLochhFH6mjFb6FsySnTQ9yRW7qkGe9X8jEr7CjL3wR5BX+Fdndrn1rYxFFd05BnUFSzk49HKafYvupyGgroBkNjLi8+jDS8zQEj4mLWTsCjX8ULUUfiPQx2VzoE2pZzUMBoPLAXUD36g6aGepUyXFHW8HFyG/NjjIm9hI4KP138XFqAXIkferkAbk5WVW42IyzZRV8UVtb/XAGWJMa2cFEUpiTlJJ7I7+aOv0vZx83YuP6ld4Zc8qHQdU3NZ6Ug3zkavA6Mh74KV4LtbzMKSUfWaVnKvYHY0dk6zNXF4RWRmRWkmDYWFe8rrh8ibuXHMauvX0CZO49aL7uVVsLIxqWAVoxy0wtAij7XoAm+j9lmEjE2j7OiVr3LYDid5A9JGC1hM1uJcBxr4S9QaGxRuI9SritasICQsvrPLuur/xgX6uVgSiiJeawpbUonzPsHP4J6ozEZXuiQDysRDFzp9iY/QoUzce0OJ80zWDafxJIdlo5lDhayoIjCEfXqdW5jhOnaYfNo1SkN371fMVORBOFxWOYsIcXaE7cbB3eeZAo0s4AiV7DN9csqxfenoigkMutEz/kV+CO6WZCIDDsYkJoaF427u3YP2EWNqxDTXYgKYO2VAhVr0cyUmUWtoIgNxbMDleAR08LRDFP47MpzHzCtyql2Q1cnoLu0KdTdXJxbTi0y1prtJnJC4jLpcpVlAb/14h+Fx3++P2HiDrAkc8qA7B8DkyudYst2klT66BoPeONKlciDRhgp30BA3LiCUuz7rYXsUYmxot9Mm1wWpwB+LygurM7IoKFoTjGxBK8eHZUEEX27CD/kHxS4mx09UAxjDRB6uu1vnzlkSYP9EE0YE5nn+GlNKH8y4AVZFqi3BIAgKOGToIAQRZOgUc5p6hMsPPnKV/DFXmEGRlwhx8bzfpGuerimASEeWrPvb5s17+foTRepl9S++bCH7Rc2LXNi3M9Tq04GeVV+qf1nNVCQ8hDpQ=";
//        Base64.Decoder decoder = Base64.getDecoder();
//        byte[] base64ByteArr = decoder.decode(reqInfo);
//        String key = "ebf814d105cc4ab288d330ead183c42d";
//        String lowerKey =  MD5Utils.MD5Encoder(key).toLowerCase();
//
//        SecretKeySpec secretKeySpec = new SecretKeySpec(lowerKey.getBytes(), "AES");
//        Security.addProvider(new BouncyCastleProvider());
//        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding");
//        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
//
//        String result = new String(cipher.doFinal(base64ByteArr));
//        System.out.println("解密结果:{}" + result);
//        Map<String,String> map = XMLUtils.doXMLParse(result);
//        System.out.println(map);


        aa();
    }



    public static void aa()throws Exception{

        BaseWxPayServiceImpl a = new BaseWxPayServiceImpl();

        ReFundRequestParam reFundParam = new ReFundRequestParam();
        reFundParam.setOutTradeNo("Cl386665408679391233");
        reFundParam.setKey("ebf814d105cc4ab288d330ead183c42d");
        reFundParam.setAppId("wx85d281aeacf93048");
        reFundParam.setMchId("1545828161");
        reFundParam.setKeyPath("/config/apiclient_cert.p12");
        reFundParam.setRefundURL("https://api.mch.weixin.qq.com/secapi/pay/refund");
        reFundParam.setNotifyUrl("https://jjg.xindongle.com/online/pay/weiXinRefundNotify");
        reFundParam.setTotalFee("110");
        reFundParam.setRefundFee("110");
        reFundParam.setNonceStr(UUID.randomUUID().toString().replaceAll("-",""));
        reFundParam.setOutRefundNo(UUID.randomUUID().toString().replaceAll("-",""));
        Object aaa = a.refund(reFundParam);
        System.out.println(aaa);

    }
}
