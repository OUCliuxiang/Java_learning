package com.GenerateExcel;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.poi.hssf.usermodel.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


import static java.lang.Math.round;
import static org.apache.poi.hssf.record.ExtendedFormatRecord.LEFT;

public class Generate {

    // static HSSFWorkbook wb;
    // static HSSFSheet sheet;
    private HSSFCellStyle style_normal;
    private HSSFCellStyle style_red;

    public void generateExcel(JSONArray inputJson, String outputName) {
            HSSFWorkbook wb = null;
            wb = new HSSFWorkbook();
            HSSFSheet sheet = wb.createSheet("sheet1");
            style_normal = this.getnerateStyle(wb, HSSFFont.COLOR_NORMAL);
            style_red = this.getnerateStyle(wb, HSSFFont.COLOR_RED);
            HSSFRow row = sheet.createRow(0);
            this.generateHeadRow(row);
            this.generateBodyRows(inputJson, sheet);
        try{
            File newExcelFile = new File(outputName);
            FileOutputStream fout = new FileOutputStream(newExcelFile);
            wb.write(fout);
            fout.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private HSSFCellStyle getnerateStyle(HSSFWorkbook wb, short color){
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(LEFT);  //居左
        HSSFFont font = wb.createFont();
        font.setFontHeightInPoints((short) 11);
        font.setColor(color);
        font.setFontName("宋体");
        style.setFont(font);
        return style;
    }

    private void generateHeadRow(HSSFRow row){
        HSSFCell cell = row.createCell(0);   // 第一个单元格
        cell.setCellValue("*物资名称(必填)");         // 设定值
        cell.setCellStyle(style_red);               // 设置字体

        cell = row.createCell(1);           //第二个单元格
        cell.setCellValue("*数量(必填)");
        cell.setCellStyle(style_red);

        cell = row.createCell(2);           //第三个单元格
        cell.setCellValue("计量单位");
        cell.setCellStyle(style_normal);

        cell = row.createCell(3);           //第四个单元格
        cell.setCellValue("*单价(人民币,必填)");
        cell.setCellStyle(style_red);

        cell = row.createCell(4);           //第五个单元格
        cell.setCellValue("主要技术参数");
        cell.setCellStyle(style_normal);

        cell = row.createCell(5);           //第六个单元格
        cell.setCellValue("品牌");
        cell.setCellStyle(style_normal);

        cell = row.createCell(6);           //第七个单元格
        cell.setCellValue("型号");
        cell.setCellStyle(style_normal);

        cell = row.createCell(7);           //第八个单元格
        cell.setCellValue("是否进口(非进口填0,进口填1)");
        cell.setCellStyle(style_normal);

        cell = row.createCell(8);           //第九个单元格
        cell.setCellValue("售后服务要求");
        cell.setCellStyle(style_normal);

        cell = row.createCell(9);           //第十个单元格
        cell.setCellValue("存放地");
        cell.setCellStyle(style_normal);

        cell = row.createCell(10);           //第十一个单元格
        cell.setCellValue("市场情况");
        cell.setCellStyle(style_normal);

        cell = row.createCell(11);           //第十二个单元格
        cell.setCellValue("工作原理");
        cell.setCellStyle(style_normal);
    }

    private void generateBodyRows(JSONArray inputJson, HSSFSheet sheet){
        for (int i = 0; i < inputJson.size(); i++) {
            JSONObject obj = inputJson.getJSONObject(i);
            String name = obj.getString("ITEM_PRODUCT_NAME");
            int count = obj.getInteger("ITEM_PRODUCT_AMOUNT");
            String unit = obj.getString("ITEM_PRODUCT_UNIT");
            double tex = Double.parseDouble( obj.getString("ITEM_PRODUCT_TAX_RATE").
                    replace("%", "")) * 0.01;
            double clearPrice = obj.getDouble("ITEM_PRODUCT_PRICE");
            double texPrice = (tex+1.0)*clearPrice;
            int truePrice;
            truePrice = (texPrice % 1 >0.99 || texPrice%1 < 0.01) ?
                    (int)round(texPrice) : 9999999;
            String type = obj.getString("ITEM_PRODUCT_TYPE");

            HSSFRow row = sheet.createRow(i+1);
            row.createCell(0).setCellValue(name);
            row.createCell(1).setCellValue(count);
            row.createCell(2).setCellValue(unit);
            row.createCell(3).setCellValue(truePrice);
            row.createCell(4).setCellValue("");
            row.createCell(5).setCellValue("");
            row.createCell(6).setCellValue(type);
            row.createCell(7).setCellValue("");
            row.createCell(8).setCellValue("");
            row.createCell(9).setCellValue("");
            row.createCell(10).setCellValue("");
            row.createCell(11).setCellValue("");
        }
    }

}
