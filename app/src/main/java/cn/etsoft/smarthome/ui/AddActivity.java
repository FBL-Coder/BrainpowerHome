package cn.etsoft.smarthome.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.domain.EquipmentBean;

/**
 * Created by Say GoBay on 2016/8/29.
 */
public class AddActivity extends Activity {
    private TextView mTitle;
    private ExpandableListView lv;
    private ArrayList<String> list;
    private ArrayList<EquipmentBean> list_two;
    private int[] image = {R.drawable.select, R.drawable.select, R.drawable.select, R.drawable.selected, R.drawable.select};
    private String[] title = {"按键板1", "按键板2", "按键板3", "按键板4", "按键板5"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_listview);
        //初始化标题栏
        initTitleBar();
        //初始化ListView
        initListView();
    }

    /**
     * 初始化标题栏
     */
    private void initTitleBar() {
        mTitle = (TextView) findViewById(R.id.tv_home);
        mTitle.setText("新增设备控制");
    }

    /**
     * 初始化ListView
     */
    private void initListView() {
        lv = (ExpandableListView) findViewById(R.id.add_lv);
        list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add("按键板 " + i);
        }

        list_two = new ArrayList<EquipmentBean>();
        for (int i = 0; i < 5; i++) {
            EquipmentBean item = new EquipmentBean(title[i], image[i]);
            list_two.add(item);
        }
        lv.setAdapter(new AddAdapter());
    }

    private class AddAdapter extends BaseExpandableListAdapter {

        @Override
        public int getGroupCount() {
            return list.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return list_two.size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return list.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return list_two.get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

            final ViewHolder viewHolder;
            if (convertView == null) {
                convertView = View.inflate(AddActivity.this, R.layout.add_listview_item, null);
                viewHolder = new ViewHolder();
                viewHolder.add_item = (TextView) convertView.findViewById(R.id.add_item);
                viewHolder.add_item_iv = (ImageView) convertView.findViewById(R.id.add_item_iv);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.add_item.setText(list.get(groupPosition));
            //更换图标
            viewHolder.add_item_iv.setImageResource(R.drawable.jiansub);
            if( !isExpanded){
                viewHolder.add_item_iv.setImageResource(R.drawable.jiaadd);
            }
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(AddActivity.this).inflate(R.layout.equipment_listview_item, null);
                viewHolder = new ViewHolder();
                viewHolder.title = (TextView) convertView.findViewById(R.id.equipment_tv);
                viewHolder.image = (ImageView) convertView.findViewById(R.id.equipment_iv);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.title.setText(list_two.get(childPosition).getTitleId());
            viewHolder.image.setImageResource(list_two.get(childPosition).getImageId());
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        public class ViewHolder {
            TextView add_item, title;
            ImageView image,add_item_iv;
        }
    }
}
