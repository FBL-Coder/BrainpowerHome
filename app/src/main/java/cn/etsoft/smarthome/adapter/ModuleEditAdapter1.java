package cn.etsoft.smarthome.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.utils.ToastUtil;

/**
 * Created by Say GoBay on 2016/9/1.
 * 高级设置-控制设置-按键情景—按键的适配器
 */
public class ModuleEditAdapter1 extends BaseAdapter {
    private Context context;
    private List<String> listData;
    int mSelect = 0;   //选中项

    public ModuleEditAdapter1(Context context, List<String> listData) {
        this.listData = listData;
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

    int pos;

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(this.context).inflate(R.layout.gridview_item_light6, null, false);
            viewHolder.appliance = (ImageView) convertView.findViewById(R.id.appliance);
            viewHolder.sure = (ImageView) convertView.findViewById(R.id.sure);
            viewHolder.cancel = (ImageView) convertView.findViewById(R.id.cancel);
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            viewHolder.name_edit = (EditText) convertView.findViewById(R.id.name_edit);
            viewHolder.name_ll = (LinearLayout) convertView.findViewById(R.id.name_ll);
            viewHolder.name_edit_ll = (LinearLayout) convertView.findViewById(R.id.name_edit_ll);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.appliance.setImageResource(R.drawable.g);
        pos = position + 1;
        viewHolder.title.setText("按键" + pos);
        viewHolder.name.setText("");
        viewHolder.name.setHint(listData.get(position));
        viewHolder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.name_ll.setVisibility(View.GONE);
                viewHolder.name_edit_ll.setVisibility(View.VISIBLE);
            }
        });
        viewHolder.sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!"".equals(viewHolder.name_edit.getText().toString())) {
                    listData.set(position, viewHolder.name_edit.getText().toString());
                }else {
                    listData.set(position, listData.get(position));
                }
                if (viewHolder.name_edit.getText().toString().length() > 24) {
                    ToastUtil.showToast(context, "输入按键名称不能过长");
                    return;
                }
                notifyDataSetChanged(listData);
                viewHolder.name_edit_ll.setVisibility(View.GONE);
                viewHolder.name_ll.setVisibility(View.VISIBLE);
            }
        });
        viewHolder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listData.set(position, listData.get(position));
                notifyDataSetChanged(listData);
                viewHolder.name_edit_ll.setVisibility(View.GONE);
                viewHolder.name_ll.setVisibility(View.VISIBLE);
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
        ImageView appliance, sure, cancel;
        TextView title, name;
        EditText name_edit;
        LinearLayout name_ll, name_edit_ll;
    }
}
