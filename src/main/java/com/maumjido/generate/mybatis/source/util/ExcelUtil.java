package com.maumjido.generate.mybatis.source.util;

import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.ShapeTypes;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFSimpleShape;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtil {
  private XSSFSheet sheet;
  private int rowIndex = 0;
  private int maxCols = 0;
  private XSSFWorkbook workbook;
  private int offsetCol = 0;

  public ExcelUtil(String sheetName) {
    workbook = new XSSFWorkbook();
    sheet = workbook.createSheet(sheetName);
  }

  public void setOffset(int col, int row) {
    offsetCol = col;
    rowIndex = row;
  }

  public void addRow(List<String> rows) {
    addRow(null, (short) 0, rows);
  }

  public void addRow(String backgroundColor, List<String> rows) {
    addRow(backgroundColor, (short) 0, rows);
  }

  public int getRowIndex() {
    return rowIndex;
  }

  public void setCellWidth(int cellnum, int width) {
    sheet.setColumnWidth(cellnum, width);
  }

  public void setCell(int cellnum, int rownum, XSSFCellStyle cellStyle, String cellValue) {
    XSSFRow row = sheet.getRow(rownum);
    if (row == null) {
      row = sheet.createRow(rownum);
    }

    XSSFCell cell = row.getCell(cellnum);
    if (cell == null) {
      cell = row.createCell(cellnum, XSSFCell.CELL_TYPE_STRING);
    }
    cell.setCellStyle(cellStyle);
    cell.setCellValue(cellValue);
  }

  public void addRow(String backgroundColor, short boldweight, List<String> cellStrings) {
    XSSFRow header = sheet.createRow(rowIndex++);
    int cellIndex = offsetCol;
    for (String value : cellStrings) {
      XSSFCell cell = header.createCell(cellIndex++, XSSFCell.CELL_TYPE_STRING);
      cell.setCellValue(value);
      cell.setCellStyle(createCellStyle(backgroundColor, boldweight));
    }
    if (maxCols < cellIndex) {
      maxCols = cellIndex;
    }
  }

  public void addRow(List<Map<String, String>> style, List<String> cellStrings) {
    XSSFRow header = sheet.createRow(rowIndex++);
    int cellIndex = offsetCol;
    for (String value : cellStrings) {
      int index = cellIndex - offsetCol;
      XSSFCell cell = header.createCell(cellIndex++, XSSFCell.CELL_TYPE_STRING);
      cell.setCellValue(value);
      String backgroundColor = null;
      short boldweight = 0;
      if (style.size() > index) {
        Map<String, String> styleMap = style.get(index);
        backgroundColor = styleMap.get("backgroundColor");
        if (styleMap.containsKey("boldweight")) {
          boldweight = Short.parseShort(styleMap.get("boldweight"));
        }
      }
      cell.setCellStyle(createCellStyle(backgroundColor, boldweight));
    }
    if (maxCols < cellIndex) {
      maxCols = cellIndex;
    }
  }

  public XSSFCellStyle createCellStyle(String backgroundColor, short boldweight) {
    XSSFCellStyle cellStyle = sheet.getWorkbook().createCellStyle();
    cellStyle.setAlignment(HorizontalAlignment.CENTER);
    cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
    if (backgroundColor != null) {
      cellStyle.setFillForegroundColor(new XSSFColor(hexToByteArray(backgroundColor)));
      cellStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
    }
    setSolidBorder(cellStyle);

    if (boldweight != 0) {
      Font headerFont = this.sheet.getWorkbook().createFont();
      headerFont.setBoldweight(boldweight);
      cellStyle.setFont(headerFont);
    }
    return cellStyle;
  }

  public void setFontHeight(int cellnum, int rownum, int height) {
    sheet.getRow(rownum).getCell(cellnum).getCellStyle().getFont().setFontHeight(height);
  }

  public void setCellAlignment(int cellnum, int rownum, HorizontalAlignment align) {
    sheet.getRow(rownum).getCell(cellnum).getCellStyle().setAlignment(align);
  }

  public void setCellWrapText(int cellnum, int rownum, boolean b) {
    XSSFRow row = sheet.getRow(rownum);
    XSSFCellStyle rowStyle = row.getRowStyle();
    if (rowStyle == null) {
      XSSFCellStyle cellStyle = sheet.getWorkbook().createCellStyle();
      cellStyle.setWrapText(b);
      row.setRowStyle(cellStyle);
    } else {
      rowStyle.setWrapText(b);
    }
    row.getCell(cellnum).getCellStyle().setWrapText(b);
  }

  // hex to byte[]
  public byte[] hexToByteArray(String hex) {
    if (hex == null || hex.length() == 0) {
      return null;
    }

    byte[] ba = new byte[hex.length() / 2];
    for (int i = 0; i < ba.length; i++) {
      ba[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
    }
    return ba;
  }

  public void setSolidBorder(XSSFCellStyle cellStyle) {
    cellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
    cellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
    cellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
    cellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
    cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
    cellStyle.setBorderTop(CellStyle.BORDER_THIN);
    cellStyle.setBorderRight(CellStyle.BORDER_THIN);
    cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
  }

  public void write(OutputStream outputStream) throws IOException {
    // adjust column width to fit the content
    for (int i = 0; i < maxCols; i++) {
      sheet.autoSizeColumn(i);
      sheet.setColumnWidth(i, sheet.getColumnWidth(i) + 1500);
    }
    for (int i = 0; i < offsetCol; i++) {
      setCellWidth(i, 900);
    }
    this.sheet.getWorkbook().write(outputStream);
    this.workbook.close();
  }

  public void merge(int firstCol, int firstRow, int lastCol, int lastRow) {
    // 셀 병합
    sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, firstCol, lastCol)); // 병합
  }

  public void drawRect(int rownum, int scolnum, int ecolnum, int dx1, int dx2) {
    XSSFDrawing patriarch = sheet.createDrawingPatriarch();
    XSSFClientAnchor a = new XSSFClientAnchor();
    a.setCol1(scolnum);
    a.setRow1(rownum);
    a.setDx1(pxToEmu(dx1));
    a.setDy1(pxToEmu(5));
    a.setDx2(pxToEmu(dx2));
    a.setDy2(pxToEmu(-5));
    a.setRow2(rownum + 1);
    a.setCol2(ecolnum);

    XSSFSimpleShape shape1 = patriarch.createSimpleShape(a);
    shape1.setShapeType(ShapeTypes.RECT);
    int red = 0, green = 0, blue = 0;
    red = Integer.parseInt("f0", 16);
    green = Integer.parseInt("ad", 16);
    blue = Integer.parseInt("4e", 16);
    shape1.setLineStyleColor(red, green, blue);
    shape1.setFillColor(red, green, blue);
  }

  public static int pxToEmu(int px) {
    return (int) Math.round(((double) px) * 72 * 20 * 635 / 96); // assume
                                                                 // 96dpi
  }

  public static int emuToPx(int emu) {
    return (int) Math.round(((double) emu) * 96 / 72 / 20 / 635); // assume
                                                                  // 96dpi
  }

  public float getDefaultRowHeightInPoints() {
    return this.sheet.getDefaultRowHeightInPoints();
  }

  public void setRowHeightInPoints(int rownum, float height) {
    sheet.getRow(rownum).setHeightInPoints(height);
  }

  public float getRowHeightInPoints(int rownum) {
    return sheet.getRow(rownum).getHeightInPoints();
  }

  /**
   * ROW 높이 자동 조절
   * 
   * @param rownum
   * @param cellValue
   */
  public void setAutoRowFit(int cellnum, int rownum) {
    XSSFRow row = sheet.getRow(rownum);
    XSSFCell cell = row.getCell(cellnum);
    XSSFFont cellFont = cell.getCellStyle().getFont();
    int fontStyle = java.awt.Font.PLAIN;
    if (cellFont.getBold())
      fontStyle = java.awt.Font.BOLD;
    if (cellFont.getItalic())
      fontStyle = java.awt.Font.ITALIC;

    java.awt.Font currFont = new java.awt.Font(cellFont.getFontName(), fontStyle, cellFont.getFontHeightInPoints());

    String cellText = cell.getStringCellValue();
    AttributedString attrStr = new AttributedString(cellText);
    attrStr.addAttribute(TextAttribute.FONT, currFont);

    // Use LineBreakMeasurer to count number of lines needed for the text
    //
    FontRenderContext frc = new FontRenderContext(null, true, true);
    LineBreakMeasurer measurer = new LineBreakMeasurer(attrStr.getIterator(), frc);
    int nextPos = 0;
    int lineCnt = 1;
    float columnWidthInPx = sheet.getColumnWidthInPixels(cellnum);
    while (measurer.getPosition() < cellText.length()) {
      nextPos = measurer.nextOffset(columnWidthInPx);
      lineCnt++;
      measurer.setPosition(nextPos);
    }
    int fromIndex = -1;
    while ((fromIndex = cellText.indexOf("\n", fromIndex + 1)) >= 0) {
      lineCnt++;
    }
    if (lineCnt > 1) {
      row.setHeightInPoints(sheet.getDefaultRowHeightInPoints() * lineCnt * /* fudge factor */ 1.1f);
    }
  }

  public static List<List<String>> readExcel(File file) throws IOException, InvalidFormatException {
    return readExcel(new FileInputStream(file), file.getName(), 0);
  }

  public static List<List<String>> readExcel(File file, int sheetAt) throws IOException, InvalidFormatException {
    return readExcel(new FileInputStream(file), file.getName(), sheetAt);
  }

  public static List<List<String>> readExcel(InputStream is) throws IOException, InvalidFormatException {
    return readExcel(is, "xlsx", 0);
  }

  private static Workbook getWorkbook(InputStream inputStream, String fileName) throws IOException {
    Workbook workbook = null;

    if (fileName.endsWith("xlsx")) {
      workbook = new XSSFWorkbook(inputStream);
    } else if (fileName.endsWith("xls")) {
      workbook = new HSSFWorkbook(inputStream);
    } else {
      throw new IllegalArgumentException("The specified file is not Excel file");
    }

    return workbook;
  }

  public static List<List<String>> readExcel(InputStream is, String fileName, int sheetAt) throws IOException, InvalidFormatException {
    List<List<String>> resultList = new ArrayList<>();
    // 파일을 읽기위해 엑셀파일을 가져온다
    Workbook workbook = getWorkbook(is, fileName);
    int rowindex = 0;
    int columnindex = 0;
    // 시트 수 (첫번째에만 존재하므로 0을 준다)
    // 만약 각 시트를 읽기위해서는 FOR문을 한번더 돌려준다
    Sheet sheet = workbook.getSheetAt(sheetAt);
    // 행의 수
    int rows = sheet.getPhysicalNumberOfRows();
    for (rowindex = 0; rowindex < rows; rowindex++) {
      // 행을 읽는다
      Row row = sheet.getRow(rowindex);
      resultList.add(new ArrayList<String>());
      if (row != null) {
        // 셀의 수
        int cells = row.getPhysicalNumberOfCells();
        for (columnindex = 0; columnindex <= cells; columnindex++) {
          // 셀값을 읽는다
          Cell cell = row.getCell(columnindex);
          String value = "";
          // 셀이 빈값일경우를 위한 널체크
          if (rowindex == 0 && cell == null) {
            continue;
          }
          if (cell != null) {
            // 타입별로 내용 읽기
            switch (cell.getCellType()) {
            case Cell.CELL_TYPE_FORMULA:
              value = cell.getCellFormula();
              break;
            case Cell.CELL_TYPE_NUMERIC:
              value = String.format("%1$,.0f", cell.getNumericCellValue());
              break;
            case Cell.CELL_TYPE_STRING:
              value = cell.getStringCellValue() + "";
              break;
            case Cell.CELL_TYPE_BLANK:
              value = cell.getBooleanCellValue() + "";
              break;
            case Cell.CELL_TYPE_ERROR:
              value = cell.getErrorCellValue() + "";
              break;
            }
          }
          if ("false".equals(value)) {
            value = "";
          }
          resultList.get(rowindex).add(value);
        }
      }
    }
    workbook.close();
    return resultList;
  }
}
