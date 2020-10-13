package com.xdl.jjg.web.controller.pc.system;

import com.shopx.common.constant.OssFileType;
import com.shopx.common.model.domain.FileDTO;
import com.shopx.common.model.domain.FileVO;
import com.shopx.common.model.result.ApiResponse;
import com.shopx.common.oss.upload.OSSUploader;
import com.shopx.common.util.FileUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author xiaoLin
 * @ClassName EsSystemFileController
 * @Description 文件图片管理
 * @create 2019/12/6 17:14
 */
@RestController
@RequestMapping("/buyer")
@Api(value="/buyer", tags="文件/图片管理")
public class EsSystemFileController {

    @Autowired
    private OSSUploader uploader;

    @ApiOperation(value = "文件/图片上传",response = FileVO.class)
    @PostMapping(value = "/upload/{scene}")
    @ApiImplicitParam(name = "scene", value = "业务场景", allowableValues = "qrcode,excel,goods,shop,ueditor,other", required = true, dataType = "String", paramType = "path")
    @ResponseBody
    public ApiResponse upload(MultipartFile file, @PathVariable String scene) {
        if (file != null && file.getOriginalFilename() != null) {
            if (!FileUtil.isAllowUp(file.getOriginalFilename())) {
                return ApiResponse.fail(1001,"不允许上传的文件格式，请上传gif,jpg,jpeg,bmp,swf,png,rar,doc,docx,xls,xlsx,pdf,zip,ico,txt,mp4格式文件。!");
            }
            FileDTO fileDTO = new FileDTO();
            fileDTO.setName(file.getOriginalFilename());//文件名（包含后缀）
            try {
                fileDTO.setStream(file.getInputStream());//文件流
            } catch (IOException e) {
                e.printStackTrace();
                return ApiResponse.fail(1002,"获取文件/图片输入流异常");
            }
            FileVO fileVO = null;
            for (OssFileType ossFileType: OssFileType.values()){
                String substring = ossFileType.getValue().substring(0, ossFileType.getValue().indexOf("/"));
                if (substring.equals(scene)){
                    fileVO = uploader.upload(fileDTO, ossFileType);//上传
                }
            }
            return ApiResponse.success(fileVO);
        } else {
            return ApiResponse.fail(1003,"没有文件/图片");
        }
    }

    @DeleteMapping(value = "/deleteFile/{filePath}")
    @ApiOperation(value = "文件/图片删除")
    @ApiImplicitParam(name = "filePath", value = "文件/图片路径", required = true, dataType = "String",paramType = "path")
    public ApiResponse deleteFile(@PathVariable("filePath") String filePath) {
        boolean b = uploader.deleteFile(filePath);
        if (b){
            return ApiResponse.success();
        }
        return ApiResponse.fail(1004,"文件/图片删除失败");
    }

}
