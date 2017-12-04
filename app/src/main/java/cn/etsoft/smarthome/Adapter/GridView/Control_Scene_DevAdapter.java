package cn.etsoft.smarthome.Adapter.GridView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.etsoft.smarthome.Domain.WareSceneEvent;
import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;

/**
 * Author：FBL  Time： 2017/6/29.
 * 定时器 设置  设备适配器
 */

public class Control_Scene_DevAdapter extends BaseAdapter {

    private List<WareSceneEvent> scenes;
    private Context mContext;
    private static int[] images = new int[]{R.drawable.scene_baitian, R.drawable.scene_yejian,
            R.drawable.scene_quankai, R.drawable.scene_quanguan, R.drawable.scene_yongcan,
            R.drawable.scene_xiuxian};

    public Control_Scene_DevAdapter(Context context) {
        scenes = MyApplication.getWareData().getSceneEvents();
        mContext = context;
    }

    public void notifyDataSetChanged() {
        scenes = MyApplication.getWareData().getSceneEvents();
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (scenes == null)
            return 0;
        return scenes.size();
    }

    @Override
    public Object getItem(int position) {
        if (scenes == null)
            return null;
        return scenes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.girdview_controlscene, null, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (scenes.get(position).getSceneName().contains("白"))
            viewHolder.mGirdviewIv.setImageResource(R.drawable.scene_baitian);
        else if (scenes.get(position).getSceneName().contains("夜"))
            viewHolder.mGirdviewIv.setImageResource(R.drawable.scene_yejian);
        else if (scenes.get(position).getSceneName().contains("客"))
            viewHolder.mGirdviewIv.setImageResource(R.drawable.scene_huike);
        else if (scenes.get(position).getSceneName().contains("休"))
            viewHolder.mGirdviewIv.setImageResource(R.drawable.scene_xiuxian);
        else if (scenes.get(position).getSceneName().contains("全开"))
            viewHolder.mGirdviewIv.setImageResource(R.drawable.scene_quankai);
        else if (scenes.get(position).getSceneName().contains("全关"))
            viewHolder.mGirdviewIv.setImageResource(R.drawable.scene_quanguan);
        else if (scenes.get(position).getSceneName().contains("用餐"))
            viewHolder.mGirdviewIv.setImageResource(R.drawable.scene_yongcan);
        else
            viewHolder.mGirdviewIv.setImageResource(images[0]);
        viewHolder.mGirdviewTv.setText(scenes.get(position).getSceneName());
        return convertView;
    }


    static class ViewHolder {
        View view;
        ImageView mGirdviewIv;
        TextView mGirdviewTv;

        ViewHolder(View view) {
            this.view = view;
            this.mGirdviewIv = (ImageView) view.findViewById(R.id.girdview_iv);
            this.mGirdviewTv = (TextView) view.findViewById(R.id.girdview_tv);
        }
    }
}
