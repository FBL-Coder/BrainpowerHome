package cn.etsoft.smarthome.Adapter.GridView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jaygoo.widget.RangeSeekBar;

import java.util.List;

import cn.etsoft.smarthome.Domain.WareDev;
import cn.etsoft.smarthome.R;

/**
 * Author：FBL  Time： 2017/10/19.
 * 用户界面添加设备界面
 */

public class SetAddDevAdapter extends BaseAdapter {

    private List<WareDev> devs;
    private Context context;


    public SetAddDevAdapter(Context context, List<WareDev> devs) {
        this.context = context;
        this.devs = devs;
    }

    @Override
    public int getCount() {
        return devs.size();
    }

    @Override
    public Object getItem(int i) {
        return devs.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.gridview_sceneset_light_item, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        if (devs.size() < 1)
            return view;
        if (devs.get(position).getType() == 0) {
            viewHolder.mSceneSetGridViewItemName.setText(devs.get(position).getDevName());
            if (devs.get(position).getbOnOff() == 1) {
                viewHolder.mSceneSetGridViewItemIV.setImageResource(R.drawable.kt_dev_item_open);
            } else {
                viewHolder.mSceneSetGridViewItemIV.setImageResource(R.drawable.kt_dev_item_close);
            }
            if (devs.get(position).isSelect()) {
                viewHolder.mSceneSetGridViewItemSelect.setImageResource(R.drawable.select_ok);
            } else {
                viewHolder.mSceneSetGridViewItemSelect.setImageResource(R.drawable.select_no);
            }
        } else if (devs.get(position).getType() == 1) {
            viewHolder.mSceneSetGridViewItemName.setText(devs.get(position).getDevName());
            if (devs.get(position).getbOnOff() == 1) {
                viewHolder.mSceneSetGridViewItemIV.setImageResource(R.drawable.tv_sam_icon);
            } else {
                viewHolder.mSceneSetGridViewItemIV.setImageResource(R.drawable.tv_sam_icon);
            }
            if (devs.get(position).isSelect()) {
                viewHolder.mSceneSetGridViewItemSelect.setImageResource(R.drawable.select_ok);
            } else viewHolder.mSceneSetGridViewItemSelect.setImageResource(R.drawable.select_no);
        } else if (devs.get(position).getType() == 2) {
            viewHolder.mSceneSetGridViewItemName.setText(devs.get(position).getDevName());
            if (devs.get(position).getbOnOff() == 1) {
                viewHolder.mSceneSetGridViewItemIV.setImageResource(R.drawable.tv_sam_icon);
            } else {
                viewHolder.mSceneSetGridViewItemIV.setImageResource(R.drawable.tv_sam_icon);
            }
            if (devs.get(position).isSelect()) {
                viewHolder.mSceneSetGridViewItemSelect.setImageResource(R.drawable.select_ok);
            } else viewHolder.mSceneSetGridViewItemSelect.setImageResource(R.drawable.select_no);
        } else if (devs.get(position).getType() == 3) {
            viewHolder.mSceneSetGridViewItemName.setText(devs.get(position).getDevName());
            if (devs.get(position).getbOnOff() == 1) {
                viewHolder.mSceneSetGridViewItemIV.setImageResource(R.drawable.light_open);
            } else {
                viewHolder.mSceneSetGridViewItemIV.setImageResource(R.drawable.light_close);
            }
            if (devs.get(position).isSelect()) {
                viewHolder.mSceneSetGridViewItemSelect.setImageResource(R.drawable.select_ok);
            } else viewHolder.mSceneSetGridViewItemSelect.setImageResource(R.drawable.select_no);
        } else if (devs.get(position).getType() == 4) {
            viewHolder.mSceneSetGridViewItemName.setText(devs.get(position).getDevName());
            if (devs.get(position).getbOnOff() == 1) {
                viewHolder.mSceneSetGridViewItemIV.setImageResource(R.drawable.cl_dev_item_open);
            } else {
                viewHolder.mSceneSetGridViewItemIV.setImageResource(R.drawable.cl_dev_item_close);
            }
            if (devs.get(position).isSelect()) {
                viewHolder.mSceneSetGridViewItemSelect.setImageResource(R.drawable.select_ok);
            } else viewHolder.mSceneSetGridViewItemSelect.setImageResource(R.drawable.select_no);
        } else if (devs.get(position).getType() == 7) {
            viewHolder.mSceneSetGridViewItemName.setText(devs.get(position).getDevName());
            if (devs.get(position).getbOnOff() == 1) {
                viewHolder.mSceneSetGridViewItemIV.setImageResource(R.drawable.freshair_open);
            } else {
                viewHolder.mSceneSetGridViewItemIV.setImageResource(R.drawable.freshair_close);
            }
            if (devs.get(position).isSelect()) {
                viewHolder.mSceneSetGridViewItemSelect.setImageResource(R.drawable.select_ok);
            } else viewHolder.mSceneSetGridViewItemSelect.setImageResource(R.drawable.select_no);
        } else if (devs.get(position).getType() == 9) {
            viewHolder.mSceneSetGridViewItemName.setText(devs.get(position).getDevName());
            if (devs.get(position).getbOnOff() == 1) {
                viewHolder.mSceneSetGridViewItemIV.setImageResource(R.drawable.floorheat_open);
            } else {
                viewHolder.mSceneSetGridViewItemIV.setImageResource(R.drawable.floorheat_close);
            }
            if (devs.get(position).isSelect()) {
                viewHolder.mSceneSetGridViewItemSelect.setImageResource(R.drawable.select_ok);
            } else viewHolder.mSceneSetGridViewItemSelect.setImageResource(R.drawable.select_no);
        }
        final ViewHolder finalViewHolder = viewHolder;
        viewHolder.mSceneSetGridViewItemSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (devs.get(position).isSelect()) {
                    devs.get(position).setSelect(false);
                    finalViewHolder.mSceneSetGridViewItemSelect.setImageResource(R.drawable.select_no);
                } else {
                    devs.get(position).setSelect(true);
                    finalViewHolder.mSceneSetGridViewItemSelect.setImageResource(R.drawable.select_ok);
                }
                notifyDataSetChanged();
            }
        });
        return view;
    }

    static class ViewHolder {
        View view;
        TextView mSceneSetGridViewItemName;
        LinearLayout mItemTitle;
        ImageView mSceneSetGridViewItemIV;
        ImageView mSceneSetGridViewItemSelect;
        RangeSeekBar mSceneSetGridViewItemSlide;

        ViewHolder(View view) {
            this.view = view;
            this.mSceneSetGridViewItemName = (TextView) view.findViewById(R.id.SceneSet_GridView_Item_Name);
            this.mItemTitle = (LinearLayout) view.findViewById(R.id.item_title);
            this.mSceneSetGridViewItemIV = (ImageView) view.findViewById(R.id.SceneSet_GridView_Item_IV);
            this.mSceneSetGridViewItemSelect = (ImageView) view.findViewById(R.id.SceneSet_GridView_Item_Select);
            this.mSceneSetGridViewItemSlide = (RangeSeekBar) view.findViewById(R.id.SceneSet_GridView_Item_Slide);
            mSceneSetGridViewItemSlide.setVisibility(View.GONE);
        }
    }
}
