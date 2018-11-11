package com.wy.schooltakenout.Tool;

import android.content.Context;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtil {
    //post方式登录
    public static String doPost(final String urlString, final String request) {
        HttpURLConnection conn = null;
        byte[] requestBody = null;  //请求体
        byte[] responseBody = null; //响应体
        String result = "";       //返回值

        try {
            URL url = new URL(urlString);
            conn = (HttpURLConnection) url.openConnection();
            //设置连接超时时间，单位ms
            conn.setConnectTimeout(2000);
            //设置读取超时时间，单位ms
            conn.setReadTimeout(2000);
            //设置是否从httpURLConnection读入，默认是false
            conn.setDoInput(true);
            //设置是否向httpURLConnection输出，因为post请求参数要放在http正文内，所以要设置为true
            conn.setDoOutput(true);
            //POST请求不能用缓存，设置为false
            conn.setUseCaches(false);
            //通过setRequestMethod将conn设置成POST方法
            conn.setRequestMethod("POST");
            //获取conn的输出流
            OutputStream os = conn.getOutputStream();
            //获取两个键值对的字节数组，将该字节数组作为请求体
            requestBody = request.getBytes("UTF-8");
            //将请求体写入到conn的输出流中
            os.write(requestBody);
            //记得调用输出流的flush方法
            os.flush();
            //关闭输出流
            os.close();

            int code = conn.getResponseCode();
            if (code == 200) {
                InputStream inputStream = conn.getInputStream();
                responseBody = getBytesByInputStream(inputStream);
                result = new String(responseBody, "UTF-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    //请求图片
    public static void getPicture(final String urlString, final String filePath) {
        HttpURLConnection conn = null;
        byte[] requestBody = null;  //请求体
        byte[] responseBody = null; //响应体

        // 如果没有该文件就创建一下
        File file = new File(filePath);
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // 向服务器请求商家头像并存储
        FileOutputStream fileOutputStream = null;
        try {
            // 向服务器请求商家头像并存储
            fileOutputStream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            URL url = new URL(urlString);
            conn = (HttpURLConnection) url.openConnection();
            //设置连接超时时间，单位ms
            conn.setConnectTimeout(2000);
            //设置读取超时时间，单位ms
            conn.setReadTimeout(2000);
            //设置是否从httpURLConnection读入，默认是false
            conn.setDoInput(true);
            //设置是否向httpURLConnection输出，因为post请求参数要放在http正文内，所以要设置为true
            conn.setDoOutput(true);
            //POST请求不能用缓存，设置为false
            conn.setUseCaches(false);
            //通过setRequestMethod将conn设置成POST方法
            conn.setRequestMethod("GET");
            int code = conn.getResponseCode();
            if (code == 200) {
                //获取conn的输入流
                InputStream inputStream = conn.getInputStream();
                responseBody = getBytesByInputStream(inputStream);
                fileOutputStream.write(responseBody);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //从InputStream中读取数据，转换成byte数组，最后关闭InputStream
    private static byte[] getBytesByInputStream(InputStream is) {
        byte[] bytes = null;
        BufferedInputStream bis = new BufferedInputStream(is);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BufferedOutputStream bos = new BufferedOutputStream(baos);
        byte[] buffer = new byte[1024 * 8];
        int length = 0;
        try {
            while ((length = bis.read(buffer)) > 0) {
                bos.write(buffer, 0, length);
            }
            bos.flush();
            bytes = baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                bis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return bytes;
    }
}

