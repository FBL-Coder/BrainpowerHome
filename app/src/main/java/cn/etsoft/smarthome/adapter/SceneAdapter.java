package cn.etsoft.smarthome.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.pullmi.common.CommonUtils;
import cn.etsoft.smarthome.pullmi.entity.WareSceneEvent;

/**
 * Created by Say GoBay on 2016/8/29.
 */
public class SceneAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<WareSceneEvent> listViewItems;
    private List<WareSceneEvent> mSceneEvents;
    private int[] image = {R.drawable.zaijiamoshi, R.drawable.waichumoshi,
            R.drawable.yingyuanmoshi, R.drawable.jiuqingmoshi,
            R.drawable.huikemoshi};

    public SceneAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        listViewItems = MyApplication.getWareData().getSceneEvents();
        mSceneEvents = new ArrayList<>();
        mSceneEvents.addAll(listViewItems);
        mSceneEvents.add(null);
    }
    @Override
    public int getCount() {
        if (null != mSceneEvents) {
            return mSceneEvents.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return mSceneEvents.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.sceneset_listview_item, null);
            viewHolder = new ViewHolder();
            viewHolder.image = (ImageView) convertView.findViewById(R.id.sceneset_iv);
            viewHolder.title = (TextView) convertView.findViewById(R.id.sceneset_tv);
            viewHolder.hui = (ImageView) convertView.findViewById(R.id.sceneset_hui);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (listViewItems.size() > 0) {
            int mcount = listViewItems.size();

            if (position < mcount) {
                viewHolder.image.setImageResource(image[position]);
                viewHolder.title.setText(mSceneEvents.get(position).getSceneName());
                viewHolder.hui.setImageResource(R.drawable.huijiantou);
            } else {
                viewHolder.image.setImageResource(R.drawable.xingzengmoshi);
                viewHolder.title.setText("新增模式");
            }
        }
        return convertView;
    }
    public class ViewHolder {
        private ImageView image, hui;
        public TextView title;
    }
}
