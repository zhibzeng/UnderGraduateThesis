package com.example.mytabhost.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.util.Log;

public class ReadFile {
	
/**
         * 读取文件中的内容
         * @param fileName
         * @return
         * @throws IOException 
         */
        public static String readFile(String fileName) throws IOException{
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1024);
                FileInputStream inputStream = new FileInputStream(fileName);
                int len = 0;
                byte[] buffer = new byte[1024];
                while((len = inputStream.read(buffer)) != -1){
                        outputStream.write(buffer, 0, len);
                }
                outputStream.close();
                inputStream.close();
                byte[] data = outputStream.toByteArray();
                return new String(data);
        }
        
        /**
         * 从 android raw 中读取文件
         * @param is
         * @return
         * @throws IOException
         */
        public static String convertStreamToString(InputStream is) throws IOException {
        	StringBuffer sb = new StringBuffer();  
        	BufferedReader br = new BufferedReader(new InputStreamReader(is,"GB2312"));  
             String data = "";  
             while ((data = br.readLine()) != null) {  
                 sb.append(data);  
             }  
             String result = sb.toString(); 
             return result;
        }
}
