package com.xdl.jjg.test;

import java.time.LocalDateTime;

/**
 * @author sanqi
 * @version 1.0.0
 * @data 2019年09月2019/9/16日
 * @Description TODO
 */
public class LocalDateTest {

    public static void main(String[] args) {

        LocalDateTime now = LocalDateTime.now().withMinute(0).withNano(0);
        System.out.println(now);

    }
}
