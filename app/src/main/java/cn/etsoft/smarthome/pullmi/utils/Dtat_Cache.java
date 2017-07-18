package cn.etsoft.smarthome.pullmi.utils;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collections;

import cn.etsoft.smarthome.domain.Safety_Data;
import cn.etsoft.smarthome.pullmi.entity.WareData;

/**
 * 作者：傅博龍  时间： 2016/7/11.
 * 数据本地缓存；（文件形式）
 */
public class Dtat_Cache {
    /**
     * 数据缓存——文件缓存；
     *
     * @param
     */
    private static String savePath = "/sdcard/Home/";
    private static String savePath_safety = "/sdcard/HomeSafety/";

    public static void writeFile(String id, WareData wareData) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            File file = new File(savePath);
            if (!file.exists()) {
                file.mkdir();
            }
            String saveFileName = "/sdcard/Home/" + id + ".txt";
            File ApkFile = new File(saveFileName);
            fos = new FileOutputStream(ApkFile);
//            fos = MyApplication.getContext().openFileOutput(id + ".txt", Context.MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(wareData);
            Log.e("Exception", "写入成功");
        } catch (Exception e) {
            Log.e("Exception", e + "");
            //e.printStackTrace();
            // 这里是保存文件产生异常
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    Log.e("Exception", e + "");
                    // fos流关闭异常
                    //e.printStackTrace();
                }
            }
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    Log.e("Exception", e + "");
                    // oos流关闭异常
                    //e.printStackTrace();
                }
            }
        }
    }

    /**
     * 数据缓存——读取缓存数据
     * @return
     * @throws IOException
     */

    public static Object readFile(String id) {
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            String saveFileName = "/sdcard/Home/" + id + ".txt";
            fis = new FileInputStream(saveFileName);
            ois = new ObjectInputStream(fis);
            Log.e("Exception", "读取成功");
            return ois.readObject();
        } catch (Exception e) {
            Log.e("Exception", e + "");
            //e.printStackTrace();
            // 这里是读取文件产生异常
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    Log.e("Exception", e + "");
                    // fis流关闭异常
                    //e.printStackTrace();
                }
            }
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    Log.e("Exception", e + "");
                    // ois流关闭异常
                    //e.printStackTrace();
                }
            }
        }
        // 读取产生异常，返回null
        return null;
    }

    /**
     * 主页安防报警信息写入文件
     * @param safety_data
     */
    public static void writeFile_safety(Safety_Data safety_data) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            File file = new File(savePath_safety);
            if (!file.exists()) {
                file.mkdir();
            }
            String saveFileName = "/sdcard/HomeSafety/" + "safety.txt";
            File ApkFile = new File(saveFileName);
            fos = new FileOutputStream(ApkFile);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(safety_data);
            Log.e("Exception", "写入成功");
        } catch (Exception e) {
            Log.e("Exception", e + "");
            //e.printStackTrace();
            // 这里是保存文件产生异常
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    Log.e("Exception", e + "");
                    // fos流关闭异常
                    //e.printStackTrace();
                }
            }
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    Log.e("Exception", e + "");
                    // oos流关闭异常
                    //e.printStackTrace();
                }
            }
        }
    }

    /**
     * 从文件中读取主页安防报警信息
     * @return
     * @throws IOException
     */

    public static Safety_Data readFile_safety(boolean isCollections) {
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            String saveFileName = "/sdcard/HomeSafety/" + "safety.txt";
            fis = new FileInputStream(saveFileName);
            ois = new ObjectInputStream(fis);
            Log.e("Exception", "读取成功");
            Safety_Data safety_Data = (Safety_Data) ois.readObject();
            if (isCollections)
                //读取的数据反向
                Collections.reverse(safety_Data.getDatas());
            return safety_Data;
        } catch (Exception e) {
            Log.e("Exception", e + "");
            return null;
            //e.printStackTrace();
            // 这里是读取文件产生异常
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    Log.e("Exception", e + "");
                    return null;
                    // fis流关闭异常
                    //e.printStackTrace();
                }
            }
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    Log.e("Exception", e + "");
                    return null;
                    // ois流关闭异常
                    //e.printStackTrace();
                }
            }
        }
        // 读取产生异常，返回null
//        return null;
    }
}