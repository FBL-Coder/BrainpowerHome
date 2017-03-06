package cn.semtec.community2.fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.util.LogUtils;

import org.linphone.squirrel.squirrelCallImpl;

import java.util.ArrayList;
import java.util.HashMap;

import cn.etsoft.smarthome.R;
import cn.semtec.community2.MyApplication;
import cn.semtec.community2.activity.CallingActivity;
import cn.semtec.community2.util.ToastUtil;

public class VideoFragment extends Fragment {
    private View layout;
    public static ArrayList<HashMap<String, String>> mlist = new ArrayList<HashMap<String, String>>();
    private MyAdapter adapter;
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.fragment_video, null);
        adapter = new MyAdapter();
        setView();
        return layout;
    }

    private void setView() {
        listView = (ListView) layout.findViewById(R.id.listView);
        listView.setAdapter(adapter);
    }

    private class MyAdapter extends BaseAdapter {

        public MyAdapter() {
            super();
        }

        @Override
        public int getCount() {
            return (mlist == null) ? 0 : mlist.size();
        }

        @Override
        public Object getItem(int position) {
            return mlist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @SuppressLint("InflateParams")
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_video_item, null);
                holder = new ViewHolder();

                holder.btn_open = (TextView) convertView.findViewById(R.id.btn_open);
                holder.tv_devName = (TextView) convertView.findViewById(R.id.tv_devName);
                holder.image = (ImageView) convertView.findViewById(R.id.image);
                convertView.setTag(holder);
            }
            holder = (ViewHolder) convertView.getTag();
            final HashMap<String, String> map = mlist.get(position);
            holder.tv_devName.setText(map.get("obj_name"));

            holder.btn_open.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (squirrelCallImpl.login_state != 1) {
                        LoseAndAgian();
                        return;
                    }
//                    String s = SipCompress.compress(MyApplication.houseProperty.sipnum, MyApplication.cellphone);
                    // 开门
                    ((squirrelCallImpl) getActivity().getApplication()).squirrelSendMessage(map.get("obj_sipnum")
                            ,MyApplication.houseProperty.sipaddr,squirrelCallImpl.serverport
                            ,squirrelCallImpl.OPENDOOR,100);
                }
            });
            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (squirrelCallImpl.login_state != 1) {
                        LoseAndAgian();
                        return;
                    }
                    // 监听Item 点击item跳转到监控画面
                    LogUtils.i("进入" + mlist.get(position).get("obj_name") + "通话对讲");
                    Intent intent = new Intent(getActivity(), CallingActivity.class);
                    intent.putExtra("username", mlist.get(position).get("obj_sipnum"));
                    intent.putExtra("devicename", mlist.get(position).get("obj_name"));
                    startActivity(intent);
                    // id从list中获取
                    ((squirrelCallImpl) getActivity().getApplication()).squirrelCall(mlist.get(position).get("obj_sipnum"), 1);
                }
            });
            return convertView;
        }

        public class ViewHolder {
            TextView tv_devName;
            TextView btn_open;
            ImageView image;
        }
    }

    private void LoseAndAgian() {
        if (MyApplication.houseProperty == null) {
            return;
        }
        squirrelCallImpl instan = (squirrelCallImpl) getActivity().getApplication();
        instan.squirrelAccountLogin(MyApplication.houseProperty.sipaddr, squirrelCallImpl.serverport, 1, null,
                MyApplication.houseProperty.sipnum, MyApplication.houseProperty.sippassword, null, 1);
        ToastUtil.l(getActivity(), "连接失败，请检查\n网络连接并重试！");
    }
}
