package com.ringme.cms;

import com.ringme.cms.model.Staff;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class TestReadFIle {
    public static MultipartFile getFileBeforeTest() {
        Path path = Paths.get("C:\\Users\\Admin\\Documents\\workspace-spring-tool-suite-4-4.8.0.RELEASE\\ringcms\\StaffTest.xlsx");
        String name = "StaffTest.xlsx";
        String originalFileName = "StaffTest.xlsx";
        String contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
        byte[] content;

        try {
            content = Files.readAllBytes(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new MockMultipartFile(name, originalFileName, contentType, content);
    }

    public static List<Staff> readFileExcel(MultipartFile multipartFile) throws IOException {
//        multipartFile = getFileBeforeTest();
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
//        System.out.println(staffList);
        return staffList;
    }

    public static void main(String[] args) throws IOException {
        System.out.println(readFileExcel(getFileBeforeTest()));
    }
}
