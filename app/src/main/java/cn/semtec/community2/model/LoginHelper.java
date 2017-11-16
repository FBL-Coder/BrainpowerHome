package cn.semtec.community2.model;

import android.os.Handler;
import android.util.Log;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.util.LogUtils;

import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONObject;
import org.linphone.squirrel.squirrelCallImpl;

import java.util.HashSet;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import cn.semtec.community2.MyApplication;
import cn.semtec.community2.adapter.MyActAdapter;
import cn.semtec.community2.entity.HouseProperty;
import cn.semtec.community2.tool.Constants;
import cn.semtec.community2.util.CatchUtil;
import cn.semtec.community2.util.SharedPreferenceUtil;

public class LoginHelper {
    private SharedPreferenceUtil prefernceUtil;
    public Handler LoginHandler;

    public LoginHelper(Handler handler) {
        LoginHandler = handler;

    }

    public void loginServer(final String cellphone, final String password) {

        try {
            final JSONObject user = new JSONObject();
            user.put("cellphone", cellphone);
            user.put("password", password);

            RequestParams params = new RequestParams();
            params.addHeader("Content-type", "application/json; charset=utf-8");
            params.setHeader("Accept", "application/json");
            StringEntity entity = new StringEntity(user.toString(), "UTF-8");
            params.setBodyEntity(entity);

            String url = Constants.CONTENT_LOGIN;

            MyHttpUtil httpUtil = new MyHttpUtil(HttpMethod.POST, url, params, new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    String mResult = responseInfo.result.toString();
                    try {
                        // 获得回传的 json字符串
                        JSONObject jo = new JSONObject(mResult);
                        // 0为成功 <0为系统异常 其他待定
                        if (jo.getInt("returnCode") == 0) {
                            cn.etsoft.smarthome.MyApplication.mApplication.setVisitor(false);
                                prefernceUtil = new SharedPreferenceUtil(MyApplication.getContext());
                            prefernceUtil.putString("cellphone", cellphone);
                            prefernceUtil.putString("password", password);
                            LogUtils.i("登录成功");
                            MyApplication.cellphone = cellphone;
                            MyApplication.logined = true;
                            LoginHandler.sendEmptyMessage(MyHttpUtil.SUCCESS0);
                            JSONArray object = (JSONArray) jo.get("object");
                            MyApplication.houseList.clear();
                            String houseId_last = prefernceUtil.getString("houseId_last");
//解析 储存房产数据
                            Log.e("object", object + "");
                            for (int i = 0; i < object.length(); i++) {
                                JSONObject args = (JSONObject) object.get(i);
                                HouseProperty houseproperty = new HouseProperty();

                                houseproperty.communityName = args.getString("communityName");
                                houseproperty.roomName = args.getString("roomName");
                                houseproperty.sipaddr = args.getString("sipaddr");
                                houseproperty.sipnum = args.getString("sipnum");
                                houseproperty.sippassword = args.getString("sippassword");
                                houseproperty.blockName = args.getString("blockName");
                                houseproperty.buildName = args.getString("buildName");
                                houseproperty.isMainSip = args.getString("isMainSip");
                                houseproperty.userType = args.getInt("userType"); //1是业主，2是户主，3是普通用户
                                houseproperty.houseId = args.getString("houseId");
                                houseproperty.userName = args.getString("userName");

                                RegistJpush(houseproperty.sipnum);
                                if (houseproperty.houseId.equals(houseId_last)) {
                                    MyApplication.houseList.add(0, houseproperty);
                                } else {
                                    MyApplication.houseList.add(houseproperty);
                                }
                            }
                            //轮询房产数据
                            squirrelCallImpl instan = (squirrelCallImpl) squirrelCallImpl.instance;
                            try {
                                for (int i = 0; i < MyApplication.houseList.size(); i++) {
                                    HouseProperty houseproperty = MyApplication.houseList.get(i);
                                    if (i == 0) {
                                        instan.squirrelAccountLogin(houseproperty.sipaddr, squirrelCallImpl.serverport, 1,
                                                null, houseproperty.sipnum, houseproperty.sippassword, null, 1);
                                        MyApplication.houseProperty = houseproperty;
                                        // 获取登录用户有权限的门禁数据
                                        MyActAdapter.getPublicClock();
                                    } else {
                                        instan.squirrelAccountLogin(houseproperty.sipaddr, squirrelCallImpl.serverport, 1,
                                                null, houseproperty.sipnum, houseproperty.sippassword, null, 0);
                                    }

                                }
                            } catch (Exception e) {
                                LoginHandler.sendEmptyMessage(MyHttpUtil.SUCCESSELSE);
                                return;
                            }catch (Error error){
                                LoginHandler.sendEmptyMessage(MyHttpUtil.SUCCESSELSE);
                                return;
                            }
                        } else {
                            LoginHandler.sendEmptyMessage(MyHttpUtil.SUCCESSELSE);
                        }
                    } catch (Exception e) {
                        CatchUtil.catchM(e);
                        LogUtils.i("数据异常");
                        LoginHandler.sendEmptyMessage(MyHttpUtil.CATCH);
                    }
                }

                @Override
                public void onFailure(HttpException error, String msg) {
                    LogUtils.i("网络异常:" + msg);
                    LoginHandler.sendEmptyMessage(MyHttpUtil.FAILURE);
                }
            });
            httpUtil.send();
        } catch (Exception e) {
            CatchUtil.catchM(e);
        }
    }

    public void RegistJpush(String sipnum) {
        String areacode = sipnum.substring(0, 4);
        String communitynum = sipnum.substring(0, 8);
        HashSet<String> set = new HashSet<String>();
        set.add(areacode);
        set.add(communitynum);
        set.add(sipnum);
        Log.e("sipnum", sipnum + "   " + MyApplication.cellphone);
        JPushInterface.setAliasAndTags(MyApplication.instance, MyApplication.cellphone, set, new TagAliasCallback() {
            @Override
            public void gotResult(int responseCode, String alias, Set<String> arg2) {
                if (responseCode == 0) {
                    LogUtils.e("jpush  注册成功");
                } else {
                    LogUtils.e("jpush 注册失败   错误码" + responseCode);
                }
            }
        });
    }
}
