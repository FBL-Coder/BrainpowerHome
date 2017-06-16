package com.example.abc.mybaseactivity.FileUtils;

import android.content.Context;
import android.util.Log;

import com.example.abc.mybaseactivity.MyApplication.MyApplication;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 作者：FBL  时间： 2016/7/11.
 * 数据本地缓存；（文件形式）
 */
public class File_Cache {
    /**
     * 数据缓存——文件缓存；
     *
     * @param data
     */
    public static void writeFileToSD(String data, String name) {
//        Environment.getExternalStorageDirectory().getAbsolutePath() + "/"+
        if (name != null && !("".equals(name))) {
            try {
//            File file = new File(name+".txt");
                FileOutputStream fos = MyApplication.getContext().openFileOutput(name + ".txt", Context.MODE_PRIVATE);
                fos.write(data.getBytes());
                fos.close();
                Log.i("HTTPData", "写入成功");
            } catch (Exception e) {
//            e.printStackTrace();
                Log.i("HTTPData", "写入失败");
            }
        }
    }

    /**
     * 数据缓存——读取缓存数据
     *
     * @return
     * @throws IOException
     */

    public static String readFile(String name) throws IOException {
        String data = "";
        if (name != null && !"".equals(name)) {
            try {
                FileInputStream fin = MyApplication.getContext().openFileInput(name + ".txt");
                BufferedReader bufferedReader = new BufferedReader( new InputStreamReader( fin , "utf-8" ));

                String line;

                while( ( line = bufferedReader.readLine()) != null ){
                    data += line;
                }

            } catch (Exception e) {
                Log.i("HTTPData", "读取失败");
                return "";
            }
            Log.i("HTTPData", "读取成功");
        }
        return data;
    }
}
