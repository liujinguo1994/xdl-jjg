package com.xdl.jjg.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.Charsets;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.FileInputStream;
import java.nio.charset.Charset;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author sanqi
 * @version 1.0.0
 * @data 2019年07月2019/7/26日
 * @Description 网络请求通用类
 */
public class HttpUtils {
    private static Logger log = LoggerFactory.getLogger(HttpUtils.class);

    /**
     * HTTPCLIENT连接池配置
     */
    private static HttpClient HTTPCLIENT = null;
    private static SSLContext sslcontext = null;
    /**
     * in milliseconds
     */
    private final static Integer CONNECT_TIMEOUT = 5000;


    public static HttpUtils.Builder builder() {
        return new HttpUtils.Builder();
    }

    public static class Builder {

        private Map<String, Object> params = new HashMap<>();

        private Map<String, String> heads = new HashMap<>();

        private String url;

        private String contentType;

        private String charset;

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder contentType(String contentType) {
            this.contentType = contentType;
            return this;
        }

        public Builder charset(String charset) {
            this.charset = charset;
            return this;
        }

        public Builder addParam(String key, Object value) {
            params.put(key, value);
            return this;
        }

        public Builder addHeads(String key, String value) {
            heads.put(key, value);
            return this;
        }

        public String get() {
            return getRequset(url, heads, params);
        }


        public String postJson() {
            return postJson(JSONObject.toJSONString(params));
        }

        public String postJson(String json) {
            return postBodyJson(url, heads, json, contentType == null ? "application/json; charset=UTF-8" : contentType);
        }

        public String postForm() {
            return postBodyForm(url, heads, params, charset == null ? "UTF-8" : charset);
        }
    }


    /**
     * @param url 请求地址
     * @return String
     * @Title: get
     * @Description:get方法，当只需要请求地址就能取到想要得数据时使用此方法
     */
    private static String getRequset(String url, Map<String, String> headers, Map<String, Object> params) {
        HTTPCLIENT = getHttpClient();
        if (!params.isEmpty()) {
            StringBuffer paramString = new StringBuffer();
            paramString.append("?");
            params.forEach((k, v) -> paramString.append(k + "=" + v + "&"));
            url = url + StringUtils.removeEnd(paramString.toString(), "&");
        }
        System.out.println("********url******" + url);
        HttpGet get = new HttpGet(url);
        if (!headers.isEmpty()) {
            headers.forEach((k, v) -> get.addHeader(k, v));
        }
        String result = null;
        HttpEntity resEntity = null;
        try {
            HttpResponse response = HTTPCLIENT.execute(get);
            if (response != null) {
                resEntity = response.getEntity();
                if (resEntity != null) {
                    result = EntityUtils.toString(resEntity, "UTF-8");
                }
            }
        } catch (Exception e) {
        } finally {
            System.out.println("-----------------------------");
            get.releaseConnection();
            get.abort();
            EntityUtils.consumeQuietly(resEntity);
        }
        return result;
    }


    /**
     * @param url     请求地址
     * @param json    请求json内容 body体是json内容
     * @param @return
     * @return JSONObject
     * @Title: postJson
     * @Description:post方法，不指定参数名
     */
    private static String postBodyJson(String url, Map<String, String> headers, String json, String contentType) {

        HTTPCLIENT = getHttpClient();
        HttpPost post = new HttpPost(url);
        HttpEntity resEntity = null;
        StringEntity entity = null;
        entity = new StringEntity(json, "UTF-8");
        entity.setContentEncoding("UTF-8");
        entity.setContentType(contentType);
        post.setEntity(entity);
        if (!headers.isEmpty()) {
            for (Map.Entry<String, String> header : headers.entrySet()) {
                post.addHeader(header.getKey(), header.getValue());
            }
        }
        try {
            HttpResponse response = HTTPCLIENT.execute(post);
            resEntity = response.getEntity();
            String res = EntityUtils.toString(resEntity, "UTF-8");
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            post.releaseConnection();
            EntityUtils.consumeQuietly(entity);
            EntityUtils.consumeQuietly(resEntity);
        }
    }


