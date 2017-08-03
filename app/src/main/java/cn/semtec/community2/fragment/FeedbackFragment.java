package cn.semtec.community2.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.util.LogUtils;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;

import cn.semtec.community2.MyApplication;
import cn.etsoft.smarthome.R;
import cn.semtec.community2.model.MyHttpUtil;
import cn.semtec.community2.tool.Constants;
import cn.semtec.community2.util.CatchUtil;
import cn.semtec.community2.util.ImageUtils;
import cn.semtec.community2.util.ToastUtil;

public class FeedbackFragment extends Fragment implements View.OnClickListener {
    private View layout;
    private TextView tab_1, tab_2, tab_3;
    private EditText et_content;
    private View img_photo;
    private LinearLayout ll_pic;
    private View btn_next;
    private Dialog buffer;
    private int tab_typy = 1;//投诉问题类型1:小区环境；2：房屋报修；3：其他
    private ArrayList<Uri> pic_list = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.fragment_feedback, null);
        setView();
        setListener();

        return layout;
    }


    private void setView() {
        tab_1 = (TextView) layout.findViewById(R.id.tab_1);
        tab_2 = (TextView) layout.findViewById(R.id.tab_2);
        tab_3 = (TextView) layout.findViewById(R.id.tab_3);
        et_content = (EditText) layout.findViewById(R.id.et_content);
        img_photo = layout.findViewById(R.id.img_photo);
        ll_pic = (LinearLayout) layout.findViewById(R.id.ll_pic);
        btn_next = layout.findViewById(R.id.btn_next);
    }

    private void setListener() {
        tab_1.setOnClickListener(this);
        tab_2.setOnClickListener(this);
        tab_3.setOnClickListener(this);
        img_photo.setOnClickListener(this);
        btn_next.setOnClickListener(this);
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tab_1:
                tab_1.setBackgroundResource(R.drawable.small_btn_checked);
                tab_2.setBackgroundResource(R.drawable.small_btn);
                tab_3.setBackgroundResource(R.drawable.small_btn);
                tab_1.setTextColor(getResources().getColor(R.color.white1));
                tab_2.setTextColor(getResources().getColor(R.color.text666));
                tab_3.setTextColor(getResources().getColor(R.color.text666));
                tab_typy = 1;
                break;
            case R.id.tab_2:
                tab_2.setBackgroundResource(R.drawable.small_btn_checked);
                tab_1.setBackgroundResource(R.drawable.small_btn);
                tab_3.setBackgroundResource(R.drawable.small_btn);
                tab_2.setTextColor(getResources().getColor(R.color.white1));
                tab_1.setTextColor(getResources().getColor(R.color.text666));
                tab_3.setTextColor(getResources().getColor(R.color.text666));
                tab_typy = 2;
                break;
            case R.id.tab_3:
                tab_3.setBackgroundResource(R.drawable.small_btn_checked);
                tab_1.setBackgroundResource(R.drawable.small_btn);
                tab_2.setBackgroundResource(R.drawable.small_btn);
                tab_3.setTextColor(getResources().getColor(R.color.white1));
                tab_1.setTextColor(getResources().getColor(R.color.text666));
                tab_2.setTextColor(getResources().getColor(R.color.text666));
                tab_typy = 3;
                break;
            case R.id.img_photo:
                if (pic_list.size() >= 4) {
                    ToastUtil.s(getActivity(), "最多上传4张图片!");
                    return;
                }
//                if (Build.VERSION.SDK_INT < 19) {
//                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                    intent.addCategory(Intent.CATEGORY_OPENABLE);
//                    intent.setType("image/*");
//                    startActivityForResult(intent, 100);
//                } else {
//                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
//                    intent.addCategory(Intent.CATEGORY_OPENABLE);
//                    intent.setType("image/*");
//                    startActivityForResult(intent, 100);
//                }
                try {
                    Intent albumIntent = new Intent(Intent.ACTION_PICK, null);
                    albumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                    startActivityForResult(albumIntent, 100);
                } catch (Exception e) {
                    CatchUtil.catchM(e);
                }
                break;
            case R.id.btn_next:
                if (MyApplication.cellphone == null) {
                    ToastUtil.s(getActivity(), "您当前没有登录！");
                    return;
                }
                if (MyApplication.houseProperty == null) {
                    ToastUtil.s(getActivity(), "您当前还没有房产，请先绑定房产！");
                    return;
                }
                if (et_content.getText().toString().length() < 5) {
                    ToastUtil.s(getActivity(), "请详细描述内容！");
                } else {
                    try {
                        sentToNET();
                    } catch (Exception e) {
                        CatchUtil.catchM(e);
                    }
                }
                break;

        }
    }

    private void sentToNET() throws Exception {
        String url = Constants.PROPERTY_CREATE;
        RequestParams params = new RequestParams();
        params.addBodyParameter("type", "" + tab_typy);
        params.addBodyParameter("infoDetails", et_content.getText().toString());
        for (int i = 0; i < pic_list.size(); i++) {
            Bitmap bitmap = ImageUtils.compressImage(pic_list.get(i), 200 * 1024);
            File file = new File(getActivity().getCacheDir(), MyApplication.cellphone + "_" + new Date().getTime() + ".jpg");
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
            if (file != null && file.exists()) {
                params.addBodyParameter("file" + i, file, "image/jpg");
            }
        }
        MyHttpUtil httpUtil = new MyHttpUtil(HttpRequest.HttpMethod.POST, url, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(
                            ResponseInfo<String> responseInfo) {
                        String mResult = responseInfo.result.toString();
                        try {
                            JSONObject jo = new JSONObject(mResult);
                            if (jo.getInt("returnCode") == 0) {
                                ToastUtil.s(getActivity(), "提交成功");
                                LogUtils.i("提交成功");
                                getActivity().finish();
                            } else {
                                ToastUtil.s(getActivity(), jo.getString("msg"));
                                LogUtils.i(jo.getString("msg"));
                            }
                        } catch (Exception e) {
                            CatchUtil.catchM(e);
                        }
                    }

                    @Override
                    public void onFailure(HttpException error,
                                          String msg) {
                        cancelProgress();
                        ToastUtil.s(getActivity(), getString(R.string.net_abnormal));
                        LogUtils.i(getString(R.string.net_abnormal) + msg);
                    }
                });
        httpUtil.send();
        showProgress();
    }

    protected void showProgress() {
        if (buffer == null) {
            buffer = new AlertDialog.Builder(getActivity()).create();
            buffer.setCanceledOnTouchOutside(false);
        }
        buffer.show();
        buffer.getWindow().setContentView(R.layout.buffer_bar);
    }

    protected void cancelProgress() {
        if (buffer != null) {
            buffer.cancel();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            Uri fromUri = ImageUtils.findUri(getActivity(), data);
            pic_list.add(fromUri);
            ImageView view = new ImageView(getActivity());
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(((int) MyApplication.density * 44),
                    LinearLayout.LayoutParams.MATCH_PARENT);
            lp.setMargins((int) (10 * MyApplication.density), 0, 0, 0);
            view.setLayoutParams(lp);
            view.setImageBitmap(ImageUtils.compressImage(fromUri, 50 * 1024));
            ll_pic.addView(view);
        }
    }
}
