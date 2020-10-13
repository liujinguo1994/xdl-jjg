package com.xdl.jjg.model.domain;

import lombok.Data;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.Serializable;

/**
 * @ClassName: ExcelDO
 * @Description: Excel传输用DO
 * @Author: bamboo  asp.bamboo@gmail.com
 * @Date: 2019/10/14 13:06
 * @Version: 1.0
 */
@Data
public class ExcelDO implements Serializable {

    /**
     * Excel名称
     */
    private String excelName;

    /**
     * Excel文件
     */
    private HSSFWorkbook workbook;
}
