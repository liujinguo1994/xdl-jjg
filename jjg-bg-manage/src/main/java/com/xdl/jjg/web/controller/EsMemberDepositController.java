package com.xdl.jjg.web.controller;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.OSSObject;
import com.xdl.jjg.constant.ApiStatus;
import com.xdl.jjg.model.form.EsImportForm;
import com.xdl.jjg.model.form.EsMemberDepositQueryForm;
import com.xdl.jjg.model.form.EsUpdateBalanceForm;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.response.web.ApiPageResponse;
import com.xdl.jjg.response.web.ApiResponse;
import com.xdl.jjg.shiro.oath.ShiroKit;
import com.xdl.jjg.shiro.oath.ShiroUser;
import com.xdl.jjg.util.BeanUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisCluster;

import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * <p>
 * 前端控制器-会员余额明细
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
@RestController
@RequestMapping("/esMemberDeposit")
@Api(value = "/esMemberDeposit", tags = "会员余额明细")
public class EsMemberDepositController {

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
    private IEsMemberDepositService iesMemberDepositService;

    @Autowired
    private IEsMemberService iesMemberService;

    @Autowired
    private JedisCluster jedisCluster;


    @ApiOperation(value = "分页查询会员账户余额明细列表", response = EsMemberDepositVO.class)
    @GetMapping(value = "/getMemberDepositList")
    @ResponseBody
    public ApiResponse getMemberDepositList(@Valid EsMemberDepositQueryForm form) {
        EsQueryAdminMemberDepositDTO dto = new EsQueryAdminMemberDepositDTO();
        BeanUtil.copyProperties(form, dto);
        DubboPageResult<EsMemberDepositDO> result = iesMemberDepositService.getAdminMemberDepositList(dto, form.getPageSize(), form.getPageNum());
        if (result.isSuccess()) {
            List<EsMemberDepositDO> data = result.getData().getList();
            List<EsMemberDepositVO> esMemberDepositVOList = BeanUtil.copyList(data, EsMemberDepositVO.class);
            return ApiPageResponse.pageSuccess(result.getData().getTotal(), esMemberDepositVOList);
        } else {
            return ApiPageResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "调整账户余额")
    @PutMapping(value = "/updateBalance")
    @ResponseBody
    public ApiResponse updateBalance(@Valid @RequestBody EsUpdateBalanceForm form) {
        //只有超级管理员才能操作
        boolean b = checkRole();
        if (!b) {
            return ApiResponse.fail(2000, "只有超级管理员才能操作!");
        }
        EsMemberDTO esMemberDTO = new EsMemberDTO();
        BeanUtil.copyProperties(form, esMemberDTO);
        DubboResult result = iesMemberService.updateMemberInfo(esMemberDTO);
        if (result.isSuccess()) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }

    }

    //获取用户是否存在操作余额功能的权限
    private boolean checkRole() {
        ShiroUser shiroUser = ShiroKit.getUser();
        Integer isAdmin = shiroUser.getIsAdmin();
        boolean flag = false;
        if (isAdmin == 1) {//只有超级管理员才能操作
            flag = true;
        } else {
            flag = false;
        }
        return flag;
    }

