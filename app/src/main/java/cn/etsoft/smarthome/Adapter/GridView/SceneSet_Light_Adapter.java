package cn.etsoft.smarthome.Adapter.GridView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.channguyen.rsv.RangeSliderView;

import java.util.List;

import cn.etsoft.smarthome.Domain.WareLight;
import cn.etsoft.smarthome.Domain.WareSceneDevItem;
import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;

/**
 * Author：FBL  Time： 2017/6/26.
 */

public class SceneSet_Light_Adapter extends BaseAdapter {

    private List<WareSceneDevItem> mSceneDev;
    private Context mContext;
    private List<WareLight> mLights;
    private boolean mIsShowSelect;

    public SceneSet_Light_Adapter(int sceneposition, Context context, List<WareLight> lights,boolean isShowSelect) {
        mContext = context;
        mLights = lights;
        mIsShowSelect = isShowSelect;
        mSceneDev = MyApplication.getWareData().getSceneEvents().get(sceneposition).getItemAry();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHoler viewHoler = null;
        if (convertView== null){
            viewHoler = new ViewHoler();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.sceneset_gridview_light_item,null);
            viewHoler.mName = (TextView) convertView.findViewById(R.id.SceneSet_DridView_Item_Name);
            viewHoler.mIV = (ImageView) convertView.findViewById(R.id.SceneSet_DridView_Item_IV);
            viewHoler.mSelect = (ImageView) convertView.findViewById(R.id.SceneSet_DridView_Item_Select);
            viewHoler.mSlide = (RangeSliderView) convertView.findViewById(R.id.SceneSet_DridView_Item_Slide);
            convertView.setTag(viewHoler);
        }else viewHoler = (ViewHoler) convertView.getTag();


        if (mSceneDev == null) {
            viewHoler.mIV.setImageResource(R.drawable.ic_launcher);
            viewHoler.mSelect.setImageResource(R.drawable.ic_launcher_round);
        } else {
            for (int i = 0; i < mSceneDev.size(); i++) {//根据设给的数据判断状态以及显示图标
                if (mSceneDev.get(i).getDevID() == mLights.get(position).getDev().getDevId()
                        && mSceneDev.get(i).getUid().equals(mLights.get(position).getDev().getCanCpuId())
                        && mSceneDev.get(i).getDevType() == mLights.get(position).getDev().getType()) {
                    mLights.get(position).getDev().setSelect(true);
                    if (mSceneDev.get(i).getbOnOff() == 0) {
                        viewHoler.mIV.setImageResource(R.drawable.ic_launcher);
                    } else if (mSceneDev.get(i).getbOnOff() == 1) {
                        viewHoler.mIV.setImageResource(R.drawable.ic_launcher);
                    }
                }
            }
        }
        return convertView;
    }
    class ViewHoler{
        ImageView mSelect,mIV;
        TextView mName;
        RangeSliderView mSlide;
    }
}
