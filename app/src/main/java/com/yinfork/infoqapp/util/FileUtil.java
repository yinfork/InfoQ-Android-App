package com.yinfork.infoqapp.util;

import android.os.Environment;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by yinjianhua on 16/6/30.
 */
public class FileUtil {


    public final static String PATH = "InfoQ";

    public static boolean isExistsFile(String fileName){
        File file = new File(fileName);
        return  file.exists();
    }

    public static void mkdir(){
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            String sdDir = Environment.getExternalStorageDirectory()
                    .getAbsolutePath();
            sdDir += File.separator + PATH;
            File newFile = new File(sdDir);
            if (!newFile.exists()) {
                newFile.mkdir();
            }
        }
    }

    public static String readSDFile(String fileName){
        File file;
        FileInputStream inStream = null;
        ByteArrayOutputStream outStream = null;

        try {
            file = new File(fileName);
            inStream = new FileInputStream(file);
            outStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length = -1;

            // 可考虑设文章最大读取值,避免大文件导致内存溢出
            while((length = inStream.read(buffer)) != -1 ){
                outStream.write(buffer, 0, length);
            }

            return outStream.toString();
        } catch (IOException e){
            Log.i("FileTest", e.getMessage());
        }finally {
            // FIXME:这样进行文件操作不好
            try {
                if(null != outStream) {
                    outStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if(null != inStream) {
                    inStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    //写文件
    public static void writeSDFile(String fileName, String write_str) {

        mkdir();

        fileName =  Environment.getExternalStorageDirectory()
                .getAbsolutePath()+File.separator + PATH+
                File.separator + fileName;


        File file = new File(fileName);
        FileOutputStream out = null;

        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            out = new FileOutputStream(file, true);//true表示在文件末尾添加
            byte[] data = write_str.getBytes();
            out.write(data);
        } catch (Exception e) {

            Log.d("AppException","fileName: "+fileName);
            Log.d("AppException","",e);


        } finally {
            if (null != out) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
