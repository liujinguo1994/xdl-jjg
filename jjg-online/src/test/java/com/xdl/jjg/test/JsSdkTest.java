package com.xdl.jjg.test;

import com.xdl.jjg.util.MD5Utils;
import com.xdl.jjg.web.service.RedisService;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author sanqi
 * @version 1.0.0
 * @data 2019年09月2019/9/6日
 * @Description TODO
 */

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("dev")
@EnableDiscoveryClient(autoRegister = false)
public class JsSdkTest {


    @Autowired
    private RedisService redisService;
    @Autowired
    private RestTemplate restTemplate;
    @Value("${cy.security.social.weixin.app-id}")
    private String appId;
    @Value("${cy.security.social.weixin.app-secret}")
    private String secret;


    public static void main(String[] args) throws Exception {
        System.out.println(new JsSdkTest().getJsSdkSign("https://www.xindongle.com/goods/detail"));
    }


    public Map<String, Object> getJsSdkSign(String url) throws Exception {
//        String noncestr = UUID.randomUUID().toString().replaceAll("-", "");
//        String ticket = geTicket();
//        String ticket = "kgt8ON7yVITDhtdwci0qeW3lrnD-Y-teGVLzc_Pmqh1GT6qPjzT0j2faO614MSecEmiBH5qkW4NPK7uE3UCDow";
        Map<String, Object> map = new HashMap();
        String noncestr = "ce922d97572441b4a1ff7d6f9ee";
        String ticket = "kgt8ON7yVITDhtdwci0qeWwPxU8lqFPQa42sW_pPYhWlv8fJ7wvwTpgDLLqJrH5kFrjyrtxCMtw-Bi0vcWct9Q";
        Long timestamp = 1567764282L;
        SortedMap<String, String> sortedMap = new TreeMap();
        sortedMap.put("noncestr", noncestr);
        sortedMap.put("jsapi_ticket", ticket);
        sortedMap.put("timestamp", timestamp.toString());
        sortedMap.put("url", url);
        StringBuilder sb = new StringBuilder();
        sb.append("jsapi_ticket=" + ticket + "&noncestr=" + noncestr + "&timestamp=" + timestamp + "&url=" + url);
        System.out.println(sb.toString());
        String signature = MD5Utils.SHA1Encoder(sb.toString());

        System.out.println("signature*********" + signature);
        map.put("noncestr", noncestr);
        map.put("timestamp", timestamp);
        map.put("appId", appId);
        map.put("signature", signature);

        return map;
    }

    @Test
    public void test() {

        try {
            System.out.println(getJsSdkSign("https://www.xindongle.com/goods/detail"));
        } catch (Exception e) {

        }


    }


    public String geTicket() {
        String ticket = redisService.getObject("JsSdkTicket");
        if (StringUtils.isNotBlank(ticket)) {
            return ticket;
        }
        String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?type=jsapi&access_token=" + getToken();
        Map map = restTemplate.getForObject(url, Map.class);
        System.out.println(map);
        ticket = map.get("ticket") + "";
        redisService.saveObject("JsSdkTicket", ticket, 7000L);
        return ticket;
    }

    public String getToken() {
        String token = redisService.getObject("JsSdkToken");
        if (StringUtils.isNotBlank(token)) {
            return token;
        }
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + appId;
        url = url + "&secret=" + secret;
        Map map = restTemplate.getForObject(url, Map.class);
        System.out.println("********************" + map);
        token = map.get("access_token") + "";
        redisService.saveObject("JsSdkToken", token, 7000L);
        return token;
    }

}


