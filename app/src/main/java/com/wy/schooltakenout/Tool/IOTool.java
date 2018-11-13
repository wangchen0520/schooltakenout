package com.wy.schooltakenout.Tool;

import android.content.Context;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.SystemClock;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class IOTool {
    private static String result;
    // 转发ip
    public static String ip = "http://47.107.140.236:8080/compus_takeout/";
    // 直接ip
    public static String pictureIp = "http://47.107.145.127:8081/";
    // 本地ip
//    public static String ip = "http://10.132.3.70:8080/";
    private static CountDownLatch count;
    private static int status;
    private static JSONObject data;
    private static JSONArray dateArray;

    public static void upAndDown(String url, List<String> list) {
        result = "";
        status = 0;
        data = null;
        dateArray = null;

        // 有无参数处理方式不同
        new MyThread(url, list).start();

        // 等待子进程任务完成才运行
        try {
            count = new CountDownLatch(1);
            count.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 处理传来的数据
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(result);
            status = jsonObject.getInt("status");
            data = jsonObject.getJSONObject("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // 如果不对就变成数组
        if(data == null) {
            try {
                jsonObject = new JSONObject(result);
                dateArray = jsonObject.getJSONArray("data");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // 如果都不对就没有状态信息
        if(data == null && dateArray == null) {
            try {
                data = new JSONObject(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(data != null) {
            TestPrinter.print("1_"+data.toString());
        }
        if(dateArray != null) {
            try {
                TestPrinter.print("1_"+dateArray.getJSONObject(0).toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static int getStatus() {
        return status;
    }

    public static JSONObject getData() {
        return data;
    }

    public static JSONArray getDateArray() {
        return dateArray;
    }

    public static void save(String string, String filename, Context context) {
        FileOutputStream outputStream;
        try{
            outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(string.getBytes());
            outputStream.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static String read(String filename, Context context) {
        FileInputStream in = null;
        ByteArrayOutputStream bout = null;
        byte[]buf = new byte[1024];
        String fileText = "";

        int length = 0;
        try {
            in = context.openFileInput(filename); //获得输入流
            bout = new ByteArrayOutputStream();
            while((length=in.read(buf))!=-1){
                bout.write(buf,0,length);
            }
            byte[] content = bout.toByteArray();
            fileText = new String(content,"UTF-8"); //设置文本框为读取的内容
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(in != null)
                    in.close();
                if(bout != null)
                    bout.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return fileText;
    }

    public static void savePicture(String urlString, String filePath) {
        HttpUtil.getPicture(urlString, filePath);
    }

    // 上传子线程
    private static class MyThread extends Thread {
        String url;
        List<String> list;

        public MyThread(String url, List<String> list) {
            this.url = url;
            this.list = list;
        }

        public void run() {
            String request = "";
            if(list != null) {
                for(int i=0; i<list.size(); i++) {
                    request += list.get(i);
                    if(i < list.size()-1 ) {
                        request += "&";
                    }
                }
            }
            result = HttpUtil.doPost(url, request);
            TestPrinter.print("0_"+result);
            count.countDown();
        }
    }
}
