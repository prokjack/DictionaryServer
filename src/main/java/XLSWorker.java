import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

class XLSWorker {
    private XSSFWorkbook workbook;
    private String fileName;

    XLSWorker(String fileName) {
        this.fileName = fileName;
    }

    void writeXLSXFile(ArrayList<Result> parseResult) throws IOException {
        try {
//            String fileName = "C:\\Users\\j2ck\\Documents\\dict.xlsx";
            FileInputStream file = new FileInputStream(fileName);

            workbook = new XSSFWorkbook(file);
            XSSFSheet sheet = workbook.getSheetAt(0);
            Cell cell = null;
            int startCellNumber = getLastUnfilledCell(sheet) + 1;
            XSSFRow row = sheet.getRow(startCellNumber);
            if (Objects.isNull(row)) {
                row = sheet.createRow(startCellNumber);
            }
            setBinyansToRow(parseResult, row);
            file.close();

            FileOutputStream outFile = new FileOutputStream(new File(fileName));
            workbook.write(outFile);
            outFile.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setBinyansToRow(ArrayList<Result> parseResult, XSSFRow row) {
        parseResult.forEach(result -> setBinyanToRow(result, row));
    }

    private void setBinyanToRow(Result result, XSSFRow row) {
        int startCellNumber = getStartCellNumber(Objects.requireNonNull(Binyan.fromString(result.getBinyan())));
        getCell(row, startCellNumber).setCellValue(result.getPresent2());
        getCell(row, startCellNumber).setCellStyle(getStyle(true, false, false, false));
        getCell(row, startCellNumber + 1).setCellValue(result.getPresent1());
        getCell(row, startCellNumber + 1).setCellStyle(getStyle(false,false,false,false));
        getCell(row, startCellNumber + 2).setCellValue(result.getPast2());
        getCell(row, startCellNumber + 2).setCellStyle(getStyle(false,false,false,false));
        getCell(row, startCellNumber + 3).setCellValue(result.getPast1());
        getCell(row, startCellNumber + 3).setCellStyle(getStyle(false,false,false,false));
        getCell(row, startCellNumber + 4).setCellValue(result.getFuture2());
        getCell(row, startCellNumber + 4).setCellStyle(getStyle(false,false,false,false));
        getCell(row, startCellNumber + 5).setCellValue(result.getFuture1());
        getCell(row, startCellNumber + 5).setCellStyle(getStyle(false,false,false,false));
        getCell(row, startCellNumber + 6).setCellValue(result.getInfinitiv());
        getCell(row, startCellNumber + 6).setCellStyle(getStyle(false,false,false,false));
        getCell(row, startCellNumber + 7).setCellValue(result.getTranslation());
        getCell(row, startCellNumber + 7).setCellStyle(getStyle(false,false,false,true));
    }

    private CellStyle getStyle(boolean left, boolean top, boolean bottom, boolean right) {
        XSSFCellStyle style = workbook.createCellStyle();
        style.setBorderLeft(left ? BorderStyle.MEDIUM : BorderStyle.THIN);
        style.setBorderRight(right ? BorderStyle.MEDIUM : BorderStyle.THIN);
        style.setBorderTop(top ? BorderStyle.MEDIUM : BorderStyle.THIN);
        style.setBorderBottom(bottom? BorderStyle.MEDIUM : BorderStyle.THIN);
        return style;
    }

    private XSSFCell getCell(XSSFRow row, int cellNumber) {
        XSSFCell cell = row.getCell(cellNumber);
        if (Objects.isNull(cell))
            cell = row.createCell(cellNumber);
        return cell;
    }

    private int getStartCellNumber(Binyan binyan) {
        switch (binyan) {
            case PAAL:
                return 0;
            case PIEL:
                return 8;
            case HIFIL:
                return 40;
            case PUAL:
                return 16;
            case HUFAL:
                return 48;
            case NIFAL:
                return 32;
            case HITPAEL:
                return 24;
            default:
                return 55;
        }
    }

    private int getLastUnfilledCell(XSSFSheet sheet) {
        return sheet.getLastRowNum();
    }
}
