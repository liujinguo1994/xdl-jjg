package com.xdl.jjg.web.controller;

import com.xdl.jjg.util.FileUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 百度Ueditor配置及文件上传支持
 *
 * @author kingapex
 * @version 1.0
 * @since 7.0.0
 * 2018/6/6
 */
@RestController
@RequestMapping("/ueditor")
public class UeditorBaseController {


    /**
     * 文件上传接口
     */
    @Autowired
    private OSSUploader ossUploader;

    /**
     * 配置内容常量，用于缓存配置信息，避免每次由硬盘读取
     */
    private static String config;

    @GetMapping(value = "", produces = "application/javascript")
    @ApiOperation(value = "获取ueditor配置")
    @ApiImplicitParam(name = "callback", value = "jsonp的callback", required = true, dataType = "String")
    public String config(String callback) throws JSONException {
        return "/**/" + callback + "(" + getConfig() + ");";

    }

    /**
     * 获取配置<br>
     * 如果config中已经存在，则直接返回，否则由硬盘读取<br>
     * 读取文件为/resource/ueditor_config.json<br><br>
     *
     * @return
     */
    private String getConfig() {

        if (config == null) {
            config = FileUtil.readFile("/ueditor_config.json");
        }

        return config;
    }

    @PostMapping(value = "")
    @ApiOperation(value = "ueditor文件/图片上传")
    public Map upload(MultipartFile upfile) throws JSONException, IOException {
        Map result = new HashMap(16);
        if (upfile != null && upfile.getOriginalFilename() != null) {
            if (!FileUtil.isAllowUp(upfile.getOriginalFilename())) {
                result.put("state", "不允许上传的文件格式，请上传gif,jpg,bmp格式文件。");
                return result;
            }
            FileDTO fileDTO = new FileDTO();
            fileDTO.setName(upfile.getOriginalFilename());//文件名（包含后缀）
            try {
                fileDTO.setStream(upfile.getInputStream());//文件流
            } catch (IOException e) {
                e.printStackTrace();
                return result;
            }
            FileVO fileVO = null;
            for (OssFileType ossFileType : OssFileType.values()) {
                String substring = ossFileType.getValue().substring(0, ossFileType.getValue().indexOf("/"));
                if (StringUtils.equals(substring, "ueditor")) {
                    fileVO = ossUploader.upload(fileDTO, ossFileType);//上传
                    String url = fileVO.getUrl();
                    String title = fileVO.getName();
                    String original = fileVO.getName();
                    result.put("state", "SUCCESS");
                    result.put("url", url);
                    result.put("title", title);
                    result.put("name", title);
                    result.put("original", original);
                    result.put("type", "." + fileVO.getExt());
                }

            }
            return result;
        } else {
            result.put("state", "没有读取要上传的文件");
            return result;
        }
    }
}
