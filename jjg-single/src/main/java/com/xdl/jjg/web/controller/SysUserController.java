package com.xdl.jjg.web.controller;

import com.xdl.jjg.util.RestResponse;
import com.xdl.jjg.web.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author sanqi
 * @version 1.0.0
 * @data 2019年10月2019/10/8日
 * @Description TODO
 */
@RestController
@RequestMapping("/user")
public class SysUserController {

    @Autowired
    private SysUserService userService;


    @Scope("prototype")
    @GetMapping("/add")
    public Object saveUser(){

         userService.saveUser();
        return RestResponse.getInstance();
    }

}
