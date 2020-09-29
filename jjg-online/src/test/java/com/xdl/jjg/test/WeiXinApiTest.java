package com.xdl.jjg.test;

import com.xdl.jjg.OnlineApplication;
import com.xdl.jjg.social.weixin.api.Weixin;
import com.xdl.jjg.social.weixin.api.WeixinImpl;
import com.xdl.jjg.social.weixin.api.WeixinUserInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author qiushi
 * @version 1.0
 * @date 2019/8/19 15:11
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {OnlineApplication.class})
public class WeiXinApiTest {

    @Test
    public void test1(){

        Weixin weixin = new WeixinImpl("24_L_uCo2Fnr1tmhn2xeo0E610377VDHHGiz5RN9zvR_Uvhg0FkyrATiuhSEKN-df2O4weyE-5Qv3fbUeZuBwMrbA");
        WeixinUserInfo userInfo = weixin.getUserInfo("oiiYuxB-4bMTuxR-uYEr2Sc3TLgk");
        System.out.println(userInfo);
    }
}
