package cn.etsoft.smarthome.Adapter.GridView;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jaygoo.widget.RangeSeekBar;

import java.util.ArrayList;
import java.util.List;

import cn.etsoft.smarthome.Domain.WareDev;
import cn.etsoft.smarthome.R;

import static android.content.ContentValues.TAG;

/**
 * Author：FBL  Time： 2017/6/26.
 * 设备详情设置——灯光 适配器
 */

public class DevInfo_Dev_LightAdapter extends BaseAdapter {

    private Context mContext;
    private List<WareDev> listDev;
    private boolean mIsShowSelect;

    public DevInfo_Dev_LightAdapter(Context context, List<WareDev> devs) {
        listDev = devs;
        mContext = context;
    }

    public void notifyDataSetChanged(List<WareDev> devs) {
        listDev = devs;
        this.mIsShowSelect = mIsShowSelect;
        super.notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return listDev.size();
    }

    @Override
    public Object getItem(int position) {
        return listDev.get(position);
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.gridview_devinfo_light, null);
            convertView.setTag(viewHoler);
        } else viewHoler = (ViewHoler) convertView.getTag();

        return convertView;
    }

    class ViewHoler {
        ImageView mSelect, mIV;
        TextView mName;
        RangeSeekBar mSeekBar;
    }
}
