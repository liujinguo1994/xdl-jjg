package com.xdl.jjg.web.service;

import java.io.File;

/**
 * @author sanqi
 * @version 1.0.0
 * @data 2019年08月2019/8/24日
 * @Description 阿里云文件上传服务
 */
public interface OssService {


    /**
     * 上传
     *
     * @param file
     * @return
     */
    String putObject(File file);
}
