package com.xdl.jjg.web.controller;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.OSSObject;
import com.jjg.member.model.domain.EsAdminMemberDO;
import com.jjg.member.model.domain.EsMemberAddressDO;
import com.jjg.member.model.domain.EsMemberDO;
import com.jjg.member.model.dto.EsMemberAddressDTO;
import com.jjg.member.model.dto.EsMemberDTO;
import com.jjg.member.model.dto.EsQueryAdminMemberDTO;
import com.jjg.member.model.vo.*;
import com.jjg.system.model.form.*;
import com.xdl.jjg.constant.ApiStatus;
import com.xdl.jjg.constant.ErrorCode;
import com.xdl.jjg.constants.OssFileType;
import com.xdl.jjg.oss.upload.OSSUploader;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.response.service.FileDTO;
import com.xdl.jjg.response.service.FileVO;
import com.xdl.jjg.response.web.ApiPageResponse;
import com.xdl.jjg.response.web.ApiResponse;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.util.FormatValidateUtils;
import com.xdl.jjg.web.service.IEsRegionsService;
import com.xdl.jjg.web.service.feign.member.MemberAddressService;
import com.xdl.jjg.web.service.feign.member.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * <p>
 * 前端控制器-会员
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
@RestController
@Api(value = "/esMember", tags = "会员")
@RequestMapping("/esMember")
public class EsMemberController extends BaseController{

    @Value("${aliyun.oss.endpoint}")
    private String endpoint;
    @Value("${aliyun.oss.bucketName}")
    private String bucketName;
    @Value("${aliyun.oss.directory}")
    private String picLocation;
    @Value("${aliyun.oss.accessKeyId}")
    private String accessKeyId;
    @Value("${aliyun.oss.accessKeySecret}")
    private String accessKeySecret;

    @Autowired
    private MemberService iesMemberService;

    @Autowired
    private IEsRegionsService iEsRegionsService;

    @Autowired
    private MemberAddressService memberAddressService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private OSSUploader ossUploader;


