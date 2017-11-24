package cn.etsoft.smarthome.Adapter.GridView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.etsoft.smarthome.Domain.WareLight;
import cn.etsoft.smarthome.R;

/**
 * Author：FBL  Time： 2017/6/26.
 * 控制页面——灯光 适配器
 */

public class Control_Light_Adapter extends BaseAdapter {

    private Context mContext;
    private List<WareLight> mLights;

    public Control_Light_Adapter(Context context, List<WareLight> lights) {
        mLights = lights;
        mContext = context;
    }

    public void notifyDataSetChanged(List<WareLight> mLights) {
        this.mLights = mLights;
        super.notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return mLights.size();
    }

    @Override
    public Object getItem(int position) {
        return mLights.get(position);
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

        if (mLights.size() == 0 || position > mLights.size() - 1)
            return convertView;

        if (mLights.get(position).getbOnOff() == 0)
            viewHoler.mIV.setImageResource(R.drawable.light_close);
        else viewHoler.mIV.setImageResource(R.drawable.light_open);

        viewHoler.mName.setText(mLights.get(position).getDev().getDevName());
        return convertView;
    }

    class ViewHoler {
        ImageView mIV;
        TextView mName;
    }
}
