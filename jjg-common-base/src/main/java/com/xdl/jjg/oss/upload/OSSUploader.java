package com.xdl.jjg.oss.upload;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.*;
import com.xdl.jjg.constants.OssFileType;
import com.xdl.jjg.oss.OssClientFactoryBean;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.FileDTO;
import com.xdl.jjg.response.service.FileVO;
import com.xdl.jjg.util.FileUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.UUID;


/**
 * 阿里云oss文件上传
 */
public class OSSUploader implements Uploader, InitializingBean {

	private static final Log LOGER = LogFactory.getLog(OSSUploader.class);

	private static final int OSS_UPLOAD_ERROR = 91;

	private OSSClient ossClient;
	private String directory;
	private String bucketName;

	@Autowired
	private OssClientFactoryBean ossClientFactoryBean;

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(this.ossClientFactoryBean, "'ossClientFactoryBean' must be not null");
		this.ossClient = ossClientFactoryBean.getObject();
		this.bucketName = ossClientFactoryBean.getBucketName();
		this.directory = ossClientFactoryBean.getDirectory();
	}

	public FileVO upload(FileDTO input, OssFileType ossFileType) {
		// 获取文件后缀
		String ext = FileUtil.getFileExt(input.getName());
		//文件名称
		String picName = UUID.randomUUID().toString().toUpperCase().replace("-", "") + "." + ext;
		// 文件名，根据UUID来
		String fileName = this.directory + ossFileType.getValue() + picName;
		FileVO file = new FileVO();
		file.setName(fileName);
		file.setUrl(this.putObject(input.getStream(), ext, fileName, bucketName));
		file.setExt(ext);
		return file;
	}

	@Override
	public String getThumbnailUrl(String url, Integer width, Integer height) {
		// 缩略图全路径
		String thumbnailPah = url + "_" + width + "x" + height;
		// 返回缩略图全路径
		return thumbnailPah;
	}

	/**
	 * 上传图片
	 *
	 * @param input
	 *            上传图片文件的输入流
	 * @param fileType
	 *            文件类型，也就是后缀
	 * @param fileName
	 *            文件名称
	 * @param bucketName
	 *            储存空间名称
	 * @return 访问图片的url路径
	 */
	private String putObject(InputStream input, String fileType, String fileName, String bucketName) {
		// 默认null
		String urls = null;
		// 最终返回的路径
		String saveUrl = null;
		try {
			// 创建上传Object的Metadata
			ObjectMetadata meta = new ObjectMetadata();
			// 设置上传内容类型
			meta.setContentType(FileUtil.contentType(fileType));
			// 被下载时网页的缓存行为
			meta.setCacheControl("no-cache");
			PutObjectRequest request = new PutObjectRequest(bucketName, fileName, input, meta);
			// 创建上传请求
			ossClient.putObject(request);
			// 设置Object权限
			boolean found = ossClient.doesObjectExist(bucketName, fileName);
			if (found = true) {
				ossClient.setObjectAcl(bucketName, fileName, CannedAccessControlList.PublicRead);
			}
			Date expiration = new Date(System.currentTimeMillis() + 3600L * 1000 * 24 * 365 * 10);
			URL url = ossClient.generatePresignedUrl(bucketName, fileName, expiration);
			// 对返回的签名url处理获取最终展示用的url
			urls = url.toString();
			String[] strs = urls.split("\\?");
			for (int i = 0, len = strs.length; i < len; i++) {
				saveUrl = strs[0].toString();
			}
			LOGER.info("OSS上传成功的地址" + saveUrl);
			return saveUrl;
		} catch (OSSException oe) {
			LOGER.error("OSSException异常",oe);
			throw new ArgumentException(OSS_UPLOAD_ERROR, "上传文件失败！");
		} catch (ClientException ce) {
			LOGER.error("ClientException异常",ce);
			ce.printStackTrace();
			throw new ArgumentException(OSS_UPLOAD_ERROR, "上传文件失败！");
		} catch (Exception e) {
			LOGER.error("Exception异常",e);
			e.printStackTrace();
			throw new ArgumentException(OSS_UPLOAD_ERROR, "上传文件失败！");
		}
	}
	/**
	 * 根据url获取fileName
	 *
	 * @param fileUrl
	 *            文件url
	 * @return String fileName 文件名称
	 */
	private static String getFileName(String fileUrl) {
		String str = "aliyuncs.com/";
		int beginIndex = fileUrl.indexOf(str);
		if (beginIndex == -1) {
			return null;
		}

		return fileUrl.substring(beginIndex + str.length());
	}

	/**
	 * 根据url获取bucketName
	 *
	 * @param fileUrl
	 *            文件url
	 * @return String bucketName 域名
	 */
	private static String getBucketName(String fileUrl) {
		String http = "http://";
		String https = "https://";
		int httpIndex = fileUrl.indexOf(http);
		int httpsIndex = fileUrl.indexOf(https);
		int startIndex = 0;
		if (httpIndex == -1) {
			if (httpsIndex == -1) {
				return null;
			} else {
				startIndex = httpsIndex + https.length();
			}
		} else {
			startIndex = httpIndex + http.length();
		}
		int endIndex = fileUrl.indexOf(".oss-");
		return fileUrl.substring(startIndex, endIndex);
	}

	/**
	 * 删除上传文件
	 *
	 * @param filePath
	 *            删除文件全路径
	 * @return
	 */
	@Override
	public boolean deleteFile(String filePath){
		// 根据url获取bucketName
		String bucketNames = getBucketName(filePath);
		// 根据url获取fileName
		String fileName = getFileName(filePath);
		if (bucketNames == null || fileName == null) {
			return false;
		}
		try {
			GenericRequest request = new DeleteObjectsRequest(bucketNames).withKey(fileName);
			ossClient.deleteObject(request);
			return true;
		} catch (Exception oe) {
			LOGER.error("Exception异常",oe);
			oe.printStackTrace();
			throw new ArgumentException(OSS_UPLOAD_ERROR, "删除文件失败！");
		}
	}
}
