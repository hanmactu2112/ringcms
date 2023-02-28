package com.ringme.cms.excelhelper;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.Map;

public class ExcelWriteFile {

    static String[] name_of_headers = {"username"};
    static String sheet_name = "Queue Error";
    private static XSSFWorkbook workbook;
    private static XSSFSheet sheet;

    public static Workbook write(Map<String,Boolean> listUserName) throws IOException {

        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) 11);
        font.setFontName("Times New Roman");
        font.setItalic(true);
        font.setStrikeout(true);

        CellStyle style;
        style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.RED.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setFont(font);

        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet(sheet_name);
        int row_index = 0;
        for(Map.Entry<String,Boolean> name : listUserName.entrySet()){
            boolean isCorrect = name.getValue();
            String nameC =  name.getKey();
            int cellIndex = 0;
            if(isCorrect){
                Row row = sheet.createRow(row_index);
                row.createCell(cellIndex).setCellValue(nameC);
//                cellIndex.
            }else{
                Row row = sheet.createRow(row_index);
                row.createCell(cellIndex).setCellValue(nameC);
                row.setRowStyle(style);
            }
        }
//        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//        workbook.write(bos);

        return workbook;
//        List<String> list
    }
}
