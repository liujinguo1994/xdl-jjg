package com.xdl.jjg.controller;

import com.xdl.jjg.data.dto.CategoryDTO;
import com.xdl.jjg.data.dto.CategoryInsertDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * @author qiushi
 * @version 1.0
 * @date 2019/7/24 14:51
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {JjgServiceBootstrap.class})
@WebAppConfiguration
public class CategoryControllerTest {

    @Autowired
    private CategoryController categoryController;

    @Test
    public void test1(){
        Object o = categoryController.query(1, 10, 0);
        System.out.println(o);
    }

    @Test
    public void test2(){
        CategoryInsertDTO categoryInsertDTO = new CategoryInsertDTO().setCname("测试分类1").setDisplay(true).setSort(11).setPid(0);
        Object o = categoryController.add(categoryInsertDTO);
        System.out.println(o);
    }

    @Test
    public void test3(){
        Object o = categoryController.del(16);
        System.out.println(o);
    }

    @Test
    public void test4(){
        CategoryDTO categoryDTO = new CategoryDTO().setId(17).setCname("测试分类1修改").setDesc("xxxxxxxxxxxxxxx").setDisplay(false).setPid(11).setSort(12);
        Object o = categoryController.update(categoryDTO);
        System.out.println(o);

    }

    @Test
    public void test5(){
        Object o = categoryController.findOne(17);
        System.out.println(o);
    }
}
