package com.utils;

import java.io.*;
import java.util.Base64;


public class Base64excel {
    public static void convert2excel(String input, String output) {
        final Base64.Decoder decoder = Base64.getDecoder();
        String base64Code = input;
        String targetPath = output;

        OutputStream out = null;
        try {
            out = new FileOutputStream(new File(targetPath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            out.write(decoder.decode(base64Code));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
