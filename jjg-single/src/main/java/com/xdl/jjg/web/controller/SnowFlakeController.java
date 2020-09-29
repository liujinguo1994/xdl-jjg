package com.xdl.jjg.web.controller;


import com.xdl.jjg.util.ResponseUtil;
import com.xdl.jjg.util.SnowFlakeProperties;
import com.xdl.jjg.web.service.impl.SnowFlakeImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author sanqi
 * @version 1.0.0
 * @data 2019年07月27日
 * @Description 获取唯一编号
 */
@RestController
@RequestMapping("/snow/flake")
public class SnowFlakeController {


    @Autowired
    private SnowFlakeProperties snowFlakeProperties;



    @GetMapping("/getNextId")
    public Object getNextId() {
        SnowFlakeImpl snowFlake = new SnowFlakeImpl(snowFlakeProperties.getDatacenterId(), snowFlakeProperties.getMachineId());
        return ResponseUtil.ok( snowFlake.nextId());
    }





}