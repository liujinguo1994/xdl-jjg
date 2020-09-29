package com.xdl.jjg.mapper;

import com.xdl.jjg.mapper.user.SysUserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * @author qiushi
 * @version 1.0
 * @date 2019/7/11 17:29
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {JjgServiceBootstrap.class})
@WebAppConfiguration
public class MapperTestForUser {

    @Autowired
    private SysUserMapper sysUserMapper;


    @Test
    public void test1(){
        sysUserMapper.selectByPrimaryKey(1);
    }


}
