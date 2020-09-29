package com.xdl.jjg.test;

import com.xdl.jjg.OnlineApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * @author qiushi
 * @version 1.0
 * @date 2019/7/24 14:51
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {OnlineApplication.class})
public class CategoryTest {

    @Autowired
    private ItemService itemCategoryService;

    @Test
    public void test1(){
        List<Integer> ids = itemCategoryService.getParentIds(13);
        System.out.println("**********"+ids);
        List<Integer> ids1 = itemCategoryService.getChildIds(13);
        System.out.println("**********"+ids1);
    }


}