    @ApiOperation(value = "分页查询会员列表", response = EsAdminMemberVO.class)
    @GetMapping(value = "/getMemberList")
    @ResponseBody
    public ApiResponse getMemberList(EsMemberQueryForm form) {
        EsQueryAdminMemberDTO dto = new EsQueryAdminMemberDTO();
        BeanUtil.copyProperties(form, dto);
        DubboPageResult<EsAdminMemberDO> result = iesMemberService.getAdminMemberList(dto, form.getPageSize(), form.getPageNum());
        if (result.isSuccess()) {
            List<EsAdminMemberDO> data = result.getData().getList();
            List<EsAdminMemberVO> voList = BeanUtil.copyList(data, EsAdminMemberVO.class);
            return ApiPageResponse.pageSuccess(result.getData().getTotal(), voList);
        } else {
            return ApiPageResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "平台添加会员")
    @PostMapping(value = "/addMember")
    @ResponseBody
    public ApiResponse addMember(@Valid @RequestBody @ApiParam(name = "会员form对象", value = "form") EsMemberForm form, HttpServletRequest request) {
        EsMemberDTO esMemberDTO = new EsMemberDTO();
        BeanUtil.copyProperties(form, esMemberDTO);
        //验证用户邮箱
        Boolean judgeEmail = FormatValidateUtils.isEmail(form.getEmail());
        if (!judgeEmail) {
            throw new ArgumentException(ErrorCode.EMAIL_ERROR.getErrorCode(), "邮箱格式有误");
        }
        //获取用户ip地址
        String ip = getIpAddress(request);
        if (ip != null) {
            esMemberDTO.setRegisterIp(ip);
        }
        esMemberDTO.setProvince(iEsRegionsService.getRegions(form.getProvinceId()).getData().getLocalName());
        esMemberDTO.setCity(iEsRegionsService.getRegions(form.getCityId()).getData().getLocalName());
        DubboResult result = iesMemberService.insertAdminMember(esMemberDTO);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "修改会员")
    @PutMapping(value = "/updateMember")
    @ResponseBody
    public ApiResponse updateMember(@Valid @RequestBody @ApiParam(name = "会员form对象", value = "form") EsUpdateMemberForm form) {
        //验证用户邮箱
        Boolean judgeEmail = FormatValidateUtils.isEmail(form.getEmail());
        if (!judgeEmail) {
            throw new ArgumentException(ErrorCode.EMAIL_ERROR.getErrorCode(), "邮箱格式有误");
        }
        if (form.getProvinceId() == null || form.getProvinceId() == 0 || form.getCityId() == null || form.getCityId() == 0) {
            throw new ArgumentException(ErrorCode.EMAIL_ERROR.getErrorCode(), "地区为必填项，请核对后再提交");
        }
        EsMemberDTO esMemberDTO = new EsMemberDTO();
        BeanUtil.copyProperties(form, esMemberDTO);
        esMemberDTO.setProvince(iEsRegionsService.getRegions(form.getProvinceId()).getData().getLocalName());
        esMemberDTO.setCity(iEsRegionsService.getRegions(form.getCityId()).getData().getLocalName());
        DubboResult result = iesMemberService.updateMemberInfo(esMemberDTO);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "禁用会员")
    @PutMapping(value = "/disableMember/{id}")
    @ApiImplicitParam(name = "id", value = "会员id", required = true, dataType = "long", paramType = "path", example = "1")
    @ResponseBody
    public ApiResponse disableMember(@PathVariable Long id) {
        DubboResult result = iesMemberService.updateMemberState(id, 1);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "启用会员")
    @PutMapping(value = "/enabledMember/{id}")
    @ApiImplicitParam(name = "id", value = "会员id", required = true, dataType = "long", paramType = "path", example = "1")
    @ResponseBody
    public ApiResponse enabledMember(@PathVariable Long id) {
        EsMemberDTO esMemberDTO = new EsMemberDTO();
        esMemberDTO.setId(id);
        esMemberDTO.setState(0);
        DubboResult result = iesMemberService.updateMemberInfo(esMemberDTO);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "根据id查询会员信息", response = EsMemberInfoVO.class)
    @GetMapping(value = "/getMember/{id}")
    @ApiImplicitParam(name = "id", value = "会员id", required = true, dataType = "long", paramType = "path", example = "1")
    @ResponseBody
    public ApiResponse getMember(@PathVariable Long id) {
        DubboResult<EsMemberDO> result = iesMemberService.getAdminMember(id);
        if (result.isSuccess()) {
            EsMemberDO data = result.getData();
            EsMemberInfoVO memberVO = new EsMemberInfoVO();
            BeanUtil.copyProperties(data, memberVO);
            return ApiResponse.success(memberVO);
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "分页查询指定会员的收货地址列表", response = EsMemberAddressVO.class)
    @GetMapping(value = "/addresses/{memberId}")
    @ApiImplicitParam(name = "memberId", value = "会员id", required = true, dataType = "long", paramType = "path", example = "1")
    @ResponseBody
    public ApiResponse getMemberAddressList(@PathVariable Long memberId, EsQueryPageForm form) {
        EsMemberAddressDTO dto = new EsMemberAddressDTO();
        dto.setMemberId(memberId);
        DubboPageResult<EsMemberAddressDO> result = memberAddressService.getMemberAddressList(dto, form.getPageSize(), form.getPageNum());
        if (result.isSuccess()) {
            List<EsMemberAddressDO> data = result.getData().getList();
            List<EsMemberAddressVO> voList = BeanUtil.copyList(data, EsMemberAddressVO.class);
            return ApiPageResponse.pageSuccess(result.getData().getTotal(), voList);
        } else {
            return ApiPageResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "批量导入会员", response = EsImportMemberVO.class)
    @PostMapping(value = "/importMember")
    @ResponseBody
    public ApiResponse importMember(@RequestBody @Valid EsImportForm form) {
        String[] split = form.getUrl().split("/");
        String tempFileName = split[split.length - 1];

        // 创建OSSClient实例。
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);

        String ossKey = tempFileName;
        OSSObject ossObject = ossClient.getObject(bucketName, picLocation + "excel/" + ossKey);
        // 下载object到文件
        InputStream inputStream = ossObject.getObjectContent();
        try {
            MultipartFile file = new MockMultipartFile("aa", "cc", "", inputStream);
            //关闭client
            ossClient.shutdown();
            DubboResult<EsImportMemberVO> result = iesMemberService.importMember(file.getBytes());
            if (result.isSuccess()) {
                return ApiResponse.success(result.getData());
            } else {
                return ApiResponse.fail(ApiStatus.wrapperException(result));
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ApiResponse.fail(1001, "文件转换失败!");
        }
    }

    @ApiOperation(value = "导出会员信息")
    @GetMapping(value = "/exportMemberInfo")
    @ResponseBody
    public ApiResponse exportMemberInfo(EsMemberQueryForm form) {
        EsQueryAdminMemberDTO dto = new EsQueryAdminMemberDTO();
        BeanUtil.copyProperties(form, dto);
        DubboPageResult<EsExportMemberVO> result = iesMemberService.exportMember(dto);
        if (result.isSuccess()) {
            List<EsExportMemberVO> list = result.getData().getList();
            //第一步，创建一个workbook对应一个excel文件
            HSSFWorkbook workbook = new HSSFWorkbook();
            //第二部，在workbook中创建一个sheet对应excel中的sheet
            HSSFSheet sheet = workbook.createSheet("会员信息");
            //第三部，在sheet表中添加表头第0行
            HSSFRow row = sheet.createRow(0);
            //第四步，创建单元格，设置表头
            HSSFCell cell = row.createCell(0);
            cell.setCellValue("会员ID");
            cell = row.createCell(1);
            cell.setCellValue("用户名");
            cell = row.createCell(2);
            cell.setCellValue("手机号");
            cell = row.createCell(3);
            cell.setCellValue("昵称");
            cell = row.createCell(4);
            cell.setCellValue("性别");
            cell = row.createCell(5);
            cell.setCellValue("身份证号");
            cell = row.createCell(6);
            cell.setCellValue("邮箱");
            cell = row.createCell(7);
            cell.setCellValue("会员等级");
            cell = row.createCell(8);
            cell.setCellValue("成长值");
            cell = row.createCell(9);
            cell.setCellValue("积分");
            cell = row.createCell(10);
            cell.setCellValue("余额");
            cell = row.createCell(11);
            cell.setCellValue("签约公司");
            cell = row.createCell(12);
            cell.setCellValue("注册时间");
            cell = row.createCell(13);
            cell.setCellValue("会员状态");

            int i = 1;
            for (EsExportMemberVO vo : list) {
                HSSFRow row1 = sheet.createRow(i);
                row1.createCell(0).setCellValue(vo.getId());
                row1.createCell(1).setCellValue(vo.getName());
                row1.createCell(2).setCellValue(vo.getMobile());
                row1.createCell(3).setCellValue(vo.getNickname());
                row1.createCell(4).setCellValue(vo.getSexText());
                row1.createCell(5).setCellValue(vo.getIdentity());
                row1.createCell(6).setCellValue(vo.getEmail());
                row1.createCell(7).setCellValue(vo.getMemberLevel());
                if (vo.getGrade() == null) {
                    row1.createCell(8).setCellValue("");
                } else {
                    row1.createCell(8).setCellValue(vo.getGrade());
                }
                if (vo.getConsumPoint() == null) {
                    row1.createCell(9).setCellValue("");
                } else {
                    row1.createCell(9).setCellValue(vo.getConsumPoint());
                }
                row1.createCell(10).setCellValue(vo.getMemberBalance());
                row1.createCell(11).setCellValue(vo.getCompany());
                if (vo.getCreateTime() == null) {
                    row1.createCell(12).setCellValue("");
                } else {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String sd = sdf.format(vo.getCreateTime());
                    row1.createCell(12).setCellValue(sd);
                }
                if (vo.getState() == 0) {
                    row1.createCell(13).setCellValue("正常");
                } else if (vo.getState() == 1) {
                    row1.createCell(13).setCellValue("禁用");
                } else {
                    row1.createCell(13).setCellValue("");
                }
                i++;
            }

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            try {
                workbook.write(stream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ByteArrayInputStream inputStream = new ByteArrayInputStream(stream.toByteArray());
            FileDTO fileDTO = new FileDTO();
            fileDTO.setName(".xlsx");
            fileDTO.setStream(inputStream);
            //上传阿里云
            FileVO fileVO = ossUploader.upload(fileDTO, OssFileType.EXCEL);

            return ApiResponse.success(fileVO.getUrl());
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }
}