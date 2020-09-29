package com.xdl.jjg.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author qiushi
 * @version 1.0
 * @date 2019/9/29 15:49
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SmsCodeTest {

    @Autowired
    private SmsSendUtil sendUtil;

    @Test
    public void test1(){
        String result = sendUtil.sendSmsMw("17665163825", "1234");
        System.out.println(result);

    }

    @Test
    public void test2() throws UnsupportedEncodingException {
        String result = URLEncoder.encode("验证码：6666，打死都不要告诉别人哦！", "GBK").toLowerCase();
        System.out.println(result);

    }

}
