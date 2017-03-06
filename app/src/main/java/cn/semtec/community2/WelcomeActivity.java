package cn.semtec.community2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.util.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import cn.etsoft.smarthome.R;
import cn.jpush.android.api.JPushInterface;
import cn.semtec.community2.activity.BaseActivity;
import cn.semtec.community2.activity.MyBaseActivity;
import cn.semtec.community2.model.MyHttpUtil;
import cn.semtec.community2.tool.Constants;
import cn.semtec.community2.util.CatchUtil;
import cn.semtec.community2.util.ToastUtil;

public class WelcomeActivity extends MyBaseActivity {
    private int versioncode;
    @SuppressWarnings("unused")
    private String versionName;
    private String new_constraint;
    private String new_uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 判断 是以何种方式启动 activity的 点击 通知栏的安装完成 会另启动一个acivity 关闭就行了
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
        setContentView(R.layout.activity_welcome);
        showProgress();

        try {
            // 获得版本号
            PackageManager pm = getPackageManager();
            PackageInfo pi = pm.getPackageInfo(getPackageName(), 0);
            versioncode = pi.versionCode;
            versionName = pi.versionName;
            //获取屏幕数据
            DisplayMetrics metric = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metric);
            MyApplication.display_width = metric.widthPixels; // 屏幕宽度（像素）
            MyApplication.display_height = metric.heightPixels;
            MyApplication.density = metric.density;
        } catch (NameNotFoundException e1) {
            CatchUtil.catchM(e1);
        }
//        getUpdateData();
        startNext();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void getUpdateData() {
        String url = Constants.CONTENT_UPDATE;
        MyHttpUtil http = new MyHttpUtil(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String mResult = responseInfo.result.toString();
                try {
                    JSONObject jo = new JSONObject(mResult);
                    if (jo.getInt("returnCode") == 0) {
                        LogUtils.i("升级地址访问成功");
                        JSONObject object = jo.getJSONObject("object");
                        // 最新版的版本号
                        String new_versioncode = object.getString("paramValue1");
                        // 最新版版本号的描述
                        String new_description = object.getString("paramValue2");
                        // 最新版的下载路径
                        new_uri = object.getString("paramValue3");
                        // 最新版 是否要求强制升级（String型）0，不用强制，1，强制升级
                        new_constraint = object.getString("paramValue4");
                        // 版本更新内容描述
                        @SuppressWarnings("unused")
                        String new_description1 = object.getString("paramValue5");

                        if (Integer.parseInt(new_versioncode) > versioncode) {
                            new AlertDialog.Builder(WelcomeActivity.this).setTitle("版本升级").setCancelable(false)
                                    .setMessage(new_constraint.equals("0") ? new_description + "\n检测到最新版本,请及时更新!"
                                            : new_description + "\n检测到最新版本,请及时更新!\n更新前不能使用!")
                                    .setPositiveButton("更新", new OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // 更新下载 new_uri
                                            cancelProgress();
                                            updateDownload();
                                        }
                                    }).setNegativeButton("取消", new OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // 判断 新版本是否必须更新
                                    if (new_constraint.equals("1")) {
                                        cancelProgress();
                                        finish();
                                    } else {
                                        startNext();
                                    }
                                }
                            }).create().show();
                        } else {
                            startNext();
                        }
                    } else {
                        LogUtils.i(jo.getString("msg"));
                        ToastUtil.s(WelcomeActivity.this, getString(R.string.data_abnormal));
                        startNext();
                    }

                } catch (JSONException e) {
                    CatchUtil.catchM(e);
                    cancelProgress();
                    ToastUtil.s(WelcomeActivity.this, getString(R.string.data_abnormal));
                    finish();
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                LogUtils.i("网络异常");
                ToastUtil.s(WelcomeActivity.this, "网络异常");
                cancelProgress();
            }
        });
        http.send();
    }

    private void updateDownload() {

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(new_uri));
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }

    // 进入下个界面
    private void startNext() {
        new Thread() {
            @Override
            public void run() {
                super.run();
//                try {
//                    sleep(2500);
//                } catch (InterruptedException e) {
//                    CatchUtil.catchM(e);
//                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        if (MyApplication.logined) {
                        Intent intent = new Intent(WelcomeActivity.this, BaseActivity.class);
                        startActivity(intent);
                        WelcomeActivity.this.finish();

//                        } else {
//                            Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
//                            startActivity(intent);
//                            WelcomeActivity.this.finish();
//                        }
                        cancelProgress();
                    }
                });
            }
        }.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }
}
