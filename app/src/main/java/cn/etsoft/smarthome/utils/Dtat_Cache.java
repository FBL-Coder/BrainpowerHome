package cn.etsoft.smarthome.utils;

import android.os.Environment;
import android.util.Log;

import org.apache.http.util.EncodingUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.etsoft.smarthome.MyApplication;

/**
 * 作者：FBL  时间： 2016/7/11.
 * 数据本地缓存；（文件形式）
 */
public class Dtat_Cache {

    /**
     * 数据缓存——文件缓存；
     * @param data
     */
    public static void writeFileToSD(String data, String name) {
        if (name != null && !("".equals(name))) {
            try {
                File file = Environment.getExternalStorageDirectory();
//                String sd = file.getPath() + "/Exception_etsoft/";
                String sd = "/mnt/sdcard/crash";
                File file1 = new File(sd);
                if (!file1.exists())
                    file1.mkdir();
                File ApkFile = new File(file1, name + ".txt");
                FileOutputStream fos = new FileOutputStream(ApkFile);
                fos.write(data.getBytes());
                fos.close();
                Log.i("HTTPData", "写入成功");
            } catch (Exception e) {
                Log.i("HTTPData", e + "写入失败");
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
                int length = fin.available();
                byte[] buffer = new byte[length];
                fin.read(buffer);
                data = EncodingUtils.getString(buffer, "UTF-8");
                fin.close();
                Log.i("HTTPData", "读取成功");
            } catch (Exception e) {
//            e.printStackTrace();
                Log.i("HTTPData", "读取失败");
            }
        }
        return data;
    }
}
