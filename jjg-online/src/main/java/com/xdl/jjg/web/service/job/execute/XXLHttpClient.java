package com.xdl.jjg.web.service.job.execute;/**
 * Description: zhuox-shop-trade
 * <p>
 * Created by Administrator on 2020/5/26 16:13
 */

/**
 * @ClassName HttpClient
 * @Description: TODO
 * @Author Administrator
 * @Date 2020/5/26 
 * @Version V1.0
 **/


import com.xdl.jjg.message.CartPromotionChangeMsg;
import com.xdl.jjg.util.JsonUtil;
import com.xdl.jjg.web.service.job.ResponseEntityMessage;
import net.sf.json.JSONObject;
import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Description TODO
 * @Author jiuwei
 * @Date Created in 下午8:13 2020/2/15
 * @Version 1.0
 */
public class XXLHttpClient {

    private static String cookie="";

    @Value("${xxl.job.admin.addresses}")
    private static String addresses;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    /**
     * 新增
     * @return
     * @throws HttpException
     * @throws IOException
     */
    public static String addJob(String addresses,
                                Long activityId,
                                String promotionType,
                                Long endTime,
                                String promotionName,
                                String handlerName) throws HttpException, IOException {

        // 执行参数
        CartPromotionChangeMsg cartPromotionChangeMsg = new CartPromotionChangeMsg();
        cartPromotionChangeMsg.setActivityId(activityId);
        cartPromotionChangeMsg.setPromotionType(promotionType);

        // cron表达式
        SimpleDateFormat sdf = new SimpleDateFormat("ss mm HH dd MM ?");
        Date date = new Date(endTime);
        String format = sdf.format(date);
        String cron = URLEncoder.encode(format, "UTF-8");
        cartPromotionChangeMsg.setCron(cron);
        String s = JsonUtil.objectToJson(cartPromotionChangeMsg);
        // 执行器名称
        String cartPromotionChangeJobHandler = handlerName;
        String executorParam = URLEncoder.encode(s,"UTF-8");
        // 第三方任务名称
        String encode = URLEncoder.encode(promotionName, "UTF-8");
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        // 为了避免秒杀审核添加秒杀活动定时任务 过多 先查询xxl-job系统中是否存在同一触发时间同一活动的任务执行器 如果存在则不再保存
        String param = "jobGroup=4&triggerStatus=1&executorParam="+executorParam+"&start=0&length=10";
        HttpPost httpPost1 = new HttpPost(addresses + "/jobinfo/pageList" + "?" + param);
        httpPost1.setHeader("Content-Type", "application/json;charset=utf8");


        String requestInfo = "jobGroup=4&jobDesc=" + encode + "&executorRouteStrategy=FIRST&cronGen_display=&jobCron=" + cron + "&glueType=BEAN&executorHandler="+ cartPromotionChangeJobHandler +"&executorBlockStrategy=SERIAL_EXECUTION&childJobId=&executorTimeout=0&executorFailRetryCount=0&author=ljg&alarmEmail=&executorParam=" + executorParam + "&glueRemark=GLUE%E4%BB%A3%E7%A0%81%E5%88%9D%E5%A7%8B%E5%8C%96&glueSource=&triggerStatus=1";
        HttpPost httpPost = new HttpPost(addresses+ "/jobinfo/add" +"?"+ requestInfo);
        httpPost.setHeader("Content-Type", "application/json;charset=utf8");
        // 验证是否存在相同 执行参数的 响应模型
        CloseableHttpResponse checkResponse = null;
        HttpEntity checkResponseEntity = null;
        // 保存成功的 响应模型
        CloseableHttpResponse response = null;
        HttpEntity responseEntity = null;
        try {

            checkResponse = httpClient.execute(httpPost1);
            checkResponseEntity = checkResponse.getEntity();

            String jobList = EntityUtils.toString(checkResponseEntity);
            ResponseEntityMessage jobListMessage = JsonUtil.jsonToObject(jobList, ResponseEntityMessage.class);
            // 如果不存在则保存新的任务执行器
            if (jobListMessage.getRecordsTotal() <= 0){
                // 由客户端执行(发送)Post请求
                response = httpClient.execute(httpPost);
                // 从响应模型中获取响应实体
                responseEntity = response.getEntity();
                if (responseEntity != null) {
                    System.out.println("响应内容长度为:" + responseEntity.getContentLength());
                }
                String s1 = EntityUtils.toString(responseEntity);
                System.out.println("不存在相同类型且执行时间相同的执行器，保存该执行器[{}]"+s1);
                return s1;
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                // 释放资源
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

//    public static void main(String[] args) throws UnsupportedEncodingException {
//
//        CartPromotionChangeMsg cartPromotionChangeMsg = new CartPromotionChangeMsg();
//        cartPromotionChangeMsg.setActivityId(12l);
//        cartPromotionChangeMsg.setPromotionType(PromotionTypeEnum.SECKILL.name());
//        String s = JsonUtil.objectToJson(cartPromotionChangeMsg);
//
//        SimpleDateFormat sdf = new SimpleDateFormat("ss mm HH dd MM ?");
//        Date date = new Date(1590801496000l);
//        String format = sdf.format(date);
//        String cron = URLEncoder.encode(format, "UTF-8");
//        String cartPromotionChangeJobHandler = "ceshi";
//        String executorParam = URLEncoder.encode(s,"UTF-8");
//        String encode = URLEncoder.encode("秒杀过期定时任务", "UTF-8");
//
//        String targetUrl = "http://localhost:8080/xxl-job-admin/jobinfo/pageList";
//        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
//        HttpPost httpPost = new HttpPost(targetUrl+"?"+"jobGroup=4&triggerStatus=-1&executorParam="+executorParam+"&start=0&length=10");
//
//        httpPost.setHeader("Content-Type", "application/json;charset=utf8");
//        // 响应模型
//        CloseableHttpResponse response = null;
//        try {
//            // 由客户端执行(发送)Post请求
//            response = httpClient.execute(httpPost);
//            // 从响应模型中获取响应实体
//            HttpEntity responseEntity = response.getEntity();
//            String s1 = EntityUtils.toString(responseEntity);
//            ResponseEntityMessage responseEntity1 = JsonUtil.jsonToObject(s1, ResponseEntityMessage.class);
//            if (responseEntity1.getRecordsTotal() > 0){
//
//            }
//            System.out.println("响应状态为:" + response.getStatusLine());
//            if (responseEntity != null) {
//                System.out.println("响应内容长度为:" + responseEntity.getContentLength());
//
//            }
//        } catch (ClientProtocolException e) {
//            e.printStackTrace();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                // 释放资源
//                if (httpClient != null) {
//                    httpClient.close();
//                }
//                if (response != null) {
//                    response.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//    }


    /**
     * 删除任务
     * @param url
     * @param id
     * @return
     * @throws HttpException
     * @throws IOException
     */
    public static JSONObject deleteJob(String url,int id) throws HttpException, IOException {
        String path = "/jobinfo/remove?id="+id;

        return doGet(url,path);
    }

    /**
     * 开始任务
     * @param url
     * @param id
     * @return
     * @throws HttpException
     * @throws IOException
     */
    public static JSONObject startJob(String url,int id) throws HttpException, IOException {
        String path = "/api/jobinfo/start?id="+id;
        return doGet(url,path);
    }

    /**
     * 停止任务
     * @param url
     * @param id
     * @return
     * @throws HttpException
     * @throws IOException
     */
    public static JSONObject stopJob(String url,int id) throws HttpException, IOException {
        String path = "/api/jobinfo/stop?id="+id;
        return doGet(url,path);
    }

    public static JSONObject doGet(String url,String path) throws HttpException, IOException {
        String targetUrl = url + path;
        HttpClient httpClient = new HttpClient();
        HttpMethod get = new GetMethod(targetUrl);
        get.setRequestHeader("cookie", cookie);
        httpClient.executeMethod(get);
        JSONObject result = new JSONObject();
        result = getJsonObject(get, result);
        return result;
    }

    private static JSONObject getJsonObject(HttpMethod get, JSONObject result) throws IOException {
        if (get.getStatusCode() == 200) {
            String responseBodyAsString = get.getResponseBodyAsString();
            result = JSONObject.fromObject(responseBodyAsString);
        } else {
            try {
                result = JSONObject.fromObject(get.getResponseBodyAsString());
            } catch (Exception e) {
                result.put("error", get.getResponseBodyAsString());
            }
        }
        return result;
    }


    public static String login(String url, String userName, String password) throws HttpException, IOException {
        String path = "/api/jobinfo/login?userName="+userName+"&password="+password;
        String targetUrl = url + path;
        HttpClient httpClient = new HttpClient();
        HttpMethod get = new GetMethod(targetUrl);
        httpClient.executeMethod(get);
        if (get.getStatusCode() == 200) {
            Cookie[] cookies = httpClient.getState().getCookies();
            StringBuffer tmpcookies = new StringBuffer();
            for (Cookie c : cookies) {
                tmpcookies.append(c.toString() + ";");
            }
            cookie = tmpcookies.toString();
        } else {
            try {
                cookie = "";
            } catch (Exception e) {
                cookie="";
            }
        }
        return cookie;
    }

    public static String update(String addresses,
                                Long activityId,
                                Long jobId,
                                String promotionType,
                                Long endTime,
                                String promotionName,
                                String handlerName) throws IOException {

        // 执行参数
        CartPromotionChangeMsg cartPromotionChangeMsg = new CartPromotionChangeMsg();
        cartPromotionChangeMsg.setActivityId(activityId);
        cartPromotionChangeMsg.setPromotionType(promotionType);
        String s = JsonUtil.objectToJson(cartPromotionChangeMsg);
        // cron表达式
        SimpleDateFormat sdf = new SimpleDateFormat("ss mm HH dd MM ?");
        Date date = new Date(endTime);
        String format = sdf.format(date);
        String cron = URLEncoder.encode(format, "UTF-8");
        // 执行器名称
        String cartPromotionChangeJobHandler = handlerName;
        String executorParam = URLEncoder.encode(s,"UTF-8");
        // 第三方任务名称
        String encode = URLEncoder.encode(promotionName, "UTF-8");
        // jonId
        int job = jobId.intValue();
        String requestInfo = "jobGroup=4&jobDesc=" + encode + "&executorRouteStrategy=FIRST&cronGen_display=&jobCron=" + cron + "&glueType=BEAN&executorHandler="+ cartPromotionChangeJobHandler +"&executorBlockStrategy=SERIAL_EXECUTION&childJobId=&executorTimeout=0&executorFailRetryCount=0&author=ljg&alarmEmail=&executorParam=" + executorParam + "&glueRemark=GLUE%E4%BB%A3%E7%A0%81%E5%88%9D%E5%A7%8B%E5%8C%96&glueSource=&triggerStatus=1&id="+job;

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(addresses+ "/jobinfo/update" +"?"+ requestInfo);

        httpPost.setHeader("Content-Type", "application/json;charset=utf8");
        // 响应模型
        CloseableHttpResponse response = null;
        HttpEntity responseEntity = null;
        try {
            // 由客户端执行(发送)Post请求
            response = httpClient.execute(httpPost);
            // 从响应模型中获取响应实体
            responseEntity = response.getEntity();
            if (responseEntity != null) {
                System.out.println("响应内容长度为:" + responseEntity.getContentLength());
            }
            return EntityUtils.toString(responseEntity);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                // 释放资源
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return EntityUtils.toString(responseEntity);
    }
}