package com.xdl.jjg.util;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * excel工具
 */
public class ExcelUtil {

    /**
     * 获取列样式
     * @param workbook Excel表
     * @param fontName 字体名称
     * @param fontSize 字体大小
     * @param align    字体位置
     * @return
     */
    public static HSSFCellStyle getCellStyle(HSSFWorkbook workbook, String fontName, short fontSize, short align){
        HSSFFont font = workbook.createFont();
        // 设置字体名称
        font.setFontName(fontName);
        // 设置字体大小
        font.setFontHeightInPoints(fontSize);

        //
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        // 设置边框:
        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        // 设置居中:
        cellStyle.setAlignment(align);
        cellStyle.setFont(font);

        return cellStyle;
    }

    /**
     * 设置标题
     * @param workbook   Excel表
     * @param title     标题名称
     * @param row       起始行
     * @param cellStyle 单元格样式
     * @param count     需要合并的单元格数
     * @return
     */
    public static HSSFSheet setTitleCell(HSSFWorkbook workbook, String title, Integer row, HSSFCellStyle cellStyle
                                        , Integer count){
        // 生成Sheet
        HSSFSheet sheet = workbook.createSheet();
        // 创建行
        HSSFRow hssfRow = sheet.createRow(row);
        // 创建列
        HSSFCell hssfCell = hssfRow.createCell(0);
        // 设置标题
        hssfCell.setCellValue(title);
        // 设置标题格式
        hssfCell.setCellStyle(cellStyle);

        for (int i = 1; i < count; i++) {
            // 创建列
            hssfCell = hssfRow.createCell(i);
            // 设置标题格式
            hssfCell.setCellStyle(cellStyle);
        }
        return sheet;
    }

    /**
     * 设置列值和列样式
     * @param hssfRow   行
     * @param cellNum   列号
     * @param cellValue 列值
     * @param cellStyle 列样式
     * @param count     需要合并的单元格数
     */
    public static void setCell(HSSFRow hssfRow, Integer cellNum, String cellValue, HSSFCellStyle cellStyle, Integer count) {
        HSSFCell hssfCell = hssfRow.createCell(cellNum);
        hssfCell.setCellValue(cellValue);
        hssfCell.setCellStyle(cellStyle);

        // 新增合并单元格所需内容以外的多余的单元格
        for (int i = 1; i < count; i++) {
            // 创建列
            hssfCell = hssfRow.createCell(i);
            // 设置标题格式
            hssfCell.setCellStyle(cellStyle);
        }
    }

    /**
     * 设置列名
     * @param sheet     sheet
     * @param row       行号
     * @param cellName  列名称
     * @param cellStyle 列样式
     */
    public static void setCellName(HSSFSheet sheet, Integer row, String[] cellName, HSSFCellStyle cellStyle){
        Integer size = cellName.length;
        HSSFRow hssfRow = sheet.createRow(row);
        for(int i = 0; i < size; i++) {
            HSSFCell hssfCell = hssfRow.createCell(i);
            hssfCell.setCellValue(cellName[i]);
            hssfCell.setCellStyle(cellStyle);
        }
    }

    /**
     * sheet页中列自适应
     * @param sheet sheet页
     * @param count 列数
     */
    public static void cellSelfAdaption(HSSFSheet sheet, Integer count){
        for(int i = 0; i < count; i++) {
            // 宽度自适应
            sheet.autoSizeColumn((short)i, true);
        }
    }

    /**
     * 列合并单元格，并重新设置样式
     * @param workbook  工作簿
     * @param hssfSheet sheet页
     * @param firstRow  起始行
     * @param lastRow   结束行
     * @param cellNum   需要合并的列
     * @param colNum    不改变样式的列号， 没有为-1
     */
    public static void cellMerged(HSSFWorkbook workbook, HSSFSheet hssfSheet, Integer firstRow, Integer lastRow, int[] cellNum, int colNum){
        // 起始行 < 结束行的时候
        if (firstRow < lastRow) {
            // 当前行
            for(int col : cellNum){
                hssfSheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, col, col));
            }
        }
    }

    /**
     * 解析Excel文件
     *
     * @param in
     * @param fileName
     */
    public static List<List<Object>> parseExcel(InputStream in, String fileName) throws Exception {
        List list = null;
        Workbook work = null;
        list = new ArrayList<>();
        //创建Excel工作薄
        work = getWorkbook(in, fileName);
        if (null == work) {
            throw new Exception("创建Excel工作薄为空！");
        }
        Sheet sheet = null;
        Row row = null;
        Cell cell = null;

        for (int i = 0; i < work.getNumberOfSheets(); i++) {
            sheet = work.getSheetAt(i);
            if (sheet == null) {
                continue;
            }

            for (int j = sheet.getFirstRowNum(); j <= sheet.getLastRowNum(); j++) {
                row = sheet.getRow(j);
                if (row == null || row.getFirstCellNum() == j) {
                    continue;
                }

                List<Object> li = new ArrayList<>();
                for (int y = row.getFirstCellNum(); y < row.getLastCellNum(); y++) {
                    String value = "";
                    cell = row.getCell(y);
                    if (cell==null){
                        li.add("");
                    }else {
                        switch (cell.getCellType()) {
                            // 数字
                            case HSSFCell.CELL_TYPE_NUMERIC:
                                //如果为时间格式的内容
                                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                                    //注：format格式 yyyy-MM-dd hh:mm:ss 中小时为12小时制，若要24小时制，则把小h变为H即可，yyyy-MM-dd HH:mm:ss
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    value = sdf.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue())).toString();
                                    li.add(value);
                                    break;
                                } else {
                                    //value = String.valueOf(cell.getNumericCellValue());
                                    DecimalFormat df = new DecimalFormat("0");
                                    value = df.format(cell.getNumericCellValue());
                                    String[] split = value.split("\\.");
                                    //整型不保留小数部分
                                    if (split.length > 1 && split[1].length() == 1 && split[1].equals("0")) {
                                        value = split[0];
                                        li.add(value);
                                        break;
                                    }
                                    li.add(value);
                                    break;
                                }
                                // 字符串
                            case HSSFCell.CELL_TYPE_STRING:
                                value = cell.getStringCellValue();
                                li.add(value);
                                break;
                            // Boolean
                            case HSSFCell.CELL_TYPE_BOOLEAN:
                                value = cell.getBooleanCellValue() + "";
                                li.add(value);
                                break;
                            // 公式
                            case HSSFCell.CELL_TYPE_FORMULA:
                                value = cell.getCellFormula() + "";
                                li.add(value);
                                break;
                            // 空值
                            case HSSFCell.CELL_TYPE_BLANK:
                                value = "";
                                li.add(value);
                                break;
                            // 故障
                            case HSSFCell.CELL_TYPE_ERROR:
                                value = "非法字符";
                                li.add(value);
                                break;
                            default:
                                value = "未知类型";
                                li.add(value);
                                break;
                        }
                    }
                }
                list.add(li);
            }
        }
        return list;

    }


    /**
     * 判断文件格式
     *
     * @param inStr
     * @param fileName
     * @return
     * @throws Exception
     */
    public static Workbook getWorkbook(InputStream inStr, String fileName) throws Exception {
        Workbook workbook = null;
        String fileType = fileName.substring(fileName.lastIndexOf("."));
        if (".xls".equals(fileType)) {
            workbook = new HSSFWorkbook(inStr);
        } else if (".xlsx".equals(fileType)) {
            workbook = new XSSFWorkbook(inStr);
        } else {
            throw new Exception("请上传excel文件！");
        }
        return workbook;
    }

}
