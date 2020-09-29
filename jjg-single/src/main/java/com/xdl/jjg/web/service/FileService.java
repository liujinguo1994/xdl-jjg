package com.xdl.jjg.web.service;

import java.io.File;
import java.io.InputStream;

/**
 * @author sanqi
 * @version 1.0.0
 * @data 2019年09月2019/9/6日
 * @Description TODO
 */
public interface FileService {


    /**
     * 下载app
     *
     * @param
     * @return
     */
    File downloadAPP();

    /**
     * 下载app
     *
     * @param inputStream
     * @param videoPath
     * @param imagePath
     * @return
     */
    void uploadVideoFiles(InputStream inputStream, String videoPath, String imagePath);
}
