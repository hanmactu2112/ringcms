package com.ringme.cms;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class main {
    public static void main(String[] args) {
        Workbook workbook = null;
        Sheet sheet = workbook.getSheetAt(0);
    }


//    public MultipartFile getFileBeforeTest(){
//        Path path = Paths.get("C:\\Users\\Internship 5\\Documents\\upload\\File Excel\\FIleUpLoadChuan.xlsx");
//        String name = "FIleUpLoadChuan.xlsx";
//        String originalFileName = "FIleUpLoadChuan.xlsx";
//        String contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"; xsls
//        byte[] content;
//
//        try{
//            content = Files.readAllBytes(path);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        multipartFile = new MockMultipartFile(name,originalFileName,contentType,content);
//        return multipartFile;
//    }
}
