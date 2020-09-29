package com.xdl.jjg.web.service;

import com.xdl.jjg.OnlineApplication;
import com.xdl.jjg.model.mq.AccountRegisteredBeanMQ;
import com.xdl.jjg.mq.MQProducerConfig;
import com.xdl.jjg.support.MqProperties;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;

/**
 * @author qiushi
 * @version 1.0
 * @date 2019/8/15 11:30
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {OnlineApplication.class})
@ActiveProfiles("test")
public class SysUserServiceTest {

    @Autowired
    private SysUserService userService;
    @Autowired
    private MqService mqService;
    @Autowired
    private MqProperties mqProperties;

    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;
    @Autowired
    private RedisService redisService;
    /**
     * 在每次构建测试用例之前执行 构建mvc环境
     */
    @Before
    public void setUp(){
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void updateRelationship() {

        userService.updateRelationship(new AccountRegisteredBeanMQ(1, "","18106560002", LocalDateTime.now()));

    }

    @Test
    public void test1(){
        userService.phoneRegistered("123456", "18667010656", (short) 0, null);

    }

    @Test
    public void test3(){
        AccountRegisteredBeanMQ bean = new AccountRegisteredBeanMQ(57,"", "admin", LocalDateTime.now());
        userService.updateRelationship(bean);
    }

    @Test
    public void test2(){
        AccountRegisteredBeanMQ bean = new AccountRegisteredBeanMQ(108,"", "18667010656", LocalDateTime.now());

        mqService.sendProducerMq(MQProducerConfig.getInstance().getProducerAccount(), mqProperties.getAccountTopic(), mqProperties.getAccountRegisteredTag(), bean);
    }

    @Test
    public void test4(){
        redisService.increment("test11");
        long i = redisService.getObject("test11");
        System.out.println("-----------------" + i);
    }
}