package com.xdl.jjg.util;


import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author sanqi
 * @version 1.0.0
 * @data 2019年07月2019/7/26日
 * @Description MD5加密
 */
public class MD5Utils {

    private static final Logger log = LoggerFactory.getLogger(MD5Utils.class);

    /**
     * MD5算法加密
     *
     * @param str 待加密字符串
     * @return String 加密字符串
     */
    public final static String MD5Encoder(String str) {
        return SHA256OrMD5Encoder(str, "MD5");
    }


    /**
     * SHA256算法加密
     *
     * @param str 待加密字符串
     * @return String 加密字符串
     */
    public final static String SHA256Encoder(String str) {
        return SHA256OrMD5Encoder(str, "SHA-256");
    }


    /**
     * sha1算法加密
     *
     * @param str 待加密字符串
     * @return String 加密字符串
     */
    public static String SHA1Encoder(String str) {
        return SHA256OrMD5Encoder(str, "SHA1");
    }

    /**
     * 加密算法
     *
     * @param str 待加密字符串
     * @return String 加密字符串
     */
    public final static String SHA256OrMD5Encoder(String str, String type) {
        if (StringUtils.isBlank(str) || StringUtils.isBlank(type)) {
            return null;
        }
        try {
            MessageDigest md = MessageDigest.getInstance(type);
            byte[] array = md.digest(str.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder(array.length * 2);
            for (byte item : array) {
                sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            log.error(type + "加密错误：" + e.getMessage());
            return "";
        } catch (UnsupportedEncodingException e) {
            log.error("UnsupportedEncodingException：" + e.getMessage());
            return "";
        }
    }


}
