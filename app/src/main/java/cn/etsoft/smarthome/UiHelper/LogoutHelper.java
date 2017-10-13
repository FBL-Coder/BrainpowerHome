package cn.etsoft.smarthome.UiHelper;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.example.abc.mybaseactivity.HttpGetDataUtils.HttpCallback;
import com.example.abc.mybaseactivity.HttpGetDataUtils.OkHttpUtils;
import com.example.abc.mybaseactivity.HttpGetDataUtils.ResultDesc;
import com.example.abc.mybaseactivity.OtherUtils.AppSharePreferenceMgr;
import com.example.abc.mybaseactivity.OtherUtils.ToastUtil;
import com.google.gson.Gson;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.util.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.etsoft.smarthome.Utils.GlobalVars;
import cn.etsoft.smarthome.Domain.Http_Result;
import cn.etsoft.smarthome.Domain.WareData;
import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.Utils.Data_Cache;
import cn.etsoft.smarthome.Utils.NewHttpPort;
import cn.semtec.community2.model.MyHttpUtil;
import cn.semtec.community2.tool.Constants;

/**
 * Author：FBL  Time： 2017/9/28.
 */

public class LogoutHelper {


    public static void logout(final Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("提示");
        builder.setMessage("您是否要退出登录？");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                Map<String, String> params = new HashMap<>();
                params.put("uid", GlobalVars.getUserid());
                OkHttpUtils.postAsyn(NewHttpPort.ROOT + NewHttpPort.LOCATION + NewHttpPort.LOGOUT, params, new HttpCallback() {
                    @Override
                    public void onSuccess(ResultDesc resultDesc) {
                        super.onSuccess(resultDesc);
                        Log.i("LOGOUT", "智能家居成功: " + resultDesc.getResult());
                        Gson gson = new Gson();
                        Http_Result result = gson.fromJson(resultDesc.getResult(), Http_Result.class);
                        if (result.getCode() == 0) {
//                                    logout_yun();
                            logout_event(activity);
                        } else {
                            Log.i("LOGOUT", "智能家具失败: " + resultDesc.getResult());
                            if ("".equals(result.getMsg()))
                                ToastUtil.showText("退出失败");
                            else
                                ToastUtil.showText(result.getMsg());
                        }
                    }

                    @Override
                    public void onFailure(int code, String message) {
                        super.onFailure(code, message);
                        Log.i("LOGOUT", "智能家具onFailure: " + code + message);
                        ToastUtil.showText("退出失败");
                    }
                });
            }
        });
        builder.create().show();
    }

    private static void logout_yun(final Activity activity) {
        String url = Constants.CONTENT_LOGOUT;
        MyHttpUtil httpUtil = new MyHttpUtil(HttpRequest.HttpMethod.POST, url,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        String mResult = responseInfo.result.toString();
                        try {
                            JSONObject jo = new JSONObject(mResult);
                            if (jo.getInt("returnCode") == 0) {
                                Log.i("LOGOUT", "云对讲服务器登出成功 ");
                                logout_event(activity);
                            } else {
                                Log.i("LOGOUT", "云对讲服务器登出失败 ");
                                ToastUtil.showText("登出失败");
                            }
                        } catch (JSONException e) {
                            Log.i("LOGOUT", "云对讲服务器登出失败 ");
                            ToastUtil.showText("数据处理失败");
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        LogUtils.i("网络异常" + error + "------" + msg);
                    }
                });
        httpUtil.send();
    }

    private static void logout_event(Activity context) {
        ToastUtil.showText("登出成功");
        Data_Cache.writeFile(GlobalVars.getDevid(), new WareData());
        GlobalVars.setDevid("");
        GlobalVars.setDevpass("");
        GlobalVars.setUserid("");
        AppSharePreferenceMgr.put(GlobalVars.RCUINFOID_SHAREPREFERENCE, "");
        AppSharePreferenceMgr.put(GlobalVars.SAFETY_TYPE_SHAREPREFERENCE, 0);
        AppSharePreferenceMgr.put(GlobalVars.RCUINFOLIST_SHAREPREFERENCE, "");
        AppSharePreferenceMgr.put(GlobalVars.LOGIN_SHAREPREFERENCE, false);
        try {
            MyApplication.mApplication.getmHomeActivity().finish();
        } catch (Exception e) {
        }
        MyApplication.mApplication.dismissLoadDialog();
        context.startActivity(new Intent(context, cn.semtec.community2.activity.LoginActivity.class));
        context.finish();
    }
}
