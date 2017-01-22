package cn.semtec.community2.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.util.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.linphone.squirrel.squirrelCallImpl;

import java.io.File;

import cn.semtec.community2.MyApplication;
import cn.etsoft.smarthome.R;
import cn.semtec.community2.database.DBhelper;
import cn.semtec.community2.entity.HouseProperty;
import cn.semtec.community2.fragment.VideoFragment;
import cn.semtec.community2.model.MyHttpUtil;
import cn.semtec.community2.tool.Constants;
import cn.semtec.community2.util.CatchUtil;
import cn.semtec.community2.util.SharedPreferenceUtil;

public class SettingActivity extends MyBaseActivity implements View.OnClickListener {

    private View btn_clear;
    private ImageView btn_back;
    private SharedPreferenceUtil preference;
    private View btn_advanced;
    private View login_out;
    private File dirFile;//图片路径
    private int size;
    private TextView tv_clear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        preference = MyApplication.getSharedPreferenceUtil();
        dirFile = new File(this.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                "recordpic/" + MyApplication.cellphone);
        if (dirFile.exists()) {
            File[] list = dirFile.listFiles();
            for (int i = 0; i < list.length; i++) {
                size += list[i].length();
            }
        }
        setView();
        setListener();
    }


    private void setView() {
        btn_clear = findViewById(R.id.btn_clear);
        btn_advanced = findViewById(R.id.btn_advanced);
        login_out = findViewById(R.id.login_out);
        tv_clear = (TextView) findViewById(R.id.tv_clear);
        tv_clear.setText(((float) ((size * 1000) / (1024 * 1024)) / 1000) + " M");
        btn_back = (ImageView) findViewById(R.id.btn_back);
        TextView tv_versions = (TextView) findViewById(R.id.tv_versions);

        try {
            PackageManager pm = getPackageManager();
            PackageInfo pi;
            pi = pm.getPackageInfo(getPackageName(), 0);
            tv_versions.setText(pi.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            CatchUtil.catchM(e);
        }
    }

    private void setListener() {
        btn_back.setOnClickListener(this);
        btn_clear.setOnClickListener(this);
        btn_advanced.setOnClickListener(this);
        login_out.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_clear:
                clear();
                break;
            case R.id.btn_advanced:
                Intent intent = new Intent(this, AdevancedSettingActivity.class);
                startActivity(intent);
                break;
            case R.id.login_out:
                loginOut();
                break;
            default:
                break;
        }
    }

    private void clear() {
        SQLiteDatabase db = MyApplication.getDB();
        db.delete(DBhelper.VIDEO_RECORD, DBhelper.RECORD_ACCOUNT + "=?", new String[]{MyApplication.cellphone});
        db.close();
        if (dirFile.exists()) {
            File[] list = dirFile.listFiles();
            for (int i = 0; i < list.length; i++) {
                list[i].delete();
            }
        }
        tv_clear.setText("0.0 M");
    }

    private void loginOut() {
        if (MyApplication.houseList.size() >= 1) {
            for (int i = 0; i < MyApplication.houseList.size(); i++) {
                HouseProperty house = MyApplication.houseList.get(i);

                ((squirrelCallImpl) getApplication()).squirrelAccountExit(house.sipaddr,
                        squirrelCallImpl.serverport, house.sipnum);
            }
        }
        //登出后台
        loginOutServers();
        MyApplication.logined = false;
        MyApplication.houseList.clear();
        MyApplication.houseProperty = null;
        MyApplication.cellphone = null;
        VideoFragment.mlist.clear();

        preference.remove("password");
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
        BaseActivity.instance.finish();
    }

    private void loginOutServers() {
        String url = Constants.CONTENT_LOGOUT;
        MyHttpUtil httpUtil = new MyHttpUtil(HttpRequest.HttpMethod.POST, url,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        String mResult = responseInfo.result.toString();
                        try {
                            JSONObject jo = new JSONObject(mResult);
                            if (jo.getInt("returnCode") == 0) {
                                LogUtils.i("登出成功");
                            } else {
                                LogUtils.i("登出失败");
                            }
                        } catch (JSONException e) {
                            CatchUtil.catchM(e);
                            LogUtils.i("数据异常:");
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        LogUtils.i("网络异常");
                    }
                });
        httpUtil.send();
    }

}
