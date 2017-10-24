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

import cn.etsoft.smarthome.Domain.WareSceneEvent;
import cn.etsoft.smarthome.R;

/**
 * Author：FBL  Time： 2017/10/19.
 * 用户界面添加设备界面
 */

public class SetAddSceneAdapter extends BaseAdapter {

    private List<WareSceneEvent> sceneEvents;
    private Context context;


    public SetAddSceneAdapter(Context context, List<WareSceneEvent> sceneEvents) {
        this.context = context;
        this.sceneEvents = sceneEvents;
    }

    @Override
    public int getCount() {
        return sceneEvents.size();
    }

    @Override
    public Object getItem(int i) {
        return sceneEvents.get(i);
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

        if (sceneEvents.size() < 1)
            return view;
        viewHolder.mSceneSetGridViewItemName.setText(sceneEvents.get(position).getSceneName());
        viewHolder.mSceneSetGridViewItemIV.setImageResource(R.drawable.logo);
        if (sceneEvents.get(position).isSelect()) {
            viewHolder.mSceneSetGridViewItemSelect.setImageResource(R.drawable.select_ok);
        } else viewHolder.mSceneSetGridViewItemSelect.setImageResource(R.drawable.select_no);
        final ViewHolder finalViewHolder = viewHolder;
        viewHolder.mSceneSetGridViewItemSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sceneEvents.get(position).isSelect()) {
                    sceneEvents.get(position).setSelect(false);
                    finalViewHolder.mSceneSetGridViewItemSelect.setImageResource(R.drawable.select_no);
                } else {
                    sceneEvents.get(position).setSelect(true);
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
