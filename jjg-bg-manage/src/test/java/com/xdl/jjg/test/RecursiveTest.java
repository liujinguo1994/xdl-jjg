package com.xdl.jjg.test;

import com.xdl.jjg.mapper.user.SysUserRelationshipMapper;
import com.xdl.jjg.pojo.user.SysUserRelationship;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author qiushi
 * @version 1.0
 * @date 2019/8/21 9:29
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {JjgServiceBootstrap.class})
@WebAppConfiguration
public class RecursiveTest {

    @Autowired
    private SysUserRelationshipMapper relationshipMapper;

    List<SysUserRelationship> relationships = new ArrayList<>();

    @Test
    public void test1(){
        List<Integer> canshu = new ArrayList<>();
        canshu.add(57);
        selectChild(canshu);
        for (SysUserRelationship s : relationships) {
            System.out.println(s);

        }
    }

    public void selectChild(List<Integer> ids){

    }

}
