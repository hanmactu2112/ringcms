package com.ringme.cms.excelhelper;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelReadFile {
//    MultipartFile.getInputStream


    public static List<String> readFileExcel(InputStream inputStream) throws IOException {
        List<String> listName = new ArrayList<>();
        try {
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();

            int rowIndex = 0;
            aa:
            while (rows.hasNext()) {
                Row currentRow = rows.next();
                if (rowIndex == 0) {
                    rowIndex++;
                    continue;
                }

                Iterator<Cell> cellsInRow = currentRow.iterator();
                int cellIndex = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();

                    switch (cellIndex) {
                        case 0:
                            if (currentCell.getCellType().equals(CellType.STRING)) {
                                String username = currentCell.getStringCellValue();
                                listName.add(username);
                            } else {
                                throw new IOException();
                            }
                            break;

                        default:
                            break;
                    }
                    cellIndex++;
                    if(currentCell.getStringCellValue().equals("")){
                        break aa;
                    }
                }
            }

        } catch (Exception e) {
            throw new IOException();
        }

        return listName;
    }
}
