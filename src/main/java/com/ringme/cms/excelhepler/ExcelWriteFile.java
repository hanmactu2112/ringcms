package com.ringme.cms.excelhepler;

import com.ringme.cms.model.Staff;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class ExcelWriteFile {
    public void createExcel(HttpServletResponse response, List<Staff> staffList) {
        response.setContentType("");
        response.setHeader("", "");

        try (XSSFWorkbook workbook = new XSSFWorkbook();) {
            XSSFSheet sheet = workbook.createSheet("Error");
            XSSFRow header = sheet.createRow(0);

            header.createCell(0).setCellValue("Id");
            header.createCell(1).setCellValue("Image");
            header.createCell(2).setCellValue("UserName");
            header.createCell(3).setCellValue("StaffName");
            header.createCell(4).setCellValue("Phone");
            header.createCell(5).setCellValue("sUserId");

            int rowIndex = 1;
            for (Staff staff : staffList) {
                XSSFRow row = sheet.createRow(rowIndex);
                row.createCell(0, CellType.STRING).setCellValue(staff.getId());
                row.createCell(1, CellType.STRING).setCellValue(staff.getImage());
                row.createCell(2, CellType.STRING).setCellValue(staff.getUserName());
                row.createCell(3, CellType.STRING).setCellValue(staff.getStaffName());
                row.createCell(4, CellType.STRING).setCellValue(staff.getPhone());
                row.createCell(5, CellType.STRING).setCellValue(staff.getSUserId());
                rowIndex++;
            }
            workbook.write(response.getOutputStream());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
