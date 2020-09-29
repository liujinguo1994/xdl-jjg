package com.xdl.jjg.web.controller;

import com.aliyun.oss.OSSClient;
import com.xdl.jjg.support.OssProperties;
import com.xdl.jjg.util.CollectionUtils;
import com.xdl.jjg.util.RestResult;
import com.xdl.jjg.web.service.FileService;
import io.swagger.annotations.Api;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author sanqi
 * @version 1.0.0
 * @data 2019年09月2019/9/6日
 * @Description 文件上传下载
 */
@RestController
@RequestMapping("/file")
@Api("文件上传下载")
public class FileController {


    @Autowired
    private FileService fileService;


    private static List<String> geshi = CollectionUtils.newArrayList("mp4", "avi", "3GP");
    @Autowired
    public OssProperties ossProperties;
    public OSSClient ossClient;


    private OSSClient getInstance() {
        if (ossClient == null) {
            ossClient = new OSSClient(ossProperties.getEndpoint(), ossProperties.getAccessId(), ossProperties.getAccessKey());
        }
        return ossClient;
    }


    @GetMapping("/downloadAPP.apk")
    public void downloadAPP(HttpServletResponse response) {


        File file = fileService.downloadAPP();
        OutputStream toClient = null;
        if (file.exists()) {
            // 以流的形式下载文件。
            try {
                //先以输入流把文件输入到buffer中，再以输出流的形式输出
                InputStream fis = new BufferedInputStream(new FileInputStream(file));
                byte[] buffer = new byte[fis.available()];
                fis.read(buffer);
                fis.close();
                // 清空response
                response.reset();
                // 设置response的Header
                response.addHeader("Content-Disposition", "attachment;filename=" + file.getName());
                response.addHeader("Content-Length", "" + file.length());
                toClient = new BufferedOutputStream(response.getOutputStream());
                response.setContentType("application/octet-stream");
                toClient.write(buffer);
                toClient.flush();
                toClient.close();
            } catch (Exception e) {
                System.out.println("下载文件错误" + e.getMessage());
            }
        } else {
            try {
                res(response);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @GetMapping("/downloadVideo")
    public void downloadVideo(@RequestParam String videoPath, HttpServletResponse response) throws Exception {


        File file = new File(videoPath);
        if (!file.exists()) {
            res(response);
        }
        OutputStream toClient = null;
        if (file.exists()) {
            // 以流的形式下载文件。
            try {
                //先以输入流把文件输入到buffer中，再以输出流的形式输出
                InputStream fis = new BufferedInputStream(new FileInputStream(file));
                byte[] buffer = new byte[fis.available()];
                fis.read(buffer);
                fis.close();
                // 清空response
                response.reset();
                // 设置response的Header
                response.addHeader("Content-Disposition", "attachment;filename=" + file.getName());
                response.addHeader("Content-Length", "" + file.length());
                toClient = new BufferedOutputStream(response.getOutputStream());
                response.setContentType("application/octet-stream");
                toClient.write(buffer);
                toClient.flush();
                toClient.close();
            } catch (Exception e) {
                System.out.println("下载文件错误" + e.getMessage());
            }
        } else {
            try {
                res(response);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @PostMapping(value = "uploadVideoFile")
    public RestResult uploadVideoFiles(@RequestParam(value = "file") MultipartFile file) {

        System.out.println("**************uploadVideoFile开始***************");
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        if (!geshi.contains(extension)) {
            System.out.println("******extension****:" + extension);
            return RestResult.fail("文件格式不正确");
        }

        try {

            //根路径
            String keyFormat = "%s/%d/%d/%d/%s";
            LocalDate now = LocalDate.now();
            String originalFilename = file.getOriginalFilename();
            String videoPath = String.format(keyFormat, "video", now.getYear(), now.getMonthValue(), now.getDayOfMonth(),
                    UUID.randomUUID().toString().replaceAll("-", "") + "." + FilenameUtils.getExtension(originalFilename));

            String imagePath = String.format(keyFormat, "images", now.getYear(), now.getMonthValue(), now.getDayOfMonth(),
                    UUID.randomUUID().toString().replaceAll("-", "") + ".jpg");

            fileService.uploadVideoFiles(file.getInputStream(),videoPath,imagePath);

            Map<String, String> map = new HashMap(2);
            map.put("videoPath", ossProperties.getCdnUrl() + "/" + videoPath);
            map.put("imagesPath",ossProperties.getCdnUrl() + "/" + imagePath);
            System.out.println("**************uploadVideoFile结束***************");
            return RestResult.ok(map);

        } catch (Exception e) {
            e.printStackTrace();
            return RestResult.fail();
        }

    }



    private void res(HttpServletResponse response) throws Exception {
        response.setContentType("application/xml; charset=utf-8");
        response.setCharacterEncoding("UTF-8");
        String res = "文件不存在";
        PrintWriter out = response.getWriter();
        out.print(res);
        out.flush();
        out.close();
    }
}
