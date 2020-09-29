package com.xdl.jjg.web.service.impl;

import com.aliyun.oss.OSSClient;
import com.xdl.jjg.mapper.version.SysVersionMapper;
import com.xdl.jjg.pojo.version.SysVersion;
import com.xdl.jjg.pojo.version.SysVersionExample;
import com.xdl.jjg.support.OssProperties;
import com.xdl.jjg.web.service.FileService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;

/**
 * @author sanqi
 * @version 1.0.0
 * @data 2019年09月2019/9/6日
 * @Description TODO
 */

@Service
public class FileServiceImpl implements FileService {


    @Autowired
    private SysVersionMapper versionMapper;
    @Autowired
    public OssProperties ossProperties;
    public OSSClient ossClient;


    private OSSClient getInstance() {
        if (ossClient == null) {
            ossClient = new OSSClient(ossProperties.getEndpoint(), ossProperties.getAccessId(), ossProperties.getAccessKey());
        }
        return ossClient;
    }

    @Override
    public File downloadAPP() {
        SysVersionExample example = new SysVersionExample();
        example.createCriteria().andPlatformEqualTo((short) 2);
        example.orderBy("id desc");
        SysVersion sysVersion = versionMapper.selectOneByExample(example);
        if (sysVersion == null || StringUtils.isBlank(sysVersion.getCreateAddress())) {
            throw new ServiceException(203, "没有可下载的版本");
        }
        File file = new File(sysVersion.getCreateAddress());
        return file;
    }


    @Override
    @Async
    public void uploadVideoFiles(InputStream inputStream, String videoPath, String imagePath) {

        try {
            long start1 = System.currentTimeMillis();
//            fetchFrame(inputStream, imagePath);
            System.out.println("转成图片并上传的时间:" + (System.currentTimeMillis() - start1));
            long start = System.currentTimeMillis();
            OSSClient ossClient = getInstance();
            ossClient.putObject(ossProperties.getBucket(), videoPath, inputStream, null);
            System.out.println("视频上传时间:" + (System.currentTimeMillis() - start));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("****************视频上传错误*****************"+e.getMessage());
        }

    }

    //参数：视频路径和缩略图保存路径
//    public void fetchFrame(InputStream inputStream, String imagePath) throws Exception {
//
//        //根路径
//
//        String key = "/" + UUID.randomUUID().toString().replaceAll("-", "") + ".jpg";
//        FFmpegFrameGrabber ff = new FFmpegFrameGrabber(inputStream);
//        ff.start();
//        int length = ff.getLengthInFrames();
//        int i = 0;
//        Frame f = null;
//        while (i < length) {
//            // 去掉前5帧，避免出现全黑的图片，依自己情况而定
//            f = ff.grabImage();
//            if ((i > 1) && (f.image != null)) {
//                break;
//            }
//            i++;
//        }
//        File imageFile = new File(key);
//        ImageIO.write(FrameToBufferedImage(f), "jpg", imageFile);
//
//        OSSClient ossClient = getInstance();
//        ossClient.putObject(ossProperties.getBucket(), imagePath, imageFile);
//        ff.stop();
//
//        if (imageFile.exists()) {
//            imageFile.delete();
//        }
//    }
//
//    public static BufferedImage FrameToBufferedImage(Frame frame) {
//        //创建BufferedImage对象
//        Java2DFrameConverter converter = new Java2DFrameConverter();
//        BufferedImage bufferedImage = converter.getBufferedImage(frame);
//        return bufferedImage;
//    }


}
