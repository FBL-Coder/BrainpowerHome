package cn.semtec.community2.adapter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.semtec.community2.MyApplication;
import cn.etsoft.smarthome.R;
import cn.semtec.community2.database.DBhelper;

public class PictureFragmentAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private Context mContext;
    private ArrayList<Map<String, String>> mList;
    public boolean isShowDelete;

    public PictureFragmentAdapter(Context context, ArrayList<Map<String, String>> mlist) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        this.mList = mlist;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        HashMap<String, String> map = (HashMap<String, String>) mList.get(position);
        final String date = map.get("date");
//        final String device = map.get("device");
//        String _id = map.get("_id");
        final String path = map.get("path");
        Bitmap picture = getPicture(path);
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.fragment_picture_item, null);
            holder = new ViewHolder();
            holder.image = (ImageView) convertView.findViewById(R.id.image);
            holder.delete = (ImageView) convertView.findViewById(R.id.delete);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.image.setImageBitmap(picture);
        holder.delete.setVisibility(isShowDelete ? View.VISIBLE : View.GONE);
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(path);
                file.delete();

                SQLiteDatabase db = MyApplication.getDB();
                db.delete(DBhelper.VIDEO_RECORD, DBhelper.RECORD_DATE + "=?", new String[]{date});
                db.close();
                mList.remove(position);
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

    public void setIsShowDelete(boolean isShowDelete) {
        this.isShowDelete = isShowDelete;
        notifyDataSetChanged();
    }


    private Bitmap getPicture(String path) {
        File f = new File(path);
        if (!f.exists()) {
            return BitmapFactory.decodeResource(mContext.getResources(),
                    R.drawable.base_sliding_head);
        }
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, newOpts);

        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        float hh = 100f;
        float ww = 100f;
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        newOpts.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(path, newOpts);
        return bitmap;
    }

    private class ViewHolder {
        public ImageView image;
        public ImageView delete;

    }
}
