package com.wy.schooltakenout.Tool;

import android.content.Context;
import android.os.AsyncTask;
import android.os.SystemClock;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class IOTool {
    private static String result;

    public static String upAndDown(String url) {
        int TIME_OUT = 1000;
        int time = 0;
        result = "";
        new MyTask().execute(url);
        while(result.equals("") && time < TIME_OUT) {
            SystemClock.sleep(100);
            time += 100;
        }
        return result;
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

    //上传异步任务
    private static class MyTask extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            return HttpUtil.doPost(strings[0], null);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            IOTool.result = result;
        }
    }
}
