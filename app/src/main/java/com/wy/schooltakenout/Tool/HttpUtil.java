package com.wy.schooltakenout.Tool;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CountDownLatch;

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

            if (conn.getResponseCode() == 200) {
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
        // 用于等待子线程
        final CountDownLatch count = new CountDownLatch(1);
        // 网络访问需要子线程
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    // 向服务器请求商家头像并存储
                    File file = new File(filePath);
                    // 如果文件不存在则获取
                    if (file.exists()) {
                        file.delete();
                    }
                    // 在文件系统中根据路径创建一个新的空文件
                    file.createNewFile();
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    // 向服务器请求商家头像
                    URL url = new URL(urlString);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    //设置连接超时时间，单位ms
                    conn.setConnectTimeout(10000);
                    //通过setRequestMethod将conn设置成GET方法
                    conn.setRequestMethod("GET");
                    //连接成功就存入，否则删除空文件
                    if (conn.getResponseCode() == 200) {
                        //获取conn的输入流
                        InputStream inputStream = conn.getInputStream();
                        byte[] responseBody = getBytesByInputStream(inputStream);
                        fileOutputStream.write(responseBody);
                    } else {
                        file.delete();
                    }
                    // 提醒主线程可以运行了
                    count.countDown();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();
        // 等待子进程任务完成才运行
        try {
            count.await();
        } catch (InterruptedException e) {
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

