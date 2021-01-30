package test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.utils.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class Json2Excel {
    public static void main(String[] args) throws UnsupportedEncodingException {
        String filePath = "E:\\documents\\otherWork\\Ticket2Excel\\test1.json";
        String str = getStr(filePath);
        int strLen = str.length();
        System.out.println(filePath.substring(0, strLen-5).concat("_compress.jpg"));

        JSONObject json = JSONObject.parseObject(str);
        JSONArray resultJson = JSONArray.parseArray(json.getString("INVOICE_DETAIL"));
        String sellerName = json.getString("INVOICE_SELLER_NAME");
        System.out.print(sellerName);
        String date = Utils.IntegerOnly( json.getString("INVOICE_TIME"));
        System.out.printf("_%s", date);
        double totalCount = Double.parseDouble( json.getString(
                "INVOICE_TOTAL_AMOUNT_AND_TAX_SMALL"));
        System.out.printf("_%d\n", (int)totalCount);

/*
        for(int i = 0; i < resultJson.size(); i++) {
            JSONObject obj = resultJson.getJSONObject(i);
            // System.out.println(obj.toJSONString());
            // int del = Integer.parseInt( obj.get("ITEM_PRODUCT_AMOUNT").toString());
            // String bookId = obj.getString("ITEM_PRODUCT_NAME");
            // System.out.printf("%s共购买了%d个\n", bookId, del);
            double tex = Double.parseDouble( obj.get( "ITEM_PRODUCT_TAX_RATE").
                    toString().replace("%", "")) * 0.01;
            System.out.printf("%.2f\n", tex);
            System.out.println(2.003%1);
            // System.out.println(bookId);
        }
*/
    }

    private static String getStr(String name){
        String str;
        File file=new File(name);
        try {
            FileInputStream in = new FileInputStream(file);
            // size  为字串的长度 ，这里一次性读完
            int size = in.available();
            byte[] buffer=new byte[size];
            in.read(buffer);
            in.close();
            str = new String(buffer,"utf-8");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            return null;
        }
        return str;
    }

    protected static void Json2Excel(JSONArray input, String name){

    }

}
