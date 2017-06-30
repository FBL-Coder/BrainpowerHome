package cn.etsoft.smarthome.adapter_group3;

import android.content.Context;
import android.util.Log;
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
 * 高级设置-控制设置-按键情景—按键的适配器
 */
public class KeyAdapter_keyscene extends BaseAdapter {
    private Context context;
    private List<String> listData;
    private List<String> listData_beremove;
    private List<String> listData_all;
    int mSelect = 0;   //选中项
    private int Sceneid;
    private int keyinputPsoition;
    private boolean ISCHOOSE = false;
    private String[] keyName;
    private List<ChnOpItem_scene.Key2sceneItemBean> items;
    private int KeyCnt;

    public KeyAdapter_keyscene(Context context, int Sceneid, int keyinputPsoition, boolean ISCHOOSE) {
        items = MyApplication.getWareData().getChnOpItem_scene().getKey2scene_item();
        this.Sceneid = Sceneid;
        this.keyinputPsoition = keyinputPsoition;
        this.ISCHOOSE = ISCHOOSE;
        keyName = MyApplication.getWareData_Scene().getKeyInputs().get(keyinputPsoition).getKeyName();
        KeyCnt = MyApplication.getWareData_Scene().getKeyInputs().get(keyinputPsoition).getKeyCnt();
        //按键名称集合
        listData = new ArrayList<>();
        //将要移除的按键名
        listData_beremove = new ArrayList<>();
        listData_all = new ArrayList<>();

        if (KeyCnt > keyName.length) {
            for (int i = 0; i < KeyCnt; i++) {
                if (i >= keyName.length) {
                    listData.add("按键" + i);
                    listData_all.add("按键" + i);
                } else {
                    listData.add(keyName[i]);
                    listData_all.add(keyName[i]);
                }
            }
        } else {
            for (int i = 0; i < KeyCnt; i++) {
                listData.add(keyName[i]);
                listData_all.add(keyName[i]);
            }
        }
        if (ISCHOOSE) {
            //打开只看选中按键的时候，先清空赋值
            for (int k = 0; k < 8; k++) {
                try {
                    MyApplication.getWareData().getKeyInputs().get(keyinputPsoition).getKeyIsSelect()[k] = 0;
                } catch (Exception e) {
                    Log.e("Exception", k + "----" + e + "");
                }
            }
            //打开只看选中按键的时候，赋值
            for (int k = 0; k < items.size(); k++) {
                if (items.get(k).getCanCpuID().equals(MyApplication.getWareData_Scene().getKeyInputs().get(keyinputPsoition).getDevUnitID()) &&
                        items.get(k).getEventId() == Sceneid) {
                    int index = items.get(k).getKeyIndex();
                    MyApplication.getWareData().getKeyInputs().get(keyinputPsoition).getKeyIsSelect()[index] = 1;
                }
            }
            //打开只看选中按键的时候，将未选中的按键去掉
            for (int i = 0; i < MyApplication.getWareData().getKeyInputs().get(keyinputPsoition).getKeyIsSelect().length; i++) {
                if (MyApplication.getWareData().getKeyInputs().get(keyinputPsoition).getKeyIsSelect()[i] == 0) {
                    listData_beremove.add(listData.get(i));
                }
            }

            for (int i = 0; i < listData.size(); i++) {
                for (int j = 0; j < listData_beremove.size(); j++) {
                    if (listData.get(i).equals(listData_beremove.get(j)))
                        listData.remove(i);
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
            convertView = LayoutInflater.from(this.context).inflate(R.layout.gridview_item_light4, null, false);
            viewHolder.appliance = (ImageView) convertView.findViewById(R.id.appliance);
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.title.setText(listData.get(position));
        viewHolder.appliance.setImageResource(R.drawable.g);
        if (items == null || items.size() == 0) {
            for (int i = 0; i < listData.size(); i++) {
                //设备默认图标  或者是默认为关闭状态
                viewHolder.appliance.setImageResource(R.drawable.g);
            }
        } else {
            for (int i = 0; i < items.size(); i++) {
                if (items.get(i).getCanCpuID().equals(MyApplication.getWareData_Scene().getKeyInputs().get(keyinputPsoition).getDevUnitID()) &&
                        items.get(i).getEventId() == Sceneid) {
                    int index = items.get(i).getKeyIndex();
                    //index位置的按键名称与原始按键名称进行匹配，相同的时候，此按键进行选中操作
                    if (listData.get(position).equals(listData_all.get(index))) {
                        MyApplication.getWareData().getKeyInputs().get(keyinputPsoition).getKeyIsSelect()[index] = 1;
                        viewHolder.appliance.setImageResource(R.drawable.k);
                        isContan = false;
                        break;
                    }
                }
            }
        }
        //解决一个情景下多个按键被选中，只显示选中一个的问题
        if (isContan) {
            MyApplication.getWareData().getKeyInputs().get(keyinputPsoition).getKeyIsSelect()[position] = 0;
            viewHolder.appliance.setImageResource(R.drawable.g);
        }

        viewHolder.appliance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyApplication.getWareData().getKeyInputs().get(keyinputPsoition).getKeyIsSelect()[position] == 1) {
                    viewHolder.appliance.setImageResource(R.drawable.g);
                    //将状态改为未选中
                    MyApplication.getWareData().getKeyInputs().get(keyinputPsoition).getKeyIsSelect()[position] = 0;
                    //如果是选中，去掉选中之后，将选中的数据去掉
                    for (int i = 0; i < items.size(); i++) {
                        if (items.get(i).getCanCpuID().equals(MyApplication.getWareData_Scene().getKeyInputs().get(keyinputPsoition).getDevUnitID()) &&
                                items.get(i).getEventId() == Sceneid) {
                            items.remove(i);
                        }
                    }
                } else {
                    viewHolder.appliance.setImageResource(R.drawable.k);
                    //如果是未选中，选中之后，将选中的数据添加进去
                    ChnOpItem_scene.Key2sceneItemBean item = new ChnOpItem_scene.Key2sceneItemBean();
                    item.setCanCpuID(MyApplication.getWareData_Scene().getKeyInputs().get(keyinputPsoition).getDevUnitID());
                    item.setEventId(Sceneid);
                    item.setKeyIndex(position);
                    items.add(item);
                    //将状态改为选中
                    MyApplication.getWareData().getKeyInputs().get(keyinputPsoition).getKeyIsSelect()[position] = 1;
                }
            }
        });
        return convertView;
    }

    //刷新方法
    public void changeSelected(int position) {
        if (position != mSelect) {
            mSelect = position;
            notifyDataSetChanged();
        }
    }

    private class ViewHolder {
        ImageView appliance;
        TextView title;
    }
}
