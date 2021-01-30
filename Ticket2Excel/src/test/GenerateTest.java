package test;

import com.GenerateExcel.Generate;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

import java.awt.*;
import java.io.FileOutputStream;
import java.io.IOException;

public class GenerateTest {

    private static XSSFCellStyle style_normal;
    private static XSSFCellStyle style_red;
    /*
        public static void main(String[] args) {

            Generate generator = new Generate();
            XSSFWorkbook wb = null;
            wb = new XSSFWorkbook();
            XSSFSheet sheet = wb.createSheet("sheet1");
            style_normal = getnerateStyle(wb, XSSFFont.COLOR_NORMAL);
            style_red = getnerateStyle(wb, XSSFFont.COLOR_RED);
            XSSFRow row = sheet.createRow(0);


        testWrite();
    }
*/

    private static XSSFCellStyle getnerateStyle(XSSFWorkbook wb, short color) {
        XSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.LEFT);  //居左
        XSSFFont font = wb.createFont();
        font.setFontHeightInPoints((short) 11);
        font.setColor(color);
        font.setFontName("宋体");
        style.setFont(font);
        return style;
    }

    public static void testWrite(){
        //创建工作簿
        Workbook wb = new HSSFWorkbook();
        //创建 Sheet页
        Sheet sheetA = wb.createSheet("A");
        for(int i=0; i<9; i++){
            //创建单元行
            Row row = sheetA.createRow(i);
            for(int j=0; j<=i; j++){
                Cell cell = row.createCell(j);
                cell.setCellValue((j+1)+" * "+(i+1)+" = " + (j+1)*(i+1));
            }
        }
        try {
            //路径需要存在
            FileOutputStream fos = new FileOutputStream("E:\\newExcel.xls");
            wb.write(fos);
            fos.close();
            System.out.println("写数据结束！");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