    /**
     * @param url     请求地址
     * @param params  请求参数,body体是表单
     * @param headers 请求头信息
     * @return String
     * @Title: post
     * @Description:post请求
     */
    private static String postBodyForm(String url, Map<String, String> headers, Map<String, Object> params, String charset) {
        HTTPCLIENT = getHttpClient();
        String result = null;
        HttpPost httpPost = null;
        HttpEntity reqEntity = null;
        HttpEntity responseEntity = null;
        try {
            httpPost = new HttpPost(url);
            List<NameValuePair> formparams = new ArrayList<NameValuePair>();
            if (params != null && !params.isEmpty()) {
                for (Map.Entry<String, Object> param : params.entrySet()) {
                    formparams.add(new BasicNameValuePair(param.getKey(), param.getValue().toString()));
                }
            }
            if (headers != null && !headers.isEmpty()) {
                for (Map.Entry<String, String> header : headers.entrySet()) {
                    httpPost.addHeader(header.getKey(), header.getValue());
                }
            }
            reqEntity = new UrlEncodedFormEntity(formparams, charset == null ? "UTF-8" : charset);
            httpPost.setEntity(reqEntity);
            HttpResponse response = HTTPCLIENT.execute(httpPost);
            responseEntity = response.getEntity();
            result = EntityUtils.toString(responseEntity);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpPost.releaseConnection();
            EntityUtils.consumeQuietly(reqEntity);
            EntityUtils.consumeQuietly(responseEntity);
        }

        return result;
    }


    /**
     * @param @return
     * @return HttpClient
     * @Title: getHttpClient
     * @Description:HTTPCLIENT实例化，自动
     */
    private static HttpClient getHttpClient() {
        //http连接初始化
        RegistryBuilder<ConnectionSocketFactory> registryBuilder = RegistryBuilder.create();
        ConnectionSocketFactory httpFactory = new PlainConnectionSocketFactory();
        //https连接初始化
        if (sslcontext == null) {
            initSSLContext();
        }
        SSLConnectionSocketFactory httpsFactory = new SSLConnectionSocketFactory(sslcontext,
                new String[]{"TLSv1"}, null, new DefaultHostnameVerifier());

        registryBuilder.register("http", httpFactory);
        registryBuilder.register("https", httpsFactory);

        // 指定信任密钥存储对象和连接套接字工厂
        Registry<ConnectionSocketFactory> registry = registryBuilder.build();
        // 设置连接管理器
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(registry);
        // 设置编码格式
        connManager.setDefaultConnectionConfig(ConnectionConfig.custom().setCharset(Charsets.toCharset(Charset.forName("UTF-8"))).build());
        // 设置超时
        connManager.setDefaultSocketConfig(SocketConfig.custom().setSoTimeout(CONNECT_TIMEOUT).build());
        // 设置最大路由
        connManager.setDefaultMaxPerRoute(20);
        // 设置最大链接数
        connManager.setMaxTotal(20);

        // 构建客户端
        HTTPCLIENT = HttpClientBuilder.create().setConnectionManager(connManager).build();
        return HTTPCLIENT;
    }


    /**
     * 初始化ssl.
     *
     * @return the ssl context
     */
    private static void initSSLContext() {
        initSSLContext(null, null);
    }

    /**
     * 初始化ssl.
     *
     * @param keyPath 绝对路径
     * @param params  参数
     * @return the ssl context
     */
    public static void initSSLContext(String keyPath, List<String> params) {
        if (keyPath == null || params == null) {
            sslcontext = SSLContexts.createDefault();
            return;
        }
        try {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            //P12文件目录 证书路径
            FileInputStream in = new FileInputStream(keyPath);
            SSLContextBuilder sslContextBuilder = SSLContexts.custom();
            for (String param : params) {
                keyStore.load(in, param.toCharArray());
                sslContextBuilder.loadKeyMaterial(keyStore, param.toCharArray());
            }
            in.close();
            sslcontext = sslContextBuilder.build();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }


}
