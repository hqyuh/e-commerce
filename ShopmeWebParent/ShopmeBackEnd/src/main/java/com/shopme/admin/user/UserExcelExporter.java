package com.shopme.admin.user;

import com.shopme.common.entity.User;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class UserExcelExporter extends AbstractExporter{

    public void export(List<User> listUsers,
                       HttpServletResponse response) throws IOException {

        super.setResponseHeader(response, "application/octet-stream", ".xlsx");

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Users");


        XSSFRow row = sheet.createRow(0);
        XSSFCell cell = row.createCell(0);
        cell.setCellValue("User ID");

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);

        workbook.close();
        outputStream.close();

    }

}
