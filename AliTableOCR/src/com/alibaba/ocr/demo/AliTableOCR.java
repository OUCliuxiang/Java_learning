package com.alibaba.ocr.demo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.api.gateway.demo.util.HttpUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.codec.binary.Base64.encodeBase64;
import com.gui.FileChooser;



/**
 * 使用APPCODE进行云市场ocr服务接口调用
 */

public class AliTableOCR {

    static long usedTime;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                FileChooser.createWindow();
            }
        });
    }

    /*
     * 获取参数的json对象
     */
    public static JSONObject getParam(int type, String dataValue) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("dataType", type);
            obj.put("dataValue", dataValue);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public static long getUsedTime() {
        return usedTime;
    }

    public static void submit2AliAPI(String imgPath, String targetPath){
        String targetExcel = "null.xlsx";
        String imgName = imgPath.substring(imgPath.lastIndexOf("\\")+1);
        int length = imgName.length();
        if (imgName.charAt(length-2)=='n') {
            targetExcel = targetPath + '\\' +
                    imgName.replace(".png", ".xlsx");
        }
        else if (imgName.charAt(length-2)=='N') {
            targetExcel = targetPath + '\\' +
                    imgName.replace(".PNG", ".xlsx");
        }
        else if (imgName.charAt(length-2)=='p'){
            targetExcel = targetPath + '\\' +
                    imgName.replace(".jpg", ".xlsx");
        }
        else if (imgName.charAt(length-2)=='P'){
            targetExcel = targetPath + '\\' +
                    imgName.replace(".JPG", ".xlsx");
        }
        String host = "https://form.market.alicloudapi.com";
        String path = "/api/predict/ocr_table_parse";
        String appcode = "6c5447ea326547498f54c22a1febfacd";
        String imgFile = imgPath;
        Boolean is_old_format = false;
        JSONObject configObj = new JSONObject();
        configObj.put("format", "xlsx");
        configObj.put("finance", false);
        configObj.put("dir_assure", false);
        String config_str = configObj.toString();
        System.out.println(config_str);
        //            configObj.put("min_size", 5);
        //String config_str = "";

        String method = "POST";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);

        Map<String, String> querys = new HashMap<String, String>();

        // 对图像进行base64编码
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
            return;
        }
        // 拼装请求body的json字符串
        JSONObject requestObj = new JSONObject();
        try {
            if(is_old_format) {
                JSONObject obj = new JSONObject();
                obj.put("image", getParam(50, imgBase64));
                if(config_str.length() > 0) {
                    obj.put("configure", getParam(50, config_str));
                }
                JSONArray inputArray = new JSONArray();
                inputArray.add(obj);
                requestObj.put("inputs", inputArray);
            }else{
                requestObj.put("image", imgBase64);
                if(config_str.length() > 0) {
                    requestObj.put("configure", config_str);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String bodys = requestObj.toString();

        try {
            /**
             * 重要提示如下:
             * HttpUtils请从
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
             * 下载
             *
             * 相应的依赖请参照
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
             */
            long startTime = System.currentTimeMillis();
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            long endTime = System.currentTimeMillis();
            usedTime = endTime-startTime;
            System.out.println(response);
            int stat = response.getStatusLine().getStatusCode();
            if(stat != 200){
                System.out.println("Http code: " + stat);
                System.out.println("http header error msg: "+ response.getFirstHeader("X-Ca-Error-Message"));
                System.out.println("Http body error msg:" + EntityUtils.toString(response.getEntity()));
                return;
            }
            String res = EntityUtils.toString(response.getEntity());

            JSONObject res_obj = JSON.parseObject(res);
            if(is_old_format) {
                JSONArray outputArray = res_obj.getJSONArray("outputs");
                String output = outputArray.getJSONObject(0).getJSONObject("outputValue").getString("dataValue");
                JSONObject out = JSON.parseObject(output);
                String base64code = out.getString("tables");
                Base64excel.convert2excel(base64code, targetExcel);
                // System.out.println(out.toJSONString());
            }else{
                String base64code = res_obj.getString("tables");
                Base64excel.convert2excel(base64code, targetExcel);
                // System.out.println(res_obj.toJSONString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}