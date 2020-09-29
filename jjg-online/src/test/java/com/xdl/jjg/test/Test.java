package com.xdl.jjg.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

public class Test {


    private static Logger log = LoggerFactory.getLogger(Test.class);

    public static void main(String[] args) {

        LocalDateTime now = LocalDateTime.now().withSecond(0).withNano(0);
        System.out.println(now);



    }


}
