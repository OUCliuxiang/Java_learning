package com.GenerateExcel;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.utils.Utils;
import gui.FileChooser;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;


import static org.apache.commons.codec.binary.Base64.encodeBase64;

//依赖请参照:https://icredit-code.oss-cn-hangzhou.aliyuncs.com/pom_v2.xml
public class Ticket2Excel {

    static long usedTime;
    static int stateCode;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                FileChooser.createWindow();
            }
        });
    }

    public static long getUsedTime() {
        return usedTime;
    }

    public static int submit2AliAPI(String inputName, String targetPath) {
        boolean baseFormat = true;

        //API产品路径
        String requestUrl = "http://vatinvoice.market.alicloudapi.com/ai_vat_invoice";
        //阿里云APPCODE
        String appcode = "6c5447ea326547498f54c22a1febfacd";

        CloseableHttpClient httpClient = null;
        try{
            httpClient = HttpClients.createDefault();
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            // 装填参数
            // if (baseFormat) {
            //启用BASE64编码方式进行识别
            //内容数据类型是BASE64编码
            String imgFile = inputName;
            String imgBase64 = "";
            try {
                File file = new File(imgFile);
                byte[] content = new byte[(int) file.length()];
                FileInputStream finputstream = new FileInputStream(file);
                finputstream.read(content);
                finputstream.close();
                imgBase64 = new String(encodeBase64(content));
            } catch (IOException e) {
                e.printStackTrace();
                return -1;
            }
            params.add(new BasicNameValuePair("AI_VAT_INVOICE_IMAGE", imgBase64));
            params.add(new BasicNameValuePair("AI_VAT_INVOICE_IMAGE_TYPE", "0"));

            // 创建一个HttpGet实例
            HttpPost httpPost = new HttpPost(requestUrl);
            httpPost.addHeader("Authorization","APPCODE " + appcode);
            httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

            // 设置请求参数
            httpPost.setEntity(new UrlEncodedFormEntity(params, "utf-8"));

            // 发送GET请求
            long startTime = System.currentTimeMillis();
            HttpResponse execute = httpClient.execute(httpPost);
            long endTime = System.currentTimeMillis();
            usedTime = endTime-startTime;

            // 获取状态码
            stateCode = execute.getStatusLine().getStatusCode();

            // 获取结果
            HttpEntity entity = execute.getEntity();
            String result = EntityUtils.toString(entity, "utf-8");
            JSONObject res_obj = JSON.parseObject(result);

            String sellerName = res_obj.getString("INVOICE_SELLER_NAME");
            String date = Utils.IntegerOnly( res_obj.getString("INVOICE_TIME"));
            double totalCount = Double.parseDouble( res_obj.getString(
                    "INVOICE_TOTAL_AMOUNT_AND_TAX_SMALL"));
            String excelName = targetPath + "\\" +
                    String.format("%s_%s_%d.xls", date, sellerName, (int) totalCount);

            JSONArray invoice_detail = res_obj.getJSONArray("INVOICE_DETAIL");
            Generate generator = new Generate();
            generator.generateExcel(invoice_detail, excelName);


            BufferedWriter newFile = new BufferedWriter(new FileWriter(
                    "E:\\documents\\otherWork\\Ticket2Excel\\test1.json"));
            newFile.write(String.valueOf(res_obj));
            newFile.close();

        }catch (Exception e) {
            e.printStackTrace();
            return  stateCode;
        }finally {
            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return stateCode;
    }
}