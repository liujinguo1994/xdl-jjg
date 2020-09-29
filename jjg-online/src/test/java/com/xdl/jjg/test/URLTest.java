package com.xdl.jjg.test;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

/**
 * @author sanqi
 * @version 1.0.0
 * @data 2019年09月2019/9/6日
 * @Description TODO
 */
public class URLTest {

    public static void main(String[] args) {
        String url = "http%3A%2F%2Fjjgtesthttp.xindongle.com%2Fgoods%2Fdetail%3Fid%3D152";

        try {
            String result = java.net.URLDecoder.decode(url, StandardCharsets.UTF_8.name());
            System.out.println(result);
        } catch (UnsupportedEncodingException e) {
            // not going to happen - value came from JDK's own StandardCharsets}

        }


    }
}