   /* @ApiOperation(value = "批量导入会员余额")
    @PostMapping(value = "/importMemberDeposit")
    @ResponseBody
    public ApiResponse importMemberDeposit(HttpServletRequest request) {
        //只有超级管理员才能操作
        boolean b = checkRole();
        if (!b){
            return ApiResponse.fail(2000, "只有超级管理员才能操作!");
        }
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile file = multipartRequest.getFile("filename");
        if (file.isEmpty()) {
            return ApiResponse.fail(2001, "文件不能为空!");
        }
        List<List<Object>> list = null;
        try {
            InputStream inputStream = file.getInputStream();
            //解析Excel文件
            list = ExcelUtil.parseExcel(inputStream, file.getOriginalFilename());
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.fail(2002, "解析Excel文件出错!");
        }

        //有异常的数据集合
        List<EsExceptionMemberDepositVO> exceptionVOList = new ArrayList();
        //无异常会员集合
        List<EsAdminDepositDTO> memberDTOList = new ArrayList();
        //导入成功的条数
        int successCount = 0;
        //导入异常的条数
        int exceptionCount = 0;

        for (int i = 0; i < list.size(); i++) {
            List<Object> rowList = list.get(i);
            //跳过空行
            if (String.valueOf(rowList.get(0)).trim().isEmpty()&&
                    String.valueOf(rowList.get(1)).trim().isEmpty()) {
                continue;
            }
            //存在空的单元格
            if (rowList.size() < 2){
                return ApiResponse.fail(2003, "存在空的单元格");
            }

            DubboResult<EsMemberDO> result = iesMemberService.getMemberInfoByNameOrMobile(String.valueOf(rowList.get(0)));
            if (!result.isSuccess()){
                return ApiResponse.fail(2004, "查询会员信息异常");
            }
            EsMemberDO memberDO = result.getData();
            //会员为空跳过
            if(memberDO == null){
                EsExceptionMemberDepositVO exceptionVO = new EsExceptionMemberDepositVO();
                exceptionVO.setMobile(String.valueOf(rowList.get(0)));
                //Object类型转Double类型
                double v = Double.parseDouble(rowList.get(1).toString());
                exceptionVO.setMemberBalance(v);
                exceptionVOList.add(exceptionVO);
                exceptionCount++;
                continue;
            }

            EsAdminDepositDTO memberDTO = new EsAdminDepositDTO();
            memberDTO.setId(memberDO.getId());
            memberDTO.setMobile(String.valueOf(rowList.get(0)));
            //Object类型转Double类型
            double v = Double.parseDouble(rowList.get(1).toString());
            memberDTO.setMemberBalance(v);
            memberDTOList.add(memberDTO);
            successCount++;
        }

        String message ="导入成功"+successCount+"条,导入失败"+exceptionCount+"条";

        //批量修改会员余额
        if (CollectionUtils.isNotEmpty(memberDTOList)) {
            iesMemberService.batchUpdateMemberDeposit(memberDTOList);
        }
        //将有异常数据放入缓存
        if (CollectionUtils.isNotEmpty(exceptionVOList)) {
            String s = JsonUtil.objectToJson(exceptionVOList);
            jedisCluster.set(CachePrefix.EXCEPTION_MEMBER_DEPOSIT.getPrefix(),s);

            return ApiResponse.fail(5201314, message);
        }
        return ApiResponse.success(message);
    }

    @ApiOperation(value = "导出异常会员余额信息")
    @GetMapping(value = "/exportExceptionDepositInfo")
    public ApiResponse exportExceptionDepositInfo(HttpServletResponse response) {
        String fileName = null;
        response.setContentType("application/msexcel;charset=UTF-8");
        try {
            fileName = "异常会员余额信息" + new String((new SimpleDateFormat("yyyy-MM-dd").format(new Date())).getBytes(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return ApiResponse.fail(1001, "设置文件名出错!");
        }
        try {
            response.setHeader("Content-disposition", "attachment; filename=" + new String(fileName.getBytes("UTF-8"), "ISO8859-1") + ".xls");
        } catch (UnsupportedEncodingException e) {
            return ApiResponse.fail(1002, "设置响应头出错!");
        }

        String s = jedisCluster.get(CachePrefix.EXCEPTION_MEMBER_DEPOSIT.getPrefix());
        List<EsExceptionMemberDepositVO> list = JsonUtil.jsonToList(s, EsExceptionMemberDepositVO.class);

        // 创建Excel
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 获得列名样式
        HSSFCellStyle titleStyle = ExcelUtil.getCellStyle(workbook, "宋体", (short) 14, HSSFCellStyle.ALIGN_CENTER);
        // 获得列数据样式
        HSSFCellStyle cellStyle = ExcelUtil.getCellStyle(workbook, "宋体", (short) 12, HSSFCellStyle.ALIGN_CENTER);

        int row = 0;
        // 生成Sheet
        HSSFSheet hssfSheet = workbook.createSheet();
        //列名
        String[] cellName = {"手机号", "余额"};
        //设置列名
        ExcelUtil.setCellName(hssfSheet, row, cellName, titleStyle);
        row = 1;
        if (CollectionUtils.isNotEmpty(list)) {
            for (int i = 0; i < list.size(); i++) {
                EsExceptionMemberDepositVO memberDepositVO = list.get(i);
                HSSFRow hssfRow = hssfSheet.createRow(row + i);
                //手机号
                ExcelUtil.setCell(hssfRow, 0, String.valueOf(memberDepositVO.getMobile()), cellStyle, 0);
                //余额
                ExcelUtil.setCell(hssfRow, 1, String.valueOf(memberDepositVO.getMemberBalance()), cellStyle, 0);
            }
            //sheet页中列宽度自适应
            ExcelUtil.cellSelfAdaption(hssfSheet, 2);
        }
        try {
            OutputStream out = new BufferedOutputStream(response.getOutputStream());
            workbook.write(out);
            out.flush();
            out.close();
        } catch (Exception e) {
            return ApiResponse.fail(1003, "文档操作错误!");
        }
        return ApiResponse.success();
    }*/

    @ApiOperation(value = "批量导入会员余额", response = EsImportBalanceVO.class)
    @PostMapping(value = "/importMemberDeposit")
    @ResponseBody
    public ApiResponse importMemberDeposit(@RequestBody @Valid EsImportForm form) {
        //只有超级管理员才能操作
        boolean b = checkRole();
        if (!b) {
            return ApiResponse.fail(2000, "只有超级管理员才能操作!");
        }
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
            DubboResult<EsImportBalanceVO> result = iesMemberService.importBalance(file.getBytes());
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

}

