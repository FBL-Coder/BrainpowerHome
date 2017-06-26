package cn.etsoft.smarthome.Utils;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.etsoft.smarthome.Domain.City;
import cn.etsoft.smarthome.Domain.Weather_All_Bean;
import cn.etsoft.smarthome.Domain.Weather_Bean;
import cn.etsoft.smarthome.MyApplication;

/**
 * Author：FBL  Time： 2017/6/14.
 */

public class WratherUtil {

    private List<City> mCityList;
    // 首字母集
    private List<String> mSections;
    // 根据首字母存放数据
    private Map<String, List<City>> mMap;
    // 首字母位置集
    private List<Integer> mPositions;
    // 首字母对应的位置
    private Map<String, Integer> mIndexer;
    private static final String FORMAT = "^[a-z,A-Z].*$";

    public WratherUtil() {
        initCityList();

        /**
         * 天气对应的图标id
         */
        MyApplication.mApplication.mWeathers_list = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 43; i++) {
                    Weather_Bean bean = new Weather_Bean();
                    bean.setCode(i);
                    MyApplication.mApplication.mWeathers_list.add(bean);
                }
            }
        }).start();
    }


    private void initCityList() {
        mCityList = new ArrayList<City>();
        mSections = new ArrayList<String>();
        mMap = new HashMap<String, List<City>>();
        mPositions = new ArrayList<Integer>();
        mIndexer = new HashMap<String, Integer>();

        new Thread(new Runnable() {
            @Override
            public void run() {

                MyApplication.mApplication.mCityDB = openCityDB();// 这个必须最先复制完,所以我放在单线程中处理
                prepareCityList();

            }
        }).start();
    }

    private CityDB openCityDB() {
        String path = "/data"
                + Environment.getDataDirectory().getAbsolutePath()
                + File.separator + MyApplication.getContext().getPackageName() + File.separator
                + CityDB.CITY_DB_NAME;
        File db = new File(path);
        if (!db.exists()) {
            // L.i("db is not exists");
            try {
                InputStream is = MyApplication.mApplication.getAssets().open("city.db");
                FileOutputStream fos = new FileOutputStream(db);
                int len = -1;
                byte[] buffer = new byte[1024];
                while ((len = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                    fos.flush();
                }
                fos.close();
                is.close();
                Log.i("DB","OPEN DB  成功");
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(0);
                Log.i("DB","OPEN DB  失败");
            }
        }
        return new CityDB(MyApplication.mContext, path);
    }

    private boolean prepareCityList() {
        mCityList = MyApplication.mApplication.mCityDB.getAllCity();// 获取数据库中所有城市
        for (City city : mCityList) {
            String firstName = city.getFirstPY();// 第一个字拼音的第一个字母
            if (firstName.matches(FORMAT)) {
                if (mSections.contains(firstName)) {
                    mMap.get(firstName).add(city);
                } else {
                    mSections.add(firstName);
                    List<City> list = new ArrayList<City>();
                    list.add(city);
                    mMap.put(firstName, list);
                }
            } else {
                if (mSections.contains("#")) {
                    mMap.get("#").add(city);
                } else {
                    mSections.add("#");
                    List<City> list = new ArrayList<City>();
                    list.add(city);
                    mMap.put("#", list);
                }
            }
        }
        Collections.sort(mSections);// 按照字母重新排序
        int position = 0;
        for (int i = 0; i < mSections.size(); i++) {
            mIndexer.put(mSections.get(i), position);// 存入map中，key为首字母字符串，value为首字母在listview中位置
            mPositions.add(position);// 首字母在listview中位置，存入list中
            position += mMap.get(mSections.get(i)).size();// 计算下一个首字母在listview的位置
        }
        Log.i("City","City COPY成功");
        return true;
    }
}
