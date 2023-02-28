package com.ringme.cms.excelhelper;

import com.ringme.cms.model.Staff;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Random;

public class ExcelWriteFile {

    //    static String[] name_of_headers = {"username"};
    static String sheet_name = "Queue Error";
//    private static XSSFWorkbook workbook;
//    private static XSSFSheet sheet;

    public static String createExcel( Map<Staff, Boolean> staffList) {

        try (XSSFWorkbook workbook = new XSSFWorkbook();) {

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

            XSSFSheet sheet = workbook.createSheet("Error");
            XSSFRow header = sheet.createRow(0);

            header.createCell(0).setCellValue("UserName");
            header.createCell(1).setCellValue("StaffName");
            header.createCell(2).setCellValue("Phone");
            header.createCell(3).setCellValue("sUserId");

            int rowIndex = 1;
            for (Map.Entry<Staff, Boolean> staff : staffList.entrySet()) {


                XSSFRow row = sheet.createRow(rowIndex);

                if (staff.getValue()) {
                    row.createCell(0, CellType.STRING).setCellValue(staff.getKey().getUserName());
                } else {
                    row.createCell(0, CellType.STRING).setCellValue(staff.getKey().getUserName());
                    row.getCell(0).setCellStyle(style);
                }

                if (staff.getValue()) {
                    row.createCell(1, CellType.STRING).setCellValue(staff.getKey().getStaffName());
                } else {
                    row.createCell(1, CellType.STRING).setCellValue(staff.getKey().getStaffName());
                    row.getCell(1).setCellStyle(style);
                }

                if (staff.getValue()) {
                    row.createCell(2, CellType.STRING).setCellValue(staff.getKey().getPhone());
                } else {
                    row.createCell(2, CellType.STRING).setCellValue(staff.getKey().getPhone());
                    row.getCell(2).setCellStyle(style);
                }

                if (staff.getValue()) {
                    row.createCell(3, CellType.STRING).setCellValue(staff.getKey().getSUserId());
                } else {
                    row.createCell(3, CellType.STRING).setCellValue(staff.getKey().getSUserId());
                    row.getCell(3).setCellStyle(style);
                }
                rowIndex++;
            }
            String fileName = "newfile";
            String uploadDir = "F:\\CMS\\execlcms";
            Path fileNameAndPath = Paths.get(uploadDir,fileName);
            Random random = new Random();
            int t = 1;
            while (t==1){
                fileName = String.format("%s%s",System.currentTimeMillis(),random.nextInt(100000)+".xlsx");
                fileNameAndPath = Paths.get(uploadDir,fileName);
                boolean exists = Files.exists(fileNameAndPath);
                if (!exists) t=0;
            }
            File file = new File(uploadDir,fileName);
            FileOutputStream fos = new FileOutputStream(file);
            workbook.write(fos);
            fos.close();
            return fileName;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
