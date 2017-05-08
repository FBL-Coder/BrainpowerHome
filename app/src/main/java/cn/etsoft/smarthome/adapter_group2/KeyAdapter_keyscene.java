package cn.etsoft.smarthome.adapter_group2;

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
import cn.etsoft.smarthome.domain.ChnOpItem_scene;

/**
 * Created by Say GoBay on 2016/9/1.
 * 情景设置——灯光
 */
public class KeyAdapter_keyscene extends BaseAdapter {
    private Context context;
    private List<String> listData;
    private List<String> listData_all;
    int mSelect = 0;   //选中项
    private int Sceneid;
    private int keyinputPsoition;
    private boolean isClose = false;
    private String[] keyName;
    private List<ChnOpItem_scene.Key2sceneItemBean> items;

    public KeyAdapter_keyscene(Context context, int Sceneid, int keyinputPsoition, boolean isClose) {
        items = MyApplication.getWareData().getChnOpItem_scene().getKey2scene_item();
        this.Sceneid = Sceneid;
        this.keyinputPsoition = keyinputPsoition;
        this.isClose = isClose;
        keyName = MyApplication.getWareData_Scene().getKeyInputs().get(keyinputPsoition).getKeyName();
        //按键名称集合
        listData = new ArrayList<>();
        listData_all = new ArrayList<>();
        for (int i = 0; i < keyName.length; i++) {
            listData.add(keyName[i]);
            listData_all.add(keyName[i]);
        }

        if (isClose) {
            //打开只看选中按键的时候，先清空赋值
            for (int k = 0; k < items.size(); k++) {
                MyApplication.getWareData().getKeyInputs().get(keyinputPsoition).getKeyIsSelect()[k] = 0;
            }
            //打开只看选中按键的时候，赋值
            for (int k = 0; k < items.size(); k++) {
                if (items.get(k).getCanCpuID().equals(MyApplication.getWareData_Scene().getKeyInputs().get(keyinputPsoition).getDevUnitID()) &&
                        items.get(k).getEventId() == Sceneid) {
                    int index = items.get(k).getKeyIndex();
                    MyApplication.getWareData().getKeyInputs().get(keyinputPsoition).getKeyIsSelect()[index] = 1;
                }
            }

            for (int i = 0; i < listData.size(); i++) {
                for (int j = listData.size() - 1; j >= i; j--) {
                    if (MyApplication.getWareData().getKeyInputs().get(keyinputPsoition).getKeyIsSelect()[j] == 0) {
                        listData.remove(j);
                    }
                }
            }
        }

        this.context = context;
    }

    public void notifyDataSetChanged(List<String> listData) {
        this.listData = listData;
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (null != listData) {
            return listData.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    boolean isContan = true;

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(this.context).inflate(R.layout.gridview_item_light2, null, false);
            viewHolder.appliance = (ImageView) convertView.findViewById(R.id.appliance);
            viewHolder.mark = (ImageView) convertView.findViewById(R.id.mark);
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.on_off = (TextView) convertView.findViewById(R.id.on_off);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.title.setText(listData.get(position));

        if (items == null || items.size() == 0) {
            for (int i = 0; i < listData.size(); i++) {
                //设备默认图标  或者是默认为关闭状态
                viewHolder.appliance.setImageResource(R.drawable.key);
                viewHolder.mark.setImageResource(R.drawable.select);
            }
        } else {
            for (int i = 0; i < items.size(); i++) {
                if (items.get(i).getCanCpuID().equals(MyApplication.getWareData_Scene().getKeyInputs().get(keyinputPsoition).getDevUnitID()) &&
                        items.get(i).getEventId() == Sceneid) {
                    int index = items.get(i).getKeyIndex();
                    if (listData.get(position).equals(listData_all.get(index))) {
                        MyApplication.getWareData().getKeyInputs().get(keyinputPsoition).getKeyIsSelect()[index] = 1;
                        viewHolder.mark.setImageResource(R.drawable.selected);
                        isContan = false;
                        break;
                    }
                }
            }
        }
        if (isContan) {
            MyApplication.getWareData().getKeyInputs().get(keyinputPsoition).getKeyIsSelect()[position] = 0;
            viewHolder.mark.setImageResource(R.drawable.select);
        }
        viewHolder.on_off.setVisibility(View.GONE);
        viewHolder.appliance.setImageResource(R.drawable.key);


        viewHolder.mark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyApplication.getWareData().getKeyInputs().get(keyinputPsoition).getKeyIsSelect()[position] == 1) {
                    viewHolder.mark.setImageResource(R.drawable.select);
                    MyApplication.getWareData().getKeyInputs().get(keyinputPsoition).getKeyIsSelect()[position] = 0;
                    for (int i = 0; i < items.size(); i++) {
                        if (items.get(i).getCanCpuID().equals(MyApplication.getWareData_Scene().getKeyInputs().get(keyinputPsoition).getDevUnitID()) &&
                                items.get(i).getEventId() == Sceneid) {
                            items.remove(i);
                        }
                    }
                } else {
                    viewHolder.mark.setImageResource(R.drawable.selected);
                    ChnOpItem_scene.Key2sceneItemBean item = new ChnOpItem_scene.Key2sceneItemBean();
                    item.setCanCpuID(MyApplication.getWareData_Scene().getKeyInputs().get(keyinputPsoition).getDevUnitID());
                    item.setEventId(Sceneid);
                    item.setKeyIndex(position);
                    items.add(item);
                    MyApplication.getWareData().getKeyInputs().get(keyinputPsoition).getKeyIsSelect()[position] = 1;
                }
            }
        });
        return convertView;
    }

    public void changeSelected(int position) { //刷新方法
        if (position != mSelect) {
            mSelect = position;
            notifyDataSetChanged();
        }
    }

    private class ViewHolder {
        ImageView appliance, mark;
        TextView title, on_off;
    }
}
