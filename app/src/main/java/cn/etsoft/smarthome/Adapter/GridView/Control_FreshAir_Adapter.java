package cn.etsoft.smarthome.Adapter.GridView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.etsoft.smarthome.Domain.WareFloorHeat;
import cn.etsoft.smarthome.Domain.WareFreshAir;
import cn.etsoft.smarthome.Domain.WareLight;
import cn.etsoft.smarthome.R;

/**
 * Author：FBL  Time： 2017/6/26.
 * 控制页面——灯光 适配器
 */

public class Control_FreshAir_Adapter extends BaseAdapter {

    private Context mContext;
    private List<WareFreshAir> mFreshAir;

    public Control_FreshAir_Adapter(Context context, List<WareFreshAir> freshAirs) {
        mFreshAir = freshAirs;
        mContext = context;
    }

    public void notifyDataSetChanged(List<WareFreshAir> freshAirs) {
        this.mFreshAir = freshAirs;
        super.notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return mFreshAir.size();
    }

    @Override
    public Object getItem(int position) {
        return mFreshAir.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHoler viewHoler = null;
        if (convertView == null) {
            viewHoler = new ViewHoler();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.gridview_control_light_item, null);
            viewHoler.mName = (TextView) convertView.findViewById(R.id.Control_GridView_Item_Name);
            viewHoler.mIV = (ImageView) convertView.findViewById(R.id.Control_GridView_Item_IV);
            convertView.setTag(viewHoler);
        } else viewHoler = (ViewHoler) convertView.getTag();
        if (mFreshAir.get(position).getbOnOff() == 0)
            viewHoler.mIV.setImageResource(R.drawable.freshair_close);
        else viewHoler.mIV.setImageResource(R.drawable.freshair_open);

        viewHoler.mName.setText(mFreshAir.get(position).getDev().getDevName());
        return convertView;
    }

    class ViewHoler {
        ImageView mIV;
        TextView mName;
    }
}
