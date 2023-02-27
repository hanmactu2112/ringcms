package com.ringme.cms.excelhepler;

import com.ringme.cms.model.Staff;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExcelReadFile {

    public static List<Staff> readFileExcel(MultipartFile multipartFile) throws IOException {
        List<Staff> staffList = new ArrayList<>();
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(multipartFile.getInputStream());
            XSSFSheet sheet = workbook.getSheetAt(0);
            int rowIndex = 0;
            for (Row row : sheet) {
                if (rowIndex == 0) {
                    if (row.getCell(0).getStringCellValue().equals("Id") &&
                            row.getCell(1).getStringCellValue().equals("Image") &&
                            row.getCell(2).getStringCellValue().equals("UserName") &&
                            row.getCell(3).getStringCellValue().equals("StaffName") &&
                            row.getCell(4).getStringCellValue().equals("Phone") &&
                            row.getCell(5).getStringCellValue().equals("sUserId")) {
                        continue;
                    }
                }else{
                    throw new IOException();
                }
                if (!row.getCell(0).getStringCellValue().equals("") &&
                        !row.getCell(1).getStringCellValue().equals("") &&
                        !row.getCell(2).getStringCellValue().equals("") &&
                        !row.getCell(3).getStringCellValue().equals("") &&
                        !row.getCell(4).getStringCellValue().equals("") &&
                        !row.getCell(5).getStringCellValue().equals("")) {
                    Staff staff = new Staff();
                    staff.setId(Long.valueOf(row.getCell(0).getStringCellValue()));
                    staff.setImage(row.getCell(1).getStringCellValue());
                    staff.setUserName(row.getCell(2).getStringCellValue());
                    staff.setStaffName((row.getCell(3).getStringCellValue()));
                    staff.setPhone((row.getCell(4).getStringCellValue()));
                    staff.setSUserId((row.getCell(5).getStringCellValue()));

                    staffList.add(staff);
                } else {
                    return staffList;
                }
                rowIndex++;
            }
        } catch (Exception exception) {
            throw new IOException();
        }
        return staffList;
    }
}
