package com.xdl.jjg.web.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * @author qiushi
 * @version 1.0
 * @date 2019/7/27 15:25
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {JjgServiceBootstrap.class})
@WebAppConfiguration
public class SysUserServiceImplTest {

    @Autowired
    private SysUserService sysUserService;

    @Test
    public void enable() {
        //sysUserService.enable(1, false, 15);
    }
}