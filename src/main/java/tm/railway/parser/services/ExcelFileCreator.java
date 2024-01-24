package tm.railway.parser.services;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import tm.railway.parser.dictionaries.Dictionaries;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExcelFileCreator {

    private final Dictionaries dictionaries;

    public byte[] makeExcelFile(List<String[]> lastOps) throws ParseException {

        try(Workbook workbook = new XSSFWorkbook()){

            SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
            Date currentDate = new Date();
            Date limitDate = format.parse("10.08.2025");

            String currentDateStr = format.format(currentDate);

            Sheet mainSheet = workbook.createSheet(currentDateStr);
            mainSheet.setDisplayGridlines(true);

            if (new Date().before(limitDate)) {
                createHeader(workbook, mainSheet);
                createDataRowsForSheet(workbook, lastOps, dictionaries);
                sizingColumns(workbook);

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                workbook.write(outputStream);
                return outputStream.toByteArray();
            }

            return null;

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }


    private static void createHeader(Workbook workbook, Sheet sheet){

        sheet.createFreezePane( 0, 1);
        Row header = sheet.createRow(0);

        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
        headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerCellStyle.setAlignment(HorizontalAlignment.CENTER);


        XSSFFont font = (XSSFFont) workbook.createFont();
        font.setBold(true);
        font.setFontName("Courier NEW");
        headerCellStyle.setFont(font);


        Cell cell = header.createCell(0);
        cell.setCellValue("№ Вагона");
        cell.setCellStyle(headerCellStyle);

        cell = header.createCell(1);
        cell.setCellValue("Собственник");
        cell.setCellStyle(headerCellStyle);

        cell = header.createCell(2);
        cell.setCellValue("Станция");
        cell.setCellStyle(headerCellStyle);

        cell = header.createCell(3);
        cell.setCellValue("Операция");
        cell.setCellStyle(headerCellStyle);

        //ROLLOVER
        cell = header.createCell(4);
        cell.setCellValue("Дата");
        cell.setCellStyle(headerCellStyle);

//        //ROLLOVER
//        cell = header.createCell(5);
//        cell.setCellValue("Год");
//        cell.setCellStyle(headerCellStyle);

        cell = header.createCell(5);
        cell.setCellValue("Время");
        cell.setCellStyle(headerCellStyle);

        cell = header.createCell(6);
        cell.setCellValue("Состояние");
        cell.setCellStyle(headerCellStyle);

        cell = header.createCell(7);
        cell.setCellValue("Станция назначения");
        cell.setCellStyle(headerCellStyle);

        cell = header.createCell(8);
        cell.setCellValue("Индекс поезда");
        cell.setCellStyle(headerCellStyle);

        cell = header.createCell(9);
        cell.setCellValue("Аренда");
        cell.setCellStyle(headerCellStyle);

    }


    private static String getFullStanName(String input, List<String> stanCodesNames) {

        //System.out.println("INPUT  : " + input);

        String streamResult = stanCodesNames.stream().filter(it -> it.substring(0, 13).contains(input)).findFirst().orElse(null);

        //System.out.println("RESULT : " + streamResult);
        if(streamResult != null) {
            return streamResult.substring(12).trim();
        } else {
            return input.trim();
        }

    }

    private static String getOperName(String input, List<String> operNames) {

        String streamResult = operNames.stream().filter(it -> it.contains(input)).findFirst().orElse(null);

        if(streamResult != null) {
            return streamResult.substring(8).trim();
        } else {
            return input.trim();
        }

    }

    private static String getArenda(String input, List<String> arenda) {

        String streamResult = arenda.stream().filter(it -> it.contains(input)).findFirst().orElse(null);

        if(streamResult != null) {
            return streamResult.substring(8).trim();
        } else {
            return "";
        }

    }

    private static String getTime(String input) {

        String[] time = input.split("-");
        return time[0] + ":" + time[1];
    }


    private static void createDataRowsForSheet(Workbook workbook, List<String[]> lastOps, Dictionaries dictionaries){

        XSSFFont font = (XSSFFont) workbook.createFont();
        //font.setBold(true);
        font.setFontName("Courier NEW");

        CellStyle cellStyleDefault = workbook.createCellStyle();
        cellStyleDefault.setFont(font);

        CellStyle cellStyleForRightAligment = workbook.createCellStyle();
        cellStyleForRightAligment.setAlignment(HorizontalAlignment.RIGHT);
        cellStyleForRightAligment.setFont(font);

        CellStyle cellStyleForCenterAligment = workbook.createCellStyle();
        cellStyleForCenterAligment.setAlignment(HorizontalAlignment.CENTER);
        cellStyleForCenterAligment.setFont(font);

        int nextRow;

        Sheet sheet;

        for (String[] lastOp : lastOps) {

            sheet = workbook.getSheetAt(0);
            nextRow = sheet.getLastRowNum();
            Row dataRow = sheet.createRow(nextRow + 1);

            //NV
            dataRow.createCell(0).setCellValue(lastOp[0]);
            dataRow.getCell(0).setCellStyle(cellStyleDefault);

            //KOD SOBSTV
            dataRow.createCell(1).setCellValue(lastOp[1]);
            dataRow.getCell(1).setCellStyle(cellStyleForCenterAligment);

            //ST FORM
            dataRow.createCell(2).setCellValue(getFullStanName(lastOp[2], dictionaries.getStanCodesNames()));
            dataRow.getCell(2).setCellStyle(cellStyleDefault);

            //KOD OP
            dataRow.createCell(3).setCellValue(getOperName(lastOp[3], dictionaries.getOperNames()));
            dataRow.getCell(3).setCellStyle(cellStyleDefault);

            //ROLLOVER
            //DATA OP
            dataRow.createCell(4).setCellValue(lastOp[5] + ".20" + lastOp[4]);
            dataRow.getCell(4).setCellStyle(cellStyleDefault);

//            //ROLLOVER
//            //GOD OP
//            dataRow.createCell(5).setCellValue(lastOp[4]);
//            dataRow.getCell(5).setCellStyle(cellStyleDefault);


            //VREM OP
            dataRow.createCell(5).setCellValue(getTime(lastOp[6]));
            dataRow.getCell(5).setCellStyle(cellStyleDefault);

            //SOST
            dataRow.createCell(6).setCellValue(lastOp[7]);
            dataRow.getCell(6).setCellStyle(cellStyleDefault);

            //ST NAZN
            dataRow.createCell(7).setCellValue(getFullStanName(lastOp[8], dictionaries.getStanCodesNames()));
            dataRow.getCell(7).setCellStyle(cellStyleDefault);

            //INDX
            dataRow.createCell(8).setCellValue(lastOp[9]);
            dataRow.getCell(8).setCellStyle(cellStyleDefault);

            //ARENDA
            dataRow.createCell(9).setCellValue(getArenda(lastOp[0], dictionaries.getArenda()));
            dataRow.getCell(9).setCellStyle(cellStyleForCenterAligment);


        }

    }

    private static void sizingColumns(Workbook wb) {

        Sheet sheet = wb.getSheetAt(0);
        for (int i = 0; i < 10; i++) {
            sheet.autoSizeColumn(i);
        }

    }

}
