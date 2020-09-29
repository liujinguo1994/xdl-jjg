package com.xdl.jjg.util;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author qiushi
 * @version 1.0
 * @date 2019/7/1 10:24
 */
public class ExcelUtil {

    /**
     * 判断浏览器类型，firefox浏览器做特殊处理，否则下载文件名乱码
     * @param request
     * @param response
     * @param excelName
     * @throws UnsupportedEncodingException
     */
    public static void compatibleFileName(HttpServletRequest request, HttpServletResponse response,String excelName) throws UnsupportedEncodingException {
        String agent = request.getHeader("USER-AGENT").toLowerCase();
        response.setContentType("application/vnd.ms-excel");
        String fileName = excelName;
        String codedFileName = java.net.URLEncoder.encode(fileName, "UTF-8");
        if (agent.contains("firefox")) {
            response.setCharacterEncoding("utf-8");
            response.setHeader("content-disposition", "attachment;filename=" + new String(fileName.getBytes(), "ISO8859-1") + ".xls");
        } else {
            response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xls");
        }

    }

    public static Boolean getaBoolean(HttpServletRequest request, HttpServletResponse response, List<Map<String, Object>> sheetsList , SimpleDateFormat sdf, ExportParams params1, String excelName) {
        // 单sheet或多sheet 只需要更改此处即可
        Workbook workbook = ExcelExportUtil.exportExcel(sheetsList, ExcelType.HSSF) ;
        // 判断数据
        if(workbook == null) {
            return false;
        }
        // 设置excel的文件名称
        // 重置响应对象
        response.reset();
        // 当前日期，用于导出文件名称
        String dateStr = "["+excelName+"-"+sdf.format(new Date())+"]";
        // 指定下载的文件名--设置响应头
        try {
            ExcelUtil.compatibleFileName(request, response, dateStr);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        // 写出数据输出流到页面
        try {
            OutputStream output = response.getOutputStream();
            BufferedOutputStream bufferedOutPut = new BufferedOutputStream(output);
            workbook.write(bufferedOutPut);
            bufferedOutPut.flush();
            bufferedOutPut.close();
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
