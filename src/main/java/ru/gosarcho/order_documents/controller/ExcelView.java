/*
package ru.gosarcho.affairs_query.controller;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.web.servlet.view.document.AbstractXlsView;
import Document;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public class ExcelView extends AbstractXlsView {
    @Override
    protected void buildExcelDocument(Map<String, Object> map, Workbook workbook, HttpServletRequest httpServletRequest,
                                      HttpServletResponse httpServletResponse) throws Exception {
        // change the file name
        httpServletResponse.setHeader("Content-Disposition", "attachment; filename=\"export.xls\"");

        @SuppressWarnings("unchecked")
        List<Document> documents = (List<GatewayManage>) map.get("documents");

        // create excel xls sheet
        Sheet sheet = workbook.createSheet("Affairs Details");
        sheet.setDefaultColumnWidth(30);

        // create style for header cells
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontName("Arial");
        style.setFillForegroundColor(HSSFColor.BLUE.index);
        font.setColor(HSSFColor.BLACK.index);
        style.setFont(font);


        // create header row
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Фонд");
        header.createCell(1).setCellValue("Опись");
        header.createCell(2).setCellValue("Дело");
        header.createCell(3).setCellValue("Читатель");
        header.createCell(4).setCellValue("Исполнитель");
        header.createCell(5).setCellValue("Дата");
        for (int i=0;i<6;i++) {
            header.getCell(i).setCellStyle(style);
        }

        int rowCount = 1;
        for (Document affair : documents) {
            Row userRow = sheet.createRow(rowCount++);
            gatewayRow.createCell(0).setCellValue(user.getFirstName());
            gatewayRow.createCell(1).setCellValue(gateway.getLastName());
            gatewayRow.createCell(2).setCellValue(gateway.getNumber());
            gatewayRow.createCell(3).setCellValue(gateway.getAge());

        }

    }
}
*/
