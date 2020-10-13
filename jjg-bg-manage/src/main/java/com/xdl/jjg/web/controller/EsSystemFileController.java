package com.xdl.jjg.web.controller;

import com.xdl.jjg.constants.OssFileType;
import com.xdl.jjg.oss.upload.OSSUploader;
import com.xdl.jjg.response.service.FileDTO;
import com.xdl.jjg.response.service.FileVO;
import com.xdl.jjg.response.web.ApiResponse;
import com.xdl.jjg.util.FileUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * <p>
 * 前端控制器-文件管理
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-06-10
 */
@RestController
@RequestMapping("/esSystemFile")
@Api(value = "/esSystemFile", tags = "文件管理")
public class EsSystemFileController {

    @Autowired
    private OSSUploader ossUploader;

    @ApiOperation(value = "文件上传", response = FileVO.class)
    @PostMapping(value = "/upload/{scene}")
    @ApiImplicitParam(name = "scene", value = "业务场景", allowableValues = "qrcode,excel,goods,shop,ueditor,other", required = true, dataType = "String", paramType = "path")
    @ResponseBody
    public ApiResponse upload(MultipartFile file, @PathVariable String scene) {
        if (file != null && file.getOriginalFilename() != null) {
            if (!FileUtil.isAllowUp(file.getOriginalFilename())) {
                return ApiResponse.fail(1001, "不允许上传的文件格式，请上传gif,jpg,jpeg,bmp,swf,png,rar,doc,docx,xls,xlsx,pdf,zip,ico,txt,mp4格式文件。!");
            }
            FileDTO fileDTO = new FileDTO();
            fileDTO.setName(file.getOriginalFilename());//文件名（包含后缀）
            try {
                fileDTO.setStream(file.getInputStream());//文件流
            } catch (IOException e) {
                e.printStackTrace();
                return ApiResponse.fail(1002, "获取文件输入流异常");
            }
            FileVO fileVO = null;
            for (OssFileType ossFileType : OssFileType.values()) {
                String substring = ossFileType.getValue().substring(0, ossFileType.getValue().indexOf("/"));
                if (substring.equals(scene)) {
                    fileVO = ossUploader.upload(fileDTO, ossFileType);//上传
                }
            }
            return ApiResponse.success(fileVO);
        } else {
            return ApiResponse.fail(1003, "没有文件");
        }
    }

    @ApiOperation(value = "文件删除")
    @ApiImplicitParam(name = "filePath", value = "文件路径", required = true, dataType = "String", paramType = "path")
    @DeleteMapping(value = "/deleteFile/{filePath}")
    public ApiResponse deleteFile(@PathVariable String filePath) {
        boolean b = ossUploader.deleteFile(filePath);
        if (b) {
            return ApiResponse.success();
        }
        return ApiResponse.fail(1004, "文件删除失败");
    }
}
