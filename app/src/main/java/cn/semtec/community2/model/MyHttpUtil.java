package cn.semtec.community2.model;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.util.LogUtils;

import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import cn.semtec.community2.MyApplication;
import cn.semtec.community2.tool.Constants;
import cn.semtec.community2.util.CatchUtil;
import cn.semtec.community2.util.SharedPreferenceUtil;

public class MyHttpUtil {
    public static final int FAILURE = -1;
    public static final int CATCH = -2;
    public static final int SUCCESS0 = 0;
    public static final int SUCCESS100 = 100;
    public static final int SUCCESSELSE = 200;

    private HttpMethod MyMethod;
    private String MyUrl;
    private RequestParams MyParams;
    private RequestCallBack<String> MyCallBack;
    private String cellphone;
    private String password;

    public MyHttpUtil(HttpMethod method, String url, RequestCallBack<String> callBack) {
        this(method, url, null, callBack);
    }

    public MyHttpUtil(HttpMethod method, String url, RequestParams params,
                      RequestCallBack<String> callBack) {
        this.MyMethod = method;
        this.MyUrl = url;
        this.MyParams = params;
        this.MyCallBack = callBack;
    }

    public void send() {
        HttpUtils httpUtil = MyApplication.getHttpUtils();
        httpUtil.send(MyMethod, MyUrl, MyParams, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String mResult = responseInfo.result.toString();
                try {
                    JSONObject jo = new JSONObject(mResult);
                    if (jo.getInt("returnCode") == SUCCESS100) {
                        SharedPreferenceUtil preference = MyApplication.getSharedPreferenceUtil();
                        cellphone = preference.getString("cellphone");
                        password = preference.getString("password");
                        loginAgain();
                    } else {
                        MyCallBack.onSuccess(responseInfo);
                    }
                } catch (Exception e) {
                    CatchUtil.catchM(e);
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                MyCallBack.onFailure(error, msg);

            }
        });
    }

    private void loginAgain() {
        try {
            JSONObject user_json = new JSONObject();
            user_json.put("cellphone", cellphone);
            user_json.put("password", password);

            RequestParams params = new RequestParams();
            params.addHeader("Content-type", "application/json; charset=utf-8");
            params.setHeader("Accept", "application/json");
            StringEntity entity = new StringEntity(user_json.toString(), "UTF-8");
            params.setBodyEntity(entity);

            final String url = Constants.CONTENT_LOGIN;
            HttpUtils httpUtil = MyApplication.getHttpUtils();
            httpUtil.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    String mResult = responseInfo.result.toString();
                    try {
                        JSONObject jo = new JSONObject(mResult);
                        if (jo.getInt("returnCode") == 0) {

                            MyHttpUtil.this.send();
                        }
                    } catch (Exception e) {
                        CatchUtil.catchM(e);
                        LogUtils.i("数据异常");
                    }
                }

                @Override
                public void onFailure(HttpException error, String msg) {
                    LogUtils.i("网络异常:" + msg);
                }
            });
        } catch (Exception e) {
            CatchUtil.catchM(e);
        }
    }


}
