package com.xdl.jjg.oss.upload;


import com.xdl.jjg.constants.OssFileType;
import com.xdl.jjg.response.service.FileDTO;
import com.xdl.jjg.response.service.FileVO;

/**
 * 上传接口
 */
public interface Uploader {


    /**
     * 上传文件
     *
     * @param input  上传对象
     * @return
     */
    FileVO upload(FileDTO input, OssFileType ossFileType);

    /**
     * 删除文件
     *
     * @param filePath 文件地址
     */
    boolean deleteFile(String filePath);

    /**
     * 生成缩略图路径
     *
     * @param url    原图片全路径
     * @param width  需要生成图片尺寸的宽
     * @param height 需要生成图片尺寸的高
     * @return 生成的缩略图路径
     */
    String getThumbnailUrl(String url, Integer width, Integer height);
}
