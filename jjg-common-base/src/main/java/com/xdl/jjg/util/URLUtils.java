package com.xdl.jjg.util;

import java.nio.charset.Charset;

/**
 * url处理工具类
 */
public class URLUtils extends org.springframework.web.util.UriUtils {

    /**
     * url 编码
     *
     * @param source  url
     * @param charset 字符集
     * @return 编码后的url
     */
    public static String encodeURL(String source, Charset charset) {
        try {
            return URLUtils.encode(source, charset.name());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
