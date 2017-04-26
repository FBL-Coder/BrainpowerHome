package cn.etsoft.smarthome.adapter_main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.pullmi.entity.WareSceneEvent;
import cn.etsoft.smarthome.utils.ToastUtil;

/**
 * Created by Say GoBay on 2016/9/1.
 */
public class SceneAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    List<WareSceneEvent> list_scene;
    Context context;

    public SceneAdapter(List<WareSceneEvent> list_scene, Context context, LayoutInflater inflater) {
        mInflater = inflater;
        this.list_scene = list_scene;
        this.context = context;
    }

    @Override
    public int getCount() {
        if (null != list_scene) {
            return list_scene.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return list_scene.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.gridview_item_scene, null);
            viewHolder = new ViewHolder();
            viewHolder.time = (ImageView) convertView.findViewById(R.id.time);
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.moudle = (TextView) convertView.findViewById(R.id.moudle);
            viewHolder.ll_scene_time = (LinearLayout) convertView.findViewById(R.id.ll_scene_time);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.title.setText(list_scene.get(position).getSceneName());

        viewHolder.time.setImageResource(R.drawable.time);
        viewHolder.moudle.setText("定时");

        viewHolder.ll_scene_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showToast(context,"暂不可用");
            }
        });
        return convertView;
    }

    private class ViewHolder {
        ImageView time;
        TextView title, moudle;
        LinearLayout ll_scene_time;
    }
}